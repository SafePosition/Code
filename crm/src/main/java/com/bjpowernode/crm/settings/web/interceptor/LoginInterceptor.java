package com.bjpowernode.crm.settings.web.interceptor;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.settings.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @BelongsProject: Code
 * @BelongsPackage: com.bjpowernode.crm.web.interceptor
 * @Author: dushicheng
 * @CreateTime: 2022-06-15  16:00
 * @Description: TODO
 * @Version: 1.0
 */
// 登录验证拦截器
public class LoginInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 获取session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        if (user == null) { // 如果没有登录过，那么一定不会有user对象，重定向到登录界面
            response.sendRedirect("/");
           //response.sendRedirect("/crm");

            System.out.println(request.getContextPath()+"/");
            return false; // false代表拦截
        }
        return true; // true代表放行
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
