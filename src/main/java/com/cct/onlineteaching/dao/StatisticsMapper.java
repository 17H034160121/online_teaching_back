package com.cct.onlineteaching.dao;

import com.cct.onlineteaching.entity.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatisticsMapper {
    List<Integer> getTodayData();
    List<Integer> getTotalData();
    void createActivity(Activity activity);
    List<Activity> getActivityData();
    int getActivityByPrevMonth();
}
