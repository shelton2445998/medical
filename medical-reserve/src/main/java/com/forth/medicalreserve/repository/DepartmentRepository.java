package com.forth.medicalreserve.repository;

import com.forth.medicalreserve.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 科室数据访问层
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    
    /**
     * 根据医院ID查询科室列表
     * @param hospitalId 医院ID
     * @return 科室列表
     */
    List<Department> findByHospitalId(Integer hospitalId);
    
    /**
     * 根据科室名称查询
     * @param departmentName 科室名称
     * @return 科室
     */
    Optional<Department> findByDepartmentName(String departmentName);
    
    /**
     * 根据医院ID和科室名称查询
     * @param hospitalId 医院ID
     * @param departmentName 科室名称
     * @return 科室
     */
    Optional<Department> findByHospitalIdAndDepartmentName(Integer hospitalId, String departmentName);
    
    /**
     * 根据状态查询科室列表
     * @param status 状态
     * @return 科室列表
     */
    List<Department> findByStatus(Integer status);
} 