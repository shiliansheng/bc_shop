package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.beans.Statement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@Controller
public class fenzu {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //分组权限列表
    @RequestMapping("/quanxian_fenzu_list") //不是@GetMapping
    public String quanxian_fenzu_list( Model model){
        String fenzu="select * from quanxian_fenzu ";
        List<Map<String, Object>> list_fenzu = jdbcTemplate.queryForList(fenzu);
        System.out.println("分组数据=" + list_fenzu);

        //读取所有1级权限id和名称
        String sql1="select  id,caidan_mingcheng from quanxian_caidan where caidan_jibie=1 ";
        List<Map<String, Object>> list_quanxian1 = jdbcTemplate.queryForList(sql1);
        System.out.println("所有1级权限id=" + list_quanxian1);

        //读取所有2级权限id和名称
        String sql2="select  id,caidan_mingcheng,caidan_suoshu from quanxian_caidan where caidan_jibie=2 ";
        List<Map<String, Object>> list_quanxian2 = jdbcTemplate.queryForList(sql2);

        //读取所有3级权限id和名称
        String sql3="select  id,caidan_mingcheng,caidan_suoshu from quanxian_caidan where caidan_jibie=3 ";
        List<Map<String, Object>> list_quanxian3 = jdbcTemplate.queryForList(sql3);

        model.addAttribute("list_fenzu",list_fenzu);
        model.addAttribute("list_quanxian1",list_quanxian1);
        model.addAttribute("list_quanxian2",list_quanxian2);
        model.addAttribute("list_quanxian3",list_quanxian3);

        return "houtai/quanxian/fenzu_list";
    }

    //分组权限 修改
    @RequestMapping("/quanxian_fenzu_xiugai") //不是@GetMapping
    public String quanxian_fenzu_xiugai(String act,Integer fenzu_id,String quanxian,Model model){

        String fenzu="select * from quanxian_fenzu where id=" + fenzu_id;
        List<Map<String, Object>> list_fenzu = jdbcTemplate.queryForList(fenzu);
        System.out.println("分组数据=" + list_fenzu);

        //读取所有1级权限id和名称
        String sql1="select  id,caidan_mingcheng from quanxian_caidan where caidan_jibie=1 ";
        List<Map<String, Object>> list_quanxian1 = jdbcTemplate.queryForList(sql1);
        System.out.println("所有1级权限id=" + list_quanxian1);

        //读取所有2级权限id和名称
        String sql2="select  id,caidan_mingcheng,caidan_suoshu from quanxian_caidan where caidan_jibie=2 ";
        List<Map<String, Object>> list_quanxian2 = jdbcTemplate.queryForList(sql2);

        //读取所有3级权限id和名称
        String sql3="select  id,caidan_mingcheng,caidan_suoshu from quanxian_caidan where caidan_jibie=3 ";
        List<Map<String, Object>> list_quanxian3 = jdbcTemplate.queryForList(sql3);

        model.addAttribute("list_fenzu",list_fenzu);
        model.addAttribute("fenzu_id",fenzu_id);
        model.addAttribute("list_quanxian1",list_quanxian1);
        model.addAttribute("list_quanxian2",list_quanxian2);
        model.addAttribute("list_quanxian3",list_quanxian3);

        if(act==null || "".equals(act)){
            System.out.println("没有操作，显示列表数据");
            return "houtai/quanxian/fenzu_xiugai";
        }else{
            System.out.println("修改权限数据");
            System.out.println("获取的3级权限数据= " + quanxian);
            //获取的数据= 311,312,319,320,325,326
            String qx_3 ="";
            qx_3 = "," + quanxian + ",";

            String qx_2 = "";
            //第1步：读取1级菜单
            String sql_a1="select * from quanxian_caidan where caidan_jibie=1 ";
            SqlRowSet rs_a1 = jdbcTemplate.queryForRowSet(sql_a1);
            while (rs_a1.next()){
                //System.out.println(rs_a1.getInt("id") + " | " + rs_a1.getString("caidan_mingcheng"));
                //第2步：读取2级菜单
                String sql_a2="select * from quanxian_caidan where caidan_jibie=2  and caidan_suoshu="+rs_a1.getInt("id");
                SqlRowSet rs_a2 = jdbcTemplate.queryForRowSet(sql_a2);
                while(rs_a2.next()){
                    //获取2级菜单和权限的id集合
                    String sql_a3="select * from quanxian_caidan where caidan_jibie=3  and caidan_suoshu="+rs_a2.getInt("id")+" and id in (0"+qx_3+"0) ";
                    SqlRowSet rs_a3 = jdbcTemplate.queryForRowSet(sql_a3);
                    while(rs_a3.next()){
                        qx_2 = qx_2 + rs_a2.getInt("id") + ",";
                        break;
                    }
                }
            }
            qx_2 = "," + qx_2;
            System.out.println("2级权限id集合=" + qx_2);

            String qx_1 = "";
            //第1步：读取1级菜单
            String sql_b1="select * from quanxian_caidan where caidan_jibie=1 ";
            SqlRowSet rs_b1 = jdbcTemplate.queryForRowSet(sql_b1);
            while (rs_b1.next()){
                //System.out.println(rs_a1.getInt("id") + " | " + rs_a1.getString("caidan_mingcheng"));
                String sql_b2="select * from quanxian_caidan where caidan_jibie=2  and caidan_suoshu="+rs_b1.getInt("id")+" and id in (0"+qx_2+"0) ";
                SqlRowSet rs_b2 = jdbcTemplate.queryForRowSet(sql_b2);
                while(rs_b2.next()){
                    //获取1级菜单和权限的id集合
                    qx_1 = qx_1 + rs_b1.getInt("id") + ",";
                    break;
                }
            }
            qx_1 = "," + qx_1;
            System.out.println("1级权限id集合=" + qx_1);
            System.out.println("2级权限id集合=" + qx_2);
            System.out.println("3级权限id集合=" + qx_3);
            System.out.println("当前用户分组id=" + fenzu_id);

            //更新分组权限设置
            String sql_update = "Update quanxian_fenzu Set quanxian_1=?,quanxian_2=?,quanxian_3=? where id=?";
            System.out.println("更新数据库="+sql_update);
            jdbcTemplate.update(sql_update, qx_1,qx_2,qx_3,fenzu_id);

            return "redirect:/quanxian_fenzu_list";


        }// if(act==null || "".equals(act)){


    }

}
