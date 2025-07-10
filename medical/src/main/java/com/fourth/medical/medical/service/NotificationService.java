package com.fourth.medical.medical.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fourth.medical.framework.page.Paging;
import com.fourth.medical.medical.dto.NotificationDto;
import com.fourth.medical.medical.entity.Notification;
import com.fourth.medical.medical.query.NotificationQuery;
import com.fourth.medical.medical.vo.NotificationVo;
import com.fourth.medical.medical.query.AppNotificationQuery;
import com.fourth.medical.medical.vo.AppNotificationVo;


/**
 * 系统通知 服务接口
 *
 * @author fourth
 * @since 2025-07-09
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 添加系统通知
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean addNotification(NotificationDto dto);

    /**
     * 修改系统通知
     *
     * @param dto
     * @return
     * @throws Exception
     */
    boolean updateNotification(NotificationDto dto);

    /**
     * 删除系统通知
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteNotification(Long id);

    /**
     * 系统通知详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    NotificationVo getNotificationById(Long id);

    /**
     * 系统通知分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<NotificationVo> getNotificationPage(NotificationQuery query);

    /**
     * App系统通知详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    AppNotificationVo getAppNotificationById(Long id);

    /**
     * App系统通知分页列表
     *
     * @param query
     * @return
     * @throws Exception
     */
    Paging<AppNotificationVo> getAppNotificationPage(AppNotificationQuery query);

}
