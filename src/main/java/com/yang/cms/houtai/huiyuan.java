package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;


@Controller
public class huiyuan {
    @RequestMapping("/login") //不是@GetMapping
    public String login(){ //登录页面
        return "houtai/login";
    }

    @RequestMapping("/main") //不是@GetMapping
    public String main(){ //后台首页
        return "houtai/main";
    }

    @RequestMapping("/top") //不是@GetMapping
    public String top(HttpServletRequest request,
                      HttpServletResponse response, Model model) throws UnsupportedEncodingException {
        //读取cookie
        //读取cookie u_id  u_fzid  u_name
        String u_id = "";
        String u_fzid = "";
        String u_name = "";
        Cookie cookie = null;
        Cookie[] cookies = null;
        // 获取 cookies 的数据,是一个数组
        cookies = request.getCookies();
        if(cookies !=null){
            for (int i = 0; i < cookies.length; i++){
                cookie = cookies[i];
                //out.print("参数名 : " + cookie.getName());
                //out.print(" | 参数值: " + URLDecoder.decode(cookie.getValue(), "utf-8") +" <br>");
                if(cookie.getName().equals("u_id")){ //  == 判断可能无效
                    u_id = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
                if(cookie.getName().equals("u_fzid")){ //  == 判断可能无效
                    u_fzid = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
                if(cookie.getName().equals("u_name")){ //  == 判断可能无效
                    u_name = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
            }
            String xinxi_denglu = "用户id："+u_id+ " | 用户分组id：" + u_fzid + " | 用户名："+u_name;
            //System.out.println("用户id："+u_id+ " | 用户分组id：" + u_fzid + " | 用户名："+u_name);
            model.addAttribute("xinxi_denglu",xinxi_denglu);

            //根据分组id，读取1级权限菜单信息
            String sql_quanxian = "select * from quanxian_fenzu where id="+u_fzid;  //查询语句
            Map<String, Object> map = jdbcTemplate.queryForMap(sql_quanxian);
            System.out.println(map);
            //{id=32, user_name=huang, user_password=123456, fenzu_id=7, add_date=2020-09-12T03:53:19, beizhu=123456}
            System.out.println(map.get("id"));
            //quanxian_1=,286,315,322,330,331,332,333,334,  1级权限菜单的id集合字符串
            String quanxian_1=map.get("quanxian_1").toString();
            System.out.println(quanxian_1);

//            String Array1[] = quanxian_1.split(",");
//            for( int j = 1; j < Array1.length; j++ ) {
//                System.out.println(Array1[j]);
//            }

            String sql2="select  id,caidan_mingcheng,paixu_id from quanxian_caidan where id in(0"+quanxian_1+"0) order by paixu_id desc";
            List<Map<String, Object>> list_quanxian1 = jdbcTemplate.queryForList(sql2);

            model.addAttribute("list_quanxian1",list_quanxian1);

        }
        return "houtai/top";
    }

    @RequestMapping("/center") //不是@GetMapping
    public String center(){
        return "houtai/center";
    }

    @RequestMapping("/down") //不是@GetMapping
    public String down(){
        return "houtai/down";
    }

    @RequestMapping("/middel") //不是@GetMapping
    public String middel(){
        return "houtai/middel";
    }

    @RequestMapping("/left") //不是@GetMapping
    public String left(HttpServletRequest request,
                       HttpServletResponse response,Integer quanxian1_id,String quanxian1_mc,Model model) throws UnsupportedEncodingException {
        System.out.println("开始读取2级菜单");
        System.out.println("获取的1级菜单id="+quanxian1_id + "| 菜单名称=" + quanxian1_mc);
        model.addAttribute("quanxian1_mc",quanxian1_mc);

        //读取cookie
        //读取cookie u_id  u_fzid  u_name
        String u_id = "";
        String u_fzid = "";
        String u_name = "";
        Cookie cookie = null;
        Cookie[] cookies = null;
        // 获取 cookies 的数据,是一个数组
        cookies = request.getCookies();
        if(cookies !=null){
            for (int i = 0; i < cookies.length; i++){
                cookie = cookies[i];
                //out.print("参数名 : " + cookie.getName());
                //out.print(" | 参数值: " + URLDecoder.decode(cookie.getValue(), "utf-8") +" <br>");
                if(cookie.getName().equals("u_id")){ //  == 判断可能无效
                    u_id = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
                if(cookie.getName().equals("u_fzid")){ //  == 判断可能无效
                    u_fzid = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
                if(cookie.getName().equals("u_name")){ //  == 判断可能无效
                    u_name = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
            }
            //根据分组id，读取1级权限菜单信息
            String sql_quanxian = "select * from quanxian_fenzu where id="+u_fzid;  //查询语句
            Map<String, Object> map = jdbcTemplate.queryForMap(sql_quanxian);
            //System.out.println(map);
            //{id=32, user_name=huang, user_password=123456, fenzu_id=7, add_date=2020-09-12T03:53:19, beizhu=123456}
            //System.out.println(map.get("id"));
            //quanxian_1=,286,315,322,330,331,332,333,334,  1级权限菜单的id集合字符串
            String quanxian_1=map.get("quanxian_1").toString();
            String quanxian_2=map.get("quanxian_2").toString();
            String quanxian_3=map.get("quanxian_3").toString();
            //System.out.println(quanxian_2);

            String sql2="select  id,caidan_mingcheng from quanxian_caidan where caidan_jibie=2 and caidan_suoshu="+quanxian1_id+" and id in(0"+quanxian_2+"0) ";
            System.out.println(sql2);
            List<Map<String, Object>> list_quanxian2 = jdbcTemplate.queryForList(sql2);
            System.out.println("2级权限菜单="+list_quanxian2);
            model.addAttribute("list_quanxian2",list_quanxian2);

            String sql3="select  id,caidan_mingcheng,caidan_suoshu,caidan_lujing from quanxian_caidan where caidan_jibie=3 and  id in(0"+quanxian_3+"0) ";
            System.out.println(sql3);
            List<Map<String, Object>> list_quanxian3 = jdbcTemplate.queryForList(sql3);
            System.out.println("3级权限菜单="+list_quanxian3);
            model.addAttribute("list_quanxian3",list_quanxian3);

        }

        return "houtai/left";
    }

    @RequestMapping("/tab") //不是@GetMapping
    public String tab(){
        return "houtai/tab";
    }

    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //会员登录
    @RequestMapping("/login_check")
    public String login_check(String yhm, String mm, HttpServletResponse response){
        System.out.println("---------会员登录-----------");
        System.out.println("用户名="+yhm);
        System.out.println("密码="+mm);

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*)  FROM quanxian_yonghu where user_name='"+yhm+"' and user_password='"+mm+"'";
        System.out.println(sql01);
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);

         String sql2="select * from quanxian_yonghu where user_name='"+yhm+"' and user_password='"+mm+"'";
//        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
//        System.out.println(list);
//        for (Map<String, Object> map : list) {
//            System.out.println(map);
//
//        }


        //下面是重定向（注意和转发的区别）
        //添加redirect:这个毋庸置疑是mvc的语法问题，其次这里需要注意的是需要手动添加.html，否则会报404，
        // 而重定向则会自动去public、static文件夹下寻找
        if(size>0){
            Map<String, Object> map = jdbcTemplate.queryForMap(sql2);
            System.out.println(map);
            //{id=32, user_name=huang, user_password=123456, fenzu_id=7, add_date=2020-09-12T03:53:19, beizhu=123456}
            System.out.println(map.get("id"));

            //写登录cookie
            Cookie u_id = new Cookie("u_id", map.get("id").toString());
            u_id.setMaxAge(60*60*24);   //设置cookie过期时间为24小时。
            response.addCookie(u_id); //在响应头部添加cookie

            Cookie u_fzid = new Cookie("u_fzid", map.get("fenzu_id").toString());
            u_fzid.setMaxAge(60*60*24);   //设置cookie过期时间为24小时。
            response.addCookie(u_fzid); //在响应头部添加cookie

            Cookie u_name = new Cookie("u_name", yhm);
            u_name.setMaxAge(60*60*24);   //设置cookie过期时间为24小时。
            response.addCookie(u_name); //在响应头部添加cookie

            return "redirect:login_ok.html";
        }else{
            return "redirect:login_error.html";
        }
    }

    //会员退出登录
    @RequestMapping("/logout")
    public String logout(){
        System.out.println("退出系统");
        return "houtai/logout.html";
    }
}

