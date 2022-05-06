package com.yang.cms.bianjiqi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class wangupload {

    @RequestMapping("/test/upload")
    @ResponseBody
    public ImgInfo setImgUrl(MultipartFile file, HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        // Get the file and save it somewhere
        byte[] bytes = file.getBytes();
//        System.out.println(new String(bytes));
        String path = request.getServletContext().getRealPath("/image/");
        File imgFile = new File(path);
        if (!imgFile.exists()) {
            imgFile.mkdirs();
        }

        //String fileName = file.getOriginalFilename();// 文件名称
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String datestr = ft.format(new  Date());
        datestr = datestr.replace(" ", "-");
        datestr = datestr.replace(":", "-");
        String fileName = datestr + file.getOriginalFilename();// 文件名称

        System.out.println(path + fileName);

        try (FileOutputStream fos = new FileOutputStream(new File(path + fileName))) {
            int len = 0;
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //String path2 ="E:/Desktop/源码/bc_shop/src/main/resources/resources/image/";
        String path2 ="E:/idea/bc_shop/src/main/resources/resources/image/";

        try (FileOutputStream fos = new FileOutputStream(new File(path2 + fileName))) {
            int len = 0;
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String value = "http://localhost:8080/image/" + fileName;
        String[] values = { value };

        ImgInfo imgInfo = new ImgInfo();
        imgInfo.setError(0);
        imgInfo.setUrl(values);

        System.out.println(imgInfo.toString());
        return imgInfo;
    }


}