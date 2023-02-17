package com.zhou.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.common.R;
import com.zhou.entity.SysRole;
import com.zhou.service.impl.SysRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 周益全
 * @since 2023-02-16
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Autowired
    private SysRoleServiceImpl roleService;

    /**
     * 新增 | 更新
     */
    @PostMapping("/save")
    public R save(@RequestBody SysRole role) {
        roleService.saveOrUpdate(role);
        return R.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Integer id) {
        roleService.removeById(id);
        return R.success();
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/del/batch")
    public R delete(@RequestBody List<Integer> ids) {
        roleService.removeByIds(ids);
        return R.success();
    }


    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping
    public R findAll() {
        return R.success(roleService.list());
    }


    /**
     * 查询单条
     *
     * @param id
     * @return
     */
    @GetMapping("/del/{id}")
    public R findOne(@PathVariable Integer id) {
        roleService.removeById(id);
        return R.success();
    }


    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R findPage(@RequestParam Integer pageNum,
                      @RequestParam Integer pageSize,
                      @RequestParam String name) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return R.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}
