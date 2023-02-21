package com.zhou.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhou.common.R;
import com.zhou.entity.SysFile;
import com.zhou.service.impl.SysFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author 周益全
 * @since 2023-02-20
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class SysFileController {

    @Autowired
    private SysFileServiceImpl fileService;

    @Value("${files.upload.path}")
    private String fileUploadPath; // 文件存储地址


    /**
     * 文件上传
     *
     * @param file
     * @return 返回图片的 url 地址
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();   // 源文件名称
        String type = FileNameUtil.extName(originalFilename);   //文件类型
        long size = file.getSize();                             //文件大小


        // 文件UUID
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + "." + type;
        // 判断文件是否存在
        File uploadParentFile = new File(fileUploadPath + fileUUID);

        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();  // 若不存在创建目录
        }


        String md5 = "";
        String url = "";

        // 存入本地磁盘
        file.transferTo(uploadParentFile);
        // 获取文件 md5
        md5 = SecureUtil.md5(uploadParentFile);

        SysFile dbMd5 = getFileMd5(md5);
        if (dbMd5 != null) {
            url = dbMd5.getUrl();
            uploadParentFile.delete();

        } else {
            url = "http://localhost:9090/file/" + fileUUID;
        }

        SysFile sysFile = new SysFile();
        sysFile.setName(originalFilename);
        sysFile.setType(type);
        sysFile.setSize(size / 1024); // 存储为 kb
        sysFile.setUrl(url);
        sysFile.setMd5(md5);
        fileService.save(sysFile);


        return url;
    }


    /**
     * 文件下载
     *
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void downloadFile(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID); //文件路径
        ServletOutputStream os = response.getOutputStream();

        // 设置输出流格式
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");
        // 读取文件字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();

    }


    /**
     * 通过文件 md5 查询该文件是否存在
     *
     * @param md5
     * @return
     */
    private SysFile getFileMd5(String md5) {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<SysFile> list = fileService.list(queryWrapper);
        return list.size() == 0 ? null : list.get(0);
    }


    /**
     * 文件分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R findPage(@RequestParam Integer pageNum,
                      @RequestParam Integer pageSize,
                      @RequestParam(defaultValue = "") String name) {

        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        IPage<SysFile> page = new Page<>(pageNum, pageSize);

        return R.success(fileService.page(page, queryWrapper));
    }


    /**
     * 删除
     *
     * @param id
     * @return true or false
     */
    @DeleteMapping("/del/{id}")
    public R delete(@PathVariable Integer id) {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SysFile file = fileService.getById(id);
        log.info("【删除】数据库查询到的值为：" + file.toString());
        file.setIsDelete(true);
        return R.success(fileService.update(file, queryWrapper));
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return true or false
     */
    @PostMapping("/del/batch")
    public R deleteBatchByIds(@RequestBody List<Integer> ids) {

        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<SysFile> list = fileService.list(queryWrapper);
        for (SysFile file : list) {
            file.setIsDelete(true);
        }
        return R.success(fileService.updateBatchById(list));
    }


    /**
     * 新增 / 更新
     *
     * @param sysFile
     * @return 返回影响行数
     */
    @PostMapping("/save")
    public R save(@RequestBody SysFile sysFile) {
        return R.success(fileService.saveOrUpdate(sysFile));
    }

}
