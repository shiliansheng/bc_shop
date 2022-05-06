package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class yonghu {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //用户列表
    @RequestMapping("/houtai_yonghu_list") //不是@GetMapping
    public String houtai_yonghu_list( String pageNumber,Model model){

        //初始化设置
        int PAGESIZE = 5;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计houtai_yonghu_list算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from huiyuan",Integer.class);
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
        sql2= sql2+"select * from huiyuan";
        sql2= sql2+" order by id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);

        model.addAttribute("curPage",curPage);
        model.addAttribute("size",size);

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        //return "redirect:/houtai_xinxi_list";
        return "houtai/yonghu/yonghu_list";
    }

    //删除用户
    @RequestMapping("/houtai_yonghu_del") //不是@GetMapping
    public String houtai_yonghu_del(Integer yonghu_id, Model model){
        System.out.println("要删除的用户id=" + yonghu_id);

        //删除数据
        String sql = "delete from huiyuan where id=?";
        jdbcTemplate.update(sql, yonghu_id);
        System.out.println("删除成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/houtai_yonghu_list";
    }
}
