package com.fourth.medical.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.auth.util.AppLoginUtil;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.user.dto.AppUserHeadDto;
import com.fourth.medical.user.dto.AppUserNicknameDto;
import com.fourth.medical.user.dto.UserDto;
import com.fourth.medical.user.entity.User;
import com.fourth.medical.user.mapper.UserMapper;
import com.fourth.medical.user.query.UserQuery;
import com.fourth.medical.user.service.UserService;
import com.fourth.medical.user.vo.AppUserVo;
import com.fourth.medical.user.vo.UserVo;
import com.fourth.medical.util.IpUtil;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 用户信息 服务实现类
 *
 * @author geekidea
 * @since 2023-11-25
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByOpenid(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        return getOne(wrapper);
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        
        // 设置当前时间
        Date now = new Date();
        // 设置注册时间
        if (user.getRegisterTime() == null) {
            user.setRegisterTime(now);
        }
        // 设置最后登录时间
        if (user.getLastLoginTime() == null) {
            user.setLastLoginTime(now);
        }
        
        // 获取当前请求的IP地址
        try {
            String ipAddress = IpUtil.getRequestIp();
            
            // 设置注册IP
            if (user.getRegisterIp() == null || user.getRegisterIp().isEmpty()) {
                user.setRegisterIp(ipAddress);
            }
            // 设置最后登录IP
            if (user.getLastLoginIp() == null || user.getLastLoginIp().isEmpty()) {
                user.setLastLoginIp(ipAddress);
            }
        } catch (Exception e) {
            log.error("获取IP地址失败", e);
            // 设置默认IP
            if (user.getRegisterIp() == null || user.getRegisterIp().isEmpty()) {
                user.setRegisterIp("127.0.0.1");
            }
            if (user.getLastLoginIp() == null || user.getLastLoginIp().isEmpty()) {
                user.setLastLoginIp("127.0.0.1");
            }
        }
        
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

}
