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
public class pc_shangpin {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //商品列表  pc_shangpin_list
    @RequestMapping("/pc_shangpin_list") //不是@GetMapping
    public String pc_shangpin_list(String fenlei_id,String fenlei_mc,String chaxun_neirong, String pageNumber,
                                   Model model, HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("-------开始商品列表数据处理-------");
        //读取信息分类
        String sql_xinxi_fenlei="select  * from shangpin_fenlei ";
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

        String sql1 = ""; //定义读取数量的sql语句
        sql1 = "select count(*) from shangpin";
        //判断是否有分类选择，没有分类选择 读取所有数据
        if(fenlei_id==null || "".equals(fenlei_id)){
            fenlei_mc = "所有商品";
            fenlei_id = "";
            if(chaxun_neirong==null){
                chaxun_neirong = "";
            }else{
                sql1 = "select count(*) from shangpin where mingcheng like '%"+chaxun_neirong+"%'";
            }
        }else{
            sql1 = "select count(*) from shangpin where lx_id1="+fenlei_id;
        }
        size = jdbcTemplate.queryForObject(sql1,Integer.class);
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
        //判断是否有分类选择，没有分类选择 读取所有数据
        if(fenlei_id==null || "".equals(fenlei_id)){
            fenlei_mc = "所有商品";
            if(chaxun_neirong==null){
                sql2= sql2+"select  shangpin.id,shangpin.lx_id1,shangpin.mingcheng,shangpin.jiage1,shangpin.cp_tupian,shangpin.jianjie_yn,shangpin.tuijian_yn,shangpin.add_riqi";
                sql2= sql2+",shangpin_fenlei.caidan_mingcheng  from shangpin,shangpin_fenlei ";
                sql2= sql2+" where shangpin.lx_id1=shangpin_fenlei.id ";
                sql2= sql2+" order by shangpin.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
            }else{
                sql2= sql2+"select  shangpin.id,shangpin.lx_id1,shangpin.mingcheng,shangpin.jiage1,shangpin.cp_tupian,shangpin.jianjie_yn,shangpin.tuijian_yn,shangpin.add_riqi";
                sql2= sql2+",shangpin_fenlei.caidan_mingcheng  from shangpin,shangpin_fenlei ";
                sql2= sql2+" where shangpin.lx_id1=shangpin_fenlei.id and  shangpin.mingcheng like '%"+chaxun_neirong+"%'";
                sql2= sql2+" order by shangpin.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
            }
        }else{
            sql2= sql2+"select  shangpin.id,shangpin.lx_id1,shangpin.mingcheng,shangpin.jiage1,shangpin.cp_tupian,shangpin.jianjie_yn,shangpin.tuijian_yn,shangpin.add_riqi";
            sql2= sql2+",shangpin_fenlei.caidan_mingcheng  from shangpin,shangpin_fenlei ";
            sql2= sql2+" where shangpin.lx_id1=shangpin_fenlei.id and shangpin.lx_id1="+fenlei_id;
            sql2= sql2+" order by shangpin.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        }


        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);
        model.addAttribute("PAGESIZE",PAGESIZE);

        model.addAttribute("curPage",curPage);
        model.addAttribute("size",size);
        model.addAttribute("fenlei_id",fenlei_id);
        model.addAttribute("fenlei_mc",fenlei_mc);
        model.addAttribute("chaxun_neirong",chaxun_neirong);

        return "/pc/pc_shangpin_list";
    }

