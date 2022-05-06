package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class pc_dizhi {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //地址列表
    @RequestMapping("/pc_mem_dizhi_list") //不是@GetMapping
    public String pc_mem_dizhi_list( Model model, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //读取当前用户的地址列表
        String sql_dizhi= "";
        sql_dizhi = sql_dizhi + " select * from huiyuan_dizhi";
        sql_dizhi = sql_dizhi + " where  u_id="+session.getAttribute("mem_id")+" order by id desc ";
        List<Map<String, Object>> list_dizhi = jdbcTemplate.queryForList(sql_dizhi);
        model.addAttribute("list_dizhi",list_dizhi);
        System.out.println("用户地址列表="+list_dizhi);

        return "/pc_mem/pc_mem_dizhi_list";
    }

    //地址录入
    @RequestMapping("/pc_mem_dizhi_add") //不是@GetMapping
    public String pc_mem_dizhi_add(String xingming, String shouji, String dizhi, String yn_moren,
                                   String act, Model model, HttpServletRequest request)
    {
        if(act==null || "".equals(act)){
            System.out.println("录入信息，默认页面");
            return "/pc_mem/pc_mem_dizhi_add";
        }else{
            HttpSession session = request.getSession();
            System.out.println("当前用户id="+session.getAttribute("mem_id"));

            System.out.println("获取数据信息");
            System.out.println("用户id=");
            if("1".equals(yn_moren)){
                //将用户所有的默认状态设置为0
                String sql = "";
                sql =  sql + "update huiyuan_dizhi set yn_moren=0 where u_id=?";
                jdbcTemplate.update(sql, session.getAttribute("mem_id"));
            }
            else {
                yn_moren = "0";
            }
            System.out.println("姓名="+xingming+"|"+"手机="+shouji+"|"+"地址="+dizhi+"|"+"默认选中="+yn_moren);

            String sql = "";
            sql =  sql + "Insert into huiyuan_dizhi(";
            sql =  sql + "xingming,shouji,dizhi,yn_moren,u_id)  values (?,?,?,?,?)";
            jdbcTemplate.update(sql, xingming,shouji,dizhi,yn_moren,session.getAttribute("mem_id"));
            System.out.println("插入成功");

            return "redirect:/pc_mem_dizhi_list";
        }
    }

    //删除地址
    @RequestMapping("/pc_mem_dizhi_del") //不是@GetMapping
    public String pc_mem_dizhi_del(Integer cs_id, Model model, HttpServletRequest request){
        System.out.println("要删除的信息id=" + cs_id);
        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //删除数据
        String sql = "delete from huiyuan_dizhi where id=? and u_id=?";
        jdbcTemplate.update(sql, cs_id,session.getAttribute("mem_id"));
        System.out.println("删除成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/pc_mem_dizhi_list";
    }

    //修改地址
    @RequestMapping("/pc_mem_dizhi_xiugai") //不是@GetMapping
    public String pc_mem_dizhi_xiugai(Integer cs_id,
                                   String xingming, String shouji, String dizhi, String yn_moren,
                                   String act, Model model, HttpServletRequest request)
    {
        if("xiugai".equals(act)){
            //根据信息的id，读取要修改的数据信息
            String sql_xinxi="select  * from huiyuan_dizhi where id=" + cs_id;
            Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
            model.addAttribute("xinxi",xinxi);
            System.out.println("获取的地址信息="+xinxi);

            return "/pc_mem/pc_mem_dizhi_xiugai";
        }else{ //修改地址信息

            HttpSession session = request.getSession();
            System.out.println("当前用户id="+session.getAttribute("mem_id"));
            System.out.println("获取数据信息");
            if("1".equals(yn_moren)){
                //将用户所有的默认状态设置为0
                String sql = "";
                sql =  sql + "update huiyuan_dizhi set yn_moren=0 where u_id=?";
                jdbcTemplate.update(sql, session.getAttribute("mem_id"));
            }
            else {
                yn_moren = "0";
            }
            System.out.println("姓名="+xingming+"|"+"手机="+shouji+"|"+"地址="+dizhi+"|"+"默认选中="+yn_moren);

            String sql = "";
            sql =  sql + "update  huiyuan_dizhi ";
            sql =  sql + " set  xingming=?,shouji=?,dizhi=?,yn_moren=?";
            sql =  sql + " where id=? and u_id=?";
            jdbcTemplate.update(sql, xingming,shouji,dizhi,yn_moren,cs_id,session.getAttribute("mem_id"));
            System.out.println("修改成功");

           return "redirect:/pc_mem_dizhi_list";

        }
    }

}
