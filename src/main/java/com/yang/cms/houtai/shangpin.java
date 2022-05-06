package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
public class shangpin {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //商品分类管理
    @RequestMapping("/houtai_shangpin_fenlei") //不是@GetMapping
    public String houtai_shangpin_fenlei(String mingcheng, Integer paixu,
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
                String sql = "Insert into shangpin_fenlei(caidan_mingcheng,caidan_jibie,paixu_id) values(?,?,?)";
                jdbcTemplate.update(sql, mingcheng,1,paixu);
                System.out.println("插入成功");
            }
            //删除数据
            if(act.equals("del")){
                System.out.println("删除数据的id="+fenlei1_id);
                String sql = "delete from shangpin_fenlei where id=?";
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
                String sql = "Update shangpin_fenlei Set caidan_mingcheng=?,paixu_id=? where id=?";
                jdbcTemplate.update(sql, mingcheng,paixu,fenlei1_id);

                caozuo = "";
                model.addAttribute("caozuo",caozuo);
            }

        }
        //读取1级分类列表
        String sql1="select  id,caidan_mingcheng,paixu_id from shangpin_fenlei where caidan_jibie=1  order by id desc ";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println(list1);
        //[{id=289, caidan_mingcheng=菜单设置和管理}, {id=290, caidan_mingcheng=分组权限管理}, {id=305, caidan_mingcheng=后台用户管理}]

        model.addAttribute("list1",list1);

        return "houtai/shangpin/fenlei";
    }

    //商品录入
    @RequestMapping("/houtai_shangpin_add") //不是@GetMapping
    public String houtai_shangpin_add(Integer lx_id1,String mingcheng,String fabu_riqi,
                            String jianjie_yn,String jianjie,
                            String cp_tupian_yn,String tuijian_yn,
                            Integer jiage1,Integer kucun,Integer yixiaoshou,Integer zhuangtai_yn,
                            String cp_tupian,String cp_tupian1,String cp_tupian2,String cp_tupian3,String cp_tupian4,
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
            String sql_fenlei="select  id,caidan_mingcheng,paixu_id from shangpin_fenlei  order by paixu_id desc,id desc";
            List<Map<String, Object>> list_fenlei = jdbcTemplate.queryForList(sql_fenlei);
            model.addAttribute("list_fenlei",list_fenlei);
            return "houtai/shangpin/shangpin_add";
        }else{
            System.out.println("类型id="+lx_id1);
            System.out.println("标题="+mingcheng);
            System.out.println("日期="+fabu_riqi);

            if("1".equals(jianjie_yn)){}else {
                jianjie = "0";
            }
            System.out.println("是否有简介="+jianjie_yn);
            System.out.println("简介内容="+jianjie);

            if("1".equals(cp_tupian_yn)){}else {
                cp_tupian_yn = "0";
            }
            System.out.println("是否有附加图状态="+cp_tupian_yn);

            if("1".equals(tuijian_yn)){}else {
                tuijian_yn = "0";
            }
            System.out.println("首页推荐状态=" + tuijian_yn);

            System.out.println("内容="+xinxi_neirong);

            //默认时间  2021/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
            java.util.Date Dates = new java.util.Date();
            SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
            SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
            String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

            String sql = "";
            sql =  sql + "Insert into shangpin(";
            sql =  sql + "mingcheng,lx_id1,kucun,yixiaoshou,jiage1"; //5
            sql =  sql + ",cp_tupian,cp_tupian_yn,cp_tupian1,cp_tupian2,cp_tupian3,cp_tupian4"; //6
            sql =  sql + ",zhuangtai_yn,tuijian_yn,jianjie_yn,jianjie"; //4
            sql =  sql + ",neirong,fabu_riqi,add_riqi,add_shijian) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//4
            jdbcTemplate.update(sql,  mingcheng,lx_id1,kucun,yixiaoshou,jiage1,cp_tupian,cp_tupian_yn,cp_tupian1,cp_tupian2,cp_tupian3,cp_tupian4,zhuangtai_yn,tuijian_yn,jianjie_yn,jianjie,xinxi_neirong,fabu_riqi,mydate1,mydate2);
            System.out.println("插入成功");
            return "redirect:/houtai_shangpin_list";
        }
        //return "houtai/xinxi/xinxi_add";

    }


    //商品列表
    @RequestMapping("/houtai_shangpin_list") //不是@GetMapping
    public String houtai_shangpin_list(String pageNumber,Model model){

        //初始化设置
        int PAGESIZE = 10;  //每页的数据条数  //一页放5个
        int pageCount = 0; //总的页数，初始化为0，读取具体表格后，计算出数值
        int curPage = 0;   //当前页数，默认第一页数字是0
        int shang=0;  //初始化上页数字
        int xia = 0;  //初始化下页数字
        int size = 0;  //总的数据条数 > 有多少数据

        size = jdbcTemplate.queryForObject("select count(*) from shangpin",Integer.class);
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
        sql2= sql2+"select  shangpin.id,shangpin.lx_id1,shangpin.mingcheng,shangpin.jiage1,shangpin.cp_tupian,shangpin.jianjie_yn,shangpin.tuijian_yn,shangpin.add_riqi";
        sql2= sql2+",shangpin_fenlei.caidan_mingcheng  from shangpin,shangpin_fenlei ";
        sql2= sql2+" where shangpin.lx_id1=shangpin_fenlei.id ";
        sql2= sql2+" order by shangpin.id desc limit "+curPage*PAGESIZE+","+PAGESIZE;
        System.out.println(sql2);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql2);
        System.out.println("结果="+list);
        model.addAttribute("xinxi_list",list);
        model.addAttribute("pageCount",pageCount-1); //总的页数
        model.addAttribute("shang",shang);
        model.addAttribute("xia",xia);

        model.addAttribute("curPage",curPage);
        model.addAttribute("size",size);


        return "houtai/shangpin/shangpin_list";
    }

    //商品修改
    @RequestMapping("/houtai_shangpin_xiugai") //不是@GetMapping
    public String houtai_shangpin_xiugai(Integer xinxi_id, Integer lx_id1,String mingcheng,String fabu_riqi,
                                         String jianjie_yn,String jianjie,
                                         String cp_tupian_yn,String tuijian_yn,
                                         Integer jiage1,Integer kucun,Integer yixiaoshou,Integer zhuangtai_yn,
                                         String cp_tupian,String cp_tupian1,String cp_tupian2,String cp_tupian3,String cp_tupian4,
                                         String xinxi_neirong,String act,Model model)
    {
        System.out.println("要修改的信息id=" + xinxi_id);
        System.out.println("操作act="+act);
        if(act==null || "".equals(act)){

        }else{
            //显示修改数据
            if(act.equals("xiugai")){
                //先读取资讯分类的列表
                String sql_fenlei="select  id,caidan_mingcheng,paixu_id from shangpin_fenlei  order by paixu_id desc,id desc";
                List<Map<String, Object>> list_fenlei = jdbcTemplate.queryForList(sql_fenlei);
                model.addAttribute("list_fenlei",list_fenlei);

                //根据信息的id，读取要修改的数据信息
                String sql_xinxi="select  * from shangpin where id=" + xinxi_id;
                Map<String, Object> xinxi = jdbcTemplate.queryForMap(sql_xinxi);
                model.addAttribute("xinxi",xinxi);
                System.out.println("获取的信息="+xinxi);

                return "/houtai/shangpin/shangpin_xiugai";
            }
            //修改数据 > 更新操作
            if(act.equals("xiugai_act")){
                System.out.println("类型id="+lx_id1);
                System.out.println("标题="+mingcheng);
                System.out.println("日期="+fabu_riqi);

                if("1".equals(jianjie_yn)){}else {
                    jianjie = "0";
                }
                System.out.println("是否有简介="+jianjie_yn);
                System.out.println("简介内容="+jianjie);

                if("1".equals(cp_tupian_yn)){}else {
                    cp_tupian_yn = "0";
                }
                System.out.println("是否有附加图状态="+cp_tupian_yn);

                if("1".equals(tuijian_yn)){}else {
                    tuijian_yn = "0";
                }
                System.out.println("首页推荐状态=" + tuijian_yn);

                System.out.println("内容="+xinxi_neirong);

                //默认时间  2021/05/05 格式不能显示 需要按后面格式处理> 2021-05-05
                java.util.Date Dates = new java.util.Date();
                SimpleDateFormat ft1 = new SimpleDateFormat ("yyyy-MM-dd");
                SimpleDateFormat ft2 = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                String mydate1 = ft1.format(Dates);  //转成指定格式的字符串
                String mydate2 = ft2.format(Dates);  //转成指定格式的字符串

                String sql = "";
                sql =  sql + "update  shangpin ";
                sql =  sql + " set  mingcheng=?,lx_id1=?,kucun=?,yixiaoshou=?,jiage1=?";
                sql =  sql + ",cp_tupian=?,cp_tupian_yn=?,cp_tupian1=?,cp_tupian2=?,cp_tupian3=?,cp_tupian4=?";
                sql =  sql + ",zhuangtai_yn=?,tuijian_yn=?,jianjie_yn=?,jianjie=?";
                sql =  sql + ",neirong=?,fabu_riqi=? where id=? ";
                jdbcTemplate.update(sql, mingcheng,lx_id1,kucun,yixiaoshou,jiage1,cp_tupian,cp_tupian_yn,cp_tupian1,cp_tupian2,
                        cp_tupian3,cp_tupian4,zhuangtai_yn,tuijian_yn,jianjie_yn,jianjie,xinxi_neirong,fabu_riqi,xinxi_id);
                System.out.println("修改成功");

                return "redirect:/houtai_shangpin_list";
            }


        }

        return "/houtai/shangpin/shangpin_xiugai";
        //return "redirect:/houtai_xinxi_list";
    }

    //商品删除
    @RequestMapping("/houtai_shangpin_del") //不是@GetMapping
    public String houtai_shangpin_del(Integer xinxi_id, Model model){
        System.out.println("要删除的信息id=" + xinxi_id);

        //删除数据
        String sql = "delete from shangpin where id=?";
        jdbcTemplate.update(sql, xinxi_id);
        System.out.println("删除成功");

        //return "redirect:/houtai/xinxi/xinxi_list"; //多层级会错误
        return "redirect:/houtai_shangpin_list";
    }



}//public class shangpin
