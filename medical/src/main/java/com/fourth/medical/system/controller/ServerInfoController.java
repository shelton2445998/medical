package com.fourth.medical.system.controller;

import cn.hutool.system.SystemUtil;
import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.framework.bean.ProjectInfo;
import com.fourth.medical.framework.bean.ServerInfo;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.util.ServerInfoUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器信息 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供服务器和项目的运行状态信息，包括系统资源、项目配置、运行环境等</li>
 *   <li>支持服务器信息的查询功能，为系统监控和运维提供数据支撑</li>
 *   <li>提供项目运行状态的实时监控服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>服务器监控：实时监控服务器运行状态和资源使用情况</li>
 *   <li>项目信息：提供项目配置和运行环境信息</li>
 *   <li>系统资源：监控CPU、内存、磁盘等系统资源使用情况</li>
 *   <li>权限控制：基于角色的访问控制，确保数据安全</li>
 *   <li>实时更新：提供实时的服务器状态信息</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>信息收集：系统自动收集服务器和项目运行信息</li>
 *   <li>信息查询：管理员查询服务器和项目运行状态</li>
 *   <li>状态监控：实时监控系统资源使用情况</li>
 *   <li>性能分析：分析系统性能和资源使用趋势</li>
 *   <li>告警处理：根据监控数据进行告警处理</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>server:info - 查看服务器信息权限</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>权限校验：每个接口都有对应的权限控制</li>
 *   <li>数据脱敏：对敏感信息进行适当脱敏处理</li>
 *   <li>访问控制：确保只有授权用户才能访问服务器信息</li>
 *   <li>信息安全：保护服务器配置和系统信息的安全</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>监控告警：可扩展支持基于监控数据的告警机制</li>
 *   <li>性能分析：可扩展支持更详细的性能分析功能</li>
 *   <li>历史记录：可扩展支持服务器状态历史记录</li>
 *   <li>自动化运维：可扩展支持基于监控数据的自动化运维</li>
 * </ul>
 *
 * @author geekidea
 * @date 2023/12/16
 */
@Slf4j
@RestController
@Tag(name = "服务信息")
@RequestMapping("/admin/serverInfo")
public class ServerInfoController {

    @Autowired
    private Environment environment;

    /**
     * 获取服务信息详情
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/getServerInfo")
    @Operation(summary = "服务信息详情")
    @Permission("server:info")
    public ApiResult<ServerInfo> getServerInfo() {
        ServerInfo serverInfo = ServerInfoUtil.getServerInfo();
        // 获取项目信息
        // 项目名称
        String name = environment.getProperty("spring.application.name");
        // 端口号
        String port = environment.getProperty("server.port");
        // 上下文路径
        String contextPath = environment.getProperty("server.servlet.context-path");
        // 激活的环境
        String active = environment.getProperty("spring.profiles.active");
        // 当前项目路径
        String userDir = SystemUtil.get("user.dir");
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName(name);
        projectInfo.setPort(port);
        projectInfo.setContextPath(contextPath);
        projectInfo.setActive(active);
        projectInfo.setUserDir(userDir);
        serverInfo.setProjectInfo(projectInfo);
        log.info("serverInfo:" + serverInfo);
        return ApiResult.success(serverInfo);
    }


}
