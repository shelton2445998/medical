// 定义包名，指定当前类所在的包路径
package com.fourth.medical.user.service.impl;

// 导入MyBatis-Plus查询条件构造器，用于构建查询条件
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// 导入MyBatis-Plus更新条件构造器，用于构建更新条件
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
// 导入MyBatis-Plus服务实现基类，提供基础CRUD操作
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
// 导入App登录工具类，用于获取当前App登录用户信息
import com.fourth.medical.auth.util.AppLoginUtil;
// 导入业务异常类，用于抛出业务逻辑异常
import com.fourth.medical.framework.exception.BusinessException;
// 导入排序项工具类，用于处理查询排序
import com.fourth.medical.framework.page.OrderByItem;
// 导入排序映射工具类，用于字段排序映射
import com.fourth.medical.framework.page.OrderMapping;
// 导入分页工具类，用于处理分页查询结果
import com.fourth.medical.framework.page.Paging;
// 导入App用户头像更新DTO，用于接收头像修改请求参数
import com.fourth.medical.user.dto.AppUserHeadDto;
// 导入App用户昵称更新DTO，用于接收昵称修改请求参数
import com.fourth.medical.user.dto.AppUserNicknameDto;
// 导入用户DTO，用于接收用户数据传输
import com.fourth.medical.user.dto.UserDto;
// 导入用户实体类，对应数据库用户表
import com.fourth.medical.user.entity.User;
// 导入用户数据访问层接口，提供数据库操作
import com.fourth.medical.user.mapper.UserMapper;
// 导入用户查询对象，用于接收查询条件
import com.fourth.medical.user.query.UserQuery;
// 导入用户服务接口，定义业务操作方法
import com.fourth.medical.user.service.UserService;
// 导入App用户视图对象，用于返回App用户信息
import com.fourth.medical.user.vo.AppUserVo;
// 导入用户视图对象，用于返回用户信息
import com.fourth.medical.user.vo.UserVo;
// 导入IP地址工具类，用于获取客户端IP地址
import com.fourth.medical.util.IpUtil;
// 导入分页工具类，用于处理分页查询
import com.fourth.medical.util.PagingUtil;
// 导入Lombok日志注解，自动生成日志对象
import lombok.extern.slf4j.Slf4j;
// 导入Spring Bean工具类，用于对象属性复制
import org.springframework.beans.BeanUtils;
// 导入Spring自动装配注解
import org.springframework.beans.factory.annotation.Autowired;
// 导入Spring服务注解，标识这是一个服务类
import org.springframework.stereotype.Service;
// 导入Spring事务注解，用于事务管理
import org.springframework.transaction.annotation.Transactional;
// 导入Spring请求上下文持有者，用于获取请求信息
import org.springframework.web.context.request.RequestContextHolder;
// 导入Spring Servlet请求属性，用于获取请求属性
import org.springframework.web.context.request.ServletRequestAttributes;

// 导入Servlet HTTP请求接口
import javax.servlet.http.HttpServletRequest;
// 导入日期类，用于处理时间相关操作
import java.util.Date;
// 导入列表接口，用于处理集合数据
import java.util.List;
// 导入App用户资料更新DTO，用于接收个人资料修改请求参数
import com.fourth.medical.user.dto.AppUserUpdateProfileDto;

/**
 * 用户信息服务实现类
 * 实现用户相关的业务逻辑，包括用户管理、App用户功能等
 * 继承MyBatis-Plus的ServiceImpl，提供基础的CRUD操作
 *
 * @author geekidea
 * @since 2023-11-25
 */
