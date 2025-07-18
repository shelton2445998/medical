package com.fourth.medical.system.controller;

import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.query.SysDictAppQuery;
import com.fourth.medical.system.service.SysDictService;
import com.fourth.medical.system.vo.AppSysDictVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * App字典数据 控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>为移动端APP提供字典数据查询服务，包括各种系统字典数据的获取</li>
 *   <li>支持字典数据的列表查询功能，为移动端提供统一的数据字典服务</li>
 *   <li>为移动端用户提供便捷的字典数据访问服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>移动端适配：专门为移动端APP设计的接口</li>
 *   <li>字典数据查询：支持字典数据的列表查询</li>
 *   <li>数据分类：支持不同类型的字典数据分类</li>
 *   <li>用户友好：提供简洁易用的移动端数据格式</li>
 *   <li>性能优化：针对移动端网络环境进行性能优化</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>用户登录：移动端用户登录后获取字典数据</li>
 *   <li>数据查询：用户查询需要的字典数据</li>
 *   <li>数据展示：在移动端展示字典数据</li>
 *   <li>数据同步：与后端字典数据保持同步</li>
 *   <li>缓存优化：合理使用缓存提高查询性能</li>
 * </ol>
 * 
 * <p><strong>权限控制：</strong></p>
 * <ul>
 *   <li>移动端用户认证：通过用户登录状态进行身份验证</li>
 *   <li>数据权限：所有用户都可以查看字典数据</li>
 *   <li>接口安全：通过Token验证确保接口调用安全</li>
 * </ul>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>数据传输：使用HTTPS确保数据传输安全</li>
 *   <li>Token验证：通过JWT Token进行身份验证</li>
 *   <li>数据缓存：合理使用缓存提高查询性能</li>
 *   <li>数据准确性：确保字典数据的准确性和时效性</li>
 *   <li>访问控制：确保字典数据访问的安全性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>智能推荐：可扩展支持基于用户偏好的字典数据推荐</li>
 *   <li>数据同步：可扩展支持字典数据的实时同步</li>
 *   <li>个性化：可扩展支持个性化的字典数据展示</li>
 *   <li>离线缓存：可扩展支持字典数据的离线缓存功能</li>
 * </ul>
 *
 * @author geekidea
 * @since 2023-11-25
 */
@Slf4j
@RestController
@Tag(name = "App字典数据")
@RequestMapping("/app/sysDict")
public class SysDictAppController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * App字典数据列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getAppSysDictList")
    @Operation(summary = "App字典数据列表")
    public ApiResult<AppSysDictVo> getAppSysDictList(@Valid @RequestBody SysDictAppQuery query) {
        Map<String, List<AppSysDictVo>> map = sysDictService.getAppSysDictList(query);
        return ApiResult.success(map);
    }

}
