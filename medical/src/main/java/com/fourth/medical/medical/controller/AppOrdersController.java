// 定义包名，指定当前类所在的包路径
package com.fourth.medical.medical.controller;

// 导入分页工具类，用于处理分页查询
import com.fourth.medical.framework.page.Paging;
// 导入框架响应结果类，用于统一API响应格式
import com.fourth.medical.framework.response.ApiResult;
// 导入App订单DTO，用于接收前端传递的订单数据
import com.fourth.medical.medical.dto.AppOrdersDto;
// 导入App订单查询对象，用于接收查询条件
import com.fourth.medical.medical.query.AppOrdersQuery;
// 导入订单服务接口，提供订单相关业务逻辑
import com.fourth.medical.medical.service.OrdersService;
// 导入体检报告服务接口，提供报告相关业务逻辑
import com.fourth.medical.medical.service.ReportService;
// 导入体检报告项目服务接口，提供报告项目相关业务逻辑
import com.fourth.medical.medical.service.ReportItemService;
// 导入医生排班服务接口，提供医生排班相关业务逻辑
import com.fourth.medical.medical.service.DoctorScheduleService;
// 导入App订单视图对象，用于返回订单信息
import com.fourth.medical.medical.vo.AppOrdersVo;
// 导入Token工具类，用于从请求中获取用户Token
import com.fourth.medical.auth.util.TokenUtil;
// 导入Swagger操作注解，用于API文档生成
import io.swagger.v3.oas.annotations.Operation;
// 导入Swagger标签注解，用于API分组
import io.swagger.v3.oas.annotations.tags.Tag;
// 导入Lombok日志注解，自动生成日志对象
import lombok.extern.slf4j.Slf4j;
// 导入Apache Commons字符串工具类
import org.apache.commons.lang3.StringUtils;
// 导入Spring自动装配注解
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring Web注解，用于REST API开发
import org.springframework.web.bind.annotation.*;

// 导入Servlet HTTP请求接口
import javax.servlet.http.HttpServletRequest;
// 导入验证注解，用于参数校验
import javax.validation.Valid;
// 导入Java集合类
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * App体检预约订单控制器
 * 
 * 功能说明：
 * 处理移动端用户的体检预约相关业务，包括创建预约、查询预约、取消预约等功能。
 * 该控制器专门服务于移动端用户，提供便捷的体检预约服务。
 * 
 * 主要功能：
 * 1. 创建体检预约订单 - 用户选择体检套餐和时间进行预约
 * 2. 查询用户预约列表 - 获取用户的所有预约记录
 * 3. 获取预约详情 - 查看特定预约的详细信息
 * 4. 取消预约 - 取消已创建的预约订单
 * 5. 修改预约 - 修改预约时间或套餐信息
 * 
 * 业务流程：
 * 1. 用户在移动端选择体检套餐和医院
 * 2. 选择可用的体检时间和医生
 * 3. 填写个人信息和联系方式
 * 4. 创建预约订单，生成订单号
 * 5. 系统自动创建体检报告模板
 * 6. 根据套餐内容生成体检项目
 * 7. 发送预约成功通知
 * 
 * 订单状态管理：
 * - 待确认：刚创建的预约，等待医院确认
 * - 已确认：医院已确认，等待体检
 * - 体检中：正在进行体检
 * - 已完成：体检完成，可查看报告
 * - 已取消：用户或医院取消预约
 * 
 * @author fourth
 * @since 2025-07-09
 * @version 1.0
 */
@Slf4j // 自动生成日志对象log
@RestController // 标识这是一个REST控制器，返回JSON格式数据
@Tag(name = "App体检预约") // Swagger文档标签，用于API分组
@RequestMapping("/app/appointment") // 设置控制器的基础请求路径
public class AppOrdersController {

    /**
     * 订单服务
     * 处理订单的创建、查询、修改、取消等业务逻辑
     */
    @Autowired
    private OrdersService ordersService;
    
    /**
     * 体检报告服务
     * 处理体检报告的创建、查询、更新等业务逻辑
     */
    @Autowired
    private ReportService reportService;
    
    /**
     * 体检报告项目服务
     * 处理体检报告中各个检查项目的管理
     */
    @Autowired
    private ReportItemService reportItemService;
    
    /**
     * 医生排班服务
     * 处理医生排班信息的查询和管理
     */
    @Autowired
    private DoctorScheduleService doctorScheduleService;

