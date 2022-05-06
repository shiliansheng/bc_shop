package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
public class xinxi {

    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //信息分类管理
    @RequestMapping("/houtai_xinxi_fenlei") //不是@GetMapping
    public String houtai_xinxi_fenlei(String mingcheng, Integer paixu,
                                      String act,
                                      Integer fenlei1_id,String  fenlei1_mc,Integer fenlei1_paixu_id,
                                      Model model)
    {
        System.out.println("参数act="+act);
        String caozuo = "";
        if(act==null || "".equals(act)){
            System.out.println("没有操作，显示列表数据");
        }else{
            //录入数据
            if(act.equals("add")){
                System.out.println("开始录入数据");
                System.out.println("录入的名称="+mingcheng);
                System.out.println("排序id="+paixu);
                String sql = "Insert into zixun_fenlei(caidan_mingcheng,caidan_jibie,paixu_id) values(?,?,?)";
                jdbcTemplate.update(sql, mingcheng,1,paixu);
                System.out.println("插入成功");
            }
            //删除数据
            if(act.equals("del")){
                System.out.println("删除数据的id="+fenlei1_id);
                String sql = "delete from zixun_fenlei where id=?";
                jdbcTemplate.update(sql, fenlei1_id);
                System.out.println("删除成功");
            }
            //修改数据 - 显示
            if(act.equals("xiugai")){
                System.out.println("开始修改，修改数据id="+fenlei1_id+"|名称="+fenlei1_mc+"|排序id="+fenlei1_paixu_id);
                caozuo = "xiugai";
                model.addAttribute("caozuo",caozuo);
                model.addAttribute("fenlei1_id",fenlei1_id);
                model.addAttribute("fenlei1_mc",fenlei1_mc);
                model.addAttribute("fenlei1_paixu_id",fenlei1_paixu_id);

            }
            //修改数据 - 更新数据库
            if(act.equals("xiugai_act")){
                System.out.println("开始更新数据");
                System.out.println("录入的名称="+mingcheng);
                System.out.println("排序id="+paixu);
                System.out.println("id="+fenlei1_id);
                //更新
                String sql = "Update zixun_fenlei Set caidan_mingcheng=?,paixu_id=? where id=?";
                jdbcTemplate.update(sql, mingcheng,paixu,fenlei1_id);

                caozuo = "";
                model.addAttribute("caozuo",caozuo);
            }

        }
        //读取1级分类列表
        String sql1="select  id,caidan_mingcheng,paixu_id from zixun_fenlei where caidan_jibie=1  order by id desc ";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println(list1);
        //[{id=289, caidan_mingcheng=菜单设置和管理}, {id=290, caidan_mingcheng=分组权限管理}, {id=305, caidan_mingcheng=后台用户管理}]

        model.addAttribute("list1",list1);

        return "houtai/xinxi/fenlei";
    }

    //信息录入
    @RequestMapping("/houtai_xinxi_add") //不是@GetMapping
    public String xinxi_add(Integer xinxi_lxid,String xinxi_biaoti,String xinxi_riqi,
                            String xinxi_jianjie_yn,String xinxi_jianjie,
                            String xinxi_tupian_yn,String xinxi_tupian,
                            String xinxi_neirong,String act,Model model)
    {
        if(act==null || "".equals(act)){
            System.out.println("录入信息，默认页面");

            //默认时间  2021/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
            java.util.Date Dates = new java.util.Date();
            SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
            SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
            String mydate2 = ft2.format(Dates);  //转成指定格式的字符串
            model.addAttribute("xinxi_riqi",mydate1);

            //先读取1级菜单的列表
            String sql_fenlei="select  id,caidan_mingcheng,paixu_id from zixun_fenlei  order by paixu_id desc,id desc";
            List<Map<String, Object>> list_fenlei = jdbcTemplate.queryForList(sql_fenlei);
            model.addAttribute("list_fenlei",list_fenlei);
            return "houtai/xinxi/xinxi_add";
        }else{
            System.out.println("类型id="+xinxi_lxid);
            System.out.println("标题="+xinxi_biaoti);
            System.out.println("日期="+xinxi_riqi);

            if("1".equals(xinxi_jianjie_yn)){}else {
                xinxi_jianjie_yn = "0";
            }
            System.out.println("是否有推荐简介="+xinxi_jianjie_yn);
            System.out.println("简介内容="+xinxi_jianjie);



            //默认时间  2022/04/05 格式不能显示 需要按后面格式处理> 2022-04-05
            java.util.Date Dates = new java.util.Date();
            SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
            SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
            String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

            String sql = "";
            sql =  sql + "Insert into zixun(";
            sql =  sql + "xinxi_lxid,xinxi_biaoti,xinxi_riqi";
            sql =  sql + ",xinxi_jianjie_yn,xinxi_jianjie";
            sql =  sql + ",xinxi_tupian_yn,xinxi_tupian";
            sql =  sql + ",xinxi_neirong,add_riqi,add_shijian) values(?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, xinxi_lxid,xinxi_biaoti,xinxi_riqi,xinxi_jianjie_yn,xinxi_jianjie,xinxi_tupian_yn,xinxi_tupian,xinxi_neirong,mydate1,mydate2);
            System.out.println("插入成功");
            return "redirect:/houtai_xinxi_list";
        }
        //return "houtai/xinxi/xinxi_add";

    }


