package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class dingdan {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //订单列表
    @RequestMapping("/houtai_dingdan_list") //不是@GetMapping
    public String houtai_dingdan_list(String pageNumber, Model model){

        //初始化设置
        int PAGESIZE = 10;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from dingdan",Integer.class);
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
        sql2= sql2+"select  dingdan.id,dingdan.danhao,dingdan.u_id,dingdan.dizhi_id,dingdan.beizhu_dingdan,dingdan.feiyong_chengjiao,dingdan.zt,dingdan.shijian_xiadan";
        sql2= sql2+ ",huiyuan.shouji as yonghuming";
        sql2= sql2+ ",huiyuan_dizhi.xingming,huiyuan_dizhi.shouji,huiyuan_dizhi.dizhi";
        sql2= sql2+" from dingdan,huiyuan,huiyuan_dizhi";
        sql2= sql2+" where 1=1";
        sql2= sql2+" and dingdan.u_id = huiyuan.id";
        sql2= sql2+" and dingdan.dizhi_id = huiyuan_dizhi.id";
        sql2= sql2+" order by dingdan.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);
        model.addAttribute("curPage",curPage);
        model.addAttribute("size",size);

        //获取当前订单分页列表，包含的产品id
        String sql3 = "";
        sql3 = sql3 + "select  dingdan.id,dingdan.danhao";
        sql3 = sql3 + ",gouwuche.sp_mingcheng,gouwuche.sp_shuliang";
        sql3 = sql3 + " from dingdan,gouwuche";
        sql3 = sql3 + " where 1=1";
        sql3 = sql3 + " and dingdan.danhao = gouwuche.danhao";
        sql3 = sql3 + " order by dingdan.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        System.out.println(sql3);
        List<Map<String, Object>> list_sql3 = jdbcTemplate.queryForList(sql3);
        System.out.println("结果="+list_sql3);
        model.addAttribute("list_sql3",list_sql3);

        return "houtai/shangpin/dingdan_list";
    }

    //订单处理
    @RequestMapping("/houtai_dingdan_chuli") //不是@GetMapping
    public String houtai_dingdan_chuli(Integer dingdan_id,Integer zt,String beizhu_fahuo,String beizhu_caozuo,
                                       String act, Model model)
    {
        if(act==null || "".equals(act)){
            //根据信息的id，读取要修改的数据信息
            String sql_xinxi="select  id,zt,beizhu_fahuo,beizhu_caozuo from dingdan where id=" + dingdan_id;
            Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
            model.addAttribute("xinxi",xinxi);
            System.out.println("获取的信息="+xinxi);

            return "houtai/shangpin/dingdan_chuli";

        }else{
            System.out.println("要更新的数据id=" + dingdan_id);
            String sql = "";
            sql =  sql + "update  dingdan ";
            sql =  sql + " set zt=?,beizhu_fahuo=?,beizhu_caozuo=?";
            sql =  sql + " where id=? ";
            jdbcTemplate.update(sql, zt,beizhu_fahuo,beizhu_caozuo,dingdan_id);
            System.out.println("修改成功");

            return "redirect:/houtai_dingdan_list";
        }

    }
}
