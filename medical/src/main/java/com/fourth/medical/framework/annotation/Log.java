package com.fourth.medical.framework.annotation;

import com.fourth.medical.common.enums.SysLogType;

/**
 * 系统操作日志注解
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>用于标记需要记录操作日志的方法</li>
 *   <li>支持自定义日志描述和类型</li>
 *   <li>配合AOP切面自动记录操作日志</li>
 *   <li>提供系统操作审计功能</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>方法级注解：可标记在Controller方法上</li>
 *   <li>自定义描述：支持自定义操作描述</li>
 *   <li>类型分类：支持不同类型的日志分类</li>
 *   <li>自动记录：通过AOP自动记录操作日志</li>
 *   <li>参数捕获：自动捕获方法参数和返回值</li>
 *   <li>用户追踪：自动记录操作用户信息</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>重要业务操作记录</li>
 *   <li>数据增删改操作审计</li>
 *   <li>用户行为追踪</li>
 *   <li>系统安全审计</li>
 *   <li>操作历史查询</li>
 *   <li>合规性要求</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于Java注解机制</li>
 *   <li>配合Spring AOP切面</li>
 *   <li>支持枚举类型参数</li>
 *   <li>默认值配置</li>
 * </ul>
 * 
 * <p><strong>日志类型：</strong></p>
 * <ul>
 *   <li>LOGIN：登录操作</li>
 *   <li>LOGOUT：退出操作</li>
 *   <li>INSERT：新增操作</li>
 *   <li>UPDATE：修改操作</li>
 *   <li>DELETE：删除操作</li>
 *   <li>SELECT：查询操作</li>
 *   <li>OTHER：其他操作</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展更多日志类型</li>
 *   <li>可添加日志级别控制</li>
 *   <li>可支持条件日志记录</li>
 *   <li>可集成第三方日志系统</li>
 * </ul>
 *
 * @author geekidea
 * @date 2022/8/3
 **/
public @interface Log {

    /**
     * 操作描述
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>描述当前操作的具体内容</li>
     *   <li>用于日志记录和查询</li>
     *   <li>支持中文描述，便于理解</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * @Log("新增用户")
     * public ApiResult addUser(@RequestBody User user) {
     *     // 业务逻辑
     * }
     * 
     * @Log("修改用户信息")
     * public ApiResult updateUser(@RequestBody User user) {
     *     // 业务逻辑
     * }
     * 
     * @Log("删除用户")
     * public ApiResult deleteUser(@PathVariable Long id) {
     *     // 业务逻辑
     * }
     * }</pre>
     * 
     * <p><strong>描述规范：</strong></p>
     * <ul>
     *   <li>使用简洁明了的描述</li>
     *   <li>包含操作的对象和动作</li>
     *   <li>便于后续查询和统计</li>
     * </ul>
     *
     * @return 操作描述，默认为空字符串
     */
    String value() default "";


    /**
     * 日志类型
     * 
     * <p><strong>功能说明：</strong></p>
     * <ul>
     *   <li>指定操作日志的类型</li>
     *   <li>用于日志分类和统计</li>
     *   <li>便于按类型查询日志</li>
     * </ul>
     * 
     * <p><strong>类型说明：</strong></p>
     * <ul>
     *   <li>LOGIN：用户登录操作</li>
     *   <li>LOGOUT：用户退出操作</li>
     *   <li>INSERT：数据新增操作</li>
     *   <li>UPDATE：数据修改操作</li>
     *   <li>DELETE：数据删除操作</li>
     *   <li>SELECT：数据查询操作</li>
     *   <li>OTHER：其他类型操作</li>
     * </ul>
     * 
     * <p><strong>使用示例：</strong></p>
     * <pre>{@code
     * @Log(value = "用户登录", type = SysLogType.LOGIN)
     * public ApiResult login(@RequestBody LoginDto loginDto) {
     *     // 登录逻辑
     * }
     * 
     * @Log(value = "新增体检项目", type = SysLogType.INSERT)
     * public ApiResult addCheckitem(@RequestBody Checkitem checkitem) {
     *     // 新增逻辑
     * }
     * 
     * @Log(value = "修改医生信息", type = SysLogType.UPDATE)
     * public ApiResult updateDoctor(@RequestBody Doctor doctor) {
     *     // 修改逻辑
     * }
     * }</pre>
     *
     * @return 日志类型，默认为OTHER
     */
    SysLogType type() default SysLogType.OTHER;

}
