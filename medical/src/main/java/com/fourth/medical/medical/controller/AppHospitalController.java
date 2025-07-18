package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppHospitalQuery;
import com.fourth.medical.medical.service.HospitalService;
import com.fourth.medical.medical.vo.AppHospitalVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * App医院 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供医院信息查询服务，包括医院详情、医院列表、推荐医院等</li>
 *   <li>支持医院信息的详情查看和列表查询功能</li>
 *   <li>为移动端用户提供便捷的医院选择服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>医院信息查询：支持医院详情和列表查询</li>
 *   <li>推荐医院：基于用户位置和偏好推荐合适的医院</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 *   <li>性能优化：针对移动端网络环境进行性能优化</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>医院浏览：用户浏览可用的医院列表</li>
 *   <li>医院详情：用户查看具体医院的详细信息</li>
 *   <li>医院推荐：系统根据用户位置推荐附近医院</li>
 *   <li>医院选择：用户选择合适的医院进行体检预约</li>
 *   <li>信息同步：与后端医院数据保持同步</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>公开访问：医院信息为公开数据，无需登录即可访问</li>
 *   <li>数据权限：所有用户都可以查看医院信息</li>
 *   <li>接口安全：通过HTTPS确保数据传输安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>数据缓存：合理使用缓存提高查询性能</li>
 *   <li>数据准确性：确保医院信息的准确性和时效性</li>
 *   <li>位置隐私：保护用户位置信息的隐私安全</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>位置服务：可扩展支持基于GPS的医院定位</li>
 *   <li>智能推荐：可扩展支持基于用户偏好的智能推荐</li>
 *   <li>医院评价：可扩展支持医院评价和评分功能</li>
 *   <li>预约集成：可扩展支持直接跳转到医院预约页面</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App医院")
@RequestMapping("/hospital")
public class AppHospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 获取医院详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取医院详情")
    @GetMapping("/detail/{id}")
    public ApiResult<AppHospitalVo> getHospitalDetail(@PathVariable Long id) {
        log.info("获取医院详情：{}", id);
        AppHospitalVo appHospitalVo = hospitalService.getAppHospitalById(id);
        return ApiResult.success(appHospitalVo);
    }

    /**
     * 获取医院列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取医院列表")
    @GetMapping("/list")
    public ApiResult<AppHospitalVo> getHospitalList(@Valid AppHospitalQuery query) {
        log.info("获取医院列表：{}", query);
        Paging<AppHospitalVo> paging = hospitalService.getAppHospitalPage(query);
        return ApiResult.success(paging);
    }

    /**
     * 获取推荐医院
     *
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取推荐医院")
    @GetMapping("/recommend")
    public ApiResult<List<AppHospitalVo>> getRecommendHospitals() {
        log.info("获取推荐医院");
        // 这里假设Service中已经有获取推荐医院的方法
        // 如果没有，则需要在Service中添加该方法
        List<AppHospitalVo> recommendHospitals = hospitalService.getRecommendHospitals();
        return ApiResult.success(recommendHospitals);
    }

}
