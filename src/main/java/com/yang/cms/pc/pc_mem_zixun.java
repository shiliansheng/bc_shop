package com.yang.cms.pc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Controller
public class pc_mem_zixun {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //会员 资讯 浏览记录
    @RequestMapping("/pc_mem_zixun_liulan") //不是@GetMapping
    public String pc_mem_zixun_liulan(String pageNumber, Model model,
                                        HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
    {
        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //初始化设置
        int PAGESIZE = 5;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from zixun_liulan where u_id="+session.getAttribute("mem_id"),Integer.class);
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
        sql2= sql2+"select  zixun.id,zixun.xinxi_biaoti";
        sql2= sql2+",zixun_fenlei.caidan_mingcheng,zixun_liulan.riqi  from zixun,zixun_fenlei,zixun_liulan";
        sql2= sql2+" where zixun.xinxi_lxid=zixun_fenlei.id and zixun_liulan.zixun_id=zixun.id";
        sql2= sql2+" order by zixun_liulan.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);
        model.addAttribute("curPage",curPage);
        model.addAttribute("size",size);

        return "pc_mem/pc_mem_zixun_liulan";
    }//会员 资讯 浏览记录

    //会员 资讯 收藏列表
    @RequestMapping("/pc_mem_zixun_shoucang") //不是@GetMapping
    public String pc_mem_zixun_shoucang(String pageNumber, Model model,
                                        HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
    {
        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //初始化设置
        int PAGESIZE = 5;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from zixun_shoucang where u_id="+session.getAttribute("mem_id"),Integer.class);
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
        sql2= sql2+"select  zixun.id,zixun.xinxi_biaoti";
        sql2= sql2+",zixun_fenlei.caidan_mingcheng,zixun_shoucang.riqi,zixun_shoucang.id as shoucang_id  from zixun,zixun_fenlei,zixun_shoucang";
        sql2= sql2+" where zixun.xinxi_lxid=zixun_fenlei.id and zixun_shoucang.zixun_id=zixun.id";
        sql2= sql2+" order by zixun_shoucang.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);
        model.addAttribute("curPage",curPage);
        model.addAttribute("size",size);

        return "pc_mem/pc_mem_zixun_shoucang";
    }//会员 资讯 收藏列表

    //会员 资讯 评论记录
    @RequestMapping("/pc_mem_zixun_pinglun") //不是@GetMapping
    public String pc_mem_zixun_pinglun(String pageNumber, Model model,
                                       HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
    {
        HttpSession session = request.getSession();
        System.out.println("当前用户id="+session.getAttribute("mem_id"));

        //初始化设置
        int PAGESIZE = 5;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from zixun_pinglun where u_id="+session.getAttribute("mem_id"),Integer.class);
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
        sql2= sql2+"select  zixun.id,zixun.xinxi_biaoti";
        sql2= sql2+",zixun_fenlei.caidan_mingcheng,zixun_pinglun.riqi";
        sql2= sql2+",zixun_pinglun.id as pinglun_id,zixun_pinglun.neirong,zixun_pinglun.shenhe  from zixun,zixun_fenlei,zixun_pinglun";
        sql2= sql2+" where zixun.xinxi_lxid=zixun_fenlei.id and zixun_pinglun.zixun_id=zixun.id";
        sql2= sql2+" order by zixun_pinglun.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);
        model.addAttribute("curPage",curPage);
        model.addAttribute("size",size);

        return "pc_mem/pc_mem_zixun_pinglun";
    }//会员 资讯 评论记录

}//public class pc_mem_zixun
