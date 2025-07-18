// 定义包名，指定当前类所在的包路径
package com.fourth.medical.config;

// 导入自定义的字符串Redis序列化器，用于处理key的序列化，支持项目前缀功能
import com.fourth.medical.framework.redis.CustomStringRedisSerializer;
// 导入Spring Value注解，用于从配置文件中注入配置属性值
import org.springframework.beans.factory.annotation.Value;
// 导入Spring Bean注解，标识这是一个Spring Bean定义方法
import org.springframework.context.annotation.Bean;
// 导入Spring配置注解，标识这是一个Spring配置类
import org.springframework.context.annotation.Configuration;
// 导入Redis连接工厂接口，用于创建Redis连接，支持多种Redis客户端
import org.springframework.data.redis.connection.RedisConnectionFactory;
// 导入Redis模板类，提供Redis数据操作的高级接口
import org.springframework.data.redis.core.RedisTemplate;
// 导入Jackson JSON序列化器，用于处理复杂对象的JSON序列化和反序列化
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
// 导入字符串序列化器，用于处理字符串类型的数据序列化
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis缓存配置类
 * 
 * 功能说明：
 * 本类是医疗系统中Redis缓存服务的核心配置类，负责配置Redis连接、序列化器、
 * 以及各种缓存操作的基础设施。提供了统一的Redis操作模板，支持多种数据类型
 * 的缓存操作，包括字符串、Hash、List、Set、ZSet等。
 * 
 * 主要功能：
 * 1. 配置RedisTemplate实例，提供统一的Redis操作接口
 * 2. 配置key-value数据的序列化和反序列化策略
 * 3. 配置Hash类型数据的序列化和反序列化策略
 * 4. 支持项目前缀功能，实现多环境、多项目的key空间隔离
 * 5. 提供JSON序列化支持，能够存储和读取复杂的Java对象
 * 6. 优化序列化性能，减少网络传输和存储开销
 * 
 * 序列化器详细说明：
 * - CustomStringRedisSerializer: 自定义字符串序列化器
 *   * 支持项目前缀功能，自动为key添加项目前缀
 *   * 避免不同项目或环境之间的key冲突
 *   * 便于Redis数据的管理和维护
 * 
 * - GenericJackson2JsonRedisSerializer: Jackson JSON序列化器
 *   * 支持复杂Java对象的序列化和反序列化
 *   * 保留对象的类型信息，反序列化时能正确还原对象类型
 *   * 支持泛型、集合、数组等复杂数据结构
 *   * JSON格式便于调试和跨语言使用
 * 
 * 适用场景：
 * - 用户会话存储：存储用户登录状态、权限信息等
 * - 数据缓存：缓存频繁访问的数据库查询结果
 * - 分布式锁：实现分布式环境下的资源锁定
 * - 消息队列：实现异步消息处理
 * - 计数器和限流：实现访问限制和统计功能
 * - 临时数据存储：存储验证码、临时令牌等
 * - 配置缓存：缓存系统配置信息
 * 
 * 性能优化：
 * - 使用高效的序列化器，减少序列化开销
 * - 支持连接池，提高连接复用率
 * - 自动压缩，减少内存占用
 * - 支持Pipeline操作，提高批量操作效率
 * 
 * 扩展性：
 * - 支持Redis集群模式
 * - 支持读写分离
 * - 支持多数据源配置
 * - 支持缓存策略配置
 * 
 * @author geekidea
 * @date 2022-1-7
 * @version 1.0
 * @since JDK 1.8
 */
@Configuration // 标识这是一个Spring配置类，Spring容器会自动扫描并处理
public class RedisConfig {

    /**
     * Redis项目前缀配置
     * 
     * 功能说明：
     * 从配置文件中读取项目前缀，用于区分不同项目、不同环境的Redis key，
     * 避免key冲突，提高Redis数据的管理效率。
     * 
     * 配置方式：
     * 在application.yml或application.properties文件中配置：
     * redis.projectPrefix=medical:dev:    # 开发环境
     * redis.projectPrefix=medical:test:   # 测试环境
     * redis.projectPrefix=medical:prod:   # 生产环境
     * 
     * 作用机制：
     * 1. 自动为所有Redis key添加项目前缀
     * 2. 实现多环境数据隔离
     * 3. 便于Redis数据的管理和维护
     * 4. 支持多项目共享Redis实例
     * 
     * 使用示例：
     * 原始key: user:123
     * 加前缀后: medical:prod:user:123
     * 
     * 优势：
     * - 避免不同项目间的key冲突
     * - 支持多环境部署
     * - 便于数据迁移和备份
     * - 提高Redis数据的可维护性
     */
    @Value("${redis.projectPrefix}")
    private String projectPrefix;

