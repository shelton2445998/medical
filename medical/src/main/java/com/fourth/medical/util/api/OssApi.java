package com.fourth.medical.util.api;

import com.alibaba.fastjson2.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.fourth.medical.config.properties.OssProperties;
import com.fourth.medical.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;

/**
 * 阿里云OSS对象存储服务工具类
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供阿里云OSS文件上传和管理功能</li>
 *   <li>支持文件流式上传到OSS存储</li>
 *   <li>自动生成文件访问URL</li>
 *   <li>提供OSS客户端生命周期管理</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>条件装配：基于配置条件自动装配</li>
 *   <li>自动初始化：应用启动时自动初始化OSS客户端</li>
 *   <li>资源管理：应用关闭时自动释放OSS连接</li>
 *   <li>异常处理：完善的OSS异常处理和日志记录</li>
 *   <li>路径管理：支持自定义根目录和文件路径</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>用户头像上传</li>
 *   <li>医疗报告文件存储</li>
 *   <li>系统文档管理</li>
 *   <li>图片和媒体文件存储</li>
 *   <li>数据备份和归档</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于阿里云OSS SDK</li>
 *   <li>使用DefaultCredentialProvider进行身份认证</li>
 *   <li>支持流式文件上传</li>
 *   <li>集成Spring Boot自动配置</li>
 * </ul>
 * 
 * <p><strong>配置要求：</strong></p>
 * <ul>
 *   <li>file.file-server-type=OSS</li>
 *   <li>OSS访问密钥配置</li>
 *   <li>OSS存储桶和域名配置</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展文件下载功能</li>
 *   <li>可添加文件删除和更新</li>
 *   <li>可支持文件访问权限控制</li>
 *   <li>可集成CDN加速服务</li>
 * </ul>
 *
 * @author geekidea
 * @date 2023/11/26
 **/
@Slf4j
@Component
@ConditionalOnProperty(name = "file.file-server-type", havingValue = "OSS", matchIfMissing = true)
public class OssApi {

    /** OSS配置属性 */
    private static OssProperties ossProperties;

    /** OSS客户端实例 */
    private static OSS ossClient;

    /**
     * 构造函数，注入OSS配置
     *
     * @param ossProperties OSS配置属性
     */
    public OssApi(OssProperties ossProperties) {
        OssApi.ossProperties = ossProperties;
    }

    /**
     * 初始化OSS客户端
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>应用启动时自动初始化OSS客户端连接</li>
     *   <li>使用配置的访问密钥创建认证提供者</li>
     *   <li>建立与阿里云OSS服务的连接</li>
     * </ul>
     * 
     * <p><strong>初始化流程：</strong></p>
     * <ol>
     *   <li>创建DefaultCredentialProvider认证提供者</li>
     *   <li>使用OSSClientBuilder构建OSS客户端</li>
     *   <li>记录初始化成功日志</li>
     * </ol>
     * 
     * <p><strong>异常处理：</strong></p>
     * <ul>
     *   <li>捕获并记录初始化异常</li>
     *   <li>打印异常堆栈信息</li>
     *   <li>不影响应用正常启动</li>
     * </ul>
     */
    @PostConstruct
    public void initOssClient() {
        try {
            DefaultCredentialProvider credentialsProvider = CredentialsProviderFactory.newDefaultCredentialProvider(ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
            // 创建OSSClient实例
            ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), credentialsProvider);
            log.info("OSS实例初始化成功：" + JSON.toJSONString(ossProperties));
        } catch (Exception e) {
            log.error("OSS实例初始化异常：" + JSON.toJSONString(ossProperties));
            e.printStackTrace();
        }
    }

    /**
     * 上传文件到OSS
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>将文件流上传到阿里云OSS存储</li>
     *   <li>自动生成文件访问URL</li>
     *   <li>支持自定义目录结构</li>
     * </ul>
     * 
     * <p><strong>上传流程：</strong></p>
     * <ol>
     *   <li>构建文件存储路径（根目录/目录名/文件名）</li>
     *   <li>创建PutObjectRequest上传请求</li>
     *   <li>执行文件上传操作</li>
     *   <li>生成并返回文件访问URL</li>
     * </ol>
     * 
     * <p><strong>路径规则：</strong></p>
     * <ul>
     *   <li>如果配置了根目录，则路径为：根目录/目录名/文件名</li>
     *   <li>否则路径为：目录名/文件名</li>
     *   <li>最终URL为：访问域名/文件路径</li>
     * </ul>
     * 
     * <p><strong>异常处理：</strong></p>
     * <ul>
     *   <li>OSSException：OSS服务异常，抛出业务异常</li>
     *   <li>ClientException：客户端异常，抛出连接异常</li>
     *   <li>其他异常：直接抛出原异常</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * InputStream fileStream = new FileInputStream("test.jpg");
     * String url = OssApi.upload(fileStream, "avatar", "user123.jpg");
     * // 返回：https://bucket.oss-cn-region.aliyuncs.com/root/avatar/user123.jpg
     * }</pre>
     *
     * @param inputStream 文件输入流
     * @param dirName 存储目录名
     * @param fileName 文件名
     * @return 文件访问URL
     * @throws BusinessException 当OSS上传失败时抛出业务异常
     */
    public static String upload(InputStream inputStream, String dirName, String fileName) {
        try {
            String rootDir = ossProperties.getRootDir();
            String fileKey = "";
            if (StringUtils.isNotBlank(rootDir)) {
                fileKey = rootDir + "/";
            }
            fileKey = fileKey + dirName + "/" + fileName;
            log.info("OSS上传文件fileKey：" + fileKey);
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketName(), fileKey, inputStream);
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            log.info("OSS上传文件结果：" + JSON.toJSONString(result));
            String accessUrl = ossProperties.getAccessDomain();
            String url = accessUrl + "/" + fileKey;
            // 返回访问路径
            log.info("OSS上传文件成功，fileKey：{}，url：{}", fileKey, url);
            return url;
        } catch (OSSException oe) {
            log.error("OSS上传文件异常，错误消息：{}，错误码：{}，请求ID：{}，主机ID：{}", oe.getErrorMessage(), oe.getErrorCode(), oe.getRequestId(), oe.getHostId());
            throw new BusinessException("OSS上传文件异常");
        } catch (ClientException ce) {
            log.error("OSS客户端异常：" + ce.getMessage());
            throw new BusinessException("OSS连接异常");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 销毁OSS客户端
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>应用关闭时自动释放OSS客户端连接</li>
     *   <li>确保资源正确释放，避免连接泄漏</li>
     *   <li>优雅关闭OSS服务连接</li>
     * </ul>
     * 
     * <p><strong>清理流程：</strong></p>
     * <ul>
     *   <li>检查OSS客户端是否为空</li>
     *   <li>调用shutdown()方法关闭连接</li>
     *   <li>捕获并记录清理异常</li>
     * </ul>
     */
    @PreDestroy
    public void destroy() {
        try {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
