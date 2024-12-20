package com.sky.controller.admin;


import cn.hutool.core.io.FileUtil;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {
    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";


    //文件上传
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传{}",file);
        String originalFilename = file.getOriginalFilename();  // 文件的原始名称    aaa.png
        log.info("文件的原始名称：{}", originalFilename);
        String mainName = FileUtil.mainName(originalFilename);  // 文件的主名称    aaa
        log.info("文件的原始主名称：{}", mainName);
        String extName = FileUtil.extName(originalFilename);  // 文件的扩展名(后缀)    .png
        log.info("文件的原始后缀：{}", extName);

        // 如果当前文件的父级目录不存在，就创建
        if(!FileUtil.exist(ROOT_PATH)) {
            FileUtil.mkdir(ROOT_PATH);    // 如果当前文件的父级目录不存在，就创建
        }

        // 如果当前上传的文件已经存在了，那么这个时候我就要重命名一个文件
        if(FileUtil.exist(ROOT_PATH + File.separator + originalFilename)){
            originalFilename = System.currentTimeMillis() + "-" + mainName + "." + extName;
            log.info("文件已经存在，重命名后的文件名：{}", originalFilename);
        }


        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);   // 要保存的文件地址/目录
        file.transferTo(saveFile);  // 存储文件到本地的磁盘里面去


        // 获取本机IP地址
        String ip = "localhost";
        // 获取本机端口号
        String port = "8080";

        // 返回文件的链接，这个链接就是文件的下载地址，这个下载地址就是我的后台提供出来的
        String url = "http://" + ip + ":" + port + "/file/download?fileName=" + originalFilename;
        log.info("文件的下载地址：{}", url);
        return Result.success(url);
        }
}
