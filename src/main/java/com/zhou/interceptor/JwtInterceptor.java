package com.zhou.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zhou.common.Constants;
import com.zhou.entity.SysUser;
import com.zhou.exception.SysUserException;
import com.zhou.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description: JWT 拦截器
 * @Version:
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        // 如果不是映射到方法，直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 如果 token 是空字符串
        if (StrUtil.isBlank(token)) {
            throw new SysUserException(Constants.CODE_401, "用户无token");
        }
        // 获取 token 携带的数据
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0); //获取 userId
        } catch (JWTDecodeException e) {
            throw new SysUserException(Constants.CODE_401, "token验证失败");

        }

        // 工具 userId 查询数据库
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new SysUserException(Constants.CODE_401, "用户不存在，请重新登录");

        }


        // 通过用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(sysUser.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new SysUserException(Constants.CODE_600, "用户验证失败");
        }

        return true;
    }
}
