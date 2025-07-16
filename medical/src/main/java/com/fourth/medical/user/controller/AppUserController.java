package com.fourth.medical.user.controller;

import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.system.dto.SysUserUpdatePasswordDto;
import com.fourth.medical.system.dto.SysUserUpdateProfileDto;
import com.fourth.medical.system.service.SysUserService;
import com.fourth.medical.system.vo.SysUserVo;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.user.dto.AppUserUpdateProfileDto;
import com.fourth.medical.user.service.UserService;
import com.fourth.medical.user.vo.AppUserVo;
import com.fourth.medical.auth.util.AppLoginUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author geekidea
 * @date 2022/7/9
 **/
@Slf4j
@RestController
@Tag(name = "App用户")
@RequestMapping("/user")
public class AppUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public ApiResult<SysUserVo> getUserInfo() {
        Long userId = LoginUtil.getUserId();
        if (userId == null) {
            throw new BusinessException("用户ID为空");
        }
        SysUserVo sysUserVo = sysUserService.getSysUserById(userId);
        return ApiResult.success(sysUserVo);
    }

    /**
     * 修改用户信息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PutMapping("/update")
    @Operation(summary = "修改用户个人资料")
    public ApiResult updateUserInfo(@Valid @RequestBody SysUserUpdateProfileDto dto) {
        boolean flag = sysUserService.updateProfile(dto);
        return ApiResult.result(flag);
    }

    /**
     * 修改用户密码
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PutMapping("/password")
    @Operation(summary = "修改用户密码")
    public ApiResult updatePassword(@Valid @RequestBody SysUserUpdatePasswordDto dto) {
        boolean flag = sysUserService.updatePassword(dto);
        return ApiResult.result(flag);
    }

    /**
     * 获取App用户信息（User表）
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/app/info")
    @Operation(summary = "获取App用户信息")
    public ApiResult<AppUserVo> getAppUserInfo() {
        Long userId = AppLoginUtil.getUserId();
        if (userId == null) {
            throw new BusinessException("用户ID为空");
        }
        AppUserVo appUserVo = userService.getProfile();
        return ApiResult.success(appUserVo);
    }

    /**
     * 修改App用户信息（User表）
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/app/update")
    @Operation(summary = "修改App用户个人资料")
    public ApiResult updateAppUserInfo(@Valid @RequestBody AppUserUpdateProfileDto dto) {
        boolean flag = userService.updateAppUserProfile(dto);
        return ApiResult.result(flag);
    }

}
