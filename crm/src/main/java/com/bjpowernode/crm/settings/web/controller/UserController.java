package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLoding()
    {
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd,
                        String isRemPwd, HttpServletRequest request,
                        HttpServletResponse response, HttpSession session) {
        //        封装参数
        Map<String ,Object > map = new HashMap<>();
        map.put("loginAct",loginAct );
        map.put("loginPwd",loginPwd );
//        调用service方法，查询对象
        User user = userService.queryByLoginActAndPwd(map);
//        根据查询结果判断相应信息
        System.out.println("进入usercontroller");
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
//            登录失败，用户名或密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else {
//            user.getExpireTime()    2020--01-22
//            new Date()              2022-09-11
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String nowStr = sdf.format(new Date());
            //使用工具类进行日期格式化
            String nowStr = DateUtils.formateDateTime(new Date());
            if (nowStr.compareTo(user.getExpireTime())>0) {
//                登录失败，账号已过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if ("0".equals(user.getLockState())) {
//                登录失败，状态被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
//                 登录失败ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else {

                // 登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //将用户名放入session中
                session.setAttribute(Contants.SESSION_USER,user);
                if ("true".equals(isRemPwd)) {
                    Cookie cookie1 = new Cookie("loginAct", user.getLoginAct());
                    cookie1.setMaxAge(10*24*60*60);
                    response.addCookie(cookie1);
                    Cookie cookie2 = new Cookie("loginPwd", user.getLoginPwd());
                    cookie2.setMaxAge(10*24*60*60);
                    response.addCookie(cookie2);
                }else{
                    //清空cooki
                    Cookie cookie1 = new Cookie("loginAct","");
                    cookie1.setMaxAge(0);
                    response.addCookie(cookie1);
                    Cookie cookie2 = new Cookie("loginPwd", "");
                    cookie2.setMaxAge(0);
                    response.addCookie(cookie2);
                }
            }

        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cooki
        Cookie cookie1 = new Cookie("loginAct","");
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        Cookie cookie2 = new Cookie("loginPwd", "");
        cookie2.setMaxAge(0);
        response.addCookie(cookie2);
        //销毁session
        session.invalidate();
        return  "redirect:/";
    }
}
