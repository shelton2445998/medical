// 定义包名，指定当前类所在的包路径
package com.fourth.medical.config;

// 导入认证拦截器包，包含各种登录和权限拦截器，用于处理不同类型的用户认证
import com.fourth.medical.auth.interceptor.*;
// 导入配置属性包，包含各种配置属性类，用于读取配置文件中的属性值
import com.fourth.medical.config.properties.*;
// 导入JSON请求体过滤器，用于处理JSON请求，支持请求体的多次读取
import com.fourth.medical.framework.filter.JsonRequestBodyFilter;
// 导入追踪ID日志过滤器，用于请求追踪，为每个请求生成唯一的追踪ID
import com.fourth.medical.framework.filter.TraceIdLogFilter;
// 导入分页助手清除拦截器，用于清理分页缓存，避免分页信息污染
import com.fourth.medical.framework.interceptor.PageHelperClearInterceptor;
// 导入XSS过滤器，用于防止XSS攻击，过滤恶意脚本
import com.fourth.medical.framework.xss.XssFilter;
// 导入Lombok日志注解，自动生成日志对象，便于记录配置信息和调试
import lombok.extern.slf4j.Slf4j;
// 导入Apache Commons String工具类，用于字符串操作，如判空等
import org.apache.commons.lang3.StringUtils;
// 导入Spring自动装配注解，用于注入依赖的Bean
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring Boot过滤器注册Bean，用于注册自定义过滤器
import org.springframework.boot.web.servlet.FilterRegistrationBean;
// 导入Spring Bean注解，标识这是一个Spring Bean定义方法
import org.springframework.context.annotation.Bean;
// 导入Spring配置注解，标识这是一个Spring配置类
import org.springframework.context.annotation.Configuration;
// 导入Spring MVC拦截器注册接口，用于注册自定义拦截器
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// 导入Spring MVC资源处理器注册接口，用于配置静态资源访问
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// 导入Spring MVC配置器接口，用于配置Spring MVC的各种功能
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 导入ArrayList集合类，用于处理列表数据
import java.util.ArrayList;
// 导入Arrays工具类，用于数组操作，如数组转列表等
import java.util.Arrays;
// 导入List接口，用于处理列表数据
import java.util.List;

