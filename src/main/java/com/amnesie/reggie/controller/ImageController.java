package com.amnesie.reggie.controller;

import com.amnesie.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description: 文件上传下载
 * @author: Amnesie
 * @Date: 2022-10-05
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class ImageController {
    @Value("${reggie.img-path}")
    private String imgPath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //使用UUID重新生成文件名，避免文件名重复冲突
        String fileName = UUID.randomUUID().toString();
        //获取原文件名后缀，并添加到生成文件名后
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        fileName = fileName + suffix;
        //检测文件目录是否存在，不存在则生成目录
        File dir = new File(imgPath);
        if(!dir.exists()){
            dir.mkdir();
        }
        //被传入的file是一个临时文件，需要转存到指定位置
        try {
            //组装文件路径+文件名
            file.transferTo(new File(imgPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，读取文件内容
            FileInputStream inputStream = new FileInputStream(new File(imgPath + name));
            //输出流，将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            //关闭资源
            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            log.error("文件丢失" + e.getMessage());
        }
    }
}
