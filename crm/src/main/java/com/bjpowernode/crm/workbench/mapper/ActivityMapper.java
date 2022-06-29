package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Thu Jun 16 22:45:27 CST 2022
     */
    int deleteByPrimaryKey(String id);



    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Thu Jun 16 22:45:27 CST 2022
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Thu Jun 16 22:45:27 CST 2022
     */
    Activity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Thu Jun 16 22:45:27 CST 2022
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Thu Jun 16 22:45:27 CST 2022
     */
    int updateByPrimaryKey(Activity record);

    /**
     *保存创建的市场活动
     */
    int insertActivity(Activity activity);

    //根据条件分页查询市场活动 的列表
    List<Activity> selectActivityByConditionForPage(Map<String, Object> map);

    //查询符合条件的活动数目
    int selectCountOfActivityByCondition(Map<String, Object> map);

    //批量删除市场活动
    int deleteActivityByIds(String[] ids);

    //根据id查询市场活动
    Activity selectActivityById(String id);

    //更新市场活动
    int updateActivity(Activity activity);

    //查询所有活动
    List<Activity> selectAllActivitys();

    //根据前台的文件向表内插入数据
   // int insertActivityByList(List<Activity> activityList);
}