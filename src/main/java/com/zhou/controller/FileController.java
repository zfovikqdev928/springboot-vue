package com.zhou.controller;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author 周益全
 * @date 2023-02
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath; // 文件存储地址


    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); // 源文件名称

        String type = FileNameUtil.extName(originalFilename);   //文件类型
        long size = file.getSize(); //文件大小


        // 先存储到磁盘
        File uploadParentFile = new File(fileUploadPath);

        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();  // 若不存在创建目录
        }

        String uuid = IdUtil.fastSimpleUUID(); // 文件唯一标识码
        File uploadFile = new File(fileUploadPath + uuid + "." + type); //文件最终存储路径


        // 把获取文件存储到磁盘目录
        file.transferTo(uploadFile);


        // 存储到数据库
        return "";

    }
}