    //商品详情
    @RequestMapping("/pc_shangpin_xiangqing") //不是@GetMapping
    public String pc_shangpin_xiangqing(String sp_id, Model model, HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("当前资讯详情id=" + sp_id);

        //根据信息的id，读取数据信息
        String sql_xinxi="select  * from shangpin where id=" + sp_id;
        Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
        model.addAttribute("xinxi",xinxi);
        System.out.println("获取的信息="+xinxi);
        Integer liulanshu = (Integer) xinxi.get("liulanshu");
        System.out.println("浏览数=" + liulanshu);
        model.addAttribute("liulanshu",liulanshu);

        //增加阅读次数
        String sql_liulanshu = "";
        sql_liulanshu =  sql_liulanshu + "update  shangpin ";
        sql_liulanshu =  sql_liulanshu + " set  liulanshu=?";
        sql_liulanshu =  sql_liulanshu + " where id=? ";
        jdbcTemplate.update(sql_liulanshu,liulanshu+1,sp_id);

        //读取收藏次数
        int size_shoucangshu = 0;  //总的数据条数 > 有多少数据
        String sql_shoucangshu = "SELECT count(*) FROM shangpin_shoucang where sp_id="+sp_id;
        size_shoucangshu = jdbcTemplate.queryForObject(sql_shoucangshu,Integer.class);
        System.out.println("查询到收藏条数="+size_shoucangshu);
        model.addAttribute("size_shoucangshu",size_shoucangshu);

        //读取评论次数
        int size_pinglunshu = 0;  //总的数据条数 > 有多少数据
        String sql_pinglunshu = "SELECT count(*) FROM shangpin_pinglun where zixun_id="+sp_id;
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
            String sql = "insert into shangpin_liulan(u_id,sp_id,riqi,shijian) values(?,?,?,?)";
            jdbcTemplate.update(sql, 0,sp_id,mydate1,mydate2);
            System.out.println("游客插入成功");
        }else{//用户
            String sql = "insert into shangpin_liulan(u_id,sp_id,riqi,shijian) values(?,?,?,?)";
            jdbcTemplate.update(sql, session.getAttribute("mem_id"),sp_id,mydate1,mydate2);
            System.out.println("用户插入成功");
        }

        //读取评论列表
        String sql_pinglun="select * from shangpin_pinglun where shenhe=1 and zixun_id="+sp_id+" order by id desc  ";
        List<Map<String, Object>> list_pinglun = jdbcTemplate.queryForList(sql_pinglun);
        model.addAttribute("list_pinglun",list_pinglun);
        System.out.println("评论内容="+list_pinglun);

