package com.fourth.medical.medical.controller;

import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppCheckitemDetailQuery;
import com.fourth.medical.medical.service.CheckitemDetailService;
import com.fourth.medical.medical.vo.AppCheckitemDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * App检查项明细 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供检查项明细查询服务，包括检查项参数、检查方法、注意事项等</li>
 *   <li>支持检查项明细的详情查看和分页查询功能</li>
 *   <li>为移动端用户提供详细的检查项信息查看服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>检查项明细查询：支持检查项详细配置的查询</li>
 *   <li>参数展示：清晰展示检查项的具体参数和检查方法</li>
 *   <li>注意事项：显示检查项的注意事项和准备要求</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>套餐选择：用户选择体检套餐后查看检查项明细</li>
 *   <li>明细查看：用户查看具体的检查项明细详情</li>
 *   <li>参数了解：用户了解检查项的具体参数和检查方法</li>
 *   <li>注意事项：用户查看检查项的注意事项和准备要求</li>
 *   <li>分页查询：支持分页加载检查项明细数据</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>移动端用户认证：通过用户登录状态进行身份验证</li>
 *   <li>数据权限：所有用户都可以查看检查项明细信息</li>
 *   <li>接口安全：通过Token验证确保接口调用安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>Token验证：通过JWT Token进行身份验证</li>
 *   <li>数据缓存：合理使用缓存提高查询性能</li>
 *   <li>数据准确性：确保检查项明细数据的准确性和完整性</li>
 *   <li>内容安全：对检查项内容进行安全过滤</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>智能推荐：可扩展支持基于用户特征的检查项推荐</li>
 *   <li>视频说明：可扩展支持检查项的视频说明功能</li>
 *   <li>在线咨询：可扩展支持检查项相关的在线咨询</li>
 *   <li>预约提醒：可扩展支持检查前的准备提醒功能</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@RestController
@Tag(name = "App检查项明细")
@RequestMapping("/app/checkitemDetail")
public class AppCheckitemDetailController {

    @Autowired
    private CheckitemDetailService checkitemDetailService;

    /**
     * 获取App检查项明细详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App检查项明细详情")
    @PostMapping("/getAppCheckitemDetail/{id}")
    public ApiResult<AppCheckitemDetailVo> getAppCheckitemDetail(@PathVariable Long id) {
        log.info("获取App检查项明细详情：{}", id);
        AppCheckitemDetailVo appCheckitemDetailVo = checkitemDetailService.getAppCheckitemDetailById(id);
        return ApiResult.success(appCheckitemDetailVo);
    }

    /**
     * 获取App检查项明细分页列表
     *
     * query
     * @return
     * @throws Exception
     */
    @Operation(summary = "获取App检查项明细分页列表")
    @PostMapping("/getAppCheckitemDetailPage")
    public ApiResult<AppCheckitemDetailVo> getAppCheckitemDetailPage(@Valid @RequestBody AppCheckitemDetailQuery query) {
        log.info("获取App检查项明细分页列表：{}", query);
        Paging<AppCheckitemDetailVo> paging = checkitemDetailService.getAppCheckitemDetailPage(query);
        return ApiResult.success(paging);
    }

}