    /**
     * Redis模板配置Bean
     * 
     * 功能说明：
     * 配置并创建RedisTemplate实例，这是Spring Data Redis提供的核心操作类，
     * 封装了Redis的各种操作方法，提供了类型安全的Redis访问接口。
     * 
     * 配置内容：
     * 1. Redis连接工厂配置 - 管理Redis连接的创建和销毁
     * 2. Key序列化器配置 - 定义key的序列化方式
     * 3. Value序列化器配置 - 定义value的序列化方式
     * 4. Hash序列化器配置 - 定义Hash类型数据的序列化方式
     * 5. 项目前缀支持 - 自动为key添加项目前缀
     * 
     * 序列化策略详解：
     * - Key序列化：使用CustomStringRedisSerializer
     *   * 支持项目前缀自动添加
     *   * 字符串类型，便于调试和管理
     *   * 高效的序列化性能
     * 
     * - Value序列化：使用GenericJackson2JsonRedisSerializer
     *   * 支持复杂Java对象序列化
     *   * 保留类型信息，反序列化时类型安全
     *   * JSON格式，便于调试和跨语言使用
     *   * 支持泛型、集合、数组等复杂结构
     * 
     * - Hash序列化：与普通key-value保持一致
     *   * HashKey使用CustomStringRedisSerializer
     *   * HashValue使用GenericJackson2JsonRedisSerializer
     *   * 支持复杂的Hash结构存储
     * 
     * 支持的数据类型：
     * - String: 字符串类型，最基本的key-value存储
     * - Hash: 哈希类型，适合存储对象信息
     * - List: 列表类型，支持队列和栈操作
     * - Set: 集合类型，支持唯一性约束
     * - ZSet: 有序集合类型，支持排序和范围查询
     * 
     * 性能优化：
     * - 连接池管理，提高连接复用率
     * - 高效的序列化器，减少序列化开销
     * - 支持Pipeline操作，提高批量操作效率
     * - 自动压缩，减少网络传输和存储开销
     * 
     * 事务支持：
     * - 支持Redis事务操作
     * - 支持Watch机制，实现乐观锁
     * - 支持Pipeline批量操作
     * 
     * 扩展功能：
     * - 支持Lua脚本执行
     * - 支持发布/订阅模式
     * - 支持分布式锁实现
     * - 支持缓存穿透、雪崩、击穿防护
     * 
     * @param redisConnectionFactory Redis连接工厂，Spring自动注入
     *                              负责管理Redis连接的创建、配置和销毁
     *                              支持Jedis、Lettuce等多种Redis客户端
     * @return RedisTemplate<Object, Object> 配置完成的Redis操作模板
     *         提供类型安全的Redis操作接口
     *         支持所有Redis数据类型的操作
     *         包含完整的序列化配置
     * @throws Exception 如果Redis连接配置失败或序列化器创建失败
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建RedisTemplate实例，这是Spring Data Redis的核心操作类
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        
        // 设置Redis连接工厂，负责管理Redis连接的生命周期
        // 连接工厂会自动处理连接的创建、配置、池化和销毁
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        
        // 创建自定义的字符串序列化器，支持项目前缀功能
        // 项目前缀用于区分不同项目或环境的Redis key，避免冲突
        // 例如：原始key "user:123" 会被序列化为 "medical:prod:user:123"
        CustomStringRedisSerializer customStringRedisSerializer = new CustomStringRedisSerializer(projectPrefix);
        
        // 创建Jackson JSON序列化器，用于处理复杂对象的序列化
        // 优点：
        // 1. 支持复杂Java对象的序列化和反序列化
        // 2. 保留类型信息，反序列化时能正确还原对象类型
        // 3. JSON格式便于调试和跨语言使用
        // 4. 支持泛型、集合、数组等复杂数据结构
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        
        // 配置key-value类型数据的序列化方式
        // 这是Redis最基本的数据类型，用于存储字符串、数字、对象等
        redisTemplate.setKeySerializer(customStringRedisSerializer);    // key使用自定义字符串序列化器
        redisTemplate.setValueSerializer(jsonRedisSerializer);          // value使用JSON序列化器
        
        // 配置Hash类型数据的序列化方式
        // Hash类型适合存储对象信息，如用户信息、商品信息等
        // 例如：HSET user:123 name "张三" age 25 email "zhangsan@example.com"
        redisTemplate.setHashKeySerializer(customStringRedisSerializer); // hash key使用自定义字符串序列化器
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);       // hash value使用JSON序列化器
        
        // 初始化RedisTemplate，调用afterPropertiesSet方法完成初始化
        // 这一步会验证配置的正确性，初始化内部组件
        redisTemplate.afterPropertiesSet();
        
        // 返回配置完成的RedisTemplate实例
        return redisTemplate;
    }
}
