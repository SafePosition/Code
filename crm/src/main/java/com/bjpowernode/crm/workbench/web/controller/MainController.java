package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @BelongsProject: Code
 * @BelongsPackage: com.bjpowernode.crm.workbench.web.controller
 * @Author: dushicheng
 * @CreateTime: 2022-06-16  13:06
 * @Description: TODO
 * @Version: 1.0
 */
@Controller
public class MainController {
    @RequestMapping("workbench/main/index.do")
    String index(){
        //跳转到/main/index
        return "workbench/main/index";
    }
}
