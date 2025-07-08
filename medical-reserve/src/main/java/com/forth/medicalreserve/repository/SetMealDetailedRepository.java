package com.forth.medicalreserve.repository;

import com.forth.medicalreserve.entity.SetMealDetailed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 体检套餐明细数据访问层
 */
@Repository
public interface SetMealDetailedRepository extends JpaRepository<SetMealDetailed, Integer> {
    
    /**
     * 根据套餐ID查询套餐明细列表
     * @param packageId 套餐ID
     * @return 套餐明细列表
     */
    List<SetMealDetailed> findByPackageId(Integer packageId);
    
    /**
     * 根据检查项ID查询套餐明细列表
     * @param itemId 检查项ID
     * @return 套餐明细列表
     */
    List<SetMealDetailed> findByItemId(Integer itemId);
    
    /**
     * 根据套餐ID和检查项ID查询套餐明细
     * @param packageId 套餐ID
     * @param itemId 检查项ID
     * @return 套餐明细
     */
    SetMealDetailed findByPackageIdAndItemId(Integer packageId, Integer itemId);
    
    /**
     * 根据套餐ID删除套餐明细
     * @param packageId 套餐ID
     */
    void deleteByPackageId(Integer packageId);
} 