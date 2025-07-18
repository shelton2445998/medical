package com.fourth.medical;

import com.fourth.medical.util.IpUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

/**
 * Spring Boot应用程序启动类
 * 
 * 该类是医疗系统后端服务的入口点，负责启动整个Spring Boot应用程序。
 * 系统提供医疗预约、用户管理、体检报告等功能。
 * 
 * 启动后会自动打印系统信息，包括：
 * - API文档访问地址（Swagger UI和Knife4j）
 * - 本机和网络IP地址访问链接
 * - 默认管理员账号密码
 * 
 * @author geekidea
 * @version 1.0
 * @date 2022-3-16
 * @since JDK 1.8
 */
@EnableAsync // 启用Spring异步任务支持，允许使用@Async注解进行异步方法调用
@SpringBootApplication // Spring Boot主配置注解，包含@Configuration、@EnableAutoConfiguration、@ComponentScan
public class SpringBootPlusApplication {

    /**
     * URL路径分隔符常量
     * 用于拼接URL路径时的分隔符
     */
    private static final String BACKSLASH = "/";

    /**
     * 应用程序主入口方法
     * 
     * 启动Spring Boot应用程序，并在启动完成后打印系统相关信息。
     * 
     * @param args 命令行参数，支持Spring Boot标准参数如--server.port等
     * @throws Exception 启动过程中可能抛出的异常
     */
    public static void main(String[] args) throws Exception {
        // 启动Spring Boot应用程序上下文
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootPlusApplication.class, args);
        
        // 打印ASCII艺术字LOGO，展示系统启动成功信息
        System.out.println("  _____ _______       _____ _______    _____ _    _  _____ _____ ______  _____ _____ \n" +
                " / ____|__   __|/\\   |  __ \\__   __|  / ____| |  | |/ ____/ ____|  ____|/ ____/ ____|\n" +
                "| (___    | |  /  \\  | |__) | | |    | (___ | |  | | |   | |    | |__  | (___| (___  \n" +
                " \\___ \\   | | / /\\ \\ |  _  /  | |     \\___ \\| |  | | |   | |    |  __|  \\___ \\\\___ \\ \n" +
                " ____) |  | |/ ____ \\| | \\ \\  | |     ____) | |__| | |___| |____| |____ ____) |___) |\n" +
                "|_____/   |_/_/    \\_\\_|  \\_\\ |_|    |_____/ \\____/ \\_____\\_____|______|_____/_____/ \n");
        
        // 打印项目相关信息，包括访问地址和默认账号密码
        printlnProjectInfo(context);
    }

    /**
     * 打印项目启动信息
     * 
     * 该方法会输出以下信息：
     * 1. Knife4j API文档地址（本地localhost访问）
     * 2. Swagger UI文档地址（本地localhost访问）
     * 3. 本机所有网络接口的IP地址对应的API文档访问地址
     * 4. 系统默认管理员账号和密码
     * 5. Swagger文档访问密码
     * 
     * @param context Spring应用程序上下文，用于获取配置信息
     */
    private static void printlnProjectInfo(ConfigurableApplicationContext context) {
        try {
            // 获取Spring环境配置对象
            ConfigurableEnvironment environment = context.getEnvironment();
            
            // 从配置文件中获取服务器端口号，默认为8080
            String serverPort = environment.getProperty("server.port");
            
            // 从配置文件中获取应用上下文路径，默认为"/"
            String contextPath = environment.getProperty("server.servlet.context-path");
            
            // 如果上下文路径不是根路径，则在末尾添加斜杠
            if (!BACKSLASH.equals(contextPath)) {
                contextPath = contextPath + BACKSLASH;
            }
            
            // 构建并打印本地Knife4j API文档访问地址
            String localhostDocUrl = "\nhttp://localhost:" + serverPort + contextPath + "doc.html";
            System.out.println(localhostDocUrl);
            
            // 构建并打印本地Swagger UI文档访问地址
            String localhostSwaggerUrl = "http://localhost:" + serverPort + contextPath + "swagger-ui/index.html";
            System.out.println(localhostSwaggerUrl);
            
            // 获取本机所有可用的IPv4地址列表
            List<String> ipV4List = IpUtil.getLocalhostIpList();
            
            // 如果存在可用的IP地址，则为每个IP地址生成对应的API文档访问链接
            if (CollectionUtils.isNotEmpty(ipV4List)) {
                for (String ip : ipV4List) {
                    // 为每个网络接口IP生成API文档访问地址
                    String ipUrl = "http://" + ip + ":" + serverPort + contextPath + "doc.html";
                    System.out.println(ipUrl);
                }
            }
            
            // 打印系统默认登录信息
            System.out.println("\n账号：admin");
            System.out.println("密码：123456");
            
            // 打印Swagger文档访问密码（MD5加密后的123456）
            System.out.println("swagger密码：e10adc3949ba59abbe56e057f20f883e\n");
            
        } catch (Exception e) {
            // 如果打印项目信息过程中出现异常，打印异常堆栈信息
            // 不影响应用程序正常启动，只是无法显示访问信息
            e.printStackTrace();
        }
    }
}