    /**
     * 创建体检预约
     * 
     * 功能说明：
     * 用户在移动端选择体检套餐、医院、时间后，创建新的体检预约订单。
     * 创建成功后，系统会自动生成体检报告模板和相关的检查项目。
     * 
     * 业务流程：
     * 1. 验证用户身份和权限
     * 2. 验证预约时间是否可用
     * 3. 验证体检套餐是否有效
     * 4. 创建预约订单
     * 5. 生成体检报告模板
     * 6. 根据套餐内容创建检查项目
     * 7. 发送预约成功通知
     * 
     * 参数说明：
     * - 用户ID：从Token中获取
     * - 体检套餐ID：用户选择的体检套餐
     * - 医院ID：用户选择的医院
     * - 预约时间：用户选择的体检时间
     * - 联系信息：用户的联系方式
     * 
     * 返回结果：
     * - 成功：返回完整的订单信息，包括订单号、状态等
     * - 失败：返回错误信息和错误代码
     * 
     * @param dto App订单数据传输对象，包含预约相关信息
     * @param request HTTP请求对象，用于获取用户Token
     * @return ApiResult<AppOrdersVo> 返回预约结果的统一响应格式
     * @throws Exception 可能抛出的异常
     */
    @Operation(summary = "创建体检预约") // Swagger文档说明
    @PostMapping("/create") // 处理POST请求，路径为/app/appointment/create
    public ApiResult<AppOrdersVo> createAppointment(@Valid @RequestBody AppOrdersDto dto, HttpServletRequest request) {
        log.info("创建体检预约：{}", dto);
        
        // 从请求中获取用户Token，用于身份验证和获取用户信息
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        // 调用订单服务创建预约订单
        // 服务层会处理业务逻辑验证、数据保存等操作
        AppOrdersVo result = ordersService.createAppOrders(dto, token);
        
        // 根据订单信息创建体检报告模板
        if (result != null && result.getId() != null) {
            // 获取订单中的检查项目ID列表
            // 这些ID用于创建对应的体检报告项目
            String checkitemIds = result.getCheckitemIds();
            
            // 如果订单创建成功且包含检查项目，则创建对应的report和report_item
            if (StringUtils.isNotBlank(checkitemIds)) {
                // 创建体检报告记录，初始状态为待检查
                // 暂不指定医生，等待医院分配
                Long reportId = reportService.createReportForOrder(
                        result.getId(),
                        result.getUserId(), 
                        checkitemIds);
                
                if (reportId != null) {
                    // 解析检查项ID列表
                    List<Long> checkItemIdList = Arrays.stream(checkitemIds.split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    
                    // 获取检查项与科室的映射关系
                    Map<Long, Long> checkItemDepartmentMap = doctorScheduleService.getCheckItemDepartmentMap(checkItemIdList);
                    
                    // 根据科室分配医生
                    Map<Long, Long> departmentDoctorMap = doctorScheduleService.assignDoctorsForDepartments(
                            result.getHospitalId(),
                            result.getAppointmentDate(),
                            checkItemDepartmentMap.values().stream().distinct().collect(Collectors.toList()));
                    
                    // 为每个检查项创建report_item记录，并根据科室分配医生
                    List<Long> createdItemIds = new ArrayList<>();
                    for (Long checkItemId : checkItemIdList) {
                        // 获取检查项对应的科室
                        Long departmentId = checkItemDepartmentMap.get(checkItemId);
                        // 获取科室对应的医生
                        Long doctorId = departmentDoctorMap.get(departmentId);
                        
                        // 创建report_item记录
                        Long reportItemId = reportItemService.createReportItemForCheckItem(
                                reportId,
                                result.getId(),
                                result.getUserId(),
                                checkItemId,
                                doctorId);
                        
                        if (reportItemId != null) {
                            createdItemIds.add(reportItemId);
                        }
                    }
                    
                    // 更新report表中的report_item_ids字段
                    if (!createdItemIds.isEmpty()) {
                        String reportItemIdsStr = createdItemIds.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","));
                        
                        reportItemService.updateReportItemIds(reportId, reportItemIdsStr);
                    }
                }
            }
        }
        
        return ApiResult.success(result);
    }

    /**
     * 获取体检预约详情
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检预约详情")
    @GetMapping("/detail/{id}")
    public ApiResult<AppOrdersVo> getAppointmentDetail(@PathVariable Long id, HttpServletRequest request) {
        log.info("获取体检预约详情：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        AppOrdersVo appOrdersVo = ordersService.getAppOrdersById(id, token);
        return ApiResult.success(appOrdersVo);
    }

    /**
     * 获取体检预约列表
     *
     * @param query
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取体检预约列表")
    @GetMapping("/list")
    public ApiResult<AppOrdersVo> getAppointmentList(@Valid AppOrdersQuery query, HttpServletRequest request) {
        log.info("获取体检预约列表：{}", query);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        Paging<AppOrdersVo> paging = ordersService.getAppOrdersPage(query, token);
        return ApiResult.success(paging);
    }

    /**
     * 取消体检预约
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "取消体检预约")
    @PutMapping("/cancel/{id}")
    public ApiResult cancelAppointment(@PathVariable Long id, HttpServletRequest request) {
        log.info("取消体检预约：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        boolean result = ordersService.cancelAppOrders(id, token);
        return ApiResult.result(result);
    }

    /**
     * 确认支付
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "确认支付")
    @PutMapping("/confirmPayment/{id}")
    public ApiResult confirmPayment(@PathVariable Long id, HttpServletRequest request) {
        log.info("确认支付：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        boolean result = ordersService.confirmPayment(id, token);
        return ApiResult.result(result);
    }

    /**
     * 删除体检预约订单
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "删除体检预约订单")
    @DeleteMapping("/delete/{id}")
    public ApiResult deleteAppointment(@PathVariable Long id, HttpServletRequest request) {
        log.info("删除体检预约订单：{}", id);
        // 从请求中获取token
        String token = TokenUtil.getToken(request);
        log.info("获取到token: {}", token);
        
        // 验证用户权限，只能删除自己的订单
        AppOrdersVo order = ordersService.getAppOrdersById(id, token);
        if (order == null) {
            return ApiResult.fail("订单不存在或无权限删除");
        }
        
        boolean result = ordersService.deleteOrders(id);
        return ApiResult.result(result);
    }
}
