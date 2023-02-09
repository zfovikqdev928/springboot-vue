package com.zhou.util;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description: JWT 工具类
 * @Version:
 */
public class TokenUtils {
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
}
