// 定义包名，指定当前类所在的包路径
package com.fourth.medical.config;

// 导入登录常量类，用于获取Token名称
import com.fourth.medical.common.constant.LoginConstant;
// 导入OpenAPI配置属性，用于读取API文档配置
import com.fourth.medical.config.properties.OpenApiProperties;
// 导入OpenAPI外部文档模型，用于配置外部链接
import io.swagger.v3.oas.models.ExternalDocumentation;
// 导入OpenAPI核心模型，用于配置API文档基本信息
import io.swagger.v3.oas.models.OpenAPI;
// 导入联系人信息模型，用于配置开发者联系信息
import io.swagger.v3.oas.models.info.Contact;
// 导入API信息模型，用于配置API基本信息
import io.swagger.v3.oas.models.info.Info;
// 导入字符串模式模型，用于定义参数类型
import io.swagger.v3.oas.models.media.StringSchema;
// 导入请求头参数模型，用于定义请求头参数
import io.swagger.v3.oas.models.parameters.HeaderParameter;
// 导入参数模型，用于定义API参数
import io.swagger.v3.oas.models.parameters.Parameter;
// 导入分组开放API，用于创建API文档分组
import org.springdoc.core.GroupedOpenApi;
// 导入操作定制器，用于自定义API操作
import org.springdoc.core.customizers.OperationCustomizer;
// 导入Spring自动装配注解
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring Bean注解，标识这是一个Bean
import org.springframework.context.annotation.Bean;
// 导入Spring配置注解，标识这是一个配置类
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger文档配置类
 * 
 * 功能说明：
 * 配置Swagger API文档的生成，包括文档信息、分组配置、
 * 认证参数等功能。
 * 
 * 主要功能：
 * 1. 配置API文档基本信息（标题、描述、联系方式等）
 * 2. 按模块分组生成API文档
 * 3. 配置全局认证参数（Token）
 * 4. 自定义API操作和参数
 * 
 * 文档分组：
 * 1. 项目接口文档 - 业务相关的API接口
 * 2. 登录授权接口文档 - 认证和授权相关接口
 * 3. 系统管理接口文档 - 系统管理相关接口
 * 4. App用户模块接口文档 - 移动端用户相关接口
 * 5. 公共服务接口文档 - 公共服务相关接口
 * 6. 生成代码接口文档 - 代码生成相关接口
 * 
 * 访问方式：
 * - Swagger UI: http://localhost:8080/swagger-ui/index.html
 * - Knife4j: http://localhost:8080/doc.html
 * - API JSON: http://localhost:8080/v3/api-docs
 * 
 * 使用场景：
 * - 前后端分离项目的API文档
 * - 接口测试和调试
 * - 团队协作和文档维护
 * - 第三方集成参考
 * 
 * @author geekidea
 * @date 2022/10/27
 * @version 1.0
 * @since JDK 1.8
 */
@Configuration // 标识这是一个Spring配置类
public class OpenApiConfig {

    /**
     * Token参数名称
     * 从登录常量中获取Token名称，用于API认证
     */
    private static final String TOKEN_NAME = LoginConstant.TOKEN_NAME;

    /**
     * 各模块包路径定义
     * 用于API文档分组，根据包路径将接口分配到不同的文档组中
     */
    private static String AUTH_PACKAGE = "io.geekidea.boot.auth";          // 认证授权包
    private static String SYSTEM_PACKAGE = "io.geekidea.boot.system";      // 系统管理包
    private static String USER_PACKAGE = "io.geekidea.boot.user";          // 用户模块包
    private static String COMMON_PACKAGE = "io.geekidea.boot.common";      // 公共服务包
    private static String GENERATOR_PACKAGE = "io.geekidea.boot.generator"; // 代码生成包

    /**
     * OpenAPI配置属性
     * 从配置文件中读取API文档相关配置
     */
    @Autowired
    private OpenApiProperties openApiProperties;

    /**
     * Token请求头参数定义
     * 用于在API文档中添加全局Token认证参数
     * 
     * 参数说明：
     * - name: 参数名称，通常为"token"或"Authorization"
     * - required: 是否必需，设置为false表示可选
     * - schema: 参数类型，定义为字符串类型
     * - default: 默认值，设置为空字符串
     */
    private Parameter tokenParameter = new HeaderParameter()
            .name(TOKEN_NAME)           // 设置参数名称
            .required(false)            // 设置为非必需参数
            .schema(new StringSchema()  // 设置参数类型为字符串
                    ._default("")       // 设置默认值为空字符串
                    .name(TOKEN_NAME)); // 设置模式名称

    /**
     * OpenAPI文档基本信息配置Bean
     * 
     * 功能说明：
     * 配置API文档的基本信息，包括标题、描述、版本、
     * 联系方式、外部文档链接等。
     * 
     * 配置内容：
     * 1. 文档标题和描述
     * 2. 服务条款链接
     * 3. 开发者联系信息
     * 4. API版本信息
     * 5. 外部文档链接
     * 
     * 这些信息会显示在Swagger UI的文档首页，
     * 帮助用户了解API的基本情况。
     * 
     * @return OpenAPI OpenAPI文档配置对象
     */
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(openApiProperties.getTitle())                     // 设置API文档标题
                        .description(openApiProperties.getDescription())         // 设置API文档描述
                        .termsOfService(openApiProperties.getTermsOfService())   // 设置服务条款链接
                        .contact(new Contact()                                    // 设置联系人信息
                                .name(openApiProperties.getContactName())        // 联系人姓名
                                .url(openApiProperties.getContactUrl())          // 联系人网址
                                .email(openApiProperties.getContactEmail()))     // 联系人邮箱
                        .version(openApiProperties.getVersion()))                // 设置API版本
                .externalDocs(new ExternalDocumentation()                        // 设置外部文档
                        .description(openApiProperties.getExternalDescription()) // 外部文档描述
                        .url(openApiProperties.getExternalUrl()));              // 外部文档链接
    }

    /**
     * 登录授权接口文档分组Bean
     * 
     * 包含功能：
     * - 用户登录接口
     * - 权限验证接口
     * - Token刷新接口
     * - 退出登录接口
     * 
     * @return GroupedOpenApi 登录授权接口文档分组
     */
    @Bean
    public GroupedOpenApi authApi() {
        String[] packagedToMatch = {AUTH_PACKAGE};
        return api("2.登录授权接口文档", packagedToMatch);
    }

    /**
     * 系统管理接口文档分组Bean
     * 
     * 包含功能：
     * - 用户管理接口
     * - 角色管理接口
     * - 菜单管理接口
     * - 部门管理接口
     * - 系统配置接口
     * 
     * @return GroupedOpenApi 系统管理接口文档分组
     */
    @Bean
    public GroupedOpenApi adminApi() {
        String[] packagedToMatch = {SYSTEM_PACKAGE};
        return api("3.系统管理接口文档", packagedToMatch);
    }

    /**
     * App用户模块接口文档分组Bean
     * 
     * 包含功能：
     * - 用户注册接口
     * - 用户信息管理接口
     * - 用户设置接口
     * - 用户反馈接口
     * 
     * @return GroupedOpenApi App用户模块接口文档分组
     */
    @Bean
    public GroupedOpenApi userApi() {
        String[] packagedToMatch = {USER_PACKAGE};
        return api("4.App用户模块接口文档", packagedToMatch);
    }

    /**
     * 公共服务接口文档分组Bean
     * 
     * 包含功能：
     * - 文件上传接口
     * - 图片处理接口
     * - 短信发送接口
     * - 邮件发送接口
     * - 数据字典接口
     * 
     * @return GroupedOpenApi 公共服务接口文档分组
     */
    @Bean
    public GroupedOpenApi commonApi() {
        String[] packagedToMatch = {COMMON_PACKAGE};
        return api("5.公共服务接口文档", packagedToMatch);
    }

    /**
     * 生成代码接口文档分组Bean
     * 
     * 包含功能：
     * - 代码生成接口
     * - 模板管理接口
     * - 数据库信息接口
     * - 表结构查询接口
     * 
     * @return GroupedOpenApi 生成代码接口文档分组
     */
    @Bean
    public GroupedOpenApi generatorApi() {
        String[] packagedToMatch = {GENERATOR_PACKAGE};
        return api("6.生成代码接口文档", packagedToMatch);
    }

    /**
     * 项目接口文档分组Bean
     * 
     * 功能说明：
     * 除了上面特定模块的接口之外，其它的接口都在项目接口文档中。
     * 这个分组主要包含业务相关的API接口。
     * 
     * 包含功能：
     * - 业务模块接口
     * - 医疗相关接口
     * - 订单管理接口
     * - 报告管理接口
     * 
     * 注意：
     * 请根据实际项目情况进行自定义配置。
     * 
     * @return GroupedOpenApi 项目接口文档分组
     */
    @Bean
    public GroupedOpenApi projectApi() {
        return GroupedOpenApi.builder()
                .group("1.项目接口文档")                                    // 设置分组名称
                .addOperationCustomizer(getOperationCustomizer())          // 添加操作定制器
                .pathsToMatch("/**")                                       // 匹配所有路径
                .packagesToExclude(AUTH_PACKAGE, SYSTEM_PACKAGE,           // 排除其他模块的包
                        USER_PACKAGE, COMMON_PACKAGE, GENERATOR_PACKAGE)
                .build();
    }

    /**
     * 配置API接口分组的通用方法
     * 
     * 功能说明：
     * 创建GroupedOpenApi实例，用于将特定包下的接口
     * 分组到指定的API文档中。
     * 
     * 配置内容：
     * 1. 设置分组名称
     * 2. 添加操作定制器（如Token参数）
     * 3. 设置路径匹配规则
     * 4. 设置包扫描范围
     * 
     * @param group 分组名称，显示在API文档中
     * @param packagedToMatch 要扫描的包路径数组
     * @return GroupedOpenApi 配置好的API分组对象
     */
    private GroupedOpenApi api(String group, String[] packagedToMatch) {
        return GroupedOpenApi.builder()
                .group(group)                                      // 设置分组名称
                .addOperationCustomizer(getOperationCustomizer())  // 添加操作定制器
                .pathsToMatch("/**")                              // 匹配所有路径
                .packagesToScan(packagedToMatch)                  // 扫描指定包
                .build();
    }

    /**
     * 获取操作定制器
     * 
     * 功能说明：
     * 为所有API操作添加自定义配置，主要用于添加
     * 全局的请求参数，如Token认证参数。
     * 
     * 工作原理：
     * 1. 对每个API操作进行拦截
     * 2. 自动添加Token请求头参数
     * 3. 在Swagger UI中显示认证参数输入框
     * 4. 方便用户在文档中直接测试需要认证的接口
     * 
     * 使用效果：
     * - 在Swagger UI中，每个接口都会显示Token参数
     * - 用户可以输入Token值进行接口测试
     * - 避免手动为每个接口添加认证参数
     * 
     * @return OperationCustomizer 操作定制器实例
     */
    public OperationCustomizer getOperationCustomizer() {
        return (operation, handlerMethod) -> {
            // 为每个API操作添加Token参数
            operation.addParametersItem(tokenParameter);
            return operation;
        };
    }
}
