package com.forth.medicalreserve.service;

import com.forth.medicalreserve.entity.SetMeal;
import com.forth.medicalreserve.entity.SetMealDetailed;

import java.util.List;

/**
 * 体检套餐服务接口
 */
public interface SetMealService {
    
    /**
     * 添加体检套餐
     * @param setMeal 体检套餐信息
     * @return 添加后的体检套餐
     */
    SetMeal addSetMeal(SetMeal setMeal);
    
    /**
     * 更新体检套餐
     * @param setMeal 体检套餐信息
     * @return 更新后的体检套餐
     */
    SetMeal updateSetMeal(SetMeal setMeal);
    
    /**
     * 删除体检套餐
     * @param packageId 套餐ID
     * @return 是否删除成功
     */
    boolean deleteSetMeal(Integer packageId);
    
    /**
     * 上架/下架体检套餐
     * @param packageId 套餐ID
     * @param status 状态：0-下架，1-上架
     * @return 更新后的体检套餐
     */
    SetMeal updateStatus(Integer packageId, Integer status);
    
    /**
     * 根据ID查询体检套餐
     * @param packageId 套餐ID
     * @return 体检套餐信息
     */
    SetMeal findById(Integer packageId);
    
    /**
     * 查询所有体检套餐
     * @return 体检套餐列表
     */
    List<SetMeal> findAll();
    
    /**
     * 根据套餐类型查询体检套餐列表
     * @param packageType 套餐类型
     * @return 体检套餐列表
     */
    List<SetMeal> findByPackageType(Integer packageType);
    
    /**
     * 根据状态查询体检套餐列表
     * @param status 状态
     * @return 体检套餐列表
     */
    List<SetMeal> findByStatus(Integer status);
    
    /**
     * 根据套餐名称模糊查询
     * @param packageName 套餐名称
     * @return 体检套餐列表
     */
    List<SetMeal> findByPackageName(String packageName);
    
    /**
     * 添加套餐明细
     * @param packageId 套餐ID
     * @param itemId 检查项ID
     * @return 添加后的套餐明细
     */
    SetMealDetailed addSetMealDetail(Integer packageId, Integer itemId);
    
    /**
     * 删除套餐明细
     * @param detailId 明细ID
     * @return 是否删除成功
     */
    boolean deleteSetMealDetail(Integer detailId);
    
    /**
     * 根据套餐ID查询套餐明细列表
     * @param packageId 套餐ID
     * @return 套餐明细列表
     */
    List<SetMealDetailed> findDetailsByPackageId(Integer packageId);
} 