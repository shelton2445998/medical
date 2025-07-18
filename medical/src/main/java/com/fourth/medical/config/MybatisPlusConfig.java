// 定义包名，指定当前类所在的包路径
package com.fourth.medical.config;

// 导入MyBatis-Plus拦截器，用于配置各种内置拦截器
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
// 导入攻击SQL阻断内置拦截器，防止全表更新和删除
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
// 导入数据权限拦截器，用于数据范围权限控制
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
// 导入登录工具类，用于获取当前登录用户信息
import com.fourth.medical.auth.util.LoginUtil;
// 导入多商户配置属性，用于多商户模式配置
import com.fourth.medical.config.properties.MerchantLineProperties;
// 导入自定义多商户内置拦截器，用于多商户数据隔离
import com.fourth.medical.framework.mybatis.plugins.MerchantLineInnerInterceptor;
// 导入数据范围处理器，用于处理数据权限范围
import com.fourth.medical.framework.mybatis.plugins.handler.DataScopeHandler;
// 导入多商户处理器，用于处理多商户逻辑
import com.fourth.medical.framework.mybatis.plugins.handler.MerchantLineHandler;
// 导入系统类型工具类，用于判断系统类型
import com.fourth.medical.util.SystemTypeUtil;
// 导入Lombok日志注解，自动生成日志对象
import lombok.extern.slf4j.Slf4j;
// 导入SQL解析器表达式接口，用于构建SQL表达式
import net.sf.jsqlparser.expression.Expression;
// 导入长整型值表达式，用于构建长整型SQL表达式
import net.sf.jsqlparser.expression.LongValue;
// 导入Apache Commons集合工具类
import org.apache.commons.collections4.CollectionUtils;
// 导入Spring自动装配注解
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring Bean注解，标识这是一个Bean
import org.springframework.context.annotation.Bean;
// 导入Spring配置注解，标识这是一个配置类
import org.springframework.context.annotation.Configuration;

// 导入Java集合类
import java.util.List;

/**
 * MyBatis-Plus配置类
 * 
 * 功能说明：
 * 配置MyBatis-Plus的各种插件和拦截器，包括数据权限、安全防护、
 * 多商户支持等功能。
 * 
 * 主要配置项：
 * 1. 数据权限拦截器 - 根据用户权限控制数据访问范围
 * 2. 攻击SQL阻断拦截器 - 防止危险的全表更新和删除操作
 * 3. 多商户拦截器 - 支持多商户数据隔离（可选）
 * 
 * 数据权限说明：
 * - 根据用户角色和权限，自动在SQL中添加数据范围限制
 * - 支持部门权限、个人权限等多种权限范围
 * - 确保用户只能访问授权范围内的数据
 * 
 * 安全防护说明：
 * - 阻止不带WHERE条件的UPDATE和DELETE操作
 * - 防止误操作导致的数据丢失
 * - 提高数据库操作的安全性
 * 
 * 多商户支持：
 * - 支持SaaS模式的多商户数据隔离
 * - 自动在SQL中添加商户ID条件
 * - 确保不同商户之间的数据隔离
 * 
 * @author geekidea
 * @date 2023/11/25
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j // 自动生成日志对象log
@Configuration // 标识这是一个Spring配置类
public class MybatisPlusConfig {

    /**
     * 多商户配置属性
     * 包含多商户功能的相关配置信息
     */
    @Autowired
    private MerchantLineProperties merchantLineProperties;

    /**
     * MyBatis-Plus拦截器配置Bean
     * 
     * 功能说明：
     * 配置MyBatis-Plus的核心拦截器，包括数据权限、安全防护等功能。
     * 拦截器按添加顺序执行，影响SQL的解析和执行过程。
     * 
     * 拦截器执行顺序：
     * 1. 数据权限拦截器 - 根据用户权限添加数据范围限制
     * 2. 多商户拦截器 - 添加商户ID条件（可选）
     * 3. 攻击SQL阻断拦截器 - 防止危险的全表操作
     * 
     * 工作原理：
     * - 在SQL执行前，拦截器会解析SQL语句
     * - 根据配置和上下文信息，动态修改SQL
     * - 添加必要的WHERE条件，确保数据安全和权限控制
     * 
     * @return MybatisPlusInterceptor 配置好的MyBatis-Plus拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 1. 数据权限拦截器
        // 根据用户权限自动在SQL中添加数据范围限制条件
        // 例如：用户只能查看自己部门的数据，则自动添加 dept_id = ? 条件
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(new DataScopeHandler()));
        
        // 2. 多商户拦截器（默认关闭）
        // 用于SaaS模式的多商户数据隔离
        // 自动在SQL中添加商户ID条件，确保不同商户之间的数据隔离
        // 如有需要，可以取消注释启用
        // interceptor.addInnerInterceptor(new MerchantLineInnerInterceptor(merchantLineHandler()));
        
        // 3. 攻击SQL阻断拦截器
        // 防止不带WHERE条件的UPDATE和DELETE操作
        // 避免误操作导致的全表数据丢失
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        
        return interceptor;
    }

    /**
     * 多商户处理器配置Bean
     * 
     * 功能说明：
     * 配置多商户模式的具体处理逻辑，包括商户ID获取、
     * 商户ID列名、表过滤等功能。
     * 
     * 多商户模式适用场景：
     * - SaaS系统需要为不同商户提供独立的数据视图
     * - 同一套代码服务多个客户，需要数据隔离
     * - 确保商户之间的数据安全和隐私
     * 
     * 工作原理：
     * 1. 获取当前请求的商户ID
     * 2. 判断表是否需要商户隔离
     * 3. 在SQL中自动添加商户ID条件
     * 4. 根据系统类型和用户角色决定是否启用
     * 
     * 注意事项：
     * - 如果不需要多商户功能，可以删除此Bean
     * - 需要在数据库表中添加商户ID列
     * - 需要在配置文件中指定需要隔离的表
     * 
     * @return MerchantLineHandler 多商户处理器实例
     */
    @Bean
    public MerchantLineHandler merchantLineHandler() {
        log.info("merchantLineProperties:" + merchantLineProperties);
        return new MerchantLineHandler() {
            /**
             * 获取当前请求的商户ID
             * 
             * 实现说明：
             * 可以从多种来源获取商户ID：
             * 1. 从登录用户信息中获取
             * 2. 从请求头中获取
             * 3. 从JWT Token中解析
             * 4. 从域名中解析
             * 
             * 当前实现：
             * 暂时返回固定值1，实际使用时需要根据业务逻辑修改
             * 
             * @return Expression 商户ID的SQL表达式
             */
            @Override
            public Expression getMerchantId() {
                // TODO 可以在LoginUtil中添加获取商户ID的方法
                // 示例：return new LongValue(LoginUtil.getMerchantId());
                return new LongValue(1);
            }

            /**
             * 获取商户ID列名
             * 
             * 说明：
             * 返回数据库表中商户ID列的名称，用于构建SQL条件
             * 
             * @return String 商户ID列名
             */
            @Override
            public String getMerchantIdColumn() {
                return merchantLineProperties.getMerchantIdColumn();
            }

            /**
             * 判断表是否需要商户隔离
             * 
             * 逻辑说明：
             * 1. 如果表在包含列表中，则不忽略（需要隔离）
             * 2. 如果表不在包含列表中，则忽略（不需要隔离）
             * 
             * 使用场景：
             * - 系统配置表通常不需要商户隔离
             * - 业务数据表通常需要商户隔离
             * 
             * @param tableName 表名
             * @return boolean true表示忽略该表，false表示不忽略
             */
            @Override
            public boolean ignoreTable(String tableName) {
                List<String> includeTables = merchantLineProperties.getIncludeTables();
                // 判断如果包含指定的表，则不忽略，否则忽略
                if (CollectionUtils.isNotEmpty(includeTables)) {
                    if (includeTables.contains(tableName)) {
                        return false; // 不忽略，需要添加商户ID条件
                    }
                }
                return true; // 忽略，不添加商户ID条件
            }

            /**
             * 判断是否忽略商户ID添加
             * 
             * 业务规则：
             * 只有满足以下条件才添加商户ID：
             * 1. 是管理后台的请求
             * 2. 且当前用户不是管理员
             * 
             * 原因说明：
             * - 管理员通常需要查看所有商户的数据
             * - 普通用户只能查看自己商户的数据
             * - 非管理后台请求（如API）可能有不同的权限控制逻辑
             * 
             * @return boolean true表示忽略商户ID，false表示添加商户ID
             */
            @Override
            public boolean ignoreMerchantId() {
                try {
                    // 如果是管理后台请求且当前用户不是管理员，则不忽略
                    if (SystemTypeUtil.isAdminSystem() && LoginUtil.isNotAdmin()) {
                        return false; // 不忽略，需要添加商户ID条件
                    }
                } catch (Exception e) {
                    // 如果判断过程中出现异常，记录日志并忽略商户ID
                    log.error("判断是否忽略商户ID时发生异常", e);
                }
                return true; // 忽略，不添加商户ID条件
            }
        };
    }
}
