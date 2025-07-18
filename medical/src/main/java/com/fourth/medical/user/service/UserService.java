// 定义包名，指定当前接口所在的包路径
package com.fourth.medical.user.service;

// 导入MyBatis-Plus服务接口，提供基础的CRUD操作
import com.baomidou.mybatisplus.extension.service.IService;
// 导入分页工具类，用于处理分页查询结果
import com.fourth.medical.framework.page.Paging;
// 导入App用户头像更新DTO，用于接收头像修改请求参数
import com.fourth.medical.user.dto.AppUserHeadDto;
// 导入App用户昵称更新DTO，用于接收昵称修改请求参数
import com.fourth.medical.user.dto.AppUserNicknameDto;
// 导入App用户资料更新DTO，用于接收个人资料修改请求参数
import com.fourth.medical.user.dto.AppUserUpdateProfileDto;
// 导入用户DTO，用于接收用户数据传输
import com.fourth.medical.user.dto.UserDto;
// 导入用户实体类，对应数据库用户表
import com.fourth.medical.user.entity.User;
// 导入用户查询对象，用于接收查询条件
import com.fourth.medical.user.query.UserQuery;
// 导入App用户视图对象，用于返回App用户信息
import com.fourth.medical.user.vo.AppUserVo;
// 导入用户视图对象，用于返回用户信息
import com.fourth.medical.user.vo.UserVo;


/**
 * 用户信息服务接口
 * 定义用户相关的业务操作方法，包括用户管理、App用户功能等
 * 继承MyBatis-Plus的IService接口，提供基础的CRUD操作
 *
 * @author geekidea
 * @since 2023-11-25
 */
public interface UserService extends IService<User> {

    /**
     * 根据微信openid获取用户
     * 通过微信用户的唯一标识符查询对应的用户信息
     *
     * @param openid 微信用户的openid，用于唯一标识微信用户
     * @return User 返回对应的用户实体对象，如果不存在则返回null
     * @throws Exception 可能抛出的异常
     */
    User getUserByOpenid(String openid);

    /**
     * 根据账号获取用户
     * 通过用户名查询对应的用户信息，用于登录验证
     *
     * @param username 用户名，用于登录的账号
     * @return User 返回对应的用户实体对象，如果不存在则返回null
     * @throws Exception 可能抛出的异常
     */
    User getUserByUsername(String username);

    /**
     * 添加用户信息
     * 创建新的用户记录，包含用户基本信息的录入和保存
     *
     * @param userDto 用户数据传输对象，包含要添加的用户信息
     * @return boolean 返回操作结果，true表示成功，false表示失败
     * @throws Exception 可能抛出的异常
     */
    boolean addUser(UserDto userDto);

    /**
     * 修改用户信息
     * 更新现有用户的基本信息，包括个人资料、联系方式等
     *
     * @param userDto 用户数据传输对象，包含要修改的用户信息
     * @return boolean 返回操作结果，true表示成功，false表示失败
     * @throws Exception 可能抛出的异常
     */
    boolean updateUser(UserDto userDto);

    /**
     * 删除用户信息
     * 根据用户ID删除指定的用户记录，执行逻辑删除操作
     *
     * @param id 要删除的用户ID
     * @return boolean 返回操作结果，true表示成功，false表示失败
     * @throws Exception 可能抛出的异常
     */
    boolean deleteUser(Long id);

    /**
     * 用户信息详情
     * 根据用户ID查询用户的详细信息，包括基本资料、联系方式等
     *
     * @param id 要查询的用户ID
     * @return UserVo 返回用户详细信息的视图对象
     * @throws Exception 可能抛出的异常
     */
    UserVo getUserById(Long id);

    /**
     * 用户信息分页列表
     * 根据查询条件分页获取用户列表，支持条件筛选和分页显示
     *
     * @param query 用户查询对象，包含查询条件和分页参数
     * @return Paging<UserVo> 返回分页用户列表，包含分页信息和用户数据
     * @throws Exception 可能抛出的异常
     */
    Paging<UserVo> getUserPage(UserQuery query);

    /**
     * 获取App用户信息
     * 获取当前登录App用户的详细信息，用于个人中心显示
     *
     * @return AppUserVo 返回App用户详细信息的视图对象
     * @throws Exception 可能抛出的异常
     */
    AppUserVo getProfile();

    /**
     * 修改用户头像
     * 更新当前App登录用户的头像信息
     *
     * @param dto App用户头像更新DTO，包含新的头像信息
     * @return boolean 返回操作结果，true表示成功，false表示失败
     * @throws Exception 可能抛出的异常
     */
    boolean updateHead(AppUserHeadDto dto);

    /**
     * 修改用户昵称
     * 更新当前App登录用户的昵称信息
     *
     * @param dto App用户昵称更新DTO，包含新的昵称信息
     * @return boolean 返回操作结果，true表示成功，false表示失败
     * @throws Exception 可能抛出的异常
     */
    boolean updateNickname(AppUserNicknameDto dto);

    /**
     * 修改App用户个人信息
     * 更新当前App登录用户的个人资料信息，包括基本信息、联系方式等
     *
     * @param dto App用户资料更新DTO，包含要修改的用户信息
     * @return boolean 返回操作结果，true表示成功，false表示失败
     * @throws Exception 可能抛出的异常
     */
    boolean updateAppUserProfile(AppUserUpdateProfileDto dto);

}
