package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.util.AppLoginUtil;
import com.fourth.medical.auth.vo.AppLoginVo;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.OrdersDto;
import com.fourth.medical.medical.dto.AppOrdersDto;
import com.fourth.medical.medical.entity.Orders;
import com.fourth.medical.medical.entity.Setmeal;
import com.fourth.medical.medical.mapper.OrdersMapper;
import com.fourth.medical.medical.mapper.SetmealMapper;
import com.fourth.medical.medical.query.OrdersQuery;
import com.fourth.medical.medical.service.OrdersService;
import com.fourth.medical.medical.vo.OrdersVo;
import com.fourth.medical.medical.query.AppOrdersQuery;
import com.fourth.medical.medical.vo.AppOrdersVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 体检预约订单 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    
    @Autowired
    private SetmealMapper setmealMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addOrders(OrdersDto dto) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(dto, orders);
        return save(orders);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateOrders(OrdersDto dto) {
        Long id = dto.getId();
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException("体检预约订单不存在");
        }
        BeanUtils.copyProperties(dto, orders);
        return updateById(orders);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteOrders(Long id) {
        return removeById(id);
    }

    @Override
    public OrdersVo getOrdersById(Long id) {
        return ordersMapper.getOrdersById(id);
    }

    @Override
    public Paging<OrdersVo> getOrdersPage(OrdersQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<OrdersVo> list = ordersMapper.getOrdersPage(query);
        Paging<OrdersVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppOrdersVo getAppOrdersById(Long id, String token) {
        log.info("获取App预约详情，id: {}, token: {}", id, token);
        
        // 验证token并获取用户信息
        AppLoginVo appLoginVo = validateTokenAndGetUser(token);
        
        // 获取订单详情
        AppOrdersVo appOrdersVo = ordersMapper.getAppOrdersById(id);
        if (appOrdersVo == null) {
            throw new BusinessException("预约订单不存在");
        }
        
        // 验证订单所属用户
        if (!appOrdersVo.getUserId().equals(appLoginVo.getUserId())) {
            throw new BusinessException("无权查看此订单");
        }
        
        log.info("获取预约详情成功 - 订单ID: {}, 用户ID: {}, 状态: {}, 订单号: {}", 
            appOrdersVo.getId(), appOrdersVo.getUserId(), appOrdersVo.getStatus(), appOrdersVo.getOrderNumber());
        
        return appOrdersVo;
    }

    @Override
    public Paging<AppOrdersVo> getAppOrdersPage(AppOrdersQuery query, String token) {
        log.info("=== 开始获取App预约列表 ===");
        log.info("输入参数 - token: {}", token);
        log.info("输入参数 - query: {}", query);
        
        // 验证token并获取用户信息
        AppLoginVo appLoginVo = validateTokenAndGetUser(token);
        log.info("从token解析出的用户信息 - userId: {}, username: {}", appLoginVo.getUserId(), appLoginVo.getUsername());
        
        // 设置查询条件，只查询当前用户的订单
        query.setUserId(appLoginVo.getUserId());
        log.info("设置查询条件 - userId: {}", query.getUserId());
        
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        log.info("分页参数 - pageNum: {}, pageSize: {}", query.getPageNum(), query.getPageSize());
        
        List<AppOrdersVo> list = ordersMapper.getAppOrdersPage(query);
        log.info("数据库查询结果 - 返回记录数: {}", list.size());
        
        // 打印查询到的订单详情
        for (int i = 0; i < list.size(); i++) {
            AppOrdersVo order = list.get(i);
            log.info("订单[{}] - id: {}, userId: {}, setmealId: {}, hospitalId: {}, doctorId: {}, status: {}", 
                i + 1, order.getId(), order.getUserId(), order.getSetmealId(), 
                order.getHospitalId(), order.getDoctorId(), order.getStatus());
        }
        
        Paging<AppOrdersVo> paging = new Paging<>(list);
        log.info("=== 获取App预约列表完成，返回记录数: {} ===", list.size());
        return paging;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppOrdersVo createAppOrders(AppOrdersDto dto, String token) {
        log.info("创建App预约，token: {}", token);
        
        // 验证token并获取用户信息
        AppLoginVo appLoginVo = validateTokenAndGetUser(token);
        
        // 获取套餐信息
        Setmeal setmeal = setmealMapper.selectById(dto.getSetmealId());
        if (setmeal == null) {
            throw new BusinessException("套餐不存在");
        }
        
        // 创建订单
        Orders orders = new Orders();
        BeanUtils.copyProperties(dto, orders);
        // 设置基本信息
        orders.setUserId(appLoginVo.getUserId());
        orders.setDoctorId(dto.getDoctorId()); // 设置医生ID
        orders.setTimeSlot(dto.getAppointmentTime()); // 设置时间段
        orders.setOrderNumber(generateOrderNumber());
        orders.setPrice(setmeal.getPrice());
        orders.setStatus(1); // 1-待支付
        
        // 保存订单
        boolean saveResult = save(orders);
        if (!saveResult) {
            throw new BusinessException("创建预约失败");
        }
        
        log.info("创建预约成功，订单ID: {}, 用户ID: {}", orders.getId(), appLoginVo.getUserId());
        
        // 查询并返回创建的订单
        return ordersMapper.getAppOrdersById(orders.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelAppOrders(Long id, String token) {
        log.info("取消App预约，id: {}, token: {}", id, token);
        
        // 验证token并获取用户信息
        AppLoginVo appLoginVo = validateTokenAndGetUser(token);
        
        // 查询订单
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException("预约订单不存在");
        }
        
        // 验证订单所属
        if (!orders.getUserId().equals(appLoginVo.getUserId())) {
            throw new BusinessException("无权操作此订单");
        }
        
        // 检查订单状态
        if (orders.getStatus() == 0) { // 0表示已取消
            throw new BusinessException("订单已取消");
        }
        
        if (orders.getStatus() == 2 || orders.getStatus() == 3) { // 2表示已支付，3表示已完成
            throw new BusinessException("订单已完成，无法取消");
        }
        
        // 更新订单状态为取消
        orders.setStatus(0); // 0表示已取消
        orders.setCancelTime(new Date());
        
        boolean result = updateById(orders);
        if (result) {
            log.info("取消预约成功，订单ID: {}, 用户ID: {}", id, appLoginVo.getUserId());
        }
        
        return result;
    }
    
    /**
     * 验证token并获取用户信息
     *
     * @param token
     * @return
     */
    private AppLoginVo validateTokenAndGetUser(String token) {
        log.info("=== 开始验证token ===");
        log.info("输入token: {}", token);
        
        if (StringUtils.isBlank(token)) {
            log.error("token为空，抛出异常");
            throw new BusinessException("请先登录");
        }
        
        log.info("调用AppLoginUtil.getLoginVo(token)");
        AppLoginVo appLoginVo = AppLoginUtil.getLoginVo(token);
        log.info("AppLoginUtil.getLoginVo()返回结果: {}", appLoginVo);
        
        if (appLoginVo == null) {
            log.error("AppLoginUtil.getLoginVo()返回null，抛出异常");
            throw new BusinessException("登录已过期，请重新登录");
        }
        
        log.info("验证token成功 - 用户ID: {}, 用户名: {}", appLoginVo.getUserId(), appLoginVo.getUsername());
        log.info("=== token验证完成 ===");
        return appLoginVo;
    }
    
    /**
     * 生成订单编号
     *
     * @return
     */
    private String generateOrderNumber() {
        // 简单实现，实际可能需要更复杂的逻辑
        return "ORD" + System.currentTimeMillis();
    }

}
