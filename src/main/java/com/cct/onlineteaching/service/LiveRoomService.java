package com.cct.onlineteaching.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cct.onlineteaching.config.CustomException;
import com.cct.onlineteaching.dao.ClassesMapper;
import com.cct.onlineteaching.dao.LiveRoomMapper;
import com.cct.onlineteaching.dao.MessageMapper;
import com.cct.onlineteaching.dao.UserMapper;
import com.cct.onlineteaching.entity.Classes;
import com.cct.onlineteaching.entity.LiveRoom;
import com.cct.onlineteaching.entity.Message;
import com.cct.onlineteaching.entity.User;
import com.cct.onlineteaching.param.QueryLiveRoom;
import com.cct.onlineteaching.param.UpdateLiveRoom;
import com.cct.onlineteaching.utils.QiniuyunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LiveRoomService {
    private LiveRoomMapper liveRoomMapper;
    private UserMapper userMapper;
    private QiniuyunUtil qiniuyunUtil;
    private PasswordEncoder passwordEncoder;
    private MessageMapper messageMapper;
    private ClassesMapper classesMapper;

    @Autowired
    public LiveRoomService(LiveRoomMapper liveRoomMapper,
                           QiniuyunUtil qiniuyunUtil,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           MessageMapper messageMapper,
                           ClassesMapper classesMapper) {
        this.liveRoomMapper = liveRoomMapper;
        this.qiniuyunUtil = qiniuyunUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.messageMapper = messageMapper;
        this.classesMapper = classesMapper;
    }

    public LiveRoom getLiveRoom(int userId) {
        return liveRoomMapper.findByUserId(userId);
    }

    @Transactional
    public void createLiveRoom(MultipartFile poster, String title, String banner, int userId, String scope) {
        String posterSrc = qiniuyunUtil.uploadImage(poster);

        LiveRoom liveRoom = liveRoomMapper.findByUserId(userId);
        if (liveRoom != null) throw new CustomException("???????????????????????????????????????????????????");

        LiveRoom newLiveRoom = new LiveRoom();
        newLiveRoom.setUserId(userId);
        newLiveRoom.setTitle(title);
        newLiveRoom.setBanner(banner);
        newLiveRoom.setPoster(posterSrc);
        newLiveRoom.setStatus("0");
        newLiveRoom.setLiveScope(scope);
        newLiveRoom.setCreateTime(new Timestamp(System.currentTimeMillis()));

        liveRoomMapper.create(newLiveRoom);
    }

    @Transactional
    public void updateLiveRoom(MultipartFile poster, int id, String title, String banner, int userId, String scope) {
        String posterSrc = null;
        if (poster != null) posterSrc = qiniuyunUtil.uploadImage(poster);

        LiveRoom newLiveRoom = new LiveRoom();

        LiveRoom liveRoom = liveRoomMapper.findById(id);
        String status = liveRoom.getStatus();
        if (status.equals("3")) throw new CustomException("???????????????????????????????????????????????????");
        if (status.equals("2")) newLiveRoom.setStatus("0");

        newLiveRoom.setId(id);
        newLiveRoom.setPoster(posterSrc);
        newLiveRoom.setTitle(title);
        newLiveRoom.setBanner(banner);
        newLiveRoom.setUserId(userId);
        newLiveRoom.setLiveScope(scope);
        newLiveRoom.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        liveRoomMapper.update(newLiveRoom);
    }

    public LiveRoom getLiveRoomByAdmin(int id) {
        return liveRoomMapper.findById(id);
    }

    public Map<String, Object> getLiveRoomList(QueryLiveRoom queryLiveRoomParams) {
        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setId(queryLiveRoomParams.getId());
        liveRoom.setStatus(queryLiveRoomParams.getStatus());
        liveRoom.setLiving(queryLiveRoomParams.getLiving());

        PageHelper.startPage(queryLiveRoomParams.getPageNum(), queryLiveRoomParams.getPageSize());
        List<LiveRoom> liveRoomList = liveRoomMapper.getLiveRoomList(liveRoom);

        PageInfo<LiveRoom> page = new PageInfo<>(liveRoomList);

        Map<String, Object> map = new HashMap<>();
        map.put("list", liveRoomList);
        map.put("total", page.getTotal());

        return map;
    }

    @Transactional
    public void updateLiveRoomStatus(UpdateLiveRoom updateLiveRoomParams) {
        int id = updateLiveRoomParams.getId();
        String status = updateLiveRoomParams.getStatus();

        LiveRoom liveRoomDetail = liveRoomMapper.findById(id);
        int userId = liveRoomDetail.getUserId();

        Message message = new Message();
        message.setUserId(userId);

        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setId(id);
        liveRoom.setStatus(status);

        String content = "";
        switch (status) {
            case "1":
                if(liveRoomDetail.getStatus().equals("0"))
                    content = "?????????????????????????????????????????????????????????????????????????????????????????????";
                if(liveRoomDetail.getStatus().equals("3"))
                    content = "???????????????????????????????????????????????????????????????????????????????????????";
                break;
            case "2":
                content = updateLiveRoomParams.getReason();
                break;
            case "3":
                content = "????????????????????????????????????????????????????????????????????????????????????????????????" +
                        "???<a href='Mailto:chaotingchen47@163.com'>????????????</a>???????????????";
        }

        message.setContent(content);
        messageMapper.create(message);
        liveRoomMapper.update(liveRoom);
    }

    public LiveRoom getLiveRoomById(int id) {
        LiveRoom liveRoom = liveRoomMapper.findById(id);

        if (liveRoom == null)
            throw new CustomException("???????????????????????????");

        if (liveRoom.getStatus().equals("0") || liveRoom.getStatus().equals("2"))
            throw new CustomException("????????????????????????");

        if (liveRoom.getStatus().equals("3"))
            throw new CustomException("????????????????????????");

        return liveRoom;
    }

    @Transactional
    public boolean onPublish(String name, String account, String password) {
        User user = userMapper.findByAccount(account);
        if (user == null || !passwordEncoder.matches(password, user.getPassword()))
            return false;

        LiveRoom liveRoom = liveRoomMapper.findByUserId(user.getId());
        if (liveRoom == null || liveRoom.getId() != Integer.parseInt(name) || !liveRoom.getStatus().equals("1"))
            return false;

        liveRoom.setLiveTime(new Timestamp(System.currentTimeMillis()));
        liveRoom.setLiving("1");
        liveRoomMapper.update(liveRoom);

        return true;
    }

    @Transactional
    public void onPublishDone(String name) {
        LiveRoom liveRoom = liveRoomMapper.findById(Integer.parseInt(name));

        if (liveRoom == null) throw new CustomException("???????????????????????????");

        liveRoom.setLiving("0");
        liveRoomMapper.update(liveRoom);
    }

    /**
     * ????????????????????????????????????
     * @param liveRoom ?????????
     * @param userId    ??????id
     * @return
     */
    public boolean checkAccess(LiveRoom liveRoom, int userId) {
        String[] keys = liveRoom.getLiveScope().split("-");
        String scope = keys[0];
        String scopeId = keys[1];

        //??????????????????????????????
        if(userId == liveRoom.getUserId()) return true;

        User user = userMapper.findById(userId);

        if("student".equals(scope)){
            return Integer.parseInt(scopeId) == userId;
        }else if("class".equals(scope)){
            return Integer.parseInt(scopeId) == user.getClassId();
        }else if("major".equals(scope)){
            Classes classes = classesMapper.findById(user.getClassId());
            return Integer.parseInt(scopeId) == classes.getCategoryId();
        }
        return false;
    }
}
