package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Controller
public class pc_mem {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //注册
    @RequestMapping("/pc_huiyuan_zhuce") //不是@GetMapping
    public String pc_huiyuan_zhuce(Model model){
        System.out.println("当前访问的是 会员注册 页面");

        //model.addAttribute("test","mytest");
        return  "/pc/pc_huiyuan_zhuce";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }//注册

    //登录
    @RequestMapping("/pc_huiyuan_denglu") //不是@GetMapping
    public String pc_huiyuan_denglu(Model model){
        System.out.println("当前访问的是 会员注册 页面");

        //model.addAttribute("test","mytest");
        return  "/pc/pc_huiyuan_denglu";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }//登录

    //会员首页
    @RequestMapping("/pc_mem_index") //不是@GetMapping
    public String pc_mem_index(Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        System.out.println("当前访问的是 会员中心 首页");

        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //根据用户id，获取用户信息
        String sql_yonghu="select  * from huiyuan where id=" + session.getAttribute("mem_id");
        Map<String, Object> yonghu = jdbcTemplate.queryForMap(sql_yonghu);
        model.addAttribute("yonghu",yonghu);
        System.out.println("获取的用户信息="+yonghu);

        //model.addAttribute("test","mytest");
        return  "/pc_mem/pc_mem_index";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }//会员首页

    //会员 》 信息修改
    @RequestMapping("/pc_mem_xinxi_xiugai") //不是@GetMapping
    public String pc_mem_xinxi_xiugai(String act,Model model,
                                      String xingming,String xingbie,String qq,String youxiang,
                                      String touxiang,String jianjie,String xinxi_neirong,
                                      HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        System.out.println("当前访问的是 会员中心 》 信息修改页面");

        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        if(act==null || "".equals(act)){
            //根据用户id，获取用户信息
            String sql_yonghu="select  * from huiyuan where id=" + session.getAttribute("mem_id");
            Map<String, Object> yonghu = jdbcTemplate.queryForMap(sql_yonghu);
            model.addAttribute("yonghu",yonghu);
            System.out.println("获取的用户信息="+yonghu);
            return  "/pc_mem/pc_mem_xinxi_xiugai";
        }else{
            //修改数据 - 更新数据库
            if(act.equals("xiugai_act")){
                System.out.println("开始更新会员数据");
                String sql = "Update huiyuan Set xingming=?,xingbie=?,qq=?,youxiang=?,touxiang=?,jianjie=?,jieshao=? where id=?";
                jdbcTemplate.update(sql, xingming,xingbie,qq,youxiang,touxiang,jianjie,xinxi_neirong,session.getAttribute("mem_id"));
                System.out.println("更新成功");
                return "redirect:/pc_mem_index";
            }
            return "redirect:/pc_mem_index";
        }


        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    } //会员 》 信息修改

    //会员密码修改
    @RequestMapping("/pc_mem_mima") //不是@GetMapping
    public String pc_mem_mima(Model model){
        System.out.println("当前访问的是 会员密码修改 页面");
        return  "/pc_mem/pc_mem_mima";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }//会员密码修改

}
