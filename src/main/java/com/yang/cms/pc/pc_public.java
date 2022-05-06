package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Controller
public class pc_public {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //top 测试用
    @RequestMapping("/pc_top") //不是@GetMapping
    public String pc_top(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println("当前访问的是顶部pc_top文件");
        System.out.println(request.getCookies());

        //读取cookie
        String mem_id = "";
        Cookie cookie = null;
        Cookie[] cookies = null;
        // 获取 cookies 的数据,是一个数组
        cookies = request.getCookies();
        if(cookies !=null) {
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                System.out.println("参数名 : " + cookie.getName());
                //out.print(" | 参数值: " + URLDecoder.decode(cookie.getValue(), "utf-8") +" <br>");
                if (cookie.getName().equals("mem_id")) { //  == 判断可能无效
                    mem_id = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
            }
        }
        System.out.println("mem_id="+mem_id);

        model.addAttribute("test","mytest");
        return  "/pc/pc_top";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }

    //头部文件
    @RequestMapping("/pc_toubu") //不是@GetMapping
    public String pc_toubu(Model model){
        System.out.println("当前访问的是顶部pc_toubu文件");

        model.addAttribute("test","mytest");
        return  "/pc/pc_toubu";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }

    //关于我们
    @RequestMapping("/pc_guanyu_women") //不是@GetMapping
    public String pc_guanyu_women(Integer id,Model model){
        System.out.println("当前访问的是 关于我们 页面");
        System.out.println("信息id="+id);

        //根据信息id，获取对应数据
        String sql_xinxi="select  * from aboutus where id=" + id;
        Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
        System.out.println(xinxi);
        model.addAttribute("xinxi",xinxi);

        //model.addAttribute("test","mytest");
        return  "/pc/pc_guanyu_women";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }

    //留言反馈 pc_guanyu_liuyan
    @RequestMapping("/pc_guanyu_liuyan") //不是@GetMapping
    public String pc_guanyu_liuyan(Integer id,Model model){
        System.out.println("当前访问的是 留言反馈 页面");

        //model.addAttribute("test","mytest");
        return  "/pc/pc_guanyu_liuyan";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }

}
