package com.fourth.medical.auth.service.impl;

import com.fourth.medical.auth.service.AppLoginRedisService;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.AppLoginVo;
import com.fourth.medical.common.constant.RedisKey;
import com.fourth.medical.common.enums.SystemType;
import com.fourth.medical.config.properties.LoginAppProperties;
import com.fourth.medical.framework.exception.LoginException;
import com.fourth.medical.framework.exception.LoginTokenException;
import com.fourth.medical.util.SystemTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author geekidea
 * @date 2022/6/26
 **/
@Slf4j
@Service
public class AppLoginRedisServiceImpl implements AppLoginRedisService {

    private static final TimeUnit TOKEN_TIME_UNIT = TimeUnit.DAYS;

    @Autowired
    private LoginAppProperties loginAppProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    private Integer tokenExpireDays;

    @PostConstruct
    public void init() {
        log.info("loginAppProperties = " + loginAppProperties);
        tokenExpireDays = loginAppProperties.getTokenExpireDays();
    }

    @Override
    public String getLoginRedisKey(String token) {
        String loginRedisKey = String.format(RedisKey.LOGIN_TOKEN, token);
        return loginRedisKey;
    }

    @Override
    public void setLoginVo(String token, AppLoginVo appLoginVo) {
        if (appLoginVo == null) {
            throw new LoginException("登录用户信息不能为空");
        }
        if (loginAppProperties.isSingleLogin()) {
            // 单点登录，则删除之前该用户的key
            deleteLoginInfoByToken(token);
        }
        // 用户信息
        String loginTokenRedisKey = getLoginRedisKey(token);
        try {
            redisTemplate.opsForValue().set(loginTokenRedisKey, appLoginVo, tokenExpireDays, TOKEN_TIME_UNIT);
            log.info("APP用户登录信息已保存到Redis: userId={}, username={}, token={}", 
                    appLoginVo.getUserId(), appLoginVo.getUsername(), token);
        } catch (Exception e) {
            log.error("保存APP用户登录信息到Redis失败: " + e.getMessage(), e);
            throw new LoginException("保存登录信息失败");
        }
    }

    @Override
    public AppLoginVo getLoginVo(String token) {
        if (StringUtils.isBlank(token)) {
            log.warn("获取APP登录信息失败: token为空");
            throw new LoginTokenException("token不能为空");
        }
        
        String loginRedisKey = getLoginRedisKey(token);
        try {
            AppLoginVo appLoginVo = (AppLoginVo) redisTemplate.opsForValue().get(loginRedisKey);
            
            if (appLoginVo == null) {
                log.warn("Redis中不存在对应的APP登录信息: token={}, key={}", token, loginRedisKey);
            } else {
                // 刷新token过期时间
                redisTemplate.expire(loginRedisKey, tokenExpireDays, TOKEN_TIME_UNIT);
                log.debug("成功从Redis获取APP用户信息: userId={}, username={}", 
                        appLoginVo.getUserId(), appLoginVo.getUsername());
            }
            
            return appLoginVo;
        } catch (Exception e) {
            log.error("从Redis获取APP登录信息异常: token=" + token, e);
            return null;
        }
    }

    @Override
    public void deleteLoginVo(String token) {
        if (StringUtils.isBlank(token)) {
            throw new LoginTokenException("token不能为空");
        }
        String loginTokenRedisKey = getLoginRedisKey(token);
        try {
            redisTemplate.delete(loginTokenRedisKey);
            log.info("删除APP用户登录信息: token={}", token);
        } catch (Exception e) {
            log.error("删除APP用户登录信息失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void refreshToken() {
        String token = TokenUtil.getToken();
        if (StringUtils.isBlank(token)) {
            return;
        }
        
        // 获取当前系统类型
        SystemType systemType = SystemTypeUtil.getSystemTypeByToken(token);
        // 只有APP端才需要刷新
        if (SystemType.APP != systemType) {
            return;
        }
        
        // 获取Redis中的登录信息
        String loginRedisKey = getLoginRedisKey(token);
        boolean hasKey = redisTemplate.hasKey(loginRedisKey);
        if (hasKey) {
            // 刷新过期时间
            redisTemplate.expire(loginRedisKey, tokenExpireDays, TOKEN_TIME_UNIT);
            log.debug("刷新APP用户Token过期时间: token={}", token);
        }
    }

    /**
     * 删除用户登录信息
     *
     * @param token
     */
    public void deleteLoginInfoByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return;
        }
        try {
            // 获取Redis中的登录信息
            AppLoginVo appLoginVo = getLoginVo(token);
            if (appLoginVo != null) {
                // 获取用户id
                Long userId = appLoginVo.getUserId();
                // 获取所有的token
                Set<String> keys = redisTemplate.keys(RedisKey.LOGIN_TOKEN.replace("%s", "*"));
                if (CollectionUtils.isEmpty(keys)) {
                    return;
                }
                for (String key : keys) {
                    AppLoginVo loginVo = (AppLoginVo) redisTemplate.opsForValue().get(key);
                    if (loginVo != null && userId.equals(loginVo.getUserId())) {
                        redisTemplate.delete(key);
                        log.info("单点登录模式，删除用户之前的登录信息: userId={}, key={}", userId, key);
                    }
                }
            }
        } catch (Exception e) {
            log.error("删除用户之前登录信息异常: " + e.getMessage(), e);
        }
    }
}
