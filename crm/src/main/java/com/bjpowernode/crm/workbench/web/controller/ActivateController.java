package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.pojo.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.pojo.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.pojo.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: Code
 * @BelongsPackage: com.bjpowernode.crm.workbench.web.controller
 * @Author: dushicheng
 * @CreateTime: 2022-06-16  17:11
 * @Description: TODO
 * @Version: 1.0
 */
@Controller
public class ActivateController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;



    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //拿到用户列表
        List<User> userList = userService.queryAllUsers();
        //把数据保存到作用域中
        request.setAttribute("userList",userList);
        //请求转发带数据，转发到市场活动
        return "/workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){

        User user = (User)session.getAttribute(Contants.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();

        try {
            // 保存创建的市场活动
            int res = activityService.saveCreateActivity(activity);
            if (res > 0) { // 保存成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else { // 保存失败，服务器端出了问题，为了不影响顾客体验，最好不要直接说出问题
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙");
            }
        }catch (Exception exception){
            exception.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙请稍后再试.......");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,
                                                  int pageNo,int pageSize){
        //封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        //查询总条数
        int totalRows = activityService.queryCountOfActivityByCondition(map);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    @RequestMapping("workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.deleteActivityByIds(id);
            if (ret >0 ) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                System.out.println("-------------------------------");
                returnObject.setMessage("系统忙。。。。。");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙。。。。。");
        }
        return returnObject;
    }

    @RequestMapping("workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id){
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }
    @RequestMapping("workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session){
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        User user =(User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityService.saveEditActivity(activity);
            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙");
        }
        return returnObject;
    }

    @RequestMapping("workbench/activity/exportAllActivity.do")
    public void exportAllActivitys  (HttpServletResponse response) throws Exception{
        List<Activity> activityList = activityService.queryAllActivitys();
        HSSFUtils.createExcelByActivityList(activityList,"activitylist.xls",response);
//        //创建excl文件
//        HSSFWorkbook wb = new HSSFWorkbook();
//        HSSFSheet sheet = wb.createSheet("市场活动列表");
//        HSSFRow row = sheet.createRow(0);
//
//        HSSFCell cell = row.createCell(0);
//        cell.setCellValue("ID");
//        cell = row.createCell(1);
//        cell.setCellValue("所有者");
//        cell = row.createCell(2);
//        cell.setCellValue("名称");
//        cell = row.createCell(3);
//        cell.setCellValue("开始日期");
//        cell = row.createCell(4);
//        cell.setCellValue("结束日期");
//        cell = row.createCell(5);
//        cell.setCellValue("成本");
//        cell = row.createCell(6);
//        cell.setCellValue("描述");
//        cell = row.createCell(7);
//        cell.setCellValue("创建时间");
//        cell = row.createCell(8);
//        cell.setCellValue("创建者");
//        cell = row.createCell(9);
//        cell.setCellValue("修改时间");
//        cell = row.createCell(10);
//        cell.setCellValue("修改者");
//
//        if (activityList != null &&activityList.size()>0) {
//            Activity activity=null;
//            for (int i = 0; i < activityList.size(); i++) {
//                activity = activityList.get(i);
//                row = sheet.createRow(i + 1);
//
//
//                cell = row.createCell(0);
//                cell.setCellValue(activity.getId());
//
//                cell = row.createCell(1);
//                cell.setCellValue(activity.getOwner());
//
//                cell = row.createCell(2);
//                cell.setCellValue(activity.getName());
//
//                cell = row.createCell(3);
//                cell.setCellValue(activity.getCreateTime());
//
//                cell = row.createCell(4);
//                cell.setCellValue(activity.getEndDate());
//
//                cell = row.createCell(5);
//                cell.setCellValue(activity.getCost());
//
//                cell = row.createCell(6);
//                cell.setCellValue(activity.getDescription());
//
//                cell = row.createCell(7);
//                cell.setCellValue(activity.getCreateTime());
//
//                cell = row.createCell(8);
//                cell.setCellValue(activity.getCreateBy());
//
//                cell = row.createCell(9);
//                cell.setCellValue(activity.getEditTime());
//
//                cell = row.createCell(10);
//                cell.setCellValue(activity.getEditBy());
//            }
//        }
//        //根据wb生成输出流
////        OutputStream os = new FileOutputStream("D:\\Code\\crm\\src\\main\\resources\\uplode\\activitylist.xls");
////        wb.write(os);
//        //关闭资源
////        os.close();
////        wb.close();
//
////        把文件推送到客户端
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        response.addHeader("Content-Disposition","attachment;filename=activitylist.xls");
//        OutputStream out = response.getOutputStream();
////        InputStream  is = new FileInputStream("D:\\Code\\crm\\src\\main\\resources\\uplode\\activitylist.xls");
////        byte[] buff = new byte[256];
////        int len = 0;
////        is.read(buff)将文件读到缓存区
////        while ((len=is.read(buff))!=-1){
////            out.write(buff,0,len);
////        }
////
//
////        is.close();
//        wb.write(out);
//        wb.close();
//        out.flush();//out由tomcat关闭
    }
//
//    @RequestMapping("workbench/activity/importActivity.do")
//    public Object importAvtivity(){
//
//    }
}
