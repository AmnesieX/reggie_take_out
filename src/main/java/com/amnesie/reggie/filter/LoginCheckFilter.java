package com.amnesie.reggie.filter;

/**
 * @Description:
 * @author: Amnesie
 * @Date: 2022-10-03
 */

import com.alibaba.fastjson.JSON;
import com.amnesie.reggie.common.BaseContext;
import com.amnesie.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否为登录状态
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        //放行不需要拦截的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login"
        };
        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //3、如果不需要处理，则直接放行
        if (check) {
            log.info("已放行: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //4.1、判断管理端登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("已登录: {}, 管理id: {}", requestURI, request.getSession().getAttribute("employee"));
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }
        //4.2、判断用户端登录状态，如已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("已登录: {}, 用户id: {}", requestURI, request.getSession().getAttribute("user"));
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));
            filterChain.doFilter(request, response);
            return;
        }
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("已拦截: {}", requestURI);
    }
        //获取当前用户IP地址
        //String remoteHost = request.getRemoteHost();
        /**
         * 待实现：
         * 对同一用户同一IP地址的密码错误登录失败次数进行记录
         * 如失败次数大于3次
         * 则禁止用户在该IP下的登录
         */


    /**
     * 路径匹配，判断本次请求是否需要处理
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
