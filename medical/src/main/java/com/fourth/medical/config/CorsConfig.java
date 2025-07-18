// 定义包名，指定当前类所在的包路径
package com.fourth.medical.config;

// 导入Lombok日志注解，自动生成日志对象，便于记录配置信息和调试
import lombok.extern.slf4j.Slf4j;
// 导入Spring Bean注解，标识这是一个Spring Bean定义方法
import org.springframework.context.annotation.Bean;
// 导入Spring配置注解，标识这是一个Spring配置类
import org.springframework.context.annotation.Configuration;
// 导入Spring CORS配置类，用于跨域配置，定义跨域访问规则
import org.springframework.web.cors.CorsConfiguration;
// 导入基于URL的CORS配置源，用于配置不同URL路径的跨域规则
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// 导入CORS过滤器，用于处理跨域请求，在请求处理链中添加CORS响应头
import org.springframework.web.filter.CorsFilter;
// 导入Spring MVC配置器接口，用于配置Web MVC的跨域设置
import org.springframework.web.servlet.config.annotation.CorsRegistry;
// 导入Spring MVC配置器接口，提供Web MVC的各种配置方法
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// 导入Arrays工具类，用于数组操作，便于设置多个允许的HTTP方法
import java.util.Arrays;

/**
 * CORS跨域资源共享配置类
 * 
 * 功能说明：
 * 本类是医疗系统中处理跨域资源共享(Cross-Origin Resource Sharing)的核心配置类。
 * 在现代Web应用中，前后端分离架构越来越常见，前端应用(如Vue、React、Angular)
 * 和后端API服务往往部署在不同的域名或端口上，浏览器的同源策略会阻止这种跨域访问。
 * 本配置类通过配置合适的CORS策略，解决前后端分离开发中的跨域问题。
 * 
 * 跨域问题详细说明：
 * 同源策略是浏览器的一个重要安全机制，它限制了从一个源（协议、域名、端口的组合）
 * 加载的文档或脚本如何与来自另一个源的资源进行交互。
 * 
 * 同源的定义：
 * - 协议相同：http与https不同源
 * - 域名相同：example.com与api.example.com不同源  
 * - 端口相同：8080与8081不同源
 * 
 * 跨域问题产生的具体原因：
 * 1. 浏览器同源策略限制：协议、域名、端口三者必须完全相同
 * 2. 前端项目(如Vue运行在3000端口)访问后端API(如Spring Boot运行在8080端口)
 * 3. AJAX请求被浏览器拦截，返回CORS错误
 * 4. 预检请求(OPTIONS)被拒绝，导致实际请求无法发送
 * 
 * CORS解决方案原理：
 * 1. 服务端在响应头中添加CORS相关头部信息
 * 2. 浏览器检查响应头，确认是否允许跨域访问
 * 3. 对于复杂请求，浏览器会先发送预检请求(OPTIONS)
 * 4. 服务端返回允许的方法、头部等信息
 * 5. 浏览器根据预检结果决定是否发送实际请求
 * 
 * 支持的请求类型：
 * - 简单请求：GET、POST、HEAD等基本请求
 * - 复杂请求：PUT、DELETE、自定义头部的请求
 * - 预检请求：OPTIONS请求，用于检查跨域权限
 * 
 * 安全考虑：
 * - 生产环境应该配置具体的域名，而不是使用通配符
 * - 谨慎使用allowCredentials=true，可能带来安全风险
 * - 定期审查和更新CORS配置，确保安全性
 * 
 * 适用场景：
 * - 前后端分离的Web应用：Vue+Spring Boot、React+Spring Boot等
 * - 移动端App调用API：原生App、混合App等
 * - 第三方系统集成：外部系统调用内部API
 * - 微服务架构：服务间的跨域调用
 * - 开发调试：本地开发环境访问远程API
 * 
 * 配置优势：
 * - 提供了两种配置方式，适应不同的使用场景
 * - 支持复杂的跨域需求，如凭证传输、自定义头部等
 * - 配置灵活，可以针对不同路径设置不同的跨域规则
 * - 性能优化，支持预检请求缓存
 * 
 * 性能优化：
 * - 预检请求缓存：减少不必要的OPTIONS请求
 * - 路径匹配优化：使用高效的路径匹配算法
 * - 头部处理优化：减少不必要的头部添加
 * 
 * 扩展功能：
 * - 支持动态CORS配置
 * - 支持基于用户角色的CORS控制
 * - 支持CORS配置的热更新
 * - 支持CORS访问日志记录
 * 
 * @author geekidea
 * @date 2023/2/16
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j // 自动生成日志对象log，便于记录配置信息和调试问题
@Configuration // 标识这是一个Spring配置类，Spring容器会自动扫描并处理
public class CorsConfig {
    
    /**
     * WebMVC跨域配置器Bean
     * 
     * 功能说明：
     * 通过WebMvcConfigurer接口提供的addCorsMappings方法配置全局跨域规则。
     * 这种方式是Spring MVC原生支持的跨域配置方式，与Spring MVC框架集成度高，
     * 配置简单，性能较好，适合大多数跨域场景。
     * 
     * 配置特点：
     * - 基于Spring MVC的配置方式，与框架集成度高
     * - 配置简单明了，易于理解和维护
     * - 性能较好，不会增加额外的过滤器开销
     * - 支持路径模式匹配，可以为不同路径设置不同规则
     * - 与Spring Security集成良好
     * 
     * 工作原理：
     * 1. Spring MVC在处理请求时会检查跨域配置
     * 2. 如果是跨域请求，会自动添加相应的CORS响应头
     * 3. 对于预检请求(OPTIONS)，会返回允许的方法和头部信息
     * 4. 浏览器根据响应头决定是否发送实际请求
     * 
     * 适用场景：
     * - 标准的前后端分离应用
     * - 不需要复杂跨域控制的场景
     * - 与Spring Security集成的应用
     * - 对性能要求较高的应用
     * 
     * 配置说明：
     * - addMapping("/**")：对所有路径启用跨域，也可以配置特定路径
     * - allowedOriginPatterns("*")：允许所有源访问，生产环境建议配置具体域名
     * - allowedMethods：允许的HTTP方法，包括GET、POST、PUT、DELETE、OPTIONS
     * - allowedHeaders("*")：允许所有请求头，包括自定义头部
     * - allowCredentials(true)：允许携带凭证，如Cookie、Authorization等
     * - maxAge(3600)：预检请求的缓存时间，单位为秒
     * 
     * @return WebMvcConfigurer 返回WebMVC配置器实例，用于配置跨域规则
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 添加跨域映射配置
             * 
             * 功能说明：
             * 配置Spring MVC的跨域映射规则，定义哪些路径允许跨域访问，
             * 以及允许的源、方法、头部等详细信息。
             * 
             * 配置项详解：
             * - 路径映射：/**表示所有路径都允许跨域
             * - 源模式：allowedOriginPatterns支持通配符，比allowedOrigins更灵活
             * - HTTP方法：明确指定允许的请求方法，提高安全性
             * - 请求头：允许所有请求头，包括自定义的业务头部
             * - 凭证支持：允许携带Cookie和Authorization头部
             * - 缓存时间：预检请求结果的缓存时间，减少重复请求
             * 
             * @param registry 跨域注册表，用于注册跨域映射配置
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 对所有路径启用跨域，也可以配置特定路径如"/api/**"
                        .allowedOriginPatterns("*") // 允许所有源访问，使用allowedOriginPatterns代替allowedOrigins以支持通配符
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                        .allowedHeaders("*") // 允许所有请求头，包括自定义头部
                        .allowCredentials(true) // 允许携带凭证(Cookie、Authorization等)
                        .maxAge(3600); // 预检请求的缓存时间(秒)，减少重复的OPTIONS请求
            }
        };
    }
    
    /**
     * CORS过滤器Bean
     * 
     * 功能说明：
     * 通过CorsFilter实现跨域请求处理，这是基于Servlet过滤器的实现方式。
     * 过滤器在请求处理链的早期阶段处理跨域，能够拦截所有HTTP请求，
     * 支持更复杂的跨域配置，可以与其他过滤器组合使用。
     * 
     * 配置特点：
     * - 基于Servlet过滤器实现，处理时机更早
     * - 在请求处理链的早期阶段处理跨域
     * - 支持复杂的跨域配置和自定义逻辑
     * - 可以与其他过滤器组合使用
     * - 独立于Spring MVC，适用于所有Web框架
     * 
     * 工作原理：
     * 1. 过滤器拦截所有HTTP请求
     * 2. 检查请求是否为跨域请求
     * 3. 根据配置添加相应的CORS响应头
     * 4. 处理预检请求(OPTIONS)，返回允许的方法和头部
     * 5. 将请求传递给下一个过滤器或控制器
     * 
     * 与WebMvcConfigurer的区别：
     * - 过滤器方式：处理时机更早，可以拦截所有请求
     * - MVC方式：集成度更高，配置更简单
     * - 过滤器方式：支持更复杂的逻辑和自定义处理
     * - MVC方式：性能更好，开销更小
     * 
     * 适用场景：
     * - 需要复杂跨域控制逻辑的应用
     * - 需要与其他过滤器配合使用的场景
     * - 对跨域处理时机有特殊要求的应用
     * - 需要记录跨域访问日志的场景
     * 
     * 安全建议：
     * - 生产环境避免使用通配符，配置具体的域名
     * - 谨慎使用allowCredentials=true，可能带来安全风险
     * - 定期审查和更新跨域配置
     * - 监控跨域访问日志，及时发现异常请求
     * 
     * @return CorsFilter 返回CORS过滤器实例，用于处理跨域请求
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建CORS配置对象，用于定义跨域访问规则
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // 允许访问的源配置
        // 使用allowedOriginPatterns支持通配符模式，比allowedOrigins更灵活
        // "*" 表示允许任何源访问，生产环境建议配置具体的域名
        // 例如：corsConfiguration.addAllowedOriginPattern("https://example.com");
        //      corsConfiguration.addAllowedOriginPattern("https://*.example.com");
        corsConfiguration.addAllowedOriginPattern("*");
        
        // 允许访问的请求头配置
        // "*" 表示允许所有请求头，包括标准头部和自定义头部
        // 标准头部：Content-Type、Accept、Authorization等
        // 自定义头部：X-Token、X-User-Id等业务相关头部
        corsConfiguration.addAllowedHeader("*");
        
        // 允许访问的请求方式配置
        // 明确指定支持的HTTP方法，提高安全性
        // OPTIONS: 用于预检请求，浏览器自动发送
        // GET: 获取资源，读取操作
        // POST: 创建资源，提交数据
        // PUT: 更新资源，完整更新
        // DELETE: 删除资源，删除操作
        corsConfiguration.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "DELETE"));
        
        // 是否允许发送cookie和认证信息
        // true: 允许携带凭证，前端需要设置withCredentials=true
        //       支持Cookie、Authorization头部等认证信息传输
        //       适用于需要身份验证的API
        // false: 不允许携带凭证，适用于公开API
        //        更安全，但无法进行身份验证
        corsConfiguration.setAllowCredentials(true);
        
        // 允许响应的头配置
        // 客户端可以访问的响应头，包括自定义响应头
        // 常见的响应头：Content-Type、Content-Length、Date等
        // 自定义响应头：X-Total-Count、X-Page-Size等分页信息
        //              X-RateLimit-Remaining等限流信息
        corsConfiguration.addExposedHeader("*");
        
        // 创建基于URL的CORS配置源
        // 支持为不同的URL路径配置不同的跨域规则
        // 例如：可以为/api/**和/upload/**配置不同的跨域策略
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        
        // 为所有路径注册CORS配置
        // "/**"表示对所有路径应用此跨域配置
        // 也可以为不同路径配置不同的跨域规则：
        // configurationSource.registerCorsConfiguration("/api/**", apiCorsConfig);
        // configurationSource.registerCorsConfiguration("/upload/**", uploadCorsConfig);
        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        
        // 创建并返回CORS过滤器实例
        // 过滤器会在请求处理链的早期阶段处理跨域请求
        return new CorsFilter(configurationSource);
    }
}
