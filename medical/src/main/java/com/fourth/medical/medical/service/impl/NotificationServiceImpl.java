package com.fourth.medical.medical.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fourth.medical.framework.exception.BusinessException;
import com.fourth.medical.framework.page.OrderByItem;
import com.fourth.medical.framework.page.OrderMapping;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.NotificationDto;
import com.fourth.medical.medical.entity.Notification;
import com.fourth.medical.medical.mapper.NotificationMapper;
import com.fourth.medical.medical.query.NotificationQuery;
import com.fourth.medical.medical.service.NotificationService;
import com.fourth.medical.medical.vo.NotificationVo;
import com.fourth.medical.medical.query.AppNotificationQuery;
import com.fourth.medical.medical.vo.AppNotificationVo;
import com.fourth.medical.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * 系统通知 服务实现类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addNotification(NotificationDto dto) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(dto, notification);
        return save(notification);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateNotification(NotificationDto dto) {
        Long id = dto.getId();
        Notification notification = getById(id);
        if (notification == null) {
            throw new BusinessException("系统通知不存在");
        }
        BeanUtils.copyProperties(dto, notification);
        return updateById(notification);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteNotification(Long id) {
        return removeById(id);
    }

    @Override
    public NotificationVo getNotificationById(Long id) {
        return notificationMapper.getNotificationById(id);
    }

    @Override
    public Paging<NotificationVo> getNotificationPage(NotificationQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<NotificationVo> list = notificationMapper.getNotificationPage(query);
        Paging<NotificationVo> paging = new Paging<>(list);
        return paging;
    }

    @Override
    public AppNotificationVo getAppNotificationById(Long id) {
        return notificationMapper.getAppNotificationById(id);
    }

    @Override
    public Paging<AppNotificationVo> getAppNotificationPage(AppNotificationQuery query) {
        OrderMapping orderMapping = new OrderMapping();
        orderMapping.put("createTime", "create_time");
        PagingUtil.handlePage(query, orderMapping, OrderByItem.desc("id"));
        List<AppNotificationVo> list = notificationMapper.getAppNotificationPage(query);
        Paging<AppNotificationVo> paging = new Paging<>(list);
        return paging;
    }

}
