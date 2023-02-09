package com.zhou.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zhou.entity.SysUser;
import com.zhou.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description: JWT 工具类
 * @Version:
 */
@Component
public class TokenUtils {

    private static SysUserServiceImpl staticSysUserService;

    @Autowired
    private SysUserServiceImpl sysUserService;

    /**
     * 生成 Token
     *
     * @param userId
     * @param password
     * @return
     */
    public static String generateToken(String userId, String password) {
        return JWT.create()
                .withAudience(userId)   // 载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 10))    // 过期时间，10小时之后过期
                .sign(Algorithm.HMAC256(password)); //以 password 作为 token 的密钥，也就是签名
    }

    /**
     * 获取当前登录的用户信息
     *
     * @return
     */
    public static SysUser getCurrentUser() {

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticSysUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @PostConstruct
    public void setSysUserService() {
        staticSysUserService = sysUserService;
    }
}
