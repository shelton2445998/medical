// 定义包名，指定当前类所在的包路径
package com.fourth.medical.config;

// 导入Hutool日期格式常量，用于日期格式化
import cn.hutool.core.date.DatePattern;
// 导入Jackson Long类型序列化器，避免Long类型精度丢失
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
// 导入自定义日期反序列化器，用于处理日期格式
import com.fourth.medical.framework.jackson.deserializer.JacksonDateDeserializer;
// 导入自定义字符串反序列化器，用于去除字符串空格
import com.fourth.medical.framework.jackson.deserializer.JacksonStringDeserializer;
// 导入自定义字符串序列化器，用于处理字符串格式
import com.fourth.medical.framework.jackson.serializer.JacksonStringSerializer;
// 导入XSS反序列化器，用于防止XSS攻击
import com.fourth.medical.framework.xss.XssJacksonDeserializer;
// 导入XSS序列化器，用于防止XSS攻击
import com.fourth.medical.framework.xss.XssJacksonSerializer;
// 导入Spring Value注解，用于注入配置属性
import org.springframework.beans.factory.annotation.Value;
// 导入Jackson对象映射器构建器定制器，用于自定义Jackson配置
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
// 导入Spring Bean注解，标识这是一个Bean
import org.springframework.context.annotation.Bean;
// 导入Spring配置注解，标识这是一个配置类
import org.springframework.context.annotation.Configuration;

// 导入Java时区类
import java.time.ZoneId;
// 导入Java日期类
import java.util.Date;
// 导入Java本地化类
import java.util.Locale;
// 导入Java时区类
import java.util.TimeZone;

/**
 * Jackson JSON配置类
 * 
 * 功能说明：
 * 配置Jackson JSON序列化和反序列化的行为，包括日期格式、字符串处理、
 * XSS防护、数据类型转换等功能。
 * 
 * 主要配置项：
 * 1. 日期时间格式统一处理
 * 2. 字符串自动去空格处理
 * 3. Long类型精度丢失防护
 * 4. XSS攻击防护
 * 5. 本地化和时区设置
 * 
 * 序列化配置（响应数据处理）：
 * - Long类型转换为字符串，防止前端精度丢失
 * - 字符串自动去除左右空格
 * - XSS攻击防护（可选）
 * 
 * 反序列化配置（请求数据处理）：
 * - 字符串自动去除左右空格
 * - 日期格式统一处理
 * - XSS攻击防护（可选）
 * 
 * 适用场景：
 * - RESTful API的JSON数据交换
 * - 前后端分离项目的数据格式统一
 * - 数据安全和格式规范化
 * 
 * @author geekidea
 * @date 2022/4/13
 * @version 1.0
 * @since JDK 1.8
 */
@Configuration // 标识这是一个Spring配置类
public class JacksonConfig {

    /**
     * XSS防护开关配置
     * 从配置文件中读取是否启用XSS防护功能
     * 
     * 配置说明：
     * - true: 启用XSS防护，对字符串进行XSS过滤
     * - false: 关闭XSS防护，提高性能
     * 
     * 配置示例：
     * xss.enable=true   # 生产环境建议开启
     * xss.enable=false  # 开发环境可以关闭
     */
    @Value("${xss.enable}")
    private boolean enableXss;

    /**
     * Jackson对象映射器构建器定制器Bean
     * 
     * 功能说明：
     * 自定义Jackson的序列化和反序列化行为，包括日期格式、
     * 字符串处理、数据类型转换等配置。
     * 
     * 配置详情：
     * 1. 本地化设置：使用中国地区设置
     * 2. 时区设置：使用系统默认时区
     * 3. 日期格式：统一使用 yyyy-MM-dd HH:mm:ss 格式
     * 4. 字符串处理：自动去除左右空格
     * 5. Long类型处理：转换为字符串防止精度丢失
     * 6. XSS防护：根据配置选择是否启用
     * 
     * 为什么需要这些配置：
     * - 日期格式统一：避免前后端日期格式不一致问题
     * - 字符串去空格：提高数据质量，避免因空格导致的问题
     * - Long类型转字符串：JavaScript中Long类型最大安全整数为2^53-1，防止精度丢失
     * - XSS防护：防止跨站脚本攻击，保护应用安全
     * 
     * @return Jackson2ObjectMapperBuilderCustomizer Jackson构建器定制器
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 设置本地化为中国，影响数字、日期的格式化
            builder.locale(Locale.CHINA);
            
            // 设置时区为系统默认时区，确保时间处理的一致性
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            
            // 设置日期格式为 yyyy-MM-dd HH:mm:ss
            // 统一前后端的日期格式，避免格式不一致问题
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            
            // ==================== 反序列化配置（处理请求参数）====================
            
            // 字符串反序列化器：自动去除请求参数中字符串左右两边的空格
            // 提高数据质量，避免因前端输入空格导致的数据问题
            builder.deserializerByType(String.class, JacksonStringDeserializer.INSTANCE);
            
            // 日期反序列化器：统一处理各种日期格式的输入
            // 支持多种日期格式的自动识别和转换
            builder.deserializerByType(Date.class, JacksonDateDeserializer.INSTANCE);
            
            // ==================== 序列化配置（处理响应结果）====================
            
            // Long类型序列化器：将Long类型转换为字符串
            // 原因：JavaScript中Long类型最大安全整数为2^53-1，超过会精度丢失
            // 解决方案：后端返回字符串格式，前端按字符串处理
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            
            // 字符串序列化器：去除响应结果中字符串左右两边的空格
            // 确保返回给前端的数据格式规范
            builder.serializerByType(String.class, JacksonStringSerializer.INSTANCE);

            // ==================== XSS防护配置 ====================
            
            // 根据配置决定是否启用XSS防护
            if (enableXss) {
                // XSS序列化器：对输出的字符串进行XSS过滤
                // 防止恶意脚本通过API响应传播
                builder.serializerByType(String.class, new XssJacksonSerializer());
                
                // XSS反序列化器：对输入的字符串进行XSS过滤
                // 防止恶意脚本通过API请求注入
                builder.deserializerByType(String.class, new XssJacksonDeserializer());
            }
        };
    }
}
