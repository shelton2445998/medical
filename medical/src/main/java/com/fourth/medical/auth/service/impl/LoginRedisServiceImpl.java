package com.fourth.medical.auth.service.impl;

import com.fourth.medical.auth.service.LoginRedisService;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.common.constant.RedisKey;
import com.fourth.medical.config.properties.LoginAdminProperties;
import com.fourth.medical.framework.exception.LoginException;
import com.fourth.medical.framework.exception.LoginTokenException;
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
 * @date 2022/7/12
 **/
@Slf4j
@Service
public class LoginRedisServiceImpl implements LoginRedisService {

    private static final TimeUnit TOKEN_TIME_UNIT = TimeUnit.MINUTES;

    @Autowired
    private LoginAdminProperties loginAdminProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    private Integer tokenExpireMinutes;

    @PostConstruct
    public void init() {
        log.info("loginAdminProperties = " + loginAdminProperties);
        tokenExpireMinutes = loginAdminProperties.getTokenExpireMinutes();
    }

    @Override
    public String getLoginRedisKey(String token) {


        String loginRedisKey = String.format(RedisKey.LOGIN_TOKEN, token);


        return loginRedisKey;
    }

    @Override
    public void setLoginVo(String token, LoginVo loginVo) {
        if (loginVo == null) {
            throw new LoginException("登录用户信息不能为空");
        }
        if (loginAdminProperties.isSingleLogin()) {
            // 单点登录，则删除之前该用户的key
            deleteLoginInfoByToken(token);
        }
        // 用户信息
        String loginTokenRedisKey = getLoginRedisKey(token);
        redisTemplate.opsForValue().set(loginTokenRedisKey, loginVo, tokenExpireMinutes, TOKEN_TIME_UNIT);
    }

    @Override
    public LoginVo getLoginVo(String token) {

        if (StringUtils.isBlank(token)) {

            throw new LoginTokenException("token不能为空");
        }

        String loginRedisKey = getLoginRedisKey(token);


        LoginVo loginVo = (LoginVo) redisTemplate.opsForValue().get(loginRedisKey);


        return loginVo;
    }

    @Override
    public void deleteLoginVo(String token) {
        if (StringUtils.isBlank(token)) {
            throw new LoginTokenException("token不能为空");
        }
        String loginTokenRedisKey = getLoginRedisKey(token);
        redisTemplate.delete(loginTokenRedisKey);
    }

    @Override
    public void refreshToken() {
        // 刷新token
        String token = TokenUtil.getToken();
        if (StringUtils.isBlank(token)) {
            return;
        }
        // 刷新key的过期时间
        String loginTokenRedisKey = getLoginRedisKey(token);
        redisTemplate.expire(loginTokenRedisKey, tokenExpireMinutes, TOKEN_TIME_UNIT);
    }

    @Override
    public void deleteLoginInfoByToken(String token) {
        log.info("清除用户的所有redis登录信息：" + token);
        if (StringUtils.isBlank(token)) {
            throw new LoginTokenException("token不能为空");
        }
        int lastIndexOf = token.lastIndexOf(".");
        String userTokenPrefix = token.substring(0, lastIndexOf + 1);
        // 删除之前该用户的key
        String userTokenRedisPrefix = userTokenPrefix + "*";
        String formatRedisTokenPrefix = String.format(RedisKey.LOGIN_TOKEN, userTokenRedisPrefix);
        Set keys = redisTemplate.keys(formatRedisTokenPrefix);
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

}
