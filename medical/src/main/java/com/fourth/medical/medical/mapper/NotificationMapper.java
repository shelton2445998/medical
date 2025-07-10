package com.fourth.medical.medical.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fourth.medical.medical.entity.Notification;
import com.fourth.medical.medical.query.NotificationQuery;
import com.fourth.medical.medical.vo.NotificationVo;
import com.fourth.medical.medical.query.AppNotificationQuery;
import com.fourth.medical.medical.vo.AppNotificationVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统通知 Mapper 接口
 *
 * @author fourth
 * @since 2025-07-09
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    /**
     * 系统通知详情
     *
     * @param id
     * @return
     */
    NotificationVo getNotificationById(Long id);

    /**
     * 系统通知分页列表
     *
     * @param query
     * @return
     */
    List<NotificationVo> getNotificationPage(NotificationQuery query);

    /**
     * App系统通知详情
     *
     * @param id
     * @return
     */
    AppNotificationVo getAppNotificationById(Long id);

    /**
     * App系统通知分页列表
     *
     * @param query
     * @return
     */
    List<AppNotificationVo> getAppNotificationPage(AppNotificationQuery query);

}
