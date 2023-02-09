package com.zhou.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 周益全 (https://gitee.com/zfovikqdev928)
 * @BugFeedBackEmail: zfovikqdev928@126.com
 * @Date: Create in 2023-02
 * @Description: 返回结果集统一封装
 * @Version:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {
    private String code;
    private String msg;
    private Object data;

    public static R success() {
        return new R(Constants.CODE_200, "", null);
    }

    public static R success(Object data) {
        return new R(Constants.CODE_200, "", data);
    }

    public static R error(String code, String msg) {
        return new R(code, msg, null);
    }

    public static R error() {
        return new R(Constants.CODE_500, "系统错误", null);
    }

}
