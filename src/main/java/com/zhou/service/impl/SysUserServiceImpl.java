package com.zhou.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhou.dto.SysUserDTO;
import com.zhou.entity.SysUser;
import com.zhou.exception.SysUserException;
import com.zhou.mapper.SysUserMapper;
import com.zhou.service.ISysUserService;
import com.zhou.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 周益全
 * @since 2023-02-07
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUserDTO login(SysUserDTO sysUserDTO) {
        SysUser one = getSysUserInfoUP(sysUserDTO);
        if (one != null) {
            BeanUtil.copyProperties(one, sysUserDTO, true);
            return sysUserDTO;
        } else {
            throw new SysUserException(Constants.CODE_600, "用户名或密码错误");
        }

    }

    @Override
    public boolean register(SysUserDTO sysUserDTO) {
        SysUser one = getSysUserInfoU(sysUserDTO);
        if (one == null) {
            one = new SysUser();
            BeanUtil.copyProperties(sysUserDTO, one, true);
            int result = sysUserMapper.insert(one);
            if (result == 1) {
                return true;
            }
        } else {
            throw new SysUserException(Constants.CODE_600, "用户已存在");
        }
        return false;
    }

    /**
     * 查询用户名和密码
     *
     * @param sysUserDTO
     * @return
     */
    private SysUser getSysUserInfoUP(SysUserDTO sysUserDTO) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", sysUserDTO.getUsername());
        queryWrapper.eq("password", sysUserDTO.getPassword());
        SysUser one;
        try {
            one = sysUserMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            throw new SysUserException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    private SysUser getSysUserInfoU(SysUserDTO sysUserDTO) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", sysUserDTO.getUsername());
        SysUser one;
        try {
            one = sysUserMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            throw new SysUserException(Constants.CODE_500, "系统错误");
        }
        return one;
    }
}
