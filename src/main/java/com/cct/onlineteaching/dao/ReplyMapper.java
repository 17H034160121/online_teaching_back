package com.cct.onlineteaching.dao;

import com.cct.onlineteaching.entity.Reply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyMapper {
    void create(Reply reply);
    List<Reply> getReplyList(int problemId);
    void updateStatus(Reply reply);
    Reply getReplyWithReceived(int problemId);
}
