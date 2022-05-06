package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class guanyu {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //系统设置
    @RequestMapping("/houtai_shezhi_guanyu") //不是@GetMapping
    public String houtai_shezhi_guanyu(Integer id,String xinxi_neirong, String act,Model model){
        System.out.println("要修改的关于我们信息=" + id);

        System.out.println("要修改的信息id=" + id);
        System.out.println("操作act="+act);
        if(act==null || "".equals(act)){

            //根据信息的id，读取要修改的数据信息
            String sql_xinxi="select  * from aboutus where id=" + id;
            Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
            model.addAttribute("xinxi",xinxi);
            System.out.println("获取的信息="+xinxi);

            return "/houtai/shezhi/shezhi_guanyu";
        }else{
            //修改数据 > 更新操作
            if(act.equals("xiugai_act")){
                System.out.println("要更新的数据id=" + id);

                String sql = "";
                sql =  sql + "update  aboutus ";
                sql =  sql + " set  Miaoshu=?";
                sql =  sql + " where id=? ";
                jdbcTemplate.update(sql, xinxi_neirong,id);
                System.out.println("修改成功");
                //return "<script language='JavaScript'>alert('修改成功')</script>";
                return "redirect:/houtai_shezhi_guanyu?id="+id;
            }
        }

        return "/houtai/shezhi/shezhi_guanyu";
       // return "redirect:/houtai_xinxi_list";
    }

    //关键字设定
    @RequestMapping("/houtai_shezhi_guanyu2") //不是@GetMapping
    public String houtai_shezhi_guanyu2(Integer id,String Miaoshu, String act,Model model){
        System.out.println("要修改的关于我们信息=" + id);

        System.out.println("要修改的信息id=" + id);
        System.out.println("操作act="+act);
        if(act==null || "".equals(act)){

            //根据信息的id，读取要修改的数据信息
            String sql_xinxi="select  * from aboutus where id=" + id;
            Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
            model.addAttribute("xinxi",xinxi);
            System.out.println("获取的信息="+xinxi);

            return "/houtai/shezhi/shezhi_guanyu2";
        }else{
            //修改数据 > 更新操作
            if(act.equals("xiugai_act")){
                System.out.println("要更新的数据id=" + id);

                String sql = "";
                sql =  sql + "update  aboutus ";
                sql =  sql + " set  Miaoshu=?";
                sql =  sql + " where id=? ";
                jdbcTemplate.update(sql, Miaoshu,id);
                System.out.println("修改成功");
                //return "<script language='JavaScript'>alert('修改成功')</script>";
                return "redirect:/houtai_shezhi_guanyu2?id="+id;
            }
        }

        return "/houtai/shezhi/shezhi_guanyu2";
        // return "redirect:/houtai_xinxi_list";
    }
    //设置广告
    @RequestMapping("/houtai_guanggao_shezhi") //不是@GetMapping
    public String houtai_shezhi_guanyu(String wz1,String tpdz1,String ljdz1,
                                       String wz2,String tpdz2,String ljdz2,
                                       String wz3,String tpdz3,String ljdz3, String act,Model model)
    {
        System.out.println("获取广告原始设置");
        System.out.println("操作act="+act);
        if(act==null || "".equals(act)){

            //根据信息的id，读取要修改的数据信息
            String sql_xinxi="select  * from guanggao where id=1";
            Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
            model.addAttribute("xinxi",xinxi);
            System.out.println("获取的信息="+xinxi);

            return "/houtai/shezhi/guanggao_shezhi";
        }else{
            //修改数据 > 更新操作
            if(act.equals("xiugai_act")){
                String sql = "";
                sql =  sql + "update  guanggao ";
                sql =  sql + " set  wz1=?,tpdz1=?,ljdz1=?";
                sql =  sql + " ,wz2=?,tpdz2=?,ljdz2=?";
                sql =  sql + " ,wz3=?,tpdz3=?,ljdz3=?";
                sql =  sql + " where id=1";
                jdbcTemplate.update(sql, wz1,tpdz1,ljdz1,wz2,tpdz2,ljdz2,wz3,tpdz3,ljdz3);
                System.out.println("修改成功");
                return "redirect:/houtai_guanggao_shezhi";
            }

        }
        return "/houtai/shezhi/guanggao_shezhi";
    }

    //留言列表
    @RequestMapping("/houtai_liuyan_list") //不是@GetMapping
    public String houtai_liuyan_list( String pageNumber,Model model)
    {
        //初始化设置
        int PAGESIZE = 5;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from liuyan",Integer.class);
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
        sql2= sql2+"select * from liuyan";
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

        return "/houtai/shezhi/liuyan_list";
    }

    //删除留言
    @RequestMapping("/houtai_liuyan_del") //不是@GetMapping
    public String houtai_yonghu_del(Integer xinxi_id, Model model){
        System.out.println("要删除的信息id=" + xinxi_id);

        //删除数据
        String sql = "delete from liuyan where id=?";
        jdbcTemplate.update(sql, xinxi_id);
        System.out.println("删除成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/houtai_liuyan_list";
    }

}//public class guanyu
