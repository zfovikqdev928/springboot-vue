package com.zhou.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.VueBlogApplication;
import com.zhou.dto.SysUserDTO;
import com.zhou.entity.SysUser;
import com.zhou.service.impl.SysUserServiceImpl;
import com.zhou.util.Constants;
import com.zhou.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserServiceImpl sysUserService;

    public static void main(String[] args) {
        SpringApplication.run(VueBlogApplication.class, args);
    }

    @GetMapping
    public List<SysUser> findAll() {
        return sysUserService.list();
    }


    @PostMapping("/login")
    public R login(@RequestBody SysUserDTO sysUserDTO) {
        String username = sysUserDTO.getUsername();
        String password = sysUserDTO.getPassword();

        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            return R.error(Constants.CODE_400, "参数错误");
        }
        SysUserDTO userDTO = sysUserService.login(sysUserDTO);
        return R.success(userDTO);
    }

    /**
     * 删除
     *
     * @param id 用户 ID
     * @return true or false
     */
    @DeleteMapping("/del/{id}")
    public boolean delete(@PathVariable Integer id) {
        return sysUserService.removeById(id);
    }


    /**
     * 批量删除
     *
     * @param ids 用户 id
     * @return true or false
     */
    @PostMapping("/del/batch")
    public boolean deleteBatchByIds(@RequestBody List<Integer> ids) {
        return sysUserService.removeByIds(ids);
    }

    /**
     * 新增 / 更新
     *
     * @param sysUser 前台传参用户Json数据
     * @return 返回影响行数
     */
    @PostMapping("/save")
    public boolean save(@RequestBody SysUser sysUser) {
        return sysUserService.saveOrUpdate(sysUser);
    }


    /**
     * 分页查询
     *
     * @param pageNum  当前页
     * @param pageSize 显示条数
     * @return
     */
    @GetMapping("/page")
    public IPage<SysUser> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String username,
                                   @RequestParam(defaultValue = "") String email,
                                   @RequestParam(defaultValue = "") String address) {
        IPage<SysUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        queryWrapper.orderByDesc("id");
        return sysUserService.page(page, queryWrapper);
    }


    /**
     * 导出 excel
     *
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<SysUser> list = sysUserService.list();
        // hutool 工具类创建 writer，写出到磁盘目录
        ExcelWriter writer = ExcelUtil.getWriter(true);
//        writer.addHeaderAlias("id", "ID");
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "手机号");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("createTime", "创建时间");
        writer.setSheet("sheet1");

        // 一次性写出 list 内的对象到 excel，使用默认样式，强制输出标题
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息表", "utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


    /**
     * 导入
     *
     * @param file 这里的名字固定
     * @throws IOException
     */
    @PostMapping("/import")
    public boolean importFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 如果表头是中文，则不能实现导入
        // 导入需要去掉 id 列
        List<SysUser> list = reader.readAll(SysUser.class);
        return sysUserService.saveBatch(list);
    }
}
