package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
public class pc_zixun {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //资讯列表
    @RequestMapping("/pc_zixun_list") //不是@GetMapping
    public String pc_zixun_list(String pageNumber,Integer fenlei_id,String fenlei_mc,String chaxun_neirong,Model model)
    {
        System.out.println("当前访问的是 资讯列表 页面");
        System.out.println("当前分类id=" + fenlei_id);

        //读取信息分类
        String sql_xinxi_fenlei="select  * from zixun_fenlei ";
        //System.out.println(sql_xinxi_fenlei);
        List<Map<String, Object>> list_xinxi_fenlei = jdbcTemplate.queryForList(sql_xinxi_fenlei);
        model.addAttribute("list_xinxi_fenlei", list_xinxi_fenlei);

        //初始化设置
        int PAGESIZE = 5;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        if(fenlei_id==null){
            fenlei_id = 0;
            fenlei_mc = "所有信息";
        }
        System.out.println("-----资讯分类=" + fenlei_mc);
        if(fenlei_id>0){
            System.out.println("分类列表");
            size = jdbcTemplate.queryForObject("select count(*) from zixun where xinxi_lxid=" + fenlei_id,Integer.class);
        }else{
            System.out.println("所有数据");
            if(chaxun_neirong==null){
                chaxun_neirong = "";
                size = jdbcTemplate.queryForObject("select count(*) from zixun",Integer.class);
            }else{
                size = jdbcTemplate.queryForObject("select count(*) from zixun where xinxi_biaoti like '%"+chaxun_neirong+"%'",Integer.class);
            }
        }

        System.out.println("总数据条数=" +size);
        pageCount = (size%PAGESIZE==0)?(size/PAGESIZE):(size/PAGESIZE+1); //获得页数
        System.out.println("总页数=" +pageCount);

        //处理当前页起始，从0开始是第一页
        String tmp = pageNumber;
        if(tmp==null){
            tmp="0"; //默认进入页面，没有参数，设置为0
        }
        curPage = Integer.parseInt(tmp); //当前页面
        //下面处理：如果当前页面 大于等于 总页数，则设置为最大的页数
        if(curPage>=pageCount) curPage = pageCount;
        //System.out.println("页数参数pageNumber="+pageNumber);
        System.out.println("页数参数pageNumber="+curPage);

        //处理上页数字
        shang = curPage -1;
        if(shang<=0){
            shang = 0;
        }

        //处理下页数字
        xia = curPage +1;
        if(xia > (pageCount-1)){
            xia = pageCount-1;
        }

        //String sql2="select  id,uname from tb1 order by id desc limit 0,3";
        //String sql2="select  * from zixun order by id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        //联合查询
        String sql2 = "";
        if(fenlei_id>0){ //分类列表
            sql2= sql2+"select  zixun.id,zixun.xinxi_lxid,zixun.xinxi_biaoti,zixun.xinxi_jianjie_yn,zixun.xinxi_tupian_yn,zixun.add_riqi";
            sql2= sql2+",zixun_fenlei.caidan_mingcheng  from zixun,zixun_fenlei ";
            sql2= sql2+" where zixun.xinxi_lxid=zixun_fenlei.id and zixun_fenlei.id="+fenlei_id;
            sql2= sql2+" order by zixun.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        }else{ //所有数据
            if(chaxun_neirong==null){
                chaxun_neirong = "";
                sql2= sql2+"select  zixun.id,zixun.xinxi_lxid,zixun.xinxi_biaoti,zixun.xinxi_jianjie_yn,zixun.xinxi_tupian_yn,zixun.add_riqi";
                sql2= sql2+",zixun_fenlei.caidan_mingcheng  from zixun,zixun_fenlei ";
                sql2= sql2+" where zixun.xinxi_lxid=zixun_fenlei.id ";
                sql2= sql2+" order by zixun.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
            }else{
                sql2= sql2+"select  zixun.id,zixun.xinxi_lxid,zixun.xinxi_biaoti,zixun.xinxi_jianjie_yn,zixun.xinxi_tupian_yn,zixun.add_riqi";
                sql2= sql2+",zixun_fenlei.caidan_mingcheng  from zixun,zixun_fenlei ";
                sql2= sql2+" where zixun.xinxi_lxid=zixun_fenlei.id and zixun.xinxi_biaoti like '%"+chaxun_neirong+"%'";
                sql2= sql2+" order by zixun.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
            }

        }

        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("curPage",curPage);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);
        model.addAttribute("size",size);
        model.addAttribute("fenlei_id",fenlei_id);
        model.addAttribute("fenlei_mc",fenlei_mc);
        model.addAttribute("chaxun_neirong",chaxun_neirong);

