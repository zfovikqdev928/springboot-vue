package com.zhou.exception;

import lombok.Data;
import lombok.Getter;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description:
 * @Version:
 */
@Getter
public class SysUserException extends RuntimeException {
    private String code;

    public SysUserException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
