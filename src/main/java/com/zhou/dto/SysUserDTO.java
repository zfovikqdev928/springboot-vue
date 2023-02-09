package com.zhou.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 接受前端登录请求的参数
 */

@Data
@ToString
public class SysUserDTO implements Serializable {
    private String username;
    private String password;
    private String nickname;
    private String email;

}
