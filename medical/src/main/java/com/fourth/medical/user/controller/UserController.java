package com.fourth.medical.user.controller;

import com.fourth.medical.auth.annotation.Permission;
import com.fourth.medical.common.enums.SysLogType;
import com.fourth.medical.framework.annotation.Log;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.user.dto.UserDto;
import com.fourth.medical.user.query.UserQuery;
import com.fourth.medical.user.service.UserService;
import com.fourth.medical.user.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户信息 控制器
 *
 * @author geekidea
 * @since 2023-11-30
 */
@Slf4j
@RestController
@Tag(name = "用户信息")
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 添加用户信息
     *
     * @param userDto
     * @return
     * @throws Exception
     */
    @Log(value = "添加用户信息", type = SysLogType.ADD)
    @PostMapping("/addUser")
    @Operation(summary = "添加用户信息")
    @Permission("user:add")
    public ApiResult addUser(@Valid @RequestBody UserDto userDto) {
        boolean flag = userService.addUser(userDto);
        return ApiResult.result(flag);
    }

    /**
     * 修改用户信息
     *
     * @param userDto
     * @return
     * @throws Exception
     */
    @Log(value = "修改用户信息", type = SysLogType.UPDATE)
    @PostMapping("/updateUser")
    @Operation(summary = "修改用户信息")
    @Permission("user:update")
    public ApiResult updateUser(@Valid @RequestBody UserDto userDto) {
        boolean flag = userService.updateUser(userDto);
        return ApiResult.result(flag);
    }

    /**
     * 删除用户信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Log(value = "删除用户信息", type = SysLogType.DELETE)
    @PostMapping("/deleteUser/{id}")
    @Operation(summary = "删除用户信息")
    @Permission("user:delete")
    public ApiResult deleteUser(@PathVariable Long id) {
        boolean flag = userService.deleteUser(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取用户信息详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/getUser/{id}")
    @Operation(summary = "用户信息详情")
    @Permission("user:info")
    public ApiResult<UserVo> getUser(@PathVariable Long id) {
        UserVo userVo = userService.getUserById(id);
        return ApiResult.success(userVo);
    }

    /**
     * 用户信息分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    @PostMapping("/getUserPage")
    @Operation(summary = "用户信息分页列表")
    @Permission("user:page")
    public ApiResult<UserVo> getUserPage(@Valid @RequestBody UserQuery query) {
        Paging<UserVo> paging = userService.getUserPage(query);
        return ApiResult.success(paging);
    }

}
