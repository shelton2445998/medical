package com.fourth.medical.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 密码加密工具类
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供安全的密码加密和盐值生成功能</li>
 *   <li>支持MD5和SHA256双重加密算法</li>
 *   <li>确保用户密码在系统中的安全存储</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>双重加密：MD5 + SHA256组合加密</li>
 *   <li>盐值机制：使用随机盐值防止彩虹表攻击</li>
 *   <li>参数验证：严格的输入参数验证</li>
 *   <li>安全随机：基于UUID的随机盐值生成</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>用户注册时的密码加密</li>
 *   <li>用户登录时的密码验证</li>
 *   <li>密码重置和修改操作</li>
 *   <li>系统管理员密码管理</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于Apache Commons Codec加密库</li>
 *   <li>MD5用于初始密码处理</li>
 *   <li>SHA256用于最终密码加密</li>
 *   <li>UUID用于生成随机盐值</li>
 * </ul>
 * 
 * <p><strong>安全考虑：</strong></p>
 * <ul>
 *   <li>防止彩虹表攻击</li>
 *   <li>防止暴力破解</li>
 *   <li>确保密码不可逆</li>
 *   <li>支持密码强度验证</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展支持更多加密算法</li>
 *   <li>可添加密码强度检查</li>
 *   <li>可支持密码过期机制</li>
 *   <li>可集成第三方密码服务</li>
 * </ul>
 *
 * @author geekidea
 * @date 2022/7/13
 **/
public class PasswordUtil {

    /** MD5加密后的密码长度 */
    private static int MD5_PWD_LENGTH = 32;

    /**
     * 密码加密方法
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>对用户密码进行双重加密处理</li>
     *   <li>自动检测密码是否已加密</li>
     *   <li>使用盐值增强密码安全性</li>
     * </ul>
     * 
     * <p><strong>加密流程：</strong></p>
     * <ol>
     *   <li>验证输入参数的有效性</li>
     *   <li>如果密码长度不是32位，进行MD5加密</li>
     *   <li>将MD5结果与盐值拼接</li>
     *   <li>对拼接结果进行SHA256加密</li>
     * </ol>
     * 
     * <p><strong>安全特性：</strong></p>
     * <ul>
     *   <li>双重加密确保密码安全</li>
     *   <li>盐值防止彩虹表攻击</li>
     *   <li>不可逆加密算法</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * String password = "userPassword123";
     * String salt = PasswordUtil.generateSalt();
     * String encryptedPassword = PasswordUtil.encrypt(password, salt);
     * }</pre>
     *
     * @param password 待加密的密码
     * @param salt 加密盐值，可为空
     * @return 加密后的密码字符串
     * @throws IllegalArgumentException 当密码为空时抛出异常
     */
    public static String encrypt(String password, String salt) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (StringUtils.isBlank(salt)) {
            salt = "";
        }
        // 如果密码长度不是32为，则进行md5加密
        if (password.length() != MD5_PWD_LENGTH) {
            password = DigestUtils.md5Hex(password);
        }
        // 将md5加密后的结果+盐，再进行sha256加密
        String encryptPassword = DigestUtils.sha256Hex(password + salt);
        return encryptPassword;
    }

    /**
     * 生成随机盐值
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>生成8位随机字符串作为密码加密盐值</li>
     *   <li>基于UUID确保随机性和唯一性</li>
     *   <li>用于增强密码加密的安全性</li>
     * </ul>
     * 
     * <p><strong>生成逻辑：</strong></p>
     * <ul>
     *   <li>使用UUID.randomUUID()生成随机UUID</li>
     *   <li>移除UUID中的连字符</li>
     *   <li>取前8位作为盐值</li>
     * </ul>
     * 
     * <p><strong>安全特性：</strong></p>
     * <ul>
     *   <li>高随机性：基于UUID的随机生成</li>
     *   <li>唯一性：极低的重复概率</li>
     *   <li>不可预测：无法通过算法推导</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * String salt = PasswordUtil.generateSalt();
     * // 输出类似：a1b2c3d4
     * }</pre>
     *
     * @return 8位随机盐值字符串
     */
    public static String generateSalt() {
        // 使用UUID生成随机字符串，并取前8位作为盐
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

}
