package com.cct.onlineteaching.service;


import com.cct.onlineteaching.config.CustomException;
import com.cct.onlineteaching.dao.ClassesMapper;
import com.cct.onlineteaching.entity.Classes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ClassesService {
    private ClassesMapper classesMapper;

    @Autowired
    public ClassesService(ClassesMapper classesMapper) {
        this.classesMapper = classesMapper;
    }

    @Transactional
    public void createClasses(Classes classesParams) {
        Classes newClasses = new Classes();

        if (classesParams.getName() == null || classesParams.getName().isEmpty())
            throw new CustomException("班级名称不能为空");

        Classes classes = classesMapper.findByName(classesParams.getName());
        if(classes != null)
            throw new CustomException("该班级已存在，请修改后重试");

        newClasses.setName(classesParams.getName());
        newClasses.setCreateTime(new Timestamp(System.currentTimeMillis()));
        newClasses.setCategoryId(classesParams.getCategoryId());
        classesMapper.create(newClasses);
    }

    public List<Classes> getClassesList() {
        return classesMapper.getClassesList();
    }

    @Transactional
    public void deleteClasses(int id) {
        classesMapper.delete(id);
    }

    @Transactional
    public void updateClasses(int id, Classes classesParams) {
        Classes newClasses = new Classes();

        if (classesParams.getName() == null || classesParams.getName().isEmpty())
            throw new CustomException("班级名称不能为空");
        if(this.existClassesName(id, classesParams.getName()))
            throw new CustomException("该班级已存在，请修改后重试");

        newClasses.setId(id);
        newClasses.setName(classesParams.getName());
        newClasses.setCreateTime(classesParams.getCreateTime());
        newClasses.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        classesMapper.update(newClasses);
    }

    private boolean existClassesName(int id, String classesName) {
        Classes classesByName = classesMapper.findByName(classesName);

        if(classesByName == null) {
            return false;
        } else {
            Classes classesById = classesMapper.findById(id);
            return !classesName.equals(classesById.getName());
        }
    }
}