        //model.addAttribute("test","mytest");
        return  "/pc/pc_zixun_list";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }

    //资讯详情
    @RequestMapping("/pc_zixun_xiangqing") //不是@GetMapping
    public String pc_zixun_xiangqing(String xinxi_id, Model model, HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("当前资讯详情id=" + xinxi_id);

        //根据信息的id，读取数据信息
        String sql_xinxi="select  * from zixun where id=" + xinxi_id;
        Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
        model.addAttribute("xinxi",xinxi);
        System.out.println("获取的信息="+xinxi);
        Integer liulanshu = (Integer) xinxi.get("liulanshu");
        System.out.println("浏览数=" + liulanshu);

        //读取当信息id的上一个id
        Integer id_shang = 0 ; //上一篇文章id
        String biaoti_shang = "";
        String sql_shang = "select  * from  zixun ";
        sql_shang = sql_shang + "where  id<"+xinxi_id+" order by id desc limit 0,1"  ;//sql语句
        SqlRowSet rs_shang = jdbcTemplate.queryForRowSet(sql_shang);
        if(rs_shang.next()){
            id_shang = rs_shang.getInt("id");
            biaoti_shang =rs_shang.getString("xinxi_biaoti");
        }
        model.addAttribute("id_shang",id_shang);
        model.addAttribute("biaoti_shang",biaoti_shang);

        //读取当信息id的下一个id
        Integer id_xia = 0 ;   //下一篇文章id
        String biaoti_xia = "";
        String sql_xia = "select  * from  zixun ";
        sql_xia = sql_xia + "where  id>"+xinxi_id+" order by id desc limit 0,1"  ;//sql语句
        SqlRowSet rs_xia = jdbcTemplate.queryForRowSet(sql_xia);
        if(rs_xia.next()){
            id_xia = rs_xia.getInt("id");
            biaoti_xia =rs_xia.getString("xinxi_biaoti");
        }
        System.out.println("上一篇id="+id_shang+"|标题=" +biaoti_shang);
        System.out.println("下一篇id="+id_xia+"|标题=" +biaoti_xia);

        model.addAttribute("id_xia",id_xia);
        model.addAttribute("biaoti_xia",biaoti_xia);

        //增加阅读次数
        String sql_liulanshu = "";
        sql_liulanshu =  sql_liulanshu + "update  zixun ";
        sql_liulanshu =  sql_liulanshu + " set  liulanshu=?";
        sql_liulanshu =  sql_liulanshu + " where id=? ";
        jdbcTemplate.update(sql_liulanshu,liulanshu+1,xinxi_id);

        //读取收藏次数
        int size_shoucangshu = 0;  //总的数据条数 > 有多少数据
        String sql_shoucangshu = "SELECT count(*) FROM zixun_shoucang where zixun_id="+xinxi_id;
        size_shoucangshu = jdbcTemplate.queryForObject(sql_shoucangshu,Integer.class);
        System.out.println("查询到收藏条数="+size_shoucangshu);
        model.addAttribute("size_shoucangshu",size_shoucangshu);

        //读取评论次数
        int size_pinglunshu = 0;  //总的数据条数 > 有多少数据
        String sql_pinglunshu = "SELECT count(*) FROM zixun_pinglun where zixun_id="+xinxi_id;
        size_pinglunshu = jdbcTemplate.queryForObject(sql_pinglunshu,Integer.class);
        System.out.println("查询到评论条数="+size_pinglunshu);
        model.addAttribute("size_pinglunshu",size_pinglunshu);

        //默认时间  2022/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        //写入浏览记录
        HttpSession session = request.getSession();
        System.out.println("用户id=" + session.getAttribute("mem_id"));
        if(session.getAttribute("mem_id")==null){//游客
            String sql = "insert into zixun_liulan(u_id,zixun_id,riqi,shijian) values(?,?,?,?)";
            jdbcTemplate.update(sql, 0,xinxi_id,mydate1,mydate2);
            System.out.println("游客插入成功");
        }else{//用户
            String sql = "insert into zixun_liulan(u_id,zixun_id,riqi,shijian) values(?,?,?,?)";
            jdbcTemplate.update(sql, session.getAttribute("mem_id"),xinxi_id,mydate1,mydate2);
            System.out.println("用户插入成功");
        }

        //读取评论列表
        String sql_pinglun="select * from zixun_pinglun where shenhe=1 and zixun_id="+xinxi_id+" order by id desc  ";
        List<Map<String, Object>> list_pinglun = jdbcTemplate.queryForList(sql_pinglun);
        model.addAttribute("list_pinglun",list_pinglun);
        System.out.println("评论内容="+list_pinglun);

        return  "/pc/pc_zixun_xiangqing";
        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
    }




} //public class pc_zixun {
