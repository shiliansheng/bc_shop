package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Controller
public class guanliyuan {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //密码修改
    @RequestMapping("/ht_mima") //不是@GetMapping
    public String ht_mima(@CookieValue("u_id") String cookie_u_id, @CookieValue("u_name") String cookie_u_name,
                          Model model,String act,String mm1,String mm2)
    {
        //登录写的cookie：u_id（登录用户id）  u_fzid（登录用户分组） u_name（登录用户名）
        System.out.println("cookie的u_id=" + cookie_u_id); //通过注解获取cookie

        model.addAttribute("cookie_u_id",cookie_u_id);
        model.addAttribute("cookie_u_name",cookie_u_name);
        model.addAttribute("jieguo","");

        if(act==null || "".equals(act)){
            System.out.println("没有操作");

        }else{
            if(act.equals("xiugai")){
                System.out.println("原始密码="+mm1);
                System.out.println("新密码="+mm2);

                int size = 0;  //总的数据条数 > 有多少数据
                String sql01 = "SELECT count(*)  FROM quanxian_yonghu where id="+cookie_u_id+" and user_password='"+mm1+"'";
                System.out.println(sql01);
                size = jdbcTemplate.queryForObject(sql01,Integer.class);
                if(size>0){//原始密码正确
                    System.out.println("原始密码正确，开始修改密码");
                    //更新密码
                    String sql_mima = "Update quanxian_yonghu Set user_password=? where id=?";
                    jdbcTemplate.update(sql_mima, mm2,cookie_u_id);

                    model.addAttribute("jieguo","修改完毕");
                }else{//原始密码错误
                    System.out.println("原始密码错误！");
                    model.addAttribute("jieguo","原始密码正确错误");
                }

            }
//            return "redirect:/tab";
        }
        return "houtai/quanxian/mima";
    }

    //录入用户
    @RequestMapping("/quanxian_user_add") //不是@GetMapping
    public String quanxian_user_add(Model model,String act,
                                    String user_name,String user_password,String beizhu,Integer fenzu_id)
    {
        //获取权限分组信息
        String sql_fenzu="select  id,fenzu_mingcheng from quanxian_fenzu where id > 6 order by id desc";
        List<Map<String, Object>> list_fenzu = jdbcTemplate.queryForList(sql_fenzu);
        System.out.println(list_fenzu);
        model.addAttribute("list_fenzu",list_fenzu);

        System.out.println("参数act="+act);
        String caozuo = "";
        if(act==null || "".equals(act)){
            System.out.println("没有操作，显示列表数据");
        }else{
            System.out.println("相关操作");
            //录入数据
            if(act.equals("add")){
                System.out.println("开始录入数据");

                //插入时间也可以使用下面代码
                java.util.Date Dates = new java.util.Date();
                java.sql.Timestamp time = new java.sql.Timestamp(Dates.getTime());

                String sql = "Insert into quanxian_yonghu(user_name,user_password,beizhu,fenzu_id,add_date) values(?,?,?,?,?)";
                jdbcTemplate.update(sql, user_name,user_password,beizhu,fenzu_id,time);
                System.out.println("插入成功");
            }
        }
        return "houtai/quanxian/user_add";
    }

    //用户列表
    @RequestMapping("/quanxian_user_list") //不是@GetMapping
    public String quanxian_user_list(Model model,String act){

        //读取1级分类列表，可以考虑下面的联合查询语句
//        select quanxian_yonghu.user_name,quanxian_fenzu.fenzu_mingcheng
//        from quanxian_yonghu,quanxian_fenzu
//        where quanxian_yonghu.fenzu_id = quanxian_fenzu.id
        String sql1="select  * from quanxian_yonghu where fenzu_id>6  order by id desc ";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println(list1);
        //[{id=34, user_name=a, user_password=b, fenzu_id=7, add_date=2021-05-05T21:38, beizhu=c}, ......


        model.addAttribute("list1",list1);

        return "houtai/quanxian/user_list";
    }

    //用户修改
    @RequestMapping("/quanxian_user_xiugai") //不是@GetMapping
    public String quanxian_user_xiugai(Model model,String act){

        return "houtai/quanxian/user_xiugai";
    }
}