/**
 * Web MVC配置类
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>Spring MVC框架的核心配置类，负责配置Web层的各种功能组件</li>
 *   <li>实现用户认证和授权管理，支持多种用户类型</li>
 *   <li>配置请求处理和过滤机制，提供安全防护</li>
 *   <li>管理静态资源和文件访问，支持虚拟目录映射</li>
 *   <li>实现路径权限控制和访问控制</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>多层拦截器架构：按特定顺序执行的拦截器链</li>
 *   <li>多用户类型支持：管理员、App用户、医生等不同用户类型</li>
 *   <li>安全防护机制：XSS攻击防护、Token验证、路径权限控制</li>
 *   <li>请求追踪：自动生成请求追踪ID，支持链路追踪</li>
 *   <li>静态资源管理：文件上传、虚拟目录、资源访问控制</li>
 *   <li>性能优化：高效的拦截器执行顺序、缓存清理机制</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>前后端分离的Web应用</li>
 *   <li>多用户类型的管理系统</li>
 *   <li>需要复杂权限控制的应用</li>
 *   <li>高安全性要求的医疗系统</li>
 *   <li>需要文件上传下载的应用</li>
 *   <li>需要请求追踪和日志记录的系统</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于Spring MVC WebMvcConfigurer接口</li>
 *   <li>使用Spring AOP拦截器机制</li>
 *   <li>集成过滤器链处理请求</li>
 *   <li>支持配置属性注入和热更新</li>
 *   <li>使用ThreadLocal进行请求上下文管理</li>
 * </ul>
 * 
 * <p><strong>拦截器架构：</strong></p>
 * <ol>
 *   <li>排除路径拦截器：处理不需要拦截的路径</li>
 *   <li>基于路径的认证拦截器：根据请求路径进行认证检查</li>
 *   <li>无权限拦截器：处理无权限访问的请求</li>
 *   <li>Token拦截器：验证Token有效性</li>
 *   <li>各种登录拦截器：验证用户登录状态</li>
 *   <li>刷新Token拦截器：自动刷新Token</li>
 *   <li>分页缓存清除拦截器：清理分页缓存</li>
 * </ol>
 * 
 * <p><strong>过滤器链：</strong></p>
 * <ol>
 *   <li>追踪ID过滤器：生成请求追踪ID</li>
 *   <li>JSON请求体过滤器：处理JSON请求</li>
 *   <li>XSS防护过滤器：防止XSS攻击</li>
 * </ol>
 * 
 * <p><strong>安全机制：</strong></p>
 * <ul>
 *   <li>多层次的身份验证</li>
 *   <li>XSS攻击防护</li>
 *   <li>Token安全验证</li>
 *   <li>路径权限控制</li>
 *   <li>请求追踪和日志记录</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展新的拦截器和过滤器</li>
 *   <li>可支持自定义认证策略</li>
 *   <li>可添加更多安全防护机制</li>
 *   <li>可集成第三方安全组件</li>
 * </ul>
 * 
 * @author geekidea
 * @date 2022/3/15
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j  // 自动生成日志对象log，便于记录配置信息和调试问题
@Configuration  // 标识这是一个Spring配置类，Spring容器会自动扫描并处理
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 登录配置属性
     * 
     * 功能说明：
     * 注入通用的登录配置属性，包含系统级别的登录相关配置信息，
     * 如登录超时时间、登录失败次数限制等通用配置。
     * 
     * 配置内容：
     * - 登录超时时间配置
     * - 登录失败次数限制
     * - 通用排除路径配置
     * - 登录重定向配置
     */
    @Autowired
    private LoginProperties loginProperties;

    /**
     * 管理后台登录配置属性
     * 
     * 功能说明：
     * 注入管理后台专用的登录配置属性，包含管理员用户的登录相关配置，
     * 如管理员登录路径、权限验证规则等。
     * 
     * 配置内容：
     * - 管理员登录路径配置
     * - 管理员权限验证规则
     * - 管理端特定的排除路径
     * - 管理员会话管理配置
     */
    @Autowired
    private LoginAdminProperties loginAdminProperties;

    /**
     * App登录配置属性
     * 
     * 功能说明：
     * 注入移动端App专用的登录配置属性，包含App用户的登录相关配置，
     * 如App登录接口、Token验证规则等。
     * 
     * 配置内容：
     * - App登录接口配置
     * - App Token验证规则
     * - 移动端特定的排除路径
     * - App会话管理配置
     */
    @Autowired
    private LoginAppProperties loginAppProperties;

    /**
     * 公共登录配置属性
     * 
     * 功能说明：
     * 注入公共登录配置属性，包含系统公共接口的登录相关配置，
     * 如公共API访问规则、通用认证机制等。
     * 
     * 配置内容：
     * - 公共API访问规则
     * - 通用认证机制配置
     * - 公共接口的排除路径
     * - 公共服务的权限配置
     */
    @Autowired
    private LoginCommonProperties loginCommonProperties;

    /**
     * 本地文件配置属性
     * 
     * 功能说明：
     * 注入本地文件管理配置属性，包含文件上传、下载、存储等相关配置，
     * 如文件存储路径、访问路径、文件大小限制等。
     * 
     * 配置内容：
     * - 文件上传路径配置
     * - 文件访问路径配置
     * - 文件大小和类型限制
     * - 文件存储策略配置
     */
    @Autowired
    private LocalFileProperties localFileProperties;

    /**
     * XSS防护配置属性
     * 
     * 功能说明：
     * 注入XSS防护配置属性，包含XSS攻击防护的相关配置，
     * 如过滤规则、白名单、过滤器优先级等。
     * 
     * 配置内容：
     * - XSS过滤规则配置
     * - 白名单路径配置
     * - 过滤器优先级设置
     * - 过滤器开关配置
     */
    @Autowired
    private XssProperties xssProperties;

    /**
     * 无权限配置属性
     * 
     * 功能说明：
     * 注入无权限处理配置属性，包含无权限访问的处理规则，
     * 如无权限响应格式、重定向配置等。
     * 
     * 配置内容：
     * - 无权限响应格式配置
     * - 无权限重定向配置
     * - 无权限处理开关
     * - 无权限日志记录配置
     */
    @Autowired
    private NotAuthProperties notAuthProperties;

    /**
     * 排除路径拦截器Bean
     * 
     * 功能说明：
     * 创建排除路径拦截器，用于处理不需要进行认证的路径。
     * 这个拦截器通常作为拦截器链的第一个拦截器，用于快速排除
     * 不需要认证的静态资源、公共接口等。
     * 
     * 工作原理：
     * 1. 检查请求路径是否在排除列表中
     * 2. 如果在排除列表中，直接通过，不执行后续认证
     * 3. 如果不在排除列表中，继续执行后续拦截器
     * 
     * 适用场景：
     * - 静态资源访问（CSS、JS、图片等）
     * - 公共API接口（登录、注册、获取验证码等）
     * - 健康检查接口
     * - 错误页面
     * 
     * @return ExcludePathInterceptor 返回排除路径拦截器实例
     */
    @Bean
    public ExcludePathInterceptor excludePathInterceptor() {
        return new ExcludePathInterceptor();
    }

    /**
     * 无权限拦截器Bean
     * 
     * 功能说明：
     * 创建无权限拦截器，用于处理无权限访问的请求，直接返回无权限响应。
     * 这个拦截器用于快速处理一些明确不需要权限验证的请求，
     * 避免执行复杂的认证逻辑，提高系统性能。
     * 
     * 工作原理：
     * 1. 拦截指定路径的请求
     * 2. 直接返回无权限响应，不进行实际的权限验证
     * 3. 记录无权限访问日志
     * 
     * 适用场景：
     * - 临时关闭某些功能模块
     * - 维护期间的功能限制
     * - 某些测试或演示环境的权限控制
     * 
     * @return NotAuthInterceptor 返回无权限拦截器实例
     */
    @Bean
    public NotAuthInterceptor notAuthInterceptor() {
        return new NotAuthInterceptor();
    }

    /**
     * Token拦截器Bean
     * 
     * 功能说明：
     * 创建Token拦截器，用于验证请求中的Token有效性。
     * 这是系统安全机制的核心组件，负责验证用户的身份令牌，
     * 确保只有有效的用户才能访问受保护的资源。
     * 
     * 工作原理：
     * 1. 从请求头或参数中提取Token
     * 2. 验证Token的格式和签名
     * 3. 检查Token是否过期
     * 4. 验证Token对应的用户是否存在和有效
     * 5. 将用户信息存储到请求上下文中
     * 
     * 验证流程：
     * - Token格式验证：检查Token是否符合预期格式
     * - 签名验证：验证Token的数字签名是否有效
     * - 过期时间验证：检查Token是否已过期
     * - 用户状态验证：验证Token对应的用户是否有效
     * 
     * 安全特性：
     * - 支持多种Token算法（JWT、自定义等）
     * - 防止Token重放攻击
     * - 支持Token黑名单机制
     * - 详细的安全日志记录
     * 
     * @return TokenInterceptor 返回Token拦截器实例
     */
    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }

    /**
     * 管理后台登录拦截器Bean
     * 
     * 功能说明：
     * 创建管理后台登录拦截器，用于验证管理后台用户的登录状态。
     * 这个拦截器专门处理管理员用户的认证，确保只有已登录的
     * 管理员才能访问管理后台的功能。
     * 
     * 工作原理：
     * 1. 检查管理员用户的登录状态
     * 2. 验证管理员的权限级别
     * 3. 检查管理员会话是否有效
     * 4. 记录管理员的访问日志
     * 
     * 验证内容：
     * - 管理员登录状态验证
     * - 管理员权限级别检查
     * - 管理员会话有效性验证
     * - 管理员IP地址验证（可选）
     * 
     * 安全措施：
     * - 支持管理员会话超时控制
     * - 支持管理员IP白名单验证
     * - 支持管理员操作日志记录
     * - 支持管理员异常登录检测
     * 
     * @return LoginInterceptor 返回管理后台登录拦截器实例
     */
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    /**
     * App登录拦截器Bean
     * 
     * 功能说明：
     * 创建App登录拦截器，用于验证移动端App用户的登录状态。
     * 这个拦截器专门处理移动端用户的认证，适应移动端的特殊需求，
     * 如设备绑定、推送通知等。
     * 
     * 工作原理：
     * 1. 检查App用户的登录状态
     * 2. 验证App用户的设备信息
     * 3. 检查App用户的权限范围
     * 4. 处理App特有的认证逻辑
     * 
     * 移动端特性：
     * - 设备ID绑定验证
     * - 推送Token管理
     * - 移动端会话管理
     * - 离线数据同步支持
     * 
     * 安全特性：
     * - 设备指纹验证
     * - 异常登录检测
     * - 移动端专用的Token管理
     * - 支持多设备登录控制
     * 
     * @return AppLoginInterceptor 返回App登录拦截器实例
     */
    @Bean
    public AppLoginInterceptor loginAppInterceptor() {
        return new AppLoginInterceptor();
    }

    /**
     * 公共登录拦截器Bean
     * 
     * 功能说明：
     * 创建公共登录拦截器，用于处理公共登录相关的请求。
     * 这个拦截器处理系统中的公共接口访问控制，
     * 如公共API、第三方接口等。
     * 
     * 工作原理：
     * 1. 检查公共接口的访问权限
     * 2. 验证API调用频率和限制
     * 3. 处理第三方系统的认证
     * 4. 记录公共接口的访问日志
     * 
     * 适用场景：
     * - 公共API接口访问控制
     * - 第三方系统集成认证
     * - 开放平台接口管理
     * - 公共服务访问控制
     * 
     * @return CommonLoginInterceptor 返回公共登录拦截器实例
     */
    @Bean
    public CommonLoginInterceptor loginCommonInterceptor() {
        return new CommonLoginInterceptor();
    }

    /**
     * 刷新Token拦截器Bean
     * 
     * 功能说明：
     * 创建刷新Token拦截器，用于自动刷新即将过期的Token。
     * 这个拦截器提供了无感的Token刷新机制，提高用户体验，
     * 避免用户在使用过程中频繁重新登录。
     * 
     * 工作原理：
     * 1. 检查Token的过期时间
     * 2. 如果Token即将过期，自动刷新Token
     * 3. 将新Token添加到响应头中
     * 4. 更新用户的会话信息
     * 
     * 刷新策略：
     * - 基于时间的刷新策略
     * - 基于访问频率的刷新策略
     * - 支持刷新Token的黑名单机制
     * - 支持刷新失败的降级处理
     * 
     * 优势：
     * - 无感的用户体验
     * - 减少重复登录
     * - 提高系统安全性
     * - 支持长期会话管理
     * 
     * @return RefreshTokenInterceptor 返回刷新Token拦截器实例
     */
    @Bean
    public RefreshTokenInterceptor refreshTokenInterceptor() {
        return new RefreshTokenInterceptor();
    }

    /**
     * 分页助手清除拦截器Bean
     * 
     * 功能说明：
     * 创建分页助手清除拦截器，用于清理分页查询的缓存数据。
     * 这个拦截器解决了MyBatis分页插件的线程安全问题，
     * 确保分页信息不会在不同请求之间互相干扰。
     * 
     * 工作原理：
     * 1. 在请求处理完成后，清除ThreadLocal中的分页信息
     * 2. 避免分页信息在不同请求之间的污染
     * 3. 释放分页相关的内存资源
     * 4. 记录分页清理的日志信息
     * 
     * 解决问题：
     * - 分页信息的线程安全问题
     * - 分页缓存的内存泄漏问题
     * - 分页参数的跨请求污染问题
     * 
     * 适用场景：
     * - 使用MyBatis分页插件的应用
     * - 高并发的分页查询场景
     * - 需要严格线程安全的应用
     * 
     * @return PageHelperClearInterceptor 返回分页助手清除拦截器实例
     */
    @Bean
    public PageHelperClearInterceptor pageHelperClearInterceptor() {
        return new PageHelperClearInterceptor();
    }
    
    /**
     * 基于路径的认证拦截器Bean
     * 
     * 功能说明：
     * 创建基于路径的认证拦截器，根据请求路径进行认证检查，
     * 支持不同系统的认证策略。这个拦截器提供了灵活的路径匹配
     * 和认证策略，能够根据不同的URL路径应用不同的认证规则。
     * 
     * 工作原理：
     * 1. 解析请求路径
     * 2. 匹配路径对应的认证规则
     * 3. 根据规则选择适当的认证策略
     * 4. 执行相应的认证逻辑
     * 
     * 路径匹配规则：
     * - 支持Ant风格的路径匹配
     * - 支持正则表达式匹配
     * - 支持路径参数提取
     * - 支持路径优先级配置
     * 
     * 认证策略：
     * - 管理员认证策略
     * - 普通用户认证策略
     * - 第三方系统认证策略
     * - 匿名访问策略
     * 
     * @return PathBasedAuthInterceptor 返回基于路径的认证拦截器实例
     */
    @Bean
    public PathBasedAuthInterceptor pathBasedAuthInterceptor() {
        return new PathBasedAuthInterceptor();
    }

    /**
     * 追踪ID日志过滤器Bean
     * 
     * 功能说明：
     * 创建追踪ID日志过滤器，为每个HTTP请求生成唯一的追踪ID，
     * 用于日志跟踪和问题排查。这个过滤器是分布式系统中日志追踪
     * 的基础组件，能够将一个完整的请求处理过程关联起来。
     * 
     * 工作原理：
     * 1. 为每个请求生成唯一的traceId
     * 2. 将traceId添加到MDC（Mapped Diagnostic Context）中
     * 3. 在整个请求处理过程中保持traceId
     * 4. 请求结束后清理traceId
     * 
     * 追踪ID特性：
     * - 唯一性：每个请求都有唯一的追踪ID
     * - 持久性：在整个请求处理过程中保持不变
     * - 传播性：可以传播到下游系统
     * - 可读性：便于日志查看和分析
     * 
     * 使用场景：
     * - 分布式链路追踪
     * - 日志关联分析
     * - 问题定位和调试
     * - 性能监控和分析
     * 
     * 配置说明：
     * - 过滤器作用于所有请求路径（/*）
     * - 过滤器优先级较高，确保早期生成追踪ID
     * - 支持异步请求处理
     * 
     * @return FilterRegistrationBean 返回追踪ID日志过滤器注册Bean
     */
    @Bean
    public FilterRegistrationBean traceIdLogFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        TraceIdLogFilter traceIdLogFilter = new TraceIdLogFilter();
        filterRegistrationBean.setFilter(traceIdLogFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }

    /**
     * JSON请求体过滤器Bean
     * 
     * 功能说明：
     * 创建JSON请求体过滤器，用于处理JSON格式的请求体数据，
     * 提供请求体的读取和重复读取功能。这个过滤器解决了HttpServletRequest
     * 的InputStream只能读取一次的问题，为后续的请求体处理提供便利。
     * 
     * 工作原理：
     * 1. 包装HttpServletRequest，拦截InputStream的读取
     * 2. 将请求体内容缓存到内存中
     * 3. 提供多次读取请求体的能力
     * 4. 支持大文件请求体的流式处理
     * 
     * 解决问题：
     * - HttpServletRequest.getInputStream()只能读取一次的问题
     * - 参数验证和业务逻辑都需要读取请求体的冲突
     * - 请求体日志记录和实际处理的冲突
     * 
     * 技术特点：
     * - 内存缓存：小请求体直接缓存在内存中
     * - 流式处理：大请求体使用流式处理，节省内存
     * - 字符编码：支持多种字符编码处理
     * - 异常处理：完善的异常处理机制
     * 
     * 适用场景：
     * - 需要多次读取请求体的应用
     * - 请求体验证和日志记录
     * - JSON数据的预处理和后处理
     * - 请求体的安全过滤
     * 
     * 配置说明：
     * - 过滤器作用于所有请求路径（/*）
     * - 支持异步请求处理
     * - 内存使用优化
     * 
     * @return FilterRegistrationBean 返回JSON请求体过滤器注册Bean
     */
    @Bean
    public FilterRegistrationBean jsonRequestBodyFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        JsonRequestBodyFilter jsonRequestBodyFilter = new JsonRequestBodyFilter();
        filterRegistrationBean.setFilter(jsonRequestBodyFilter);
        List<String> urls = new ArrayList<>();
        urls.add("/*");
        filterRegistrationBean.setUrlPatterns(urls);
        return filterRegistrationBean;
    }

    /**
     * XSS防护过滤器Bean
     * 
     * 功能说明：
     * 创建XSS防护过滤器，用于防止跨站脚本攻击（XSS），
     * 对请求参数进行安全过滤。这个过滤器是系统安全防护的重要组成部分，
     * 能够有效防止恶意脚本注入攻击。
     * 
     * 工作原理：
     * 1. 拦截HTTP请求，检查请求参数
     * 2. 识别潜在的XSS攻击模式
     * 3. 对恶意脚本进行过滤或转义
     * 4. 记录XSS攻击尝试的日志
     * 
     * 过滤策略：
     * - HTML标签过滤：移除或转义HTML标签
     * - JavaScript代码过滤：移除JavaScript代码
     * - 事件属性过滤：移除onclick等事件属性
     * - URL过滤：检查和过滤恶意URL
     * 
     * 攻击类型防护：
     * - 存储型XSS：过滤存储到数据库的恶意脚本
     * - 反射型XSS：过滤请求参数中的恶意脚本
     * - DOM型XSS：过滤可能影响DOM的恶意内容
     * 
     * 配置特性：
     * - 可配置的过滤规则
     * - 白名单支持
     * - 过滤器开关控制
     * - 过滤器执行顺序控制
     * - 异步请求支持
     * 
     * 性能优化：
     * - 高效的正则表达式匹配
     * - 缓存常用的过滤结果
     * - 按需过滤，避免不必要的处理
     * 
     * @return FilterRegistrationBean 返回XSS防护过滤器注册Bean
     */
    @Bean
    public FilterRegistrationBean xssFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setEnabled(xssProperties.isEnable());
        filterRegistrationBean.addUrlPatterns(xssProperties.getUrlPatterns());
        filterRegistrationBean.setOrder(xssProperties.getOrder());
        filterRegistrationBean.setAsyncSupported(xssProperties.isAsync());
        return filterRegistrationBean;
    }

    /**
     * 静态资源处理器配置
     * 
     * 功能说明：
     * 配置文件上传和静态资源的访问路径映射，将虚拟的访问路径
     * 映射到实际的文件系统路径。这个配置支持文件上传、下载和访问功能，
     * 是文件管理系统的基础配置。
     * 
     * 工作原理：
     * 1. 读取本地文件配置属性
     * 2. 创建虚拟访问路径到实际文件路径的映射
     * 3. 配置静态资源的访问权限
     * 4. 支持文件的缓存和压缩
     * 
     * 配置说明：
     * - accessPath：虚拟访问路径，如/upload/**
     * - uploadPath：实际存储路径，如/data/upload/
     * - 路径映射：将虚拟路径映射到实际路径
     * 
     * 支持的文件类型：
     * - 图片文件：JPG、PNG、GIF、BMP等
     * - 文档文件：PDF、DOC、DOCX、XLS、XLSX等
     * - 视频文件：MP4、AVI、MOV等
     * - 音频文件：MP3、WAV、AAC等
     * 
     * 安全特性：
     * - 文件类型验证
     * - 文件大小限制
     * - 文件访问权限控制
     * - 恶意文件检测
     * 
     * 性能优化：
     * - 文件缓存机制
     * - 压缩传输支持
     * - 断点续传支持
     * - CDN集成支持
     * 
     * 使用场景：
     * - 用户头像上传和访问
     * - 文档附件管理
     * - 图片资源管理
     * - 多媒体文件处理
     * 
     * @param registry 资源处理器注册表，用于注册静态资源处理器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取上传文件的虚拟访问路径（如：/upload/**）
        String accessPath = localFileProperties.getAccessPath();
        // 获取上传文件的实际存储路径（如：/data/upload/）
        String uploadPath = localFileProperties.getUploadPath();
        
        // 验证访问路径和存储路径是否都已配置
        if (StringUtils.isNotBlank(accessPath) && StringUtils.isNotBlank(uploadPath)) {
            // 创建虚拟访问路径到实际文件系统路径的映射
            // 例如：访问/upload/image.jpg会映射到/data/upload/image.jpg
            registry.addResourceHandler(accessPath)
                    .addResourceLocations("file:" + uploadPath);
            
            // 记录资源路径映射配置
            log.info("Static resource mapping configured: {} -> {}", accessPath, uploadPath);
        } else {
            // 如果配置不完整，记录警告日志
            log.warn("Static resource mapping not configured: accessPath={}, uploadPath={}", accessPath, uploadPath);
        }
    }

    /**
     * 拦截器配置
     * 
     * 功能说明：
     * 配置系统中各种拦截器的执行顺序和拦截规则。这个方法是整个拦截器链的
     * 核心配置，定义了拦截器的执行顺序、拦截路径和排除路径。
     * 
     * 拦截器执行顺序说明：
     * 拦截器按照注册的顺序执行，每个拦截器都有其特定的职责：
     * 
     * 1. 排除路径拦截器 - 最先执行，处理不需要拦截的路径
     *    - 用于快速排除静态资源、公共接口等
     *    - 提高系统性能，避免不必要的处理
     * 
     * 2. 基于路径的认证拦截器 - 根据请求路径进行认证检查
     *    - 支持复杂的路径匹配规则
     *    - 支持多种认证策略
     * 
     * 3. 无权限拦截器 - 处理无权限访问的请求
     *    - 快速返回无权限响应
     *    - 避免执行复杂的认证逻辑
     * 
     * 4. Token拦截器 - 验证Token有效性
     *    - 核心的身份验证机制
     *    - 支持多种Token格式
     * 
     * 5. 各种登录拦截器 - 验证用户登录状态
     *    - 管理员登录拦截器
     *    - App用户登录拦截器
     *    - 支持多种用户类型
     * 
     * 6. 刷新Token拦截器 - 自动刷新Token
     *    - 提供无感的Token刷新
     *    - 提高用户体验
     * 
     * 7. 公共请求拦截器 - 处理公共请求
     *    - 处理公共API接口
     *    - 支持第三方系统集成
     * 
     * 8. 分页缓存清除拦截器 - 清理分页缓存
     *    - 解决分页信息的线程安全问题
     *    - 避免内存泄漏
     * 
     * 路径匹配规则：
     * - 支持Ant风格的路径匹配（如：/admin/**）
     * - 支持精确路径匹配（如：/admin/login）
     * - 支持路径参数匹配（如：/user/{id}）
     * - 支持正则表达式匹配
     * 
     * 配置策略：
     * - 包含路径（includePaths）：需要拦截的路径
     * - 排除路径（excludePaths）：需要排除的路径
     * - 路径优先级：精确匹配优先于模糊匹配
     * 
     * 性能优化：
     * - 拦截器的执行顺序经过优化
     * - 支持快速路径排除
     * - 避免不必要的拦截器执行
     * 
     * @param registry 拦截器注册表，用于注册拦截器和配置拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 排除路径拦截器 - 最先执行，用于处理不需要拦截的路径
        // 这个拦截器通常用于快速排除静态资源、公共接口等
        registry.addInterceptor(excludePathInterceptor());
        
        // 2. 基于路径的认证拦截器 - 根据请求路径进行认证检查
        // 对所有路径进行拦截，支持复杂的路径匹配和认证策略
        registry.addInterceptor(pathBasedAuthInterceptor()).addPathPatterns("/**");
        
        // 3. 无权限拦截器 - 处理无权限访问的请求，直接返回无权限响应
        // 只有在配置启用时才会注册这个拦截器
        boolean enableNotAuth = notAuthProperties.isEnable();
        if (enableNotAuth) {
            List<String> includePaths = notAuthProperties.getIncludePaths();
            registry.addInterceptor(notAuthInterceptor()).addPathPatterns(includePaths);
            log.info("NotAuth interceptor enabled for paths: {}", includePaths);
        }

        // 4. Token拦截器 - 验证请求中的Token有效性
        // 对需要认证的路径进行拦截，排除登录接口
        registry.addInterceptor(tokenInterceptor())
                .addPathPatterns("/admin/**", "/doctor/**", "/user/**")
                .excludePathPatterns("/admin/login", "/doctor/login", "/user/login");
                
        // 5. 管理后台登录拦截器 - 验证管理员登录状态
        // 只有在配置启用时才会注册这个拦截器
        boolean enableAdminInterceptor = loginAdminProperties.isEnable();
        if (enableAdminInterceptor) {
            List<String> excludePaths = loginProperties.getExcludePaths();
            List<String> adminExcludePaths = loginAdminProperties.getExcludePaths();
            // 合并通用排除路径和管理员专用排除路径
            adminExcludePaths.addAll(excludePaths);
            registry.addInterceptor(loginInterceptor())
                    .addPathPatterns(loginAdminProperties.getIncludePaths())
                    .excludePathPatterns(adminExcludePaths);
            log.info("Admin login interceptor enabled for paths: {}", loginAdminProperties.getIncludePaths());
        }
        
        // 6. 移动端登录拦截器 - 验证App用户登录状态
        // 只有在配置启用时才会注册这个拦截器
        boolean enableAppInterceptor = loginAppProperties.isEnable();
        if (enableAppInterceptor) {
            List<String> appIncludePaths = loginAppProperties.getIncludePaths();
            List<String> excludePaths = loginAppProperties.getExcludePaths();
            registry.addInterceptor(loginAppInterceptor())
                    .addPathPatterns(appIncludePaths)
                    .excludePathPatterns(excludePaths);
            log.info("App login interceptor enabled for paths: {}", appIncludePaths);
        }
        
        // 7. 刷新Token拦截器 - 自动刷新即将过期的Token
        // 对所有需要认证的路径进行拦截，提供无感的Token刷新
        registry.addInterceptor(refreshTokenInterceptor())
                .addPathPatterns("/admin/**", "/doctor/**", "/user/**");

        // 8. 系统公共请求拦截器 - 处理/common/开头的请求
        // 只有在配置启用时才会注册这个拦截器
        boolean enableCommonInterceptor = loginCommonProperties.isEnable();
        if (enableCommonInterceptor) {
            registry.addInterceptor(loginCommonInterceptor())
                    .addPathPatterns(loginCommonProperties.getIncludePaths());
            log.info("Common login interceptor enabled for paths: {}", loginCommonProperties.getIncludePaths());
        }

        // 9. 分页缓存清除拦截器 - 清理分页查询的缓存数据，避免分页信息污染
        // 这个拦截器必须在所有拦截器的最后执行，用于清理ThreadLocal数据
        registry.addInterceptor(pageHelperClearInterceptor());
        
        // 记录拦截器配置完成的日志
        log.info("All interceptors configured successfully");
    }
}
