package com.yang.cms.shangchuan;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class myupload {


    @RequestMapping("/upload2")
    public String upload2(MultipartFile fileImage,String formid) throws IOException {
        System.out.println("传递过来的formid=" + formid);
        System.out.println("原始文件名称：" + fileImage.getOriginalFilename());

        // final String imagePathRoot = "E:/Desktop/源码/bc_shop/src/main/resources/resources/upload/";
        final String imagePathRoot = "E:/idea/bc_shop/src/main/resources/resources/upload/";

        File file = new File(imagePathRoot);

        if (!file.exists()) {
            file.mkdirs();
        }

        String fileName = fileImage.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String imageFilePath = imagePathRoot + uuid + fileType;
        fileImage.transferTo(new File(imageFilePath));

        System.out.println("保存的文件名：" + uuid + fileType);
        String wenjianming = uuid + fileType;

        //return "upload ok"; //直接输出
        return "<script language='JavaScript'>window.parent.document.example."+formid+".value='"+wenjianming+"';</script>";
    }
}
