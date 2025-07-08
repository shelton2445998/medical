package com.forth.medicalreserve.repository;

import com.forth.medicalreserve.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 医生数据访问层
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    
    /**
     * 根据医生姓名查询
     * @param doctorName 医生姓名
     * @return 医生列表
     */
    List<Doctor> findByDoctorNameContaining(String doctorName);
    
    /**
     * 根据医院ID查询医生列表
     * @param hospitalId 医院ID
     * @return 医生列表
     */
    List<Doctor> findByHospitalId(Integer hospitalId);
    
    /**
     * 根据科室ID查询医生列表
     * @param departmentId 科室ID
     * @return 医生列表
     */
    List<Doctor> findByDepartmentId(Integer departmentId);
    
    /**
     * 根据医院ID和科室ID查询医生列表
     * @param hospitalId 医院ID
     * @param departmentId 科室ID
     * @return 医生列表
     */
    List<Doctor> findByHospitalIdAndDepartmentId(Integer hospitalId, Integer departmentId);
    
    /**
     * 根据医生姓名查询
     * @param doctorName 医生姓名
     * @return 医生
     */
    Optional<Doctor> findByDoctorName(String doctorName);
    
    /**
     * 根据手机号查询
     * @param mobile 手机号
     * @return 医生
     */
    Optional<Doctor> findByMobile(String mobile);
} 