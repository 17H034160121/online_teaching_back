package com.cct.onlineteaching.controller;

import com.cct.onlineteaching.service.CommonService;
import com.cct.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CommonController {
    private CommonService commonService;

    @Autowired
    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/search")
    public Map<String, Object> getSearchData(@RequestParam("value") String value) {
        Map<String, Object> searchData = commonService.getSearchData(value);
        return ResponseUtil.success(searchData);
    }

    @GetMapping("/home")
    public Map<String, Object> getHomeData() {
        Map<String, Object> homeData = commonService.getHomeData();
        return ResponseUtil.success(homeData);
    }

    @GetMapping("/admin/home")
    public Map<String, Object> getAdminHomeData() {
        Map<String, Object> adminHomeData = commonService.getAdminHomeData();
        return ResponseUtil.success(adminHomeData);
    }

    @GetMapping("/scope")
    public Map<String, Object> getLiveScopeData() {
        Map<String, Object> liveScopeData = commonService.getLiveScopeData();
        return ResponseUtil.success(liveScopeData);
    }
}
