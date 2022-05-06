package com.yang.cms.shangchuan;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class uploadController {
    @RequestMapping("/upload")
    public  String upload(String formid,Model model){
        System.out.println("父页面的formid=" + formid);
        model.addAttribute("formid",formid);
        return "shangchuan/up";
    }
}
