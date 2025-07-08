package com.forth.medicalreserve.repository;

import com.forth.medicalreserve.entity.SetMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 体检套餐数据访问层
 */
@Repository
public interface SetMealRepository extends JpaRepository<SetMeal, Integer> {
    
    /**
     * 根据套餐类型查询套餐列表
     * @param packageType 套餐类型
     * @return 套餐列表
     */
    List<SetMeal> findByPackageType(Integer packageType);
    
    /**
     * 根据状态查询套餐列表
     * @param status 状态
     * @return 套餐列表
     */
    List<SetMeal> findByStatus(Integer status);
    
    /**
     * 根据套餐名称模糊查询
     * @param packageName 套餐名称
     * @return 套餐列表
     */
    List<SetMeal> findByPackageNameContaining(String packageName);
    
    /**
     * 根据套餐类型和状态查询套餐列表
     * @param packageType 套餐类型
     * @param status 状态
     * @return 套餐列表
     */
    List<SetMeal> findByPackageTypeAndStatus(Integer packageType, Integer status);
} 