// 定义包名，指定当前类所在的包路径
package com.fourth.medical.auth.controller;

// 导入医生登录DTO，用于接收医生登录请求参数
import com.fourth.medical.auth.dto.DoctorLoginDto;
// 导入医生登录服务接口，提供医生登录相关业务逻辑
import com.fourth.medical.auth.service.DoctorLoginService;
// 导入登录工具类，用于获取登录信息和操作
import com.fourth.medical.auth.util.LoginUtil;
// 导入Token工具类，用于Token操作
import com.fourth.medical.auth.util.TokenUtil;
// 导入登录令牌视图对象，用于返回登录令牌信息
import com.fourth.medical.auth.vo.LoginTokenVo;
// 导入登录视图对象，用于返回登录用户信息
import com.fourth.medical.auth.vo.LoginVo;
// 导入登录常量类，定义登录相关的常量值
import com.fourth.medical.common.constant.LoginConstant;
// 导入框架响应结果类，用于统一API响应格式
import com.fourth.medical.framework.response.ApiResult;
// 导入医生报告服务接口，用于获取报告相关数据
import com.fourth.medical.medical.service.DoctorReportService;
// 导入医生排班服务接口，用于获取排班相关数据
import com.fourth.medical.medical.service.DoctorScheduleService;
// 导入医生仪表盘视图对象，用于返回仪表盘数据
import com.fourth.medical.medical.vo.DoctorDashboardVo;
// 导入系统菜单服务接口，用于获取菜单信息
import com.fourth.medical.system.service.SysMenuService;
// 导入系统导航菜单树视图对象，用于返回菜单树
import com.fourth.medical.system.vo.SysNavMenuTreeVo;
// 导入Cookie工具类，用于处理Cookie相关操作
import com.fourth.medical.util.CookieUtil;
// 导入Swagger操作注解，用于API文档生成
import io.swagger.v3.oas.annotations.Operation;
// 导入Swagger标签注解，用于API分组
import io.swagger.v3.oas.annotations.tags.Tag;
// 导入Lombok日志注解，自动生成日志对象
import lombok.extern.slf4j.Slf4j;
// 导入Spring自动装配注解
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring Web注解，用于REST API开发
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 导入Servlet HTTP请求接口
import javax.servlet.http.HttpServletRequest;
// 导入Servlet HTTP响应接口
import javax.servlet.http.HttpServletResponse;
// 导入验证注解，用于参数校验
import javax.validation.Valid;
// 导入Java集合类
import java.util.List;
// 导入Java日期类
import java.time.LocalDate;

/**
 * 医生端登录控制器
 * 
 * 功能说明：
 * 提供医生端的登录、获取医生信息、获取导航菜单、获取仪表盘数据、退出登录等功能。
 * 专门为医生用户提供认证和基础数据服务。
 * 
 * 主要功能：
 * 1. 医生登录认证 - 手机号+密码登录
 * 2. 获取医生信息 - 获取当前登录医生的详细信息
 * 3. 获取导航菜单 - 获取医生端的系统菜单
 * 4. 获取仪表盘数据 - 获取医生工作台的统计数据
 * 5. 退出登录 - 清除登录状态和缓存
 * 
 * 业务流程：
 * 1. 医生使用手机号和密码登录
 * 2. 系统验证身份并生成Token
 * 3. 前端获取医生信息和菜单权限
 * 4. 显示医生工作台和相关数据
 * 5. 医生可以查看待处理报告和排班信息
 * 
 * 权限说明：
 * - 医生只能查看和处理自己负责的报告
 * - 根据医生角色显示相应的菜单权限
 * - 数据范围限制在医生所属的科室或医院
 * 
 * 技术特点：
 * - 支持Token自动刷新
 * - 使用Redis缓存登录信息
 * - 支持Cookie和Header双重Token传递
 * - 完整的异常处理和日志记录
 * 
 * @author fourth
 * @date 2025-07-09
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j // 自动生成日志对象log
@RestController // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "医生登录") // Swagger文档标签，用于API分组
@RequestMapping("/doctor") // 设置控制器的基础请求路径
public class DoctorLoginController {

    /**
     * 医生登录服务
     * 处理医生登录、获取医生信息、退出登录等业务逻辑
     */
    @Autowired
    private DoctorLoginService doctorLoginService;
    
    /**
     * 系统菜单服务
     * 用于获取医生端的导航菜单和权限信息
     */
    @Autowired
    private SysMenuService sysMenuService;
    
    /**
     * 医生报告服务
     * 用于获取医生相关的报告统计数据
     */
    @Autowired
    private DoctorReportService doctorReportService;
    
    /**
     * 医生排班服务
     * 用于获取医生的排班信息和统计数据
     */
    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 医生登录
     * 
     * 功能说明：
     * 医生使用手机号和密码进行登录验证，成功后生成Token
     * 并设置到Cookie中，供后续请求使用。
     * 
     * 登录流程：
     * 1. 验证手机号和密码格式
     * 2. 检查医生账号是否存在
     * 3. 验证密码是否正确
     * 4. 检查账号状态（是否禁用）
     * 5. 生成Token并缓存登录信息
     * 6. 设置Cookie并返回Token
     * 
     * 安全措施：
     * - 密码加密存储和验证
     * - 登录失败次数限制
     * - Token有效期控制
     * - 日志记录登录行为
     * 
     * @param doctorLoginDto 医生登录数据传输对象，包含手机号和密码
     * @param request HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于设置Cookie
     * @return ApiResult<LoginTokenVo> 返回登录令牌的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/login") // 处理POST请求，路径为/doctor/login
    @Operation(summary = "医生登录") // Swagger文档说明
    public ApiResult<LoginTokenVo> login(@Valid @RequestBody DoctorLoginDto doctorLoginDto, HttpServletRequest request, HttpServletResponse response) {
        log.info("医生登录请求: {}", doctorLoginDto.getMobile());
        
        // 调用医生登录服务进行手机号+密码登录验证
        LoginTokenVo loginTokenVo = doctorLoginService.login(doctorLoginDto);
        log.info("医生登录成功: {}", loginTokenVo.getToken());
        
        // 将Token添加到Cookie中，供后续请求使用
        CookieUtil.addCookie(LoginConstant.DOCTOR_COOKIE_TOKEN_NAME, loginTokenVo.getToken(), request, response);
        
        return ApiResult.success(loginTokenVo);
    }

    /**
     * 获取医生信息
     * 
     * 功能说明：
     * 获取当前登录医生的详细信息，包括基本资料、科室信息、
     * 权限信息等。需要Token验证。
     * 
     * 获取流程：
     * 1. 从Token中获取医生ID
     * 2. 从Redis缓存中获取登录信息
     * 3. 设置到ThreadLocal中供后续使用
     * 4. 调用服务层获取完整的医生信息
     * 5. 返回医生详细信息
     * 
     * 数据内容：
     * - 医生基本信息（姓名、手机号、邮箱等）
     * - 科室信息（科室名称、科室ID等）
     * - 权限信息（角色、菜单权限等）
     * - 工作信息（职称、专业等）
     * 
     * 缓存机制：
     * - 登录信息缓存在Redis中
     * - 使用ThreadLocal提供线程级别的缓存
     * - 减少数据库查询提高性能
     * 
     * @return ApiResult<LoginVo> 返回医生信息的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @GetMapping("/info") // 处理GET请求，路径为/doctor/info
    @Operation(summary = "获取医生信息") // Swagger文档说明
    public ApiResult<LoginVo> getLoginUserInfo() {
        log.info("开始执行getLoginUserInfo方法");
        try {
            // 从Token中获取登录信息并设置到ThreadLocal中
            String token = TokenUtil.getToken();
            log.info("获取到的token: {}", token);
            
            if (org.apache.commons.lang3.StringUtils.isNotBlank(token)) {
                log.info("token不为空，从Redis获取登录信息");
                
                // 从Redis中获取完整的登录信息
                LoginVo redisLoginVo = LoginUtil.getLoginVo(token);
                
                if (redisLoginVo != null) {
                    log.info("手动将登录信息设置到LoginCache: userId={}", redisLoginVo.getUserId());
                    // 设置到ThreadLocal中，供后续业务逻辑使用
                    com.fourth.medical.auth.cache.LoginCache.set(redisLoginVo);
                }
            }
            
            log.info("准备获取医生信息");
            // 调用服务层获取医生详细信息
            LoginVo loginVo = doctorLoginService.getLoginDoctorInfo();
            log.info("成功获取医生信息: userId={}, username={}", loginVo.getUserId(), loginVo.getUsername());
            
            return ApiResult.success(loginVo);
        } catch (Exception e) {
            log.error("获取医生信息发生异常", e);
            throw e;
        }
    }

    /**
     * 医生退出登录
     * 
     * 功能说明：
     * 处理医生的退出登录请求，清除服务端的登录状态缓存
     * 和客户端的Cookie信息。
     * 
     * 退出流程：
     * 1. 从Token中获取医生信息
     * 2. 清除Redis中的登录缓存
     * 3. 清除ThreadLocal中的登录信息
     * 4. 删除客户端Cookie中的Token
     * 5. 记录退出日志
     * 
     * 安全考虑：
     * - 服务端Token立即失效
     * - 客户端Cookie清除
     * - 防止Token被恶意使用
     * - 完整的操作日志记录
     * 
     * @param request HTTP请求对象，用于获取请求信息
     * @param response HTTP响应对象，用于删除Cookie
     * @return ApiResult<Boolean> 返回退出结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/logout") // 处理POST请求，路径为/doctor/logout
    @Operation(summary = "医生退出") // Swagger文档说明
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("医生退出登录");
        
        // 调用服务层清除登录缓存和会话信息
        doctorLoginService.logout();
        
        // 从Cookie中删除Token，清除客户端的登录状态
        CookieUtil.deleteCookie(LoginConstant.DOCTOR_COOKIE_TOKEN_NAME, request, response);
        
        return ApiResult.success();
    }

    /**
     * 获取医生导航菜单
     * 
     * 功能说明：
     * 获取当前登录医生的导航菜单树，根据医生的角色和权限
     * 返回相应的菜单结构。
     * 
     * 菜单获取流程：
     * 1. 验证医生登录状态
     * 2. 从Token中获取医生角色信息
     * 3. 根据角色权限查询可访问的菜单
     * 4. 构建菜单树结构
     * 5. 返回菜单树数据
     * 
     * 权限控制：
     * - 根据医生角色显示不同菜单
     * - 支持菜单层级权限控制
     * - 隐藏无权限访问的菜单项
     * - 动态菜单权限验证
     * 
     * 菜单结构：
     * - 支持多级菜单嵌套
     * - 包含菜单图标、路径、名称等信息
     * - 支持菜单排序和显示控制
     * 
     * @return ApiResult<List<SysNavMenuTreeVo>> 返回菜单树的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @PostMapping("/sysMenu/getNavMenuTreeList") // 处理POST请求，路径为/doctor/sysMenu/getNavMenuTreeList
    @Operation(summary = "获取医生导航菜单") // Swagger文档说明
    public ApiResult<List<SysNavMenuTreeVo>> getNavMenuTreeList() {
        log.info("开始执行医生getNavMenuTreeList方法");
        try {
            // 获取并验证Token，确保用户已登录
            String token = TokenUtil.getToken();
            log.info("获取到的token: {}", token);
            
            if (org.apache.commons.lang3.StringUtils.isNotBlank(token)) {
                log.info("token不为空，从Redis获取登录信息");
                
                // 从Redis中获取登录信息，包含角色权限数据
                LoginVo redisLoginVo = LoginUtil.getLoginVo(token);
                
                if (redisLoginVo != null) {
                    log.info("手动将登录信息设置到LoginCache: userId={}", redisLoginVo.getUserId());
                    // 设置到ThreadLocal中，供权限验证使用
                    com.fourth.medical.auth.cache.LoginCache.set(redisLoginVo);
                } else {
                    log.warn("从Redis获取的登录信息为null");
                }
            } else {
                log.warn("token为空，可能未登录");
            }
            
            log.info("调用sysMenuService.getNavMenuTreeList()获取菜单");
            // 调用系统菜单服务获取导航菜单树
            List<SysNavMenuTreeVo> list = sysMenuService.getNavMenuTreeList();
            log.info("获取菜单成功，菜单数量: {}", list != null ? list.size() : 0);
            
            if (list == null || list.isEmpty()) {
                log.warn("获取到的菜单为空，可能是权限问题");
            }
            
            return ApiResult.success(list);
        } catch (Exception e) {
            log.error("获取菜单信息发生异常", e);
            throw e;
        }
    }

    /**
     * 获取医生仪表盘数据
     * 
     * 功能说明：
     * 获取医生工作台的统计数据，包括待处理报告数量、
     * 今日排班情况、本月报告统计等信息。
     * 
     * 数据统计内容：
     * 1. 待处理报告数量 - 需要医生审核的报告数
     * 2. 今日排班状态 - 今天是否有排班
     * 3. 本月报告数量 - 本月已处理的报告总数
     * 4. 其他统计数据 - 根据业务需求扩展
     * 
     * 数据获取流程：
     * 1. 验证医生登录状态
     * 2. 获取当前医生ID
     * 3. 并行查询各项统计数据
     * 4. 组装仪表盘数据对象
     * 5. 返回统计结果
     * 
     * 权限控制：
     * - 只能查看自己负责的数据
     * - 数据范围限制在医生权限内
     * - 实时数据更新
     * 
     * 性能优化：
     * - 统计数据可以考虑缓存
     * - 异步查询提高响应速度
     * - 数据库查询优化
     * 
     * @return ApiResult<DoctorDashboardVo> 返回仪表盘数据的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @GetMapping("/dashboard") // 处理GET请求，路径为/doctor/dashboard
    @Operation(summary = "获取医生仪表盘数据") // Swagger文档说明
    public ApiResult<DoctorDashboardVo> getDashboard() {
        log.info("获取医生仪表盘数据");
        
        // 获取当前登录医生信息
        LoginVo loginVo = null;
        try {
            // 从Token中获取登录信息
            String token = TokenUtil.getToken();
            if (org.apache.commons.lang3.StringUtils.isBlank(token)) {
                log.error("获取登录医生信息失败，请先登录");
                return ApiResult.fail("请先登录");
            }
            
            // 从Redis中获取完整的登录信息
            loginVo = LoginUtil.getLoginVo(token);
            if (loginVo == null) {
                log.error("获取登录医生信息失败，请先登录");
                return ApiResult.fail("请先登录");
            }
        } catch (Exception e) {
            log.error("获取登录医生信息失败", e);
            return ApiResult.fail("获取登录信息失败：" + e.getMessage());
        }
        
        // 获取医生ID用于数据查询
        Long doctorId = loginVo.getUserId();
        log.info("获取医生[{}]仪表盘数据", doctorId);
        
        // 创建仪表盘数据对象
        DoctorDashboardVo dashboardVo = new DoctorDashboardVo();
        
        // 获取待处理报告数量
        // 查询分配给该医生但尚未审核的报告数量
        Integer pendingReports = doctorReportService.countPendingReportsByDoctorId(doctorId);
        dashboardVo.setPendingReports(pendingReports);
        
        // 判断今日是否有排班
        // 检查医生今天是否有工作安排
        Boolean hasTodaySchedule = doctorScheduleService.checkDoctorHasScheduleToday(doctorId);
        dashboardVo.setHasTodaySchedule(hasTodaySchedule);
        
        // 获取本月体检报告数量
        // 统计医生本月已处理的报告总数
        Integer monthReports = doctorReportService.countMonthReportsByDoctorId(doctorId);
        dashboardVo.setMonthReports(monthReports);
        
        return ApiResult.success(dashboardVo);
    }
} 