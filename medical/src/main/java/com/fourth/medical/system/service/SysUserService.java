package com.fourth.medical.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.system.dto.SysUserDto;
import com.fourth.medical.system.dto.SysUserResetPasswordDto;
import com.fourth.medical.system.dto.SysUserUpdatePasswordDto;
import com.fourth.medical.system.dto.SysUserUpdateProfileDto;
import com.fourth.medical.system.entity.SysUser;
import com.fourth.medical.system.query.SysUserQuery;
import com.fourth.medical.system.vo.SysUserVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统用户 服务接口
 *
 * @author geekidea
 * @since 2022-12-26
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 添加系统用户
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addSysUser(SysUserDto dto);

    /**
     * 修改系统用户
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateSysUser(SysUserDto dto);

    /**
     * 删除系统用户
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysUser(Long id);

    /**
     * 系统用户详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    SysUserVo getSysUserById(Long id);

    /**
     * 系统用户分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<SysUserVo> getSysUserPage(SysUserQuery query);

    /**
     * 重置系统用户密码
     *
     * @param sysUserResetPasswordDto
     * @return
     * @throws Exception
     */
    boolean resetSysUserPassword(SysUserResetPasswordDto sysUserResetPasswordDto);

    /**
     * 修改个人信息
     *
     * @param sysUserUpdateProfileDto
     * @return
     * @throws Exception
     */
    boolean updateProfile(SysUserUpdateProfileDto sysUserUpdateProfileDto);

    /**
     * 修改系统用户密码
     *
     * @param sysUserUpdatePasswordDto
     * @return
     * @throws Exception
     */
    boolean updatePassword(SysUserUpdatePasswordDto sysUserUpdatePasswordDto);

    /**
     * 检查username是否存在
     *
     * @param username
     * @return
     * @throws Exception
     */
    void checkUsernameExists(String username);

    /**
     * 导入Excel用户数据
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    boolean importExcel(MultipartFile multipartFile) throws Exception;

}
