package com.fourth.medical.util;

import com.fourth.medical.auth.util.AppLoginUtil;
import com.fourth.medical.auth.util.LoginUtil;
import com.fourth.medical.auth.vo.AppLoginVo;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.common.enums.SystemType;
import com.fourth.medical.framework.query.DataRangeQuery;

/**
 * @author geekidea
 * @date 2023/12/1
 **/
public class DataRangeUtil {

    /**
     * 设置Admin登录用户信息到查询参数中
     *
     * @param dataRangeQuery
     */
    public static void handleAdminLogin(DataRangeQuery dataRangeQuery) {
        LoginVo loginVo = LoginUtil.getLoginVo();
        if (loginVo != null) {
            Long userId = loginVo.getUserId();
            dataRangeQuery.setLoginCommonUserId(userId);
            dataRangeQuery.setLoginAdmin(loginVo.isAdmin());
            dataRangeQuery.setLoginUserId(userId);
            dataRangeQuery.setLoginRoleId(loginVo.getRoleId());
            dataRangeQuery.setLoginRoleCode(loginVo.getRoleCode());
        }
    }

    /**
     * 设置App登录用户信息到查询参数中
     *
     * @param dataRangeQuery
     */
    public static void handleAppLogin(DataRangeQuery dataRangeQuery) {
        AppLoginVo appLoginVo = AppLoginUtil.getLoginVo();
        if (appLoginVo != null) {
            Long userId = appLoginVo.getUserId();
            dataRangeQuery.setLoginCommonUserId(userId);
            dataRangeQuery.setLoginAppUserId(userId);
            dataRangeQuery.setLoginAppUserRoleId(appLoginVo.getUserRoleId());
            dataRangeQuery.setLoginAppUserRoleCode(appLoginVo.getUserRoleCode());
        }
    }

    /**
     * 设置登录用户信息到查询参数中
     *
     * @param dataRangeQuery
     */
    public static void handleCommonLogin(DataRangeQuery dataRangeQuery) {
        // 判断token是那个端
        SystemType systemType = SystemTypeUtil.getSystemTypeByToken();
        if (SystemType.ADMIN == systemType) {
            LoginVo loginVo = LoginUtil.getLoginVo();
            if (loginVo != null) {
                dataRangeQuery.setLoginCommonUserId(loginVo.getUserId());
            }
        } else if (SystemType.APP == systemType) {
            AppLoginVo appLoginVo = AppLoginUtil.getLoginVo();
            if (appLoginVo != null) {
                dataRangeQuery.setLoginCommonUserId(appLoginVo.getUserId());
            }
        }
    }

}
