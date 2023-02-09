package com.zhou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhou.dto.SysUserDTO;
import com.zhou.entity.SysUser;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 周益全
 * @since 2023-02-07
 */
public interface ISysUserService extends IService<SysUser> {
    SysUserDTO login(SysUserDTO sysUserDTO);
}
