package com.zhou.exception;


import com.zhou.util.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description: 自定义异常
 * @Version:
 */
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(SysUserException.class)
    @ResponseBody
    public R SysUserEx(SysUserException e) {
        System.out.println("消息" + e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }

}
