package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Orders;
import com.fourth.medical.medical.query.OrdersQuery;
import com.fourth.medical.medical.vo.OrdersVo;
import com.fourth.medical.medical.query.AppOrdersQuery;
import com.fourth.medical.medical.vo.AppOrdersVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
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
    
    /**
     * 统计医生在指定日期范围内的预约数量
     *
     * @param doctorId 医生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预约数量
     */
    @Select("SELECT COUNT(*) FROM orders WHERE doctor_id = #{doctorId} " +
            "AND appointment_date >= #{startDate} " +
            "AND appointment_date < #{endDate} " +
            "AND status IN (1, 2, 3)")
    Integer countAppointmentsByDoctorIdAndDateRange(
            @Param("doctorId") Long doctorId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
