package com.cct.onlineteaching.dao;

import com.cct.onlineteaching.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    void create(Category category);
    List<Category> getCategoryList();
    void delete(int id);
    void update(Category category);
    Category findById(int id);
    Category findByName(String name);
}
