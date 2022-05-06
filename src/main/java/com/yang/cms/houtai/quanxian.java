package com.yang.cms.houtai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Controller
public class quanxian {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    //读取1级菜单列表
    @RequestMapping("/quanxian_caidan_1ji") //不是@GetMapping
    public String quanxian_caidan_1ji(String mingcheng, Integer paixu,
                                      String act,
                                      Integer quanxian1_id,String  quanxian1_mc,Integer quanxian1_paixu_id,
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
                String sql = "Insert into quanxian_caidan(caidan_mingcheng,caidan_jibie,paixu_id) values(?,?,?)";
                jdbcTemplate.update(sql, mingcheng,1,paixu);
                System.out.println("插入成功");
            }
            //删除数据
            if(act.equals("del")){
                System.out.println("删除数据的id="+quanxian1_id);
                String sql = "delete from quanxian_caidan where id=?";
                jdbcTemplate.update(sql, quanxian1_id);
                System.out.println("删除成功");
            }
            //修改数据 - 显示
            if(act.equals("xiugai")){
                System.out.println("开始修改，修改数据id="+quanxian1_id+"|名称="+quanxian1_mc+"|排序id="+quanxian1_paixu_id);
                caozuo = "xiugai";
                model.addAttribute("caozuo",caozuo);
                model.addAttribute("quanxian1_id",quanxian1_id);
                model.addAttribute("quanxian1_mc",quanxian1_mc);
                model.addAttribute("quanxian1_paixu_id",quanxian1_paixu_id);

            }
            //修改数据 - 更新数据库
            if(act.equals("xiugai_act")){
                System.out.println("开始更新数据");
                System.out.println("录入的名称="+mingcheng);
                System.out.println("排序id="+paixu);
                System.out.println("id="+quanxian1_id);
                //更新
                String sql = "Update quanxian_caidan Set caidan_mingcheng=?,paixu_id=? where id=?";
                jdbcTemplate.update(sql, mingcheng,paixu,quanxian1_id);

                caozuo = "";
                model.addAttribute("caozuo",caozuo);
            }

        }
        //读取1级分类列表
        String sql1="select  id,caidan_mingcheng,paixu_id from quanxian_caidan where caidan_jibie=1  order by paixu_id desc,id desc ";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println(list1);
        //[{id=289, caidan_mingcheng=菜单设置和管理}, {id=290, caidan_mingcheng=分组权限管理}, {id=305, caidan_mingcheng=后台用户管理}]

        model.addAttribute("list1",list1);

        return "houtai/quanxian/caidan_1ji";
    }

    //读取2级菜单列表
    @RequestMapping("/quanxian_caidan_2ji") //不是@GetMapping
    public String quanxian_caidan_2ji( Model model){
        //（1）先读取1级菜单的列表
        String sql1="select  id,caidan_mingcheng,paixu_id from quanxian_caidan where caidan_jibie=1 order by id desc";
        List<Map<String, Object>> list_quanxian1 = jdbcTemplate.queryForList(sql1);
        model.addAttribute("list_quanxian1",list_quanxian1);

        return "houtai/quanxian/caidan_2ji";
    }
    //2级菜单 操作
    @RequestMapping("/quanxian_caidan_2ji_nei") //不是@GetMapping
    public String quanxian_caidan_2ji_nei(String quanxian2_mc, Integer paixu_id,Integer quanxian2_id,
                                          String act,Integer quanxian1_id,Model model){
        System.out.println("1级权限的id="+quanxian1_id);
        System.out.println("参数act="+act);
        String caozuo = "";

        if(act==null || "".equals(act)){
            System.out.println("没有操作，显示列表数据");
        }else{
            //录入数据
            if(act.equals("add")){
                System.out.println("开始录入数据");
                System.out.println("录入的名称="+quanxian2_mc);
                System.out.println("排序id="+paixu_id);
                String sql = "Insert into quanxian_caidan(caidan_mingcheng,caidan_jibie,paixu_id,caidan_suoshu) values(?,?,?,?)";
                jdbcTemplate.update(sql, quanxian2_mc,2,paixu_id,quanxian1_id);
                System.out.println("插入成功");
            }
            //删除数据
            if(act.equals("del")){
                System.out.println("删除数据的id="+quanxian2_id);
                String sql = "delete from quanxian_caidan where id=?";
                jdbcTemplate.update(sql, quanxian2_id);
                System.out.println("删除成功");
            }
            //修改数据 - 显示
            if(act.equals("xiugai")){
                System.out.println("开始修改，修改数据id="+quanxian2_id+"|名称="+quanxian2_mc+"|排序id="+paixu_id);
                caozuo = "xiugai";
                model.addAttribute("caozuo",caozuo);
                model.addAttribute("quanxian2_id",quanxian2_id);
                model.addAttribute("quanxian2_mc",quanxian2_mc);
                model.addAttribute("paixu_id",paixu_id);

            }
            //修改数据 - 更新数据库
            if(act.equals("xiugai_act")){
                System.out.println("开始更新数据");
                System.out.println("录入的名称="+quanxian2_mc);
                System.out.println("排序id="+paixu_id);
                System.out.println("id="+quanxian2_id);
                //更新
                String sql = "Update quanxian_caidan Set caidan_mingcheng=?,paixu_id=? where id=?";
                jdbcTemplate.update(sql, quanxian2_mc,paixu_id,quanxian2_id);

                caozuo = "";
                model.addAttribute("caozuo",caozuo);
            }
        }

        //读取2级分类列表
        String sql2="select  id,caidan_mingcheng,paixu_id from quanxian_caidan where caidan_jibie=2 and caidan_suoshu="+quanxian1_id+ " order by id desc ";
        System.out.println(sql2);
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
        System.out.println(list2);
        //[{id=324, caidan_mingcheng=资讯中心, paixu_id=1}, {id=323, caidan_mingcheng=资讯分类, paixu_id=1}]
        model.addAttribute("list2",list2);
        model.addAttribute("quanxian1_id",quanxian1_id);


        return "houtai/quanxian/caidan_2ji_nei";
    }


    //读取3级菜单列表
    @RequestMapping("/quanxian_caidan_3ji") //不是@GetMapping
    public String quanxian_caidan_3ji( Integer menu1, String act,Model model){

        //（1）先读取1级菜单的列表
        String sql1="select  id,caidan_mingcheng,paixu_id from quanxian_caidan where caidan_jibie=1 order by id desc";
        List<Map<String, Object>> list_quanxian1 = jdbcTemplate.queryForList(sql1);
        model.addAttribute("list_quanxian1",list_quanxian1);

        System.out.println("menu1=" + menu1);
        model.addAttribute("menu1",menu1);

        if(menu1==null || "".equals(menu1)){
            System.out.println("没有选择1级菜单");
        }else{
            //读取2级菜单列表
            String sql2="select  id,caidan_mingcheng from quanxian_caidan where caidan_jibie=2 and caidan_suoshu="+menu1;
            System.out.println(sql2);
            List<Map<String, Object>> list_quanxian2 = jdbcTemplate.queryForList(sql2);
            model.addAttribute("list_quanxian2",list_quanxian2);

        }
        return "houtai/quanxian/caidan_3ji";
    }

    //3级菜单 操作页面
    @RequestMapping("/quanxian_caidan_3ji_nei") //不是@GetMapping
    public String quanxian_caidan_3ji_nei(String quanxian3_mc, Integer paixu_id,Integer quanxian3_id,String quanxian3_lujing,
                                          String act,Integer quanxian2_id,Model model){
        System.out.println("2级权限的id="+quanxian2_id);
        System.out.println("参数act="+act);
        String caozuo = "";

        if(act==null || "".equals(act)){
            System.out.println("没有操作，显示列表数据");
        }else{
            //录入数据
            if(act.equals("add")){
                System.out.println("开始录入数据");
                System.out.println("录入的名称="+quanxian3_mc);
                System.out.println("排序id="+paixu_id);
                String sql = "Insert into quanxian_caidan(caidan_mingcheng,caidan_lujing,caidan_jibie,paixu_id,caidan_suoshu) values(?,?,?,?,?)";
                jdbcTemplate.update(sql, quanxian3_mc,quanxian3_lujing,3,paixu_id,quanxian2_id);
                System.out.println("插入成功");
            }
            //删除数据
            if(act.equals("del")){
                System.out.println("删除数据的id="+quanxian3_id);
                String sql = "delete from quanxian_caidan where id=?";
                jdbcTemplate.update(sql, quanxian3_id);
                System.out.println("删除成功");
            }
            //修改数据 - 显示
            if(act.equals("xiugai")){
                System.out.println("开始修改，修改数据id="+quanxian3_id+"|名称="+quanxian3_mc+"|排序id="+paixu_id+"|程序路径="+quanxian3_lujing);
                caozuo = "xiugai";
                model.addAttribute("caozuo",caozuo);
                model.addAttribute("quanxian3_id",quanxian3_id);
                model.addAttribute("quanxian3_mc",quanxian3_mc);
                model.addAttribute("quanxian3_lujing",quanxian3_lujing);
                model.addAttribute("paixu_id",paixu_id);

            }
            //修改数据 - 更新数据库
            if(act.equals("xiugai_act")){
                System.out.println("开始更新数据");
                System.out.println("录入的名称="+quanxian3_mc);
                System.out.println("程序路径="+quanxian3_lujing);
                System.out.println("排序id="+paixu_id);
                System.out.println("id="+quanxian3_id);
                //更新
                String sql = "Update quanxian_caidan Set caidan_mingcheng=?,caidan_lujing=?,paixu_id=? where id=?";
                jdbcTemplate.update(sql, quanxian3_mc,quanxian3_lujing,paixu_id,quanxian3_id);

                caozuo = "";
                model.addAttribute("caozuo",caozuo);
            }
        }

        //读取3级分类列表
        String sql3="select  id,caidan_mingcheng,caidan_lujing,paixu_id from quanxian_caidan where caidan_jibie=3 and caidan_suoshu="+quanxian2_id+ " order by id desc ";
        System.out.println(sql3);
        List<Map<String, Object>> list3 = jdbcTemplate.queryForList(sql3);
        System.out.println(list3);
        //[{id=324, caidan_mingcheng=资讯中心, paixu_id=1}, {id=323, caidan_mingcheng=资讯分类, paixu_id=1}]
        model.addAttribute("list3",list3);
        model.addAttribute("quanxian2_id",quanxian2_id);


        return "houtai/quanxian/caidan_3ji_nei";
    }

}


