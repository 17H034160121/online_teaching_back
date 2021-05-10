package com.cct.onlineteaching.service;

import com.github.pagehelper.PageHelper;
import com.cct.onlineteaching.dao.*;
import com.cct.onlineteaching.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonService {
    private LiveRoomMapper liveRoomMapper;
    private CourseMapper courseMapper;
    private ProblemMapper problemMapper;
    private StatisticsMapper statisticsMapper;
    private CategoryMapper categoryMapper;
    private ClassesMapper classesMapper;
    private UserMapper userMapper;

    @Autowired
    public CommonService(LiveRoomMapper liveRoomMapper,
                         CourseMapper courseMapper,
                         ProblemMapper problemMapper,
                         StatisticsMapper statisticsMapper,
                         CategoryMapper categoryMapper,
                         ClassesMapper classesMapper,
                         UserMapper userMapper) {
        this.liveRoomMapper = liveRoomMapper;
        this.courseMapper = courseMapper;
        this.problemMapper = problemMapper;
        this.statisticsMapper = statisticsMapper;
        this.categoryMapper = categoryMapper;
        this.classesMapper = classesMapper;
        this.userMapper = userMapper;
    }

    public Map<String, Object> getSearchData(String value) {
        Map<String, Object> map = new HashMap<>();
        int id = 0;

        if (value.matches("^?[0-9]+$")) {
            id = Integer.parseInt(value);
        }

        Course course = new Course();
        course.setName(value);

        LiveRoom liveRoom = liveRoomMapper.findById(id);

        PageHelper.startPage(1, 3);
        List<Course> courseList = courseMapper.getCourseList(course);

        PageHelper.startPage(1, 3);

        map.put("liveRoom", liveRoom);
        map.put("course", courseList);

        return map;
    }

    public Map<String, Object> getHomeData() {
        Map<String, Object> map = new HashMap<>();

        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setLiving("1");

        Course course = new Course();
        course.setPriority("1");

        PageHelper.startPage(1, 4);
        List<Course> bannerList = courseMapper.getCourseList(course);

        PageHelper.startPage(1, 4);
        List<LiveRoom> liveRoomList = liveRoomMapper.getLiveRoomList(liveRoom);

        PageHelper.startPage(1, 4);
        List<Course> courseList = courseMapper.getCourseList(null);

        PageHelper.startPage(1, 7);

        PageHelper.startPage(1, 7);
        List<Problem> problemList = problemMapper.getProblemList();

        map.put("banner", bannerList);
        map.put("liveRoom", liveRoomList);
        map.put("course", courseList);
        map.put("problem", problemList);

        return map;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void createActivityTask() {
        Calendar now = Calendar.getInstance();

        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int count = statisticsMapper.getActivityByPrevMonth();

        Activity activity = new Activity();
        activity.setYear(year);
        activity.setMonth(month);
        activity.setCount(count);

        statisticsMapper.createActivity(activity);
    }

    public Map<String, Object> getAdminHomeData() {
        List<Integer> todayData = statisticsMapper.getTodayData();
        List<Integer> totalData = statisticsMapper.getTotalData();

        PageHelper.startPage(1,12);
        List<Activity> activityData = statisticsMapper.getActivityData();

        Map<String, Object> map = new HashMap<>();
        map.put("todayData", todayData);
        map.put("totalData", totalData);
        map.put("activityData", activityData);

        return map;
    }

    public Map<String, Object> getLiveScopeData() {
        List<Category> categoryData = categoryMapper.getCategoryList();
        List<Integer> categoryIdData = categoryData.stream().map(Category::getId).collect(Collectors.toList());
        List<String> categoryNameData = categoryData.stream().map(Category::getName).collect(Collectors.toList());

        List<Classes> classesData = classesMapper.getClassesList();
        List<Integer> classesIdData = classesData.stream().map(Classes::getId).collect(Collectors.toList());
        List<String> classesNameData = classesData.stream().map(Classes::getName).collect(Collectors.toList());

        List<User> userData = userMapper.getUserList(new User());
        List<Integer> userIdData = userData.stream().map(User::getId).collect(Collectors.toList());
        List<String> userNameData = userData.stream().map(User::getUsername).collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("categoryIdData", categoryIdData);
        map.put("categoryNameData", categoryNameData);
        map.put("classesIdData", classesIdData);
        map.put("classesNameData", classesNameData);
        map.put("userIdData", userIdData);
        map.put("userNameData", userNameData);
        return map;
    }
}
