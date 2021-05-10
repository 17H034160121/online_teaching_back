package com.cct.onlineteaching.dao;

import com.cct.onlineteaching.entity.Evaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EvaluationMapper {
    void create(Evaluation evaluation);
    Object getAvgScore(int courseId);
    List<Evaluation> getEvaluationList(int courseId);
    Evaluation findByUserIdAnCourseId(@Param("userId") int userId, @Param("courseId") int courseId);
}
