package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

@RestController
public class pc_shop_controller {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //收藏接口
    @RequestMapping("/api_shop_sp_shoucang")
    public String api_shop_sp_shoucang(Integer u_id,Integer sp_id)
    {
        System.out.println("商品收藏，用户id="+ u_id + " | 商品id=" + sp_id);
        //写入收藏信息

        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*)  FROM shangpin_shoucang where  u_id="+u_id+"  and sp_id="+sp_id;
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据
            return "<script language='JavaScript'>alert('已经收藏');</script>";
        }else{ //可以写入数据
            System.out.println("插入到数据库");
            String sql = "insert into shangpin_shoucang(u_id,sp_id,riqi,shijian) values(?,?,?,?)";
            jdbcTemplate.update(sql, u_id,sp_id,mydate1,mydate2);
            System.out.println("商品收藏信息，成功插入到数据库");
            return "<script language='JavaScript'>alert('收藏成功');</script>";
        }

        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        //return "<script language='JavaScript'>window.location='/pc_zixun_list?chaxun_neirong="+chaxun_neirong+"';</script>";
    }

    //商品评论 开始
    @RequestMapping("/api_shop_sp_pinglun_add")
    public String api_shop_sp_pinglun_add(Integer u_id,Integer sp_id,String neirong)
    {
        System.out.println("商品评论，用户id="+ u_id + " | 商品id=" + sp_id + " | 内容=" +neirong);
        //写入资讯评论

        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*) FROM shangpin_pinglun where u_id="+u_id+" and zixun_id="+sp_id+" and riqi='"+mydate1+"'";
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据
            return "<script language='JavaScript'>alert('每天只能评论1次');</script>";
        }else{ //可以写入数据
            System.out.println("插入到数据库");
            String sql = "insert into shangpin_pinglun(u_id,zixun_id,neirong,riqi,shijian) values(?,?,?,?,?)";
            jdbcTemplate.update(sql, u_id,sp_id,neirong,mydate1,mydate2);
            System.out.println("评论信息，成功插入到数据库");
            return "<script language='JavaScript'>alert('评论已经提交，审核后显示');</script>";
        }

        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        //return "<script language='JavaScript'>window.location='/pc_zixun_list?chaxun_neirong="+chaxun_neirong+"';</script>";
    }//商品评论 结束

    //添加购物车 开始
    @RequestMapping("/api_shop_sp_gouwuche")
    public String api_shop_sp_gouwuche(Integer u_id,Integer sp_id,Integer sp_jiage1,String sp_mc)
    {
        System.out.println("添加购物车，用户id="+ u_id + " | 商品id=" + sp_id + " | 价格=" +sp_jiage1+ " | 商品名称=" +sp_mc);
        //写入资讯评论

        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*) FROM gouwuche where zt=1 and u_id="+u_id+" and sp_id="+sp_id;
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据
            return "<script language='JavaScript'>alert('已经在购物车');</script>";
        }else{ //可以写入数据
            System.out.println("插入到数据库");
            String sql = "insert into gouwuche(u_id,sp_id,sp_mingcheng,jiage_shichang,jiage_chengjiao,sp_shuliang,zt,shijian_gouwuche) values(?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, u_id,sp_id,sp_mc,sp_jiage1,sp_jiage1,1,1,mydate2);
            System.out.println("加入购物车，成功插入到数据库");
            return "<script language='JavaScript'>alert('成功加入购物车');</script>";
        }

        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        //return "<script language='JavaScript'>window.location='/pc_zixun_list?chaxun_neirong="+chaxun_neirong+"';</script>";
    }//添加购物车 结束

    //立即购买 开始
    @RequestMapping("/api_shop_sp_goumai")
    public String api_shop_sp_goumai(Integer u_id,Integer sp_id,Integer sp_jiage1,String sp_mc)
    {
        System.out.println("添加购物车，用户id="+ u_id + " | 商品id=" + sp_id + " | 价格=" +sp_jiage1+ " | 商品名称=" +sp_mc);
        //写入资讯评论

        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*) FROM gouwuche where zt=1 and u_id="+u_id+" and sp_id="+sp_id;
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据
            return "<script language='JavaScript'>window.parent.location='/pc_shangpin_gouwuche'</script>";
        }else{ //可以写入数据
            System.out.println("插入到数据库");
            String sql = "insert into gouwuche(u_id,sp_id,sp_mingcheng,jiage_shichang,jiage_chengjiao,sp_shuliang,zt,shijian_gouwuche) values(?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, u_id,sp_id,sp_mc,sp_jiage1,sp_jiage1,1,1,mydate2);
            System.out.println("加入购物车，成功插入到数据库");
            return "<script language='JavaScript'>window.parent.location='/pc_shangpin_gouwuche'</script>";
        }

        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        //return "<script language='JavaScript'>window.location='/pc_zixun_list?chaxun_neirong="+chaxun_neirong+"';</script>";
    }//立即购买 结束

    //更新购物车 商品 数量 api_shop_gwc_update_shuliang
    @RequestMapping("/api_shop_gwc_update_shuliang")
    public String api_shop_gwc_update_shuliang(Integer u_id,Integer gwc_id,Integer sp_shuliang)
    {
        String sql= "update gouwuche set sp_shuliang=? where u_id=? and id=?";
        jdbcTemplate.update(sql,sp_shuliang,u_id,gwc_id);
        return "";
    }

}//public class pc_shop_controller

