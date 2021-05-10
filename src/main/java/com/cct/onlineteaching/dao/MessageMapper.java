package com.cct.onlineteaching.dao;

import com.cct.onlineteaching.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    void create(Message message);
    List<Message> getMessageList(int userId);
    void update(Message message);
    Message findByIdAndUserId(@Param("userId") int userId, @Param("id") int id);
}
