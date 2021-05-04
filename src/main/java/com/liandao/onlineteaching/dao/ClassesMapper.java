package com.liandao.onlineteaching.dao;

import com.liandao.onlineteaching.entity.Classes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassesMapper {

    void create(Classes classes);
    List<Classes> getClassesList();
    void delete(int id);
    void update(Classes classes);
    Classes findById(int id);
    Classes findByName(String name);

}
