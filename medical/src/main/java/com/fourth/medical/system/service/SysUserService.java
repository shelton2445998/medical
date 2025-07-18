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
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供系统用户管理的核心业务逻辑，包括用户的增删改查、密码管理、个人信息维护等</li>
 *   <li>支持系统用户的完整生命周期管理，为系统提供用户数据服务</li>
 *   <li>提供用户认证、授权和安全管理的基础服务</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>用户管理：支持系统用户的创建、编辑、删除和查询</li>
 *   <li>密码管理：支持用户密码的重置和修改</li>
 *   <li>个人信息：支持用户个人信息的维护和更新</li>
 *   <li>数据导入：支持Excel批量导入用户数据</li>
 *   <li>分页查询：支持大数据量的分页查询优化</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>用户创建：管理员创建新的系统用户</li>
 *   <li>信息维护：用户或管理员维护用户信息</li>
 *   <li>密码管理：用户修改密码或管理员重置密码</li>
 *   <li>数据验证：验证用户名唯一性等业务规则</li>
 *   <li>批量导入：支持Excel批量导入用户数据</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>密码加密：用户密码采用加密存储</li>
 *   <li>权限控制：基于角色的数据访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>唯一性检查：确保用户名等关键字段的唯一性</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>用户认证：可扩展支持多种认证方式</li>
 *   <li>用户同步：可扩展支持与外部系统的用户同步</li>
 *   <li>用户审计：可扩展支持用户操作审计功能</li>
 *   <li>用户画像：可扩展支持用户行为分析</li>
 * </ul>
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
