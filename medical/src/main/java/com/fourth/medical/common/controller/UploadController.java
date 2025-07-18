package com.fourth.medical.common.controller;

import com.fourth.medical.auth.annotation.Login;
import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.service.UploadService;
import com.fourth.medical.common.vo.UploadVo;
import com.fourth.medical.framework.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传控制器
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供文件上传的统一接口</li>
 *   <li>支持单个和批量文件上传</li>
 *   <li>支持多种文件类型和存储方式</li>
 *   <li>提供文件上传结果反馈</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>权限控制：需要登录和文件上传权限</li>
 *   <li>多文件支持：支持单个和批量文件上传</li>
 *   <li>类型分类：支持按类型分类存储文件</li>
 *   <li>统一响应：返回标准化的上传结果</li>
 *   <li>Swagger文档：提供完整的API文档</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>用户头像上传</li>
 *   <li>医疗报告文件上传</li>
 *   <li>系统文档上传</li>
 *   <li>图片和媒体文件上传</li>
 *   <li>批量数据导入</li>
 * </ul>
 * 
 * <p><strong>权限要求：</strong></p>
 * <ul>
 *   <li>单个文件上传：sys:file:upload</li>
 *   <li>批量文件上传：sys:file:upload-batch</li>
 *   <li>需要用户登录认证</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于Spring Boot文件上传</li>
 *   <li>支持multipart/form-data格式</li>
 *   <li>集成Swagger API文档</li>
 *   <li>统一的异常处理机制</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展文件类型验证</li>
 *   <li>可添加文件大小限制</li>
 *   <li>可支持文件压缩处理</li>
 *   <li>可集成文件预览功能</li>
 * </ul>
 *
 * @author geekidea
 * @date 2023/6/18
 **/
@Slf4j
@Login
@RestController
@RequestMapping("/common/upload")
@Tag(name = "文件上传")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 单个文件上传
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>上传单个文件到服务器或云存储</li>
     *   <li>支持按类型分类存储</li>
     *   <li>返回文件上传结果信息</li>
     * </ul>
     * 
     * <p><strong>上传流程：</strong></p>
     * <ol>
     *   <li>验证用户登录状态和权限</li>
     *   <li>接收文件上传请求</li>
     *   <li>调用上传服务处理文件</li>
     *   <li>返回标准化的上传结果</li>
     * </ol>
     * 
     * <p><strong>参数说明：</strong></p>
     * <ul>
     *   <li>type：文件类型，用于分类存储（可选）</li>
     *   <li>file：要上传的文件（必需）</li>
     * </ul>
     * 
     * <p><strong>返回结果：</strong></p>
     * <ul>
     *   <li>文件访问URL</li>
     *   <li>文件存储路径</li>
     *   <li>文件大小和类型信息</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * POST /common/upload
     * Content-Type: multipart/form-data
     * 
     * type: avatar
     * file: [文件数据]
     * }</pre>
     *
     * @param type 文件类型，用于分类存储
     * @param multipartFile 要上传的文件
     * @return 文件上传结果
     * @throws Exception 上传过程中的异常
     */
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "单个文件上传")
    @Permission("sys:file:upload")
    public ApiResult<UploadVo> upload(@RequestParam(required = false) String type,
                                      @RequestPart("file") MultipartFile multipartFile) throws Exception {
        UploadVo uploadVo = uploadService.upload(type, multipartFile);
        return ApiResult.success(uploadVo);
    }

    /**
     * 多个文件上传
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>批量上传多个文件</li>
     *   <li>支持按类型分类存储</li>
     *   <li>返回所有文件的上传结果</li>
     * </ul>
     * 
     * <p><strong>上传流程：</strong></p>
     * <ol>
     *   <li>验证用户登录状态和权限</li>
     *   <li>接收批量文件上传请求</li>
     *   <li>逐个处理文件上传</li>
     *   <li>返回所有文件的上传结果列表</li>
     * </ol>
     * 
     * <p><strong>参数说明：</strong></p>
     * <ul>
     *   <li>type：文件类型，用于分类存储（可选）</li>
     *   <li>files：要上传的文件列表（必需）</li>
     * </ul>
     * 
     * <p><strong>返回结果：</strong></p>
     * <ul>
     *   <li>所有文件的访问URL列表</li>
     *   <li>每个文件的存储路径</li>
     *   <li>文件大小和类型信息</li>
     * </ul>
     * 
     * <p><strong>注意事项：</strong></p>
     * <ul>
     *   <li>请使用Swagger UI测试多文件上传</li>
     *   <li>批量上传可能耗时较长</li>
     *   <li>建议控制单次上传文件数量</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * POST /common/upload/batch
     * Content-Type: multipart/form-data
     * 
     * type: report
     * files: [文件1, 文件2, 文件3]
     * }</pre>
     *
     * @param type 文件类型，用于分类存储
     * @param multipartFiles 要上传的文件列表
     * @return 所有文件的上传结果列表
     * @throws Exception 上传过程中的异常
     */
    @PostMapping(value = "/batch", consumes = "multipart/form-data")
    @Operation(summary = "多个文件上传")
    @Permission("sys:file:upload-batch")
    public ApiResult<List<UploadVo>> uploadBatch(@RequestParam(required = false) String type,
                                                 @RequestPart("files") List<MultipartFile> multipartFiles) throws Exception {
        List<UploadVo> uploadVos = uploadService.uploadBatch(type, multipartFiles);
        return ApiResult.success(uploadVos);
    }

}
