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
import com.fourth.medical.medical.entity.Checkitem;
import com.fourth.medical.medical.mapper.OrdersMapper;
import com.fourth.medical.medical.mapper.SetmealMapper;
import com.fourth.medical.medical.mapper.CheckitemMapper;
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
import java.math.BigDecimal;

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
    
    @Autowired
    private CheckitemMapper checkitemMapper;

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
        
        // 创建订单
        Orders orders = new Orders();
        BeanUtils.copyProperties(dto, orders);
        
        // 设置基本信息
        orders.setUserId(appLoginVo.getUserId());
        orders.setDoctorId(dto.getDoctorId()); // 设置医生ID
        orders.setTimeSlot(dto.getAppointmentTime()); // 设置时间段
        orders.setOrderNumber(generateOrderNumber());
        orders.setStatus(1); // 1-待支付
        
        // 设置患者信息
        orders.setPatientName(dto.getPatientName());
        orders.setPatientAge(dto.getPatientAge());
        orders.setPatientGender(dto.getPatientGender());
        orders.setPatientPhone(dto.getPatientPhone());
        
        // 处理套餐信息和价格
        BigDecimal totalPrice = BigDecimal.ZERO;
        String checkitemIds = null;
        
        if (dto.getSetmealId() != null) {
            // 套餐预约
            Setmeal setmeal = setmealMapper.selectById(dto.getSetmealId());
            if (setmeal == null) {
                throw new BusinessException("套餐不存在");
            }
            orders.setSetmealId(dto.getSetmealId());
        orders.setPrice(setmeal.getPrice());
            orders.setAmount(setmeal.getPrice());
            
            // 使用套餐中的检查项ID列表
            if (setmeal.getCheckitemIds() != null && !setmeal.getCheckitemIds().isEmpty()) {
                checkitemIds = setmeal.getCheckitemIds();
            }
        } else {
            // 普通项目预约
            orders.setSetmealId(0L); // 套餐ID设为0表示普通项目预约
            
            // 计算检查项总价
            if (dto.getCheckitemIds() != null && !dto.getCheckitemIds().isEmpty()) {
                checkitemIds = dto.getCheckitemIds();
                // 根据检查项ID计算总价
                totalPrice = calculateCheckitemTotalPrice(checkitemIds);
            }
            
            orders.setPrice(totalPrice);
            orders.setAmount(totalPrice);
        }
        
        // 如果前端传入了自定义的检查项ID列表，优先使用前端的
        if (dto.getCheckitemIds() != null && !dto.getCheckitemIds().isEmpty()) {
            checkitemIds = dto.getCheckitemIds();
        }
        
        orders.setCheckitemIds(checkitemIds);
        
        // 保存订单
        boolean saveResult = save(orders);
        if (!saveResult) {
            throw new BusinessException("创建预约失败");
        }
        
        log.info("创建预约成功，订单ID: {}, 用户ID: {}, 套餐ID: {}, 检查项IDs: {}", 
            orders.getId(), appLoginVo.getUserId(), orders.getSetmealId(), checkitemIds);
        
        // 查询并返回创建的订单
        return ordersMapper.getAppOrdersById(orders.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelAppOrders(Long id, String token) {
        log.info("取消App预约，id: {}, token: {}", id, token);
        
        // 验证token并获取用户信息
        AppLoginVo appLoginVo = validateTokenAndGetUser(token);
        
        // 获取订单详情
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException("预约订单不存在");
        }
        
        // 验证订单所属用户
        if (!orders.getUserId().equals(appLoginVo.getUserId())) {
            throw new BusinessException("无权操作此订单");
        }
        
        // 更新订单状态为已取消
        orders.setStatus(0); // 0-已取消
        orders.setCancelTime(new Date());
        
        boolean result = updateById(orders);
        log.info("取消App预约结果：{}", result);
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmPayment(Long id, String token) {
        log.info("确认支付，id: {}, token: {}", id, token);
        
        // 验证token并获取用户信息
        AppLoginVo appLoginVo = validateTokenAndGetUser(token);
        
        // 获取订单详情
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException("预约订单不存在");
        }
        
        // 验证订单所属用户
        if (!orders.getUserId().equals(appLoginVo.getUserId())) {
            throw new BusinessException("无权操作此订单");
        }
        
        // 更新订单状态为已支付
        orders.setStatus(2); // 2-已支付
        orders.setPayTime(new Date());
        orders.setPayType(1); // 1-支付宝（默认）
        orders.setTransactionId("PAY" + System.currentTimeMillis()); // 生成交易号
        
        boolean result = updateById(orders);
        log.info("确认支付结果：{}", result);
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
    
    /**
     * 根据检查项ID列表计算总价
     *
     * @param checkitemIds 检查项ID列表，逗号分隔
     * @return 总价
     */
    private BigDecimal calculateCheckitemTotalPrice(String checkitemIds) {
        if (StringUtils.isBlank(checkitemIds)) {
            return BigDecimal.ZERO;
        }
        
        try {
            String[] ids = checkitemIds.split(",");
            BigDecimal totalPrice = BigDecimal.ZERO;
            
            for (String idStr : ids) {
                if (StringUtils.isNotBlank(idStr)) {
                    Long checkitemId = Long.parseLong(idStr.trim());
                    // 查询检查项价格
                    BigDecimal price = getCheckitemPrice(checkitemId);
                    if (price != null) {
                        totalPrice = totalPrice.add(price);
                    }
                }
            }
            
            log.info("计算检查项总价 - checkitemIds: {}, totalPrice: {}", checkitemIds, totalPrice);
            return totalPrice;
        } catch (Exception e) {
            log.error("计算检查项总价失败 - checkitemIds: {}", checkitemIds, e);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * 获取检查项价格
     *
     * @param checkitemId 检查项ID
     * @return 价格
     */
    private BigDecimal getCheckitemPrice(Long checkitemId) {
        try {
            // 查询数据库获取检查项价格
            Checkitem checkitem = checkitemMapper.selectById(checkitemId);
            if (checkitem != null && checkitem.getPrice() != null) {
                log.info("获取检查项价格成功 - checkitemId: {}, price: {}", checkitemId, checkitem.getPrice());
                return checkitem.getPrice();
            } else {
                log.warn("检查项不存在或价格为空 - checkitemId: {}", checkitemId);
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            log.error("获取检查项价格失败 - checkitemId: {}", checkitemId, e);
            return BigDecimal.ZERO;
        }
    }

}
