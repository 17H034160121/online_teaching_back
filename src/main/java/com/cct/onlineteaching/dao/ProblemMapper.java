package com.cct.onlineteaching.dao;

import com.cct.onlineteaching.entity.Problem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemMapper {
    void create(Problem problem);
    List<Problem> getProblemList();
    Problem getProblem(int id);
    void updateStatus(int id);
    List<Problem> getProblemListByUser(int userId);
}
