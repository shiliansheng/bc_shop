package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.CookieStore;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Controller
public class pc_index {

    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //首页
    @RequestMapping("/pc_index") //不是@GetMapping
    public String pc_index(Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        System.out.println("当前访问的是首页");
        System.out.println("登录会员id=" );

        //读取广告设置
        String sql_ad="select * from guanggao where id=1";
        Map<String, Object> xinxi_ad = jdbcTemplate.queryForMap(sql_ad);
        model.addAttribute("xinxi_ad",xinxi_ad);

        //读取最新5个商品 //zhuangtai_yn 0正常 1表示下架
        String sql_shangping_zuixin="select  * from shangpin where zhuangtai_yn=0 and tuijian_yn=1  order by id desc limit 5";
        List<Map<String, Object>> list_shangping_zuixin = jdbcTemplate.queryForList(sql_shangping_zuixin);
        model.addAttribute("list_shangping_zuixin",list_shangping_zuixin);
        System.out.println("最新商品="+list_shangping_zuixin);

        //读取热门文章
        String sql_remen_wenzhang="select id,add_riqi,xinxi_biaoti from zixun  order by id desc limit 0,6 ";
        List<Map<String, Object>> list_remen_wenzhang = jdbcTemplate.queryForList(sql_remen_wenzhang);
        model.addAttribute("list_remen_wenzhang",list_remen_wenzhang);
        System.out.println("热门文章="+list_remen_wenzhang);

        //读取商品分类
        String sql_shangpin_fenlei="select  * from shangpin_fenlei ";
        List<Map<String, Object>> list_shangpin_fenlei = jdbcTemplate.queryForList(sql_shangpin_fenlei);
        model.addAttribute("list_shangpin_fenlei",list_shangpin_fenlei);

        String  str_fenlei_id=""; //存储分类id
        String  str_shangpin_id = ""; //存储首页显示信息的id
        String  str_shangpin_id_tuijian = ""; //存储首页显示信息的id 推荐
        List<Map<String, Object>> xinxi = null; //存储信息列表
        for (Map<String, Object> m : list_shangpin_fenlei)
        {
            str_fenlei_id = str_fenlei_id + m.get("id") + ",";
            //根据分类id，读取下面的首页显示的5条数据的id  - 图片展示用
            String sql_a1="select id from shangpin  where lx_id1="+m.get("id") + " order by id desc  limit 0,5" ;
            SqlRowSet rs_a1 = jdbcTemplate.queryForRowSet(sql_a1);
            while (rs_a1.next()){
                str_shangpin_id = str_shangpin_id + rs_a1.getInt("id") + ",";
            }
            //推荐id - 文字列表展示用
            String sql_a2="select id from shangpin  where lx_id1="+m.get("id") + "  and zhuangtai_yn=0   and tuijian_yn=1 order by id desc  limit 0,6" ;
            SqlRowSet rs_a2 = jdbcTemplate.queryForRowSet(sql_a2);
            while (rs_a2.next()){
                str_shangpin_id_tuijian = str_shangpin_id_tuijian + rs_a2.getInt("id") + ",";
            }

        }
        System.out.println("分类id=" + str_fenlei_id);
        System.out.println("图片商品id=" + str_shangpin_id);
        System.out.println("文字推荐商品id=" + str_shangpin_id_tuijian);

        //读取商品的 推荐的文字列表
        String sql_shangpin_tuijian="select  id,lx_id1,mingcheng,jiage1,cp_tupian,add_riqi from shangpin where id in("+str_shangpin_id_tuijian+"0)  " ;
        sql_shangpin_tuijian = sql_shangpin_tuijian + " and zhuangtai_yn=0   order by  id  desc ";
        List<Map<String, Object>> list_shangpin_tuijian = jdbcTemplate.queryForList(sql_shangpin_tuijian);
        model.addAttribute("list_shangpin_tuijian",list_shangpin_tuijian);
        System.out.println("推荐商品文字="+list_shangpin_tuijian);

        //读取商品的 图片列表
        String sql_shangpin="select  id,lx_id1,mingcheng,jiage1,cp_tupian,add_riqi from shangpin where id in("+str_shangpin_id+"0) order by id desc" ;
        List<Map<String, Object>> list_shangpin = jdbcTemplate.queryForList(sql_shangpin);
        model.addAttribute("list_shangpin",list_shangpin);

        return  "/pc/pc_index";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }

}//public class pc_index
