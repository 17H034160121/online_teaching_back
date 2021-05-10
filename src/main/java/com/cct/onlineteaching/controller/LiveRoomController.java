package com.cct.onlineteaching.controller;

import com.cct.onlineteaching.entity.LiveRoom;
import com.cct.onlineteaching.param.QueryLiveRoom;
import com.cct.onlineteaching.param.UpdateLiveRoom;
import com.cct.onlineteaching.service.LiveRoomService;
import com.cct.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LiveRoomController {
    private LiveRoomService liveRoomService;

    @Autowired
    public LiveRoomController(LiveRoomService liveRoomService) {
        this.liveRoomService = liveRoomService;
    }

    @GetMapping("/liveRoom")
    public Map<String, Object> getLiveRoom(@RequestParam("uid") int userId) {
        LiveRoom liveRoom = liveRoomService.getLiveRoom(userId);
        return ResponseUtil.success(liveRoom);
    }

    @PostMapping("/liveRoom")
    public Map<String, Object> createLiveRoom(@RequestParam("poster") MultipartFile poster,
                                              @RequestParam("uid") int userId,
                                              @RequestParam("title") String title,
                                              @RequestParam("banner") String banner,
                                              @RequestParam("scope") String scope) {
        liveRoomService.createLiveRoom(poster, title, banner, userId, scope);
        return ResponseUtil.success(null);
    }

    @PatchMapping("/liveRoom")
    public Map<String, Object> updateLiveRoom(@RequestParam(value = "poster", required = false) MultipartFile poster,
                                              @RequestParam("uid") int userId,
                                              @RequestParam("title") String title,
                                              @RequestParam("banner") String banner,
                                              @RequestParam("id") int id,
                                              @RequestParam("scope") String scope) {
        liveRoomService.updateLiveRoom(poster, id, title, banner, userId, scope);
        return ResponseUtil.success(null);
    }

    @GetMapping("/liveRoom/{id}")
    public Map<String, Object> getLiveRoomById(@PathVariable("id") int id, @RequestParam("uid") int uid) {
        LiveRoom liveRoom = liveRoomService.getLiveRoomById(id);
        boolean check = liveRoomService.checkAccess(liveRoom, uid);
        if(check) return ResponseUtil.success(liveRoom);
        return ResponseUtil.fail("直播限制开放");
    }

    @GetMapping("/admin/liveRoom/{id}")
    public Map<String, Object> getLiveRoomByAdmin(@PathVariable("id") int id) {
        LiveRoom liveRoom = liveRoomService.getLiveRoomByAdmin(id);
        return ResponseUtil.success(liveRoom);
    }

    @GetMapping("/liveRoom/list")
    public Map<String, Object> getLiveRoomListWithLiving(@ModelAttribute QueryLiveRoom queryLiveRoomParams) {
        queryLiveRoomParams.setLiving("1");
        Map<String, Object> liveRoomData = liveRoomService.getLiveRoomList(queryLiveRoomParams);
        return ResponseUtil.success(liveRoomData);
    }

    @GetMapping("/admin/liveRoom/list")
    public Map<String, Object> getLiveRoomListOfAdmin(@ModelAttribute QueryLiveRoom queryLiveRoomParams) {
        Map<String, Object> liveRoomData = liveRoomService.getLiveRoomList(queryLiveRoomParams);
        return ResponseUtil.success(liveRoomData);
    }

    @PatchMapping("/admin/liveRoom/status")
    public Map<String, Object> updateLiveRoomStatus(@RequestBody UpdateLiveRoom updateLiveRoomParams) {
        liveRoomService.updateLiveRoomStatus(updateLiveRoomParams);
        return ResponseUtil.success(null);
    }

    /**
     * 直播鉴权方法，确认推流用户为实际用户
     * @param request
     * @param response
     */
    @PostMapping("/onPublish")
    public void onPublish(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        boolean canPublish = liveRoomService.onPublish(name, account, password);
        response.setStatus(canPublish ? 200 : 500);
    }

    @PostMapping("/onPublishDone")
    public void onPublishDone(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");

        liveRoomService.onPublishDone(name);
        response.setStatus(200);
    }
}
