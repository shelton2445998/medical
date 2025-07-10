package com.fourth.medical.auth.service.impl;

import com.fourth.medical.auth.dto.LoginDto;
import com.fourth.medical.auth.service.LoginRedisService;
import com.fourth.medical.auth.service.LoginService;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.LoginTokenVo;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.common.constant.LoginConstant;
import com.fourth.medical.common.enums.SystemType;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.exception.LoginException;
import com.fourth.medical.system.entity.SysRole;
import com.fourth.medical.system.entity.SysUser;
import com.fourth.medical.system.mapper.SysMenuMapper;
import com.fourth.medical.system.mapper.SysRoleMapper;
import com.fourth.medical.system.mapper.SysUserMapper;
import com.fourth.medical.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author geekidea
 * @date 2022/7/12
 **/
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private LoginRedisService loginRedisService;


    @Override
    public LoginTokenVo login(LoginDto dto) {
        String username = dto.getUsername();
        SysUser sysUser = sysUserMapper.getSysUserByUsername(username);
        if (sysUser == null) {
            throw new BusinessException("用户信息不存在");
        }
        // 用户ID
        Long userId = sysUser.getId();
        // 校验密码
        String dbPassword = sysUser.getPassword();
        String dbSalt = sysUser.getSalt();
        String password = dto.getPassword();
        String encryptPassword = PasswordUtil.encrypt(password, dbSalt);
        if (!encryptPassword.equals(dbPassword)) {
            throw new BusinessException("账号密码错误");
        }
        // 生成token
        String token = TokenUtil.generateAdminToken(userId);
        // 刷新登录信息
        refreshLoginInfo(sysUser, token, new Date());
        // 返回token
        LoginTokenVo loginTokenVo = new LoginTokenVo();
        loginTokenVo.setToken(token);
        return loginTokenVo;
    }

    @Override
    public LoginVo refreshLoginInfo(SysUser sysUser, String token, Date loginTime) {
        // 用户ID
        Long userId = sysUser.getId();
        // 校验用户状态
        Boolean status = sysUser.getStatus();
        if (status == false) {
            throw new BusinessException("用户已禁用");
        }
        // 查询用户角色
        Long roleId = sysUser.getRoleId();
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        if (sysRole == null) {
            throw new BusinessException("该用户不存在可用的角色");
        }
        // 设置登录用户对象
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(sysUser, loginVo);
        loginVo.setUserId(userId);
        loginVo.setLoginTime(loginTime);
        loginVo.setAdmin(sysUser.getIsAdmin());
        loginVo.setRoleCode(sysRole.getCode());
        loginVo.setRoleName(sysRole.getName());
        // 系统类型 1：管理后台，2：用户端
        loginVo.setSystemType(SystemType.ADMIN.getCode());
        // 获取登录用户的权限编码
        List<String> permissions = sysMenuMapper.getPermissionCodesByUserId(userId);
        loginVo.setPermissions(permissions);
        // 保存登录信息到redis中
        loginRedisService.setLoginVo(token, loginVo);
        return loginVo;
    }

    @Override
    public LoginVo getLoginUserInfo() {
        LoginVo loginVo = LoginUtil.getLoginVo();
        if (loginVo == null) {
            throw new LoginException("请先登录");
        }
        // 根据用户ID获取用户信息
        Long userId = loginVo.getUserId();
        // 获取用户登录时间
        Date loginTime = loginVo.getLoginTime();
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException("用户信息不存在");
        }
        // 刷新登录信息
        String token = TokenUtil.getToken();
        loginVo = refreshLoginInfo(sysUser, token, loginTime);
        return loginVo;
    }

    @Override
    public void logout() {
        try {
            // 获取token
            String token = TokenUtil.getToken();
            // 删除缓存
            loginRedisService.deleteLoginVo(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginTokenVo register(LoginDto dto) {
        String username = dto.getUsername();
        // 检查用户名是否已存在
        SysUser existUser = sysUserMapper.getSysUserByUsername(username);
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建新用户
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        
        // 密码加密
        String salt = PasswordUtil.generateSalt();
        String encryptPassword = PasswordUtil.encrypt(dto.getPassword(), salt);
        
        sysUser.setPassword(encryptPassword);
        sysUser.setSalt(salt);
        sysUser.setStatus(true); // 启用账号
        sysUser.setIsAdmin(false); // 普通用户
        sysUser.setCreateTime(new Date());
        
        // 设置为用户角色
        sysUser.setRoleId(LoginConstant.APP_NORMAL_USER_ROLE);
        
        // 保存用户
        sysUserMapper.insert(sysUser);
        
        // 生成token
        String token = TokenUtil.generateAppToken(sysUser.getId());
        // 刷新登录信息
        refreshLoginInfo(sysUser, token, new Date());
        
        // 返回token
        LoginTokenVo loginTokenVo = new LoginTokenVo();
        loginTokenVo.setToken(token);
        return loginTokenVo;
    }
}