@Slf4j  // 自动生成日志对象log
@Service  // 标识这是一个Spring服务类，会被Spring容器管理
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 注入用户数据访问层，用于执行数据库操作
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByOpenid(String openid) {
        // 创建Lambda查询条件构造器，用于构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 设置查询条件：openid字段等于传入的openid值
        wrapper.eq(User::getOpenid, openid);
        // 执行查询并返回单个结果
        return getOne(wrapper);
    }

    @Override
    public User getUserByUsername(String username) {
        // 创建Lambda查询条件构造器，用于构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        // 设置查询条件：username字段等于传入的用户名
        wrapper.eq(User::getUsername, username);
        // 执行查询并返回单个结果
        return getOne(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)  // 开启事务，异常时回滚
    @Override
    public boolean addUser(UserDto userDto) {
        // 创建新的用户实体对象
        User user = new User();
        // 将DTO对象的属性复制到实体对象中
        BeanUtils.copyProperties(userDto, user);
        
        // 设置当前时间作为注册和登录时间
        Date now = new Date();
        // 设置注册时间，如果为空则设置为当前时间
        if (user.getRegisterTime() == null) {
            user.setRegisterTime(now);
        }
        // 设置最后登录时间，如果为空则设置为当前时间
        if (user.getLastLoginTime() == null) {
            user.setLastLoginTime(now);
        }
        
        // 获取当前请求的IP地址，用于记录用户注册和登录的IP
        try {
            // 通过工具类获取客户端IP地址
            String ipAddress = IpUtil.getRequestIp();
            
            // 设置注册IP，如果为空则设置为当前IP
            if (user.getRegisterIp() == null || user.getRegisterIp().isEmpty()) {
                user.setRegisterIp(ipAddress);
            }
            // 设置最后登录IP，如果为空则设置为当前IP
            if (user.getLastLoginIp() == null || user.getLastLoginIp().isEmpty()) {
                user.setLastLoginIp(ipAddress);
            }
        } catch (Exception e) {
            // 记录获取IP地址失败的日志
            log.error("获取IP地址失败", e);
            // 设置默认IP地址作为备选方案
            if (user.getRegisterIp() == null || user.getRegisterIp().isEmpty()) {
                user.setRegisterIp("127.0.0.1");
            }
            if (user.getLastLoginIp() == null || user.getLastLoginIp().isEmpty()) {
                user.setLastLoginIp("127.0.0.1");
            }
        }
        
        // 保存用户信息到数据库并返回操作结果
        return save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(UserDto userDto) {
        Long id = userDto.getId();
        User user = getById(id);
        if (user == null) {
            throw new BusinessException("用户信息不存在");
        }
        
        // 只更新非空字段
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getNickname() != null) {
            user.setNickname(userDto.getNickname());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        if (userDto.getSalt() != null) {
            user.setSalt(userDto.getSalt());
        }
        if (userDto.getOpenid() != null) {
            user.setOpenid(userDto.getOpenid());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getHead() != null) {
            user.setHead(userDto.getHead());
        }
        if (userDto.getUserRoleId() != null) {
            user.setUserRoleId(userDto.getUserRoleId());
        }
        if (userDto.getStatus() != null) {
            user.setStatus(userDto.getStatus());
        }
        
        return updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUser(Long id) {
        return removeById(id);
    }

    @Override
    public UserVo getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public Paging<UserVo> getUserPage(UserQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<UserVo> list = userMapper.getUserPage(query);
        Paging<UserVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppUserVo getProfile() {
        Long userId = AppLoginUtil.getUserId();
        return userMapper.getAppUserById(userId);
    }

    @Override
    public boolean updateHead(AppUserHeadDto dto) {
        Long userId = AppLoginUtil.getUserId();
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getHead, dto.getHead());
        wrapper.eq(User::getId, userId);
        return update(new User(), wrapper);
    }

    @Override
    public boolean updateNickname(AppUserNicknameDto dto) {
        Long userId = AppLoginUtil.getUserId();
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getNickname, dto.getNickname());
        wrapper.eq(User::getId, userId);
        return update(new User(), wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAppUserProfile(AppUserUpdateProfileDto dto) {
        Long userId = AppLoginUtil.getUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户信息不存在");
        }
        
        // 只更新非空字段
        if (dto.getNickname() != null) {
            user.setNickname(dto.getNickname());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
        if (dto.getHead() != null) {
            user.setHead(dto.getHead());
        }
        if (dto.getOpenid() != null) {
            user.setOpenid(dto.getOpenid());
        }
        if (dto.getRemark() != null) {
            user.setRemark(dto.getRemark());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getIdCard() != null) {
            user.setIdCard(dto.getIdCard());
        }
        if (dto.getIntroduction() != null) {
            user.setIntroduction(dto.getIntroduction());
        }
        
        return updateById(user);
    }

}
