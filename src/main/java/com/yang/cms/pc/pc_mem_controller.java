package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

@RestController
public class pc_mem_controller {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //验证 用户注册
    @RequestMapping("/check_mem_reg")
    public String check_mem_reg(String cs_yhm,String cs_mm)
    {
        System.out.println("开始校验输入的用户名和密码");
        System.out.println("输入的用户名=" + cs_yhm);
        System.out.println("输入的密码="+cs_mm);

        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        //判断要注册的账号是否已经被注册
        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*)  FROM huiyuan where shouji='"+cs_yhm+"'";
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据，表示该账号已经注册
            return "<script language='JavaScript'>alert('该账号已经注册');</script>";
        }else{ //注册账号
            System.out.println("注册账号，插入到数据库");
            String sql = "Insert into huiyuan(shouji,mima,add_riqi,add_shijian) values(?,?,?,?)";
            jdbcTemplate.update(sql, cs_yhm,cs_mm,mydate1,mydate2);
            System.out.println("注册用户信息，成功插入到数据库");
            return "<script language='JavaScript'>alert('注册成功');window.parent.location='/pc_huiyuan_denglu';</script>";
        }

        //return "注册成功"; //直接输出
        //return "<script language='JavaScript'>window.parent.document.example."+formid+".value='"+wenjianming+"';</script>";
    }//验证 用户注册

    //验证 用户登录
    @RequestMapping("/check_mem_login")
    public String check_mem_login(String cs_yhm, String cs_mm, HttpServletResponse response)
    {
        System.out.println("开始校验输入的用户名和密码");
        System.out.println("输入的用户名=" + cs_yhm);
        System.out.println("输入的密码="+cs_mm);

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*)  FROM huiyuan where shouji='"+cs_yhm+"' and mima='"+cs_mm+"'";
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据，表示账号密码正确
            //写登录cookie
            String sql2="select * from huiyuan where shouji='"+cs_yhm+"' and mima='"+cs_mm+"'";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql2);
            System.out.println(map);
            //{id=744, shouji=13516821613, mima=123456, fl_id=1, xingming=李四, xingbie=男, qq=444444,......
            System.out.println(map.get("id"));

            //写登录cookie,登录会员的id
            Cookie mem_id = new Cookie("mem_id", map.get("id").toString());
            mem_id.setMaxAge(60*60*24);   //设置cookie过期时间为24小时。
            response.addCookie(mem_id); //在响应头部添加cookie
            //写登录cookie,登录会员的名称（这里是手机号码）
            Cookie mem_name = new Cookie("mem_name", map.get("shouji").toString());
            mem_name.setMaxAge(60*60*24);   //设置cookie过期时间为24小时。
            response.addCookie(mem_name); //在响应头部添加cookie

            //成功登录，页面跳转
            return "<script language='JavaScript'>alert('登录成功');window.parent.location='/pc_mem_index';</script>";
        }else{ //账号错误
            return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        }

        //return "注册成功"; //直接输出
        //return "<script language='JavaScript'>window.parent.document.example."+formid+".value='"+wenjianming+"';</script>";
    }//验证 用户登录

    //删除 用户资讯收藏
    @RequestMapping("/pc_mem_zixun_shoucang_del")
    public String pc_mem_zixun_shoucang_del(Integer shoucang_id)
    {
        //删除数据
        String sql = "delete from zixun_shoucang where id=?";
        jdbcTemplate.update(sql, shoucang_id);
        System.out.println("删除成功");

        return "<script language='JavaScript'>alert('删除成功');window.parent.location='/pc_mem_zixun_shoucang';</script>";
    }//删除 用户资讯收藏
}
