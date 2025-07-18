package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.DoctorDto;
import com.fourth.medical.medical.entity.Doctor;
import com.fourth.medical.medical.query.DoctorQuery;
import com.fourth.medical.medical.vo.DoctorVo;
import com.fourth.medical.medical.query.AppDoctorQuery;
import com.fourth.medical.medical.vo.AppDoctorVo;
import com.fourth.medical.medical.dto.DoctorUpdatePasswordDto;


/**
 * 医生 服务接口
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供医生信息管理的核心业务逻辑，包括医生的增删改查、密码管理、排班查询等</li>
 *   <li>支持医生信息的完整生命周期管理，为医疗系统提供医生数据服务</li>
 *   <li>提供移动端和Web端统一的医生数据访问接口</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>医生管理：支持医生信息的创建、编辑、删除和查询</li>
 *   <li>密码管理：支持医生密码的修改和验证</li>
 *   <li>排班查询：支持医生排班信息的查询</li>
 *   <li>分页查询：支持大数据量的分页查询优化</li>
 *   <li>移动端适配：提供专门的移动端数据格式</li>
 * </ul>
 * 
 * <p><strong>业务流程：</strong></p>
 * <ol>
 *   <li>医生注册：新医生注册，创建医生账户</li>
 *   <li>信息维护：医生或管理员维护医生信息</li>
 *   <li>密码管理：医生修改个人密码</li>
 *   <li>排班查询：查询医生的排班信息</li>
 *   <li>数据查询：支持多种条件的医生信息查询</li>
 * </ol>
 * 
 * <p><strong>数据安全：</strong></p>
 * <ul>
 *   <li>密码加密：医生密码采用加密存储</li>
 *   <li>权限控制：基于角色的数据访问控制</li>
 *   <li>数据验证：对输入数据进行严格验证</li>
 *   <li>隐私保护：保护医生个人隐私信息</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>医生认证：可扩展支持医生资质认证功能</li>
 *   <li>在线咨询：可扩展支持医生在线咨询功能</li>
 *   <li>评价系统：可扩展支持医生评价和评分功能</li>
 *   <li>统计分析：可扩展支持医生工作统计分析</li>
 * </ul>
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface DoctorService extends IService<Doctor> {

    /**
     * 添加医生
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addDoctor(DoctorDto dto);

    /**
     * 修改医生
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateDoctor(DoctorDto dto);

    /**
     * 删除医生
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteDoctor(Long id);

    /**
     * 医生详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    DoctorVo getDoctorById(Long id);

    /**
     * 医生分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<DoctorVo> getDoctorPage(DoctorQuery query);

    /**
     * App医生详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppDoctorVo getAppDoctorById(Long id);

    /**
     * App医生分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppDoctorVo> getAppDoctorPage(AppDoctorQuery query);

    /**
     * 根据医院ID和部门ID查找医生
     *
     * @param hospitalId 医院ID
     * @param departmentId 部门ID
     * @return 医生列表
     * @throws Exception
     */
    Object getDoctorByHospitalAndDepartment(Long hospitalId, Long departmentId);

    /**
     * 医生修改密码
     *
     * @param dto 密码修改信息
     * @return 是否修改成功
     * @throws Exception
     */
    boolean updatePassword(DoctorUpdatePasswordDto dto);

}
