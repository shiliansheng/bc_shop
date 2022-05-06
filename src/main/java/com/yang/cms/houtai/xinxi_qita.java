package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class xinxi_qita {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;


    //后台 信息 评论 列表
    @RequestMapping("/houtai_xinxi_pinglun_list") //不是@GetMapping
    public String houtai_xinxi_pinglun_list(String pageNumber, Model model){

        //初始化设置
        int PAGESIZE = 5;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from zixun_pinglun ",Integer.class);
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

        return "houtai/xinxi/xinxi_pinglun_list";
    }//后台 信息 评论 列表

    //后台 信息 评论 列表操作（修改xiugai和删除del）
    @RequestMapping("/houtai_xinxi_pinglun_caozuo") //不是@GetMapping
    public String houtai_xinxi_pinglun_caozuo(Integer pinglun_id, String act,Model model,String shenhe_beizhu,Integer shenhe)
    {
        System.out.println("要修改的信息id=" + pinglun_id);
        System.out.println("操作act="+act);
        if(act==null || "".equals(act)){
            return "redirect:/houtai_xinxi_pinglun_list";
        }else{
            //显示修改数据
            if(act.equals("xiugai")){
                //根据评论id，读取评论信息的状态和备注
                String sql_xinxi="select  * from zixun_pinglun where id=" + pinglun_id;
                Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
                model.addAttribute("xinxi",xinxi);
                System.out.println("获取的信息="+xinxi);

                return "/houtai/xinxi/xinxi_pinglun_xiugai";
            }
            //修改数据 > 更新操作
            if(act.equals("xiugai_act")){
                System.out.println("获取的审核状态="+shenhe + "|备注="+shenhe_beizhu+ "|修改id="+pinglun_id) ;
                String sql = "";
                sql =  sql + "update  zixun_pinglun ";
                sql =  sql + " set  shenhe=?,shenhe_beizhu=?";
                sql =  sql + " where id=? ";
                jdbcTemplate.update(sql,shenhe, shenhe_beizhu,pinglun_id);
                System.out.println("修改成功");
                return "redirect:/houtai_xinxi_pinglun_list";
            }
            //删除数据
            if(act.equals("del")){
                System.out.println("要删除的信息id=" + pinglun_id);

                //删除数据
                String sql = "delete from zixun_pinglun where id=?";
                jdbcTemplate.update(sql, pinglun_id);
                System.out.println("删除成功");

                return "redirect:/houtai_xinxi_pinglun_list";
            }

            return "redirect:/houtai_xinxi_pinglun_list";
        }

        //return "/houtai/xinxi/xinxi_pinglun_xiugai";
        //return "redirect:/houtai_xinxi_list";
    }

}
