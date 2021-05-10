package com.cct.onlineteaching.controller;


import com.cct.onlineteaching.entity.Classes;
import com.cct.onlineteaching.service.ClassesService;
import com.cct.onlineteaching.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ClassesController {
    private ClassesService classesService;

    @Autowired
    public ClassesController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @PostMapping("/admin/classes")
    public Map<String, Object> createClass(@RequestBody Classes classes) {
        classesService.createClasses(classes);
        return ResponseUtil.success(null);
    }

    @GetMapping("/classes/list")
    public Map<String, Object> getClassList() {
        List<Classes> classList = classesService.getClassesList();
        return ResponseUtil.success(classList);
    }

    @DeleteMapping("/admin/classes/{id}")
    public Map<String, Object> deleteClass(@PathVariable("id") int id ) {
        classesService.deleteClasses(id);
        return ResponseUtil.success(null);
    }

    @PatchMapping("/admin/classes/{id}")
    public Map<String, Object> updateClass(@PathVariable("id") int id, @RequestBody Classes classes) {
        classesService.updateClasses(id, classes);
        return ResponseUtil.success(null);
    }
}