        return "/pc/pc_shangpin_xiangqing";
    }

    //购物车 列表  pc_shangpin_gouwuche
    @RequestMapping("/pc_shangpin_gouwuche") //不是@GetMapping
    public String pc_shangpin_gouwuche(String sp_id, Model model, HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession();
        System.out.println("购物车页面；当前用户id="+session.getAttribute("mem_id"));

        //读取购物车
        String sql_gouwuche= "";
        sql_gouwuche = sql_gouwuche + " select gouwuche.*,shangpin.cp_tupian,shangpin.kucun,shangpin.yixiaoshou from gouwuche,shangpin ";
        sql_gouwuche = sql_gouwuche + " where  gouwuche.sp_id=shangpin.id and gouwuche.zt=1 and u_id="+session.getAttribute("mem_id")+" order by gouwuche.id  ";
        List<Map<String, Object>> list_gouwuche = jdbcTemplate.queryForList(sql_gouwuche);
        model.addAttribute("list_gouwuche",list_gouwuche);
        System.out.println("购物车数据="+list_gouwuche);

        return "/pc/pc_shangpin_gouwuche";
    }

    //购物车 下单填写页面  pc_shangpin_gouwuche_xiadan
    @RequestMapping("/pc_shangpin_gouwuche_xiadan") //不是@GetMapping
    public String pc_shangpin_gouwuche_xiadan(String xuhao,String sp_id, Model model, HttpServletRequest request, HttpServletResponse response)
    {
        //获取要下单的产品id结合
        String danhao = "";
        String id_cps = "";  //购物车id
        System.out.println("下单的产品id集合="+xuhao); //200752,159,200753,209,200754,1223
        String Array1[] = xuhao.split(",");
        for( int x = 0; x < Array1.length-1; x++ ) {
            if(x%2==0){
                //System.out.println(Array1[x]);
                id_cps = id_cps + Array1[x] + ",";
                danhao = Array1[x];
            }
        }
        System.out.println("下单单号="+danhao + "| 下单ids="+id_cps);
        model.addAttribute("danhao",danhao);
        model.addAttribute("id_cps", id_cps);

        //读取地址列表
        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));
        //读取当前用户的地址列表
        String sql_dizhi= "";
        sql_dizhi = sql_dizhi + " select * from huiyuan_dizhi";
        sql_dizhi = sql_dizhi + " where  u_id="+session.getAttribute("mem_id")+" order by id desc ";
        List<Map<String, Object>> list_dizhi = jdbcTemplate.queryForList(sql_dizhi);
        model.addAttribute("list_dizhi",list_dizhi);
        System.out.println("用户地址列表="+list_dizhi);

        //读取要下单的产品列表
        String sql_shangpin = "";
        sql_shangpin = sql_shangpin + "select  shangpin.id as sp_id,shangpin.mingcheng,shangpin.jiage1,shangpin.cp_tupian,shangpin.kucun,shangpin.yixiaoshou";
        sql_shangpin = sql_shangpin + ",gouwuche.sp_shuliang from shangpin,gouwuche";
        sql_shangpin = sql_shangpin + " where shangpin.id=gouwuche.sp_id and  gouwuche.id in("+id_cps+"0) ";
        List<Map<String, Object>> list_shangpin = jdbcTemplate.queryForList(sql_shangpin);
        model.addAttribute("list_shangpin",list_shangpin);
        System.out.println("下单商品列表="+list_shangpin);

        //计算费用
        Integer zongfeiyong = 0;
        String sql_a1="select * from gouwuche where zt=1 and id in("+id_cps+"0) and u_id="+session.getAttribute("mem_id");
        SqlRowSet rs_a1 = jdbcTemplate.queryForRowSet(sql_a1);
        while (rs_a1.next()){
            System.out.println("单价="+rs_a1.getInt("jiage_shichang") + "|数量=" + rs_a1.getInt("sp_shuliang") );
            zongfeiyong = zongfeiyong + rs_a1.getInt("jiage_shichang") * rs_a1.getInt("sp_shuliang");
        }
        System.out.println("总费用=" + zongfeiyong);
        model.addAttribute("zongfeiyong", zongfeiyong);

        return  "/pc/pc_shangpin_gouwuche_xiadan";
    }

    //购物车 下单处理程序  pc_shangpin_gouwuche_xiadan_chuli
    @RequestMapping("/pc_shangpin_gouwuche_xiadan_chuli") //不是@GetMapping
    public String pc_shangpin_gouwuche_xiadan_chuli(String danhao,String id_cps,Integer zongfeiyong, Integer dizhi,String beizhu,
                                              Model model, HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("单号：" + danhao + "|购物车id集合=" + id_cps + "|总费用="+zongfeiyong);
        System.out.println("地址id=" + dizhi + "|" + "|备注=" + beizhu);

        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //默认时间  2021/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
        java.util.Date Dates = new java.util.Date();
        SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
        SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
        String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

        //（1）：获取订单号，以购物车的id为订单号,上面参数已经获取
        //（2）：将订单信息写入订单表
        //（3）：更新购物车 - > 这里可以插入你的支付
        //（4）：跳转到订单列表

        //（2）：将订单信息写入订单表 '1在购物车；2下单未付款；3已付款，还没发货；4已经发货；5用户已收
        String sql_dingdan = "";
        sql_dingdan = sql_dingdan + "insert into dingdan";
        sql_dingdan = sql_dingdan + "(danhao,u_id,dizhi_id,beizhu_dingdan,feiyong_chengjiao,zt,shijian_xiadan)";
        sql_dingdan = sql_dingdan + " values(?,?,?,?,?,?,?)";
        //System.out.println(sql_dingdan);
        jdbcTemplate.update(sql_dingdan, danhao,session.getAttribute("mem_id"),dizhi,beizhu,zongfeiyong,2,mydate2);
        System.out.println("插入订单表成功");

        //（3）：更新购物车
        String sql_gouwuche = "";
        sql_gouwuche = sql_gouwuche + "update gouwuche ";
        sql_gouwuche = sql_gouwuche + " set danhao=?,zt=2,shijian_xiadan=?";
        sql_gouwuche = sql_gouwuche + " where id in("+id_cps+"0)";
        jdbcTemplate.update(sql_gouwuche, danhao,mydate2);
        System.out.println("更新购物车信息成功");
        //（4）：跳转到订单列表
        return "redirect:/pc_mem_shangpin_dingdan_list";
    }

    //购物车删除
    @RequestMapping("/api_shop_gwc_del") //不是@GetMapping
    public String api_shop_gwc_del(Integer gwc_id, Model model){
        System.out.println("要删除的信息id=" + gwc_id);

        //删除数据
        String sql = "delete from gouwuche where id=?";
        jdbcTemplate.update(sql, gwc_id);
        System.out.println("删除成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/pc_shangpin_gouwuche";
    }

}//public class pc_shangpin
