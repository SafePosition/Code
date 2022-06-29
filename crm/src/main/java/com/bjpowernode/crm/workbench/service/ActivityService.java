package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;


/**
 * @BelongsProject: Code
 * @BelongsPackage: com.bjpowernode.crm.workbench.service
 * @Author: dushicheng
 * @CreateTime: 2022-06-16  23:13
 * @Description: TODO
 * @Version: 1.0
 */

public interface ActivityService {
    //添加记录
    int saveCreateActivity(Activity activity);
    //查询符合的列表
    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);
    //符合查询的条数
    int queryCountOfActivityByCondition(Map<String,Object> map);
    //根据ids批量删除数据
    int deleteActivityByIds(String[] ids);
    //通过id查询市场活动
    Activity queryActivityById(String id);
    //更新市场活动信息
    int saveEditActivity(Activity activity);
    //查询所有市场活动
    List<Activity> queryAllActivitys();
    //根據上傳文件向表中插入數據
//    int saveActivityByList(List<Activity> activityList);
}
