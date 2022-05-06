package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
public class
pc_controller {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;


    //资讯查询 pc_zixun_chaxun
    @RequestMapping("/pc_zixun_chaxun")
    public String pc_zixun_chaxun(String chaxun_neirong)
    {
        System.out.println("查询内容="+ chaxun_neirong);
        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        return "<script language='JavaScript'>window.location='/pc_zixun_list?chaxun_neirong="+chaxun_neirong+"';</script>";
    }
    //商品查询 pc_shangpin_chaxun
    @RequestMapping("/pc_shangpin_chaxun")
    public String pc_shangpin_chaxun(String chaxun_neirong)
    {
        System.out.println("查询内容="+ chaxun_neirong);
        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        return "<script language='JavaScript'>window.location='/pc_shangpin_list?chaxun_neirong="+chaxun_neirong+"';</script>";
    }

    //收藏接口
    @RequestMapping("/api_zixun_shoucang")
    public String api_zixun_shoucang(Integer u_id,Integer xinxi_id)
    {
        System.out.println("用户id="+ u_id + " | 信息id=" + xinxi_id);
        //写入收藏信息

        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*)  FROM zixun_shoucang where  u_id="+u_id+"  and zixun_id="+xinxi_id;
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据
            return "<script language='JavaScript'>alert('已经收藏');</script>";
        }else{ //可以写入数据
            System.out.println("插入到数据库");
            String sql = "insert into zixun_shoucang(u_id,zixun_id,riqi,shijian) values(?,?,?,?)";
            jdbcTemplate.update(sql, u_id,xinxi_id,mydate1,mydate2);
            System.out.println("注册用户信息，成功插入到数据库");
            return "<script language='JavaScript'>alert('收藏成功');</script>";
        }

        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        //return "<script language='JavaScript'>window.location='/pc_zixun_list?chaxun_neirong="+chaxun_neirong+"';</script>";
    }

    //资讯评论 开始
    @RequestMapping("/api_zixun_pinglun_add")
    public String api_zixun_pinglun_add(Integer u_id,Integer xinxi_id,String neirong)
    {
        System.out.println("用户id="+ u_id + " | 信息id=" + xinxi_id + " | 内容=" +neirong);
        //写入资讯评论

        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*) FROM zixun_pinglun where u_id="+u_id+" and zixun_id="+xinxi_id+" and riqi='"+mydate1+"'";
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据
            return "<script language='JavaScript'>alert('每篇文章每天只能发表一篇评论');</script>";
        }else{ //可以写入数据
            System.out.println("插入到数据库");
            String sql = "insert into zixun_pinglun(u_id,zixun_id,neirong,riqi,shijian) values(?,?,?,?,?)";
            jdbcTemplate.update(sql, u_id,xinxi_id,neirong,mydate1,mydate2);
            System.out.println("注册用户信息，成功插入到数据库");
            return "<script language='JavaScript'>alert('评论已经提交，审核后显示');</script>";
        }

        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        //return "<script language='JavaScript'>window.location='/pc_zixun_list?chaxun_neirong="+chaxun_neirong+"';</script>";
    }//资讯评论 结束



    //密码修改
    @RequestMapping("/pc_mem_mima_act")
    public String pc_mem_mima_act(String mm1, String mm2,
                                  HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
    {
        System.out.println("开始用户密码更新操作");
        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*) FROM huiyuan where id="+session.getAttribute("mem_id")+" and mima='"+mm1+"'";
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据
            //增加阅读次数
            String sql_liulanshu = "";
            sql_liulanshu =  sql_liulanshu + "update  huiyuan ";
            sql_liulanshu =  sql_liulanshu + " set  mima=?";
            sql_liulanshu =  sql_liulanshu + " where id=? ";
            jdbcTemplate.update(sql_liulanshu,mm2,session.getAttribute("mem_id"));
            return "<script language='JavaScript'>alert('密码更新成功');window.location='/pc_mem_index';</script>";
        }else{ //没有数据
            return "<script language='JavaScript'>alert('原始密码错误');window.location='/pc_mem_mima';</script>";
        }

        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        //return "<script language='JavaScript'>window.location='/pc_mem_index';</script>";
    }//密码修改

    //退出登录
    @RequestMapping("/pc_mem_logout")
    public String pc_mem_logout( HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
    {
        Cookie cookies[] = request.getCookies();
        //删除Cookie
        for(int i=0;i<cookies.length;i++){
            cookies[i].setMaxAge(0);
            response.addCookie(cookies[i]);
        }
        //return "<script language='JavaScript'>alert('账号错误或者不存在！');</script>";
        return "<script language='JavaScript'>alert('成功退出！');window.location='/pc_index';</script>";
    }//退出登录


    //留言信息 写入数据库
    @RequestMapping("/pc_guanyu_liuyan_add")
    public String pc_guanyu_liuyan_add(String zhuti,String xingming,String dianhua,String youxiang,String neirong)
    {
        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        int size = 0;  //总的数据条数 > 有多少数据
        String sql01 = "SELECT count(*) FROM liuyan where zhuti='"+zhuti+"' and riqi='"+mydate1+"'";
        size = jdbcTemplate.queryForObject(sql01,Integer.class);
        System.out.println("查询到数据条数="+size);
        if(size>0){ //有数据
            return "<script language='JavaScript'>alert('你已经留言');</script>";
        }else{ //可以写入数据
            System.out.println("插入到数据库");
            String sql = "insert into liuyan(xingming,dianhua,youxiang,zhuti,neirong,riqi,shijian) values(?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, xingming,dianhua,youxiang,zhuti,neirong,mydate1,mydate2);
            System.out.println("注册用户信息，成功插入到数据库");
            return "<script language='JavaScript'>alert('留言成功');window.location='/pc_index';</script>";
        }
        //return "<script language='JavaScript'>alert('评论已经提交，审核后显示');</script>";
    }

}//pc_controller {
