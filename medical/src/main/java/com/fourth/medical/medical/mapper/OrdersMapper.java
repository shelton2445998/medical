package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Orders;
import com.fourth.medical.medical.query.OrdersQuery;
import com.fourth.medical.medical.vo.OrdersVo;
import com.fourth.medical.medical.query.AppOrdersQuery;
import com.fourth.medical.medical.vo.AppOrdersVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检预约订单 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 体检预约订单详情
     *
     * @param id
     * @return
     */
    OrdersVo getOrdersById(Long id);

    /**
     * 体检预约订单分页列表
     *
     * @param query
     * @return
     */
    List<OrdersVo> getOrdersPage(OrdersQuery query);

    /**
     * App体检预约订单详情
     *
     * @param id
     * @return
     */
    AppOrdersVo getAppOrdersById(Long id);

    /**
     * App体检预约订单分页列表
     *
     * @param query
     * @return
     */
    List<AppOrdersVo> getAppOrdersPage(AppOrdersQuery query);

}