    //信息列表
    @RequestMapping("/houtai_xinxi_list") //不是@GetMapping
    public String houtai_xinxi_list(String pageNumber,Model model){

        //初始化设置
        int PAGESIZE = 10;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from zixun",Integer.class);
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
        sql2= sql2+"select  zixun.id,zixun.xinxi_lxid,zixun.xinxi_biaoti,zixun.xinxi_jianjie_yn,zixun.xinxi_tupian_yn,zixun.add_riqi";
        sql2= sql2+",zixun_fenlei.caidan_mingcheng  from zixun,zixun_fenlei ";
        sql2= sql2+" where zixun.xinxi_lxid=zixun_fenlei.id ";
        sql2= sql2+" order by zixun.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);

        model.addAttribute("curPage",curPage);
        model.addAttribute("size",size);


        return "houtai/xinxi/xinxi_list";
    }

    //修改资讯
    @RequestMapping("/houtai_xinxi_xiugai") //不是@GetMapping
    public String houtai_xinxi_xiugai(Integer xinxi_id, String act,Model model,
                                      Integer xinxi_lxid,String xinxi_biaoti,String xinxi_riqi,
                                      String xinxi_jianjie_yn,String xinxi_jianjie,
                                      String xinxi_tupian_yn,String xinxi_tupian,
                                      String xinxi_neirong)
    {
        System.out.println("要修改的信息id=" + xinxi_id);
        System.out.println("操作act="+act);
        if(act==null || "".equals(act)){

        }else{
            //显示修改数据
            if(act.equals("xiugai")){
                //先读取资讯分类的列表
                String sql_fenlei="select  id,caidan_mingcheng,paixu_id from zixun_fenlei  order by paixu_id desc,id desc";
                List<Map<String, Object>> list_fenlei = jdbcTemplate.queryForList(sql_fenlei);
                model.addAttribute("list_fenlei",list_fenlei);

                //根据信息的id，读取要修改的数据信息
                String sql_xinxi="select  * from zixun where id=" + xinxi_id;
                Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
                model.addAttribute("xinxi",xinxi);
                System.out.println("获取的信息="+xinxi);

                return "/houtai/xinxi/xinxi_xiugai";
            }
            //修改数据 > 更新操作
            if(act.equals("xiugai_act")){
                System.out.println("类型id="+xinxi_lxid);
                System.out.println("标题="+xinxi_biaoti);
                System.out.println("日期="+xinxi_riqi);

                if("1".equals(xinxi_jianjie_yn)){}else {
                    xinxi_jianjie_yn = "0";
                }
                System.out.println("是否有推荐简介="+xinxi_jianjie_yn);
                System.out.println("简介内容="+xinxi_jianjie);

                if("1".equals(xinxi_tupian_yn)){}else {
                    xinxi_tupian_yn = "0";
                }
                System.out.println("是否有图片="+xinxi_tupian_yn);
                System.out.println("图片内容="+xinxi_tupian);

                System.out.println("内容="+xinxi_neirong);

                String sql = "";
                sql =  sql + "update  zixun ";
                sql =  sql + " set  xinxi_lxid=?,xinxi_biaoti=?,xinxi_riqi=?";
                sql =  sql + ",xinxi_jianjie_yn=?,xinxi_jianjie=?";
                sql =  sql + ",xinxi_tupian_yn=?,xinxi_tupian=?";
                sql =  sql + ",xinxi_neirong=? where id=? ";
                jdbcTemplate.update(sql, xinxi_lxid,xinxi_biaoti,xinxi_riqi,xinxi_jianjie_yn,xinxi_jianjie,xinxi_tupian_yn,xinxi_tupian,xinxi_neirong,xinxi_id);
                System.out.println("修改成功");

                return "redirect:/houtai_xinxi_list";
            }


        }

        return "/houtai/xinxi/xinxi_xiugai";
        //return "redirect:/houtai_xinxi_list";
    }

    //删除资讯
    @RequestMapping("/houtai_xinxi_del") //不是@GetMapping
    public String houtai_xinxi_del(Integer xinxi_id, Model model){
        System.out.println("要删除的信息id=" + xinxi_id);

        //删除数据
        String sql = "delete from zixun where id=?";
        jdbcTemplate.update(sql, xinxi_id);
        System.out.println("删除成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/houtai_xinxi_list";
    }



} //public class xinxi  结束
