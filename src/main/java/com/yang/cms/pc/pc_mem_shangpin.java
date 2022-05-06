package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class pc_mem_shangpin {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //会员 商品 订单 列表
    @RequestMapping("/pc_mem_shangpin_dingdan_list") //不是@GetMapping
    public String pc_mem_shangpin_dingdan_list(String cs_zt,
                                               Model model, HttpServletRequest request, HttpServletResponse response)
    {
        if(cs_zt==null || "".equals(cs_zt)){
            cs_zt = "0";
        }else{}
        System.out.println("当前选择状态=" +cs_zt);
        model.addAttribute("cs_zt", cs_zt);

        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //读取用户的订单列表
        String sql_dingdan= "";

        String zt_neirong = "";
        switch (cs_zt){
            case "0":
                zt_neirong = "所有订单";
                sql_dingdan = sql_dingdan + " select  dingdan.id,dingdan.danhao,dingdan.zt,dingdan.shijian_xiadan,dingdan.feiyong_chengjiao,dingdan.beizhu_fahuo";
                sql_dingdan = sql_dingdan + " from dingdan where ";
                sql_dingdan = sql_dingdan + " u_id="+session.getAttribute("mem_id")+" order by dingdan.id desc ";
                break;
            case "2":
                zt_neirong = "待付款";
                sql_dingdan = sql_dingdan + " select  dingdan.id,dingdan.danhao,dingdan.zt,dingdan.shijian_xiadan,dingdan.feiyong_chengjiao,dingdan.beizhu_fahuo";
                sql_dingdan = sql_dingdan + " from dingdan where dingdan.zt=2 and";
                sql_dingdan = sql_dingdan + " u_id="+session.getAttribute("mem_id")+" order by dingdan.id desc ";
                break;
            case "3":
                zt_neirong = "待发货";
                sql_dingdan = sql_dingdan + " select  dingdan.id,dingdan.danhao,dingdan.zt,dingdan.shijian_xiadan,dingdan.feiyong_chengjiao,dingdan.beizhu_fahuo";
                sql_dingdan = sql_dingdan + " from dingdan where dingdan.zt=3 and";
                sql_dingdan = sql_dingdan + " u_id="+session.getAttribute("mem_id")+" order by dingdan.id desc ";
                break;
            case "4":
                zt_neirong = "待收货";
                sql_dingdan = sql_dingdan + " select  dingdan.id,dingdan.danhao,dingdan.zt,dingdan.shijian_xiadan,dingdan.feiyong_chengjiao,dingdan.beizhu_fahuo";
                sql_dingdan = sql_dingdan + " from dingdan where dingdan.zt=4 and";
                sql_dingdan = sql_dingdan + " u_id="+session.getAttribute("mem_id")+" order by dingdan.id desc ";
                break;
            case "5":
                zt_neirong = "已完结";
                sql_dingdan = sql_dingdan + " select  dingdan.id,dingdan.danhao,dingdan.zt,dingdan.shijian_xiadan,dingdan.feiyong_chengjiao,dingdan.beizhu_fahuo";
                sql_dingdan = sql_dingdan + " from dingdan where dingdan.zt=5 and";
                sql_dingdan = sql_dingdan + " u_id="+session.getAttribute("mem_id")+" order by dingdan.id desc ";
                break;
        }
        System.out.println("当前状态中文=" + zt_neirong);
        model.addAttribute("zt_neirong", zt_neirong);
        System.out.println(sql_dingdan);

        List<Map<String, Object>> list_dingdan = jdbcTemplate.queryForList(sql_dingdan);
        model.addAttribute("list_dingdan",list_dingdan);
        System.out.println("订单数据="+list_dingdan);

        //读取用户所有订单的购物车和商品信息
        String sql_shangpin="";
        sql_shangpin = sql_shangpin + "select";
        sql_shangpin = sql_shangpin + " gouwuche.danhao,gouwuche.id as gwcid,gouwuche.sp_mingcheng,gouwuche.jiage_shichang,gouwuche.sp_shuliang,";
        sql_shangpin = sql_shangpin + " shangpin.cp_tupian,shangpin.kucun,shangpin.yixiaoshou";
        sql_shangpin = sql_shangpin + " from dingdan,gouwuche,shangpin";
        sql_shangpin = sql_shangpin +  " where dingdan.u_id=" +session.getAttribute("mem_id");
        sql_shangpin = sql_shangpin +  " and dingdan.danhao = gouwuche.danhao  and gouwuche.sp_id=shangpin.id order by dingdan.id";
        List<Map<String, Object>> list_shangpin = jdbcTemplate.queryForList(sql_shangpin);
        model.addAttribute("list_shangpin",list_shangpin);
        System.out.println("商品数据="+list_shangpin);


//        select
//        shangpin.cp_tupian,shangpin.kucun,shangpin.yixiaoshou,
//                gouwuche.id as gwcid,gouwuche.sp_mingcheng,gouwuche.jiage_shichang,gouwuche.sp_shuliang,gouwuche.danhao
//        from dingdan,gouwuche,shangpin
//        where dingdan.u_id=744 and dingdan.danhao = gouwuche.danhao  and gouwuche.sp_id=shangpin.id order by dingdan.id


        return "/pc_mem/pc_mem_shangpin_dingdan_list";
    }

    //订单删除  pc_mem_shop_dingdan_del
    @RequestMapping("/pc_mem_shop_dingdan_del") //不是@GetMapping
    public String pc_mem_shop_dingdan_del(Integer dingdan_id, Model model,HttpServletRequest request){
        System.out.println("要删除的信息id=" + dingdan_id);

        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //删除数据
        String sql = "delete from dingdan where id=? and u_id="+session.getAttribute("mem_id");
        jdbcTemplate.update(sql, dingdan_id);
        System.out.println("删除成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/pc_mem_shangpin_dingdan_list";
    }
    //订单付款  pc_mem_shop_dingdan_fukuan
    @RequestMapping("/pc_mem_shop_dingdan_fukuan") //不是@GetMapping
    public String pc_mem_shop_dingdan_fukuan(Integer dingdan_id, Model model,HttpServletRequest request){
        System.out.println("订单id=" + dingdan_id);

        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //更新数据  状态值：2待付款 3待发货 4待收货 5待评论
        String sql = "update dingdan set zt=3 where id=? and u_id="+session.getAttribute("mem_id");
        jdbcTemplate.update(sql, dingdan_id);
        System.out.println("状态更新成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/pc_mem_shangpin_dingdan_list";
    }
    //订单收货  pc_mem_shop_dingdan_shouhuo
    @RequestMapping("/pc_mem_shop_dingdan_shouhuo") //不是@GetMapping
    public String pc_mem_shop_dingdan_shouhuo(Integer dingdan_id, Model model,HttpServletRequest request){
        System.out.println("订单id=" + dingdan_id);

        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //更新数据  状态值：2待付款 3待发货 4待收货 5完结
        String sql = "update dingdan set zt=5 where id=? and u_id="+session.getAttribute("mem_id");
        jdbcTemplate.update(sql, dingdan_id);
        System.out.println("状态更新成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/pc_mem_shangpin_dingdan_list";
    }

    //收藏列表 pc_mem_shangpin_shoucang_list
    @RequestMapping("/pc_mem_shangpin_shoucang_list") //不是@GetMapping
    public String pc_mem_shangpin_shoucang_list(Model model, HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        System.out.println("商品收藏页面；当前用户id="+session.getAttribute("mem_id"));

        //读取用户的收藏商品列表
        String sql_dingdan= "";
        sql_dingdan = sql_dingdan + " select  shangpin_shoucang.riqi,shangpin_shoucang.id";
        sql_dingdan = sql_dingdan + ",shangpin.mingcheng,shangpin.kucun,shangpin.yixiaoshou,shangpin.jiage1,shangpin.cp_tupian";
        sql_dingdan = sql_dingdan + " from shangpin_shoucang,shangpin ";
        sql_dingdan = sql_dingdan + " where shangpin_shoucang.sp_id=shangpin.id";
        sql_dingdan = sql_dingdan + " and u_id="+session.getAttribute("mem_id")+" order by shangpin_shoucang.id  ";
        System.out.println(sql_dingdan);
        List<Map<String, Object>> list_dingdan = jdbcTemplate.queryForList(sql_dingdan);
        model.addAttribute("list_dingdan",list_dingdan);
        System.out.println("收藏商品数据="+list_dingdan);

        return "/pc_mem/pc_mem_shangpin_shoucang_list";
    }
    //商品收藏删除  pc_mem_shangpin_shoucang_del
    @RequestMapping("/pc_mem_shangpin_shoucang_del") //不是@GetMapping
    public String pc_mem_shangpin_shoucang_del(Integer shoucang_id, Model model,HttpServletRequest request){
        System.out.println("要删除的信息id=" + shoucang_id);

        HttpSession session = request.getSession();
        System.out.println("商品收藏页面；当前用户id="+session.getAttribute("mem_id"));

        //删除数据
        String sql = "delete from shangpin_shoucang where id=? and u_id="+session.getAttribute("mem_id");
        jdbcTemplate.update(sql, shoucang_id);
        System.out.println("删除成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/pc_mem_shangpin_shoucang_list";
    }

}
