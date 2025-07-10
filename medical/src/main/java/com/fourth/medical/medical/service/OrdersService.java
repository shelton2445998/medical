package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.OrdersDto;
import com.fourth.medical.medical.entity.Orders;
import com.fourth.medical.medical.query.OrdersQuery;
import com.fourth.medical.medical.vo.OrdersVo;
import com.fourth.medical.medical.query.AppOrdersQuery;
import com.fourth.medical.medical.vo.AppOrdersVo;
import com.fourth.medical.medical.dto.AppOrdersDto;


/**
 * 体检预约订单 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 添加体检预约订单
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addOrders(OrdersDto dto);

    /**
     * 修改体检预约订单
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateOrders(OrdersDto dto);

    /**
     * 删除体检预约订单
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteOrders(Long id);

    /**
     * 体检预约订单详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    OrdersVo getOrdersById(Long id);

    /**
     * 体检预约订单分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<OrdersVo> getOrdersPage(OrdersQuery query);

    /**
     * App体检预约订单详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppOrdersVo getAppOrdersById(Long id);

    /**
     * App体检预约订单分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppOrdersVo> getAppOrdersPage(AppOrdersQuery query);
    
    /**
     * 创建App体检预约
     *
     * @param dto
     * @return
     * @throws Exception
     */
    AppOrdersVo createAppOrders(AppOrdersDto dto);
    
    /**
     * 取消App体检预约
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean cancelAppOrders(Long id);

}
