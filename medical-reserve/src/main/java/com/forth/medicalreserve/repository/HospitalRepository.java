package com.forth.medicalreserve.repository;

import com.forth.medicalreserve.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医院数据访问层
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    
    /**
     * 根据状态查询医院列表
     * @param status 状态
     * @return 医院列表
     */
    List<Hospital> findByStatus(Integer status);
    
    /**
     * 根据医院名称模糊查询
     * @param hospitalName 医院名称
     * @return 医院列表
     */
    List<Hospital> findByHospitalNameContaining(String hospitalName);
} 