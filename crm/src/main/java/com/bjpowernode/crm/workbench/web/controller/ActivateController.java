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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

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

//    @RequestMapping("workbench/activity/importActivity.do")
//    @ResponseBody
//    public Object importAvtivity(MultipartFile activityFile,HttpSession session){
//        User user =(User) session.getAttribute(Contants.SESSION_USER);
//        ReturnObject returnObject = new ReturnObject();
//        try {
//            //吧Excel文件写入到磁盘
//            String originalFilename = activityFile.getOriginalFilename();
//            File file = new File(".\\resource\\",originalFilename);
//            activityFile.transferTo(file);
//
//            //解析文件中数据并封装成activityList
//            FileInputStream is = new FileInputStream(".\\resource\\" + originalFilename);
//            HSSFWorkbook wb = new HSSFWorkbook(is);
//            HSSFSheet sheet = wb.getSheetAt(0);
//            List<Activity> activityList = new ArrayList<>();
//
//            HSSFRow row = null;
//            HSSFCell cell = null;
//            Activity activity = null;
//            for (int i = 0; i <= sheet.getLastRowNum() ; i++) {
//                //先行遍历再列遍历
//                row = sheet.getRow(i);
//                activity = new Activity();
//                activity.setId(UUIDUtils.getUUID());
//                activity.setOwner(user.getId());
//                activity.setCreateBy(DateUtils.formateDateTime(new Date()));
//                activity.setCreateBy(user.getId());
//                for (int j = 0; j <row.getLastCellNum() ; j++) {
//                    cell = row.getCell(j);
//                    String cellValue = HSSFUtils.getCellValueForStr(cell);
//                    switch (j){
//                        case 0:activity.setName(cellValue);
//                        case 1:activity.setStartDate(cellValue);
//                        case 2:activity.setEndDate(cellValue);
//                        case 3:activity.setCost(cellValue);
//                        case 4:activity.setDescription(cellValue);
//                    }
//                }
//                activityList.add(activity);
//            }
//            int res = activityService.saveActivityByList(activityList);
//            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
//            returnObject.setRetData(res);
//        }catch (Exception e){
//            e.printStackTrace();
//            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
//            returnObject.setMessage("系统繁忙，请稍后重试...");
//        }
//        return returnObject;
//    }

    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject = new ReturnObject();
        try {
            InputStream is = activityFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(is);
            // 根据wb获取HSSFSheet对象，封装了一页的所有信息
            HSSFSheet sheet = wb.getSheetAt(0); // 页的下标，下标从0开始，依次增加
            // 根据sheet获取HSSFRow对象，封装了一行的所有信息
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            for(int i = 1; i <= sheet.getLastRowNum(); i++) { // sheet.getLastRowNum()：最后一行的下标
                row = sheet.getRow(i); // 行的下标，下标从0开始，依次增加
                activity = new Activity();
                // 补充部分参数
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());
//                for(int j = 0; j < row.getLastCellNum(); j++) { // row.getLastCellNum():最后一列的下标+1
//                    // 根据row获取HSSFCell对象，封装了一列的所有信息
//                    cell=row.getCell(j); // 列的下标，下标从0开始，依次增加
//                    // 获取列中的数据
//                    String cellValue = HSSFUtils.getCellValueForStr(cell);
//                    if(j == 0) {
//                        activity.setName(cellValue);
//                    } else if(j == 1){
//                        activity.setStartDate(cellValue);
//                    } else if(j == 2){
//                        activity.setEndDate(cellValue);
//                    } else if(j == 3){
//                        activity.setCost(cellValue);
//                    } else if(j == 4){
//                        activity.setDescription(cellValue);
//                    }
//                }
                for (int j = 0; j <row.getLastCellNum() ; j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    switch (j){
                        case 0:activity.setName(cellValue);
                        case 1:activity.setStartDate(cellValue);
                        case 2:activity.setEndDate(cellValue);
                        case 3:activity.setCost(cellValue);
                        case 4:activity.setDescription(cellValue);
                    }
                }
                //每一行中所有列都封装完成之后，把activity保存到list中
                activityList.add(activity);
            }
            // 调用service层方法，保存市场活动
            int res = activityService.saveActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(res);
        } catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }
}
