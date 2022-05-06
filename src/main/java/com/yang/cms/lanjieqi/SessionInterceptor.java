package com.yang.cms.lanjieqi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired    // 自动注入，spring boot会帮我们实例化一个对象
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        System.out.println("----------------这里是拦截器-----------");
        //读取热门关键词
        String remen_key = "";
        String sql_key="select miaoshu from aboutus where id=6" ;
        SqlRowSet rs_key = jdbcTemplate.queryForRowSet(sql_key);
        if (rs_key.next()){
            remen_key = rs_key.getString("miaoshu");
        }
        System.out.println("热门词集合="+remen_key);
        String[] arr_key = remen_key.split("\\|");	// 分割成数组
        //model.addAttribute("arr_key",arr_key);
        HttpSession session = request.getSession();
        session.setAttribute("arr_key", arr_key);

        //读取商品分类:顶部商品分类
        String sql_fenlei_dingbu="select  * from shangpin_fenlei ";
        List<Map<String, Object>> list_fenlei_dingbu = jdbcTemplate.queryForList(sql_fenlei_dingbu);
        session.setAttribute("list_fenlei_dingbu", list_fenlei_dingbu);

        //读取用户是否登录信息
        //读取cookie u_id  u_fzid  u_name
        String mem_id = "";
        String mem_name = "";
        Cookie cookie = null;
        Cookie[] cookies = null;
        // 获取 cookies 的数据,是一个数组
        cookies = request.getCookies();
        if(cookies !=null){
            for (int i = 0; i < cookies.length; i++){
                cookie = cookies[i];
                //out.print(" | 参数值: " + URLDecoder.decode(cookie.getValue(), "utf-8") +" <br>");
                if(cookie.getName().equals("mem_id")){ //  == 判断可能无效
                    mem_id = URLDecoder.decode(cookie.getValue(), "utf-8");
                    session.setAttribute("mem_id", mem_id);
                }
                if(cookie.getName().equals("mem_name")){ //  == 判断可能无效
                    mem_name = URLDecoder.decode(cookie.getValue(), "utf-8");
                }
            }
            String xinxi_denglu = "用户id："+mem_id+ " | 用户名："+mem_name;
            System.out.println(xinxi_denglu);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {

    }
}
