package com.zhou.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.common.R;
import com.zhou.entity.SysMenu;
import com.zhou.service.impl.SysMenuServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 周益全
 * @since 2023-02-16
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class SysMenuController {
    @Autowired
    private SysMenuServiceImpl menuService;


    /**
     * 新增 | 更新
     */
    @PostMapping("/save")
    public R save(@RequestBody SysMenu menu) {
        menuService.saveOrUpdate(menu);
        return R.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/del/{id}")
    public R delete(@PathVariable Integer id) {
        menuService.removeById(id);
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
        menuService.removeByIds(ids);
        return R.success();
    }


    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping
    public R findAll(@RequestParam String name) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");

        List<SysMenu> list = menuService.list(queryWrapper);
        log.info("All：", list);

        //找出 pid 为 null 的一级菜单
        List<SysMenu> parentNode = list.stream().filter(SysMenu -> SysMenu.getPid() == null).collect(Collectors.toList());
        log.info("parentNode：",parentNode);

        // 找出一级菜单的子菜单
        for (SysMenu menu : parentNode) {
            // 筛选所有数据中，pid = 父级id的数据，就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return R.success(parentNode);
    }


    /**
     * 查询单条
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R findOne(@PathVariable Integer id) {
        menuService.removeById(id);
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
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return R.success(menuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
