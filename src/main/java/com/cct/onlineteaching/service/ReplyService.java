package com.cct.onlineteaching.service;

import com.cct.onlineteaching.config.CustomException;
import com.cct.onlineteaching.dao.ProblemMapper;
import com.cct.onlineteaching.dao.ReplyMapper;
import com.cct.onlineteaching.entity.Problem;
import com.cct.onlineteaching.entity.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplyService {
    private ReplyMapper replyMapper;
    private ProblemMapper problemMapper;

    @Autowired
    public ReplyService(ReplyMapper replyMapper, ProblemMapper problemMapper) {
        this.replyMapper = replyMapper;
        this.problemMapper = problemMapper;
    }

    @Transactional
    public void createReply(Reply reply) {
        if(this.hasReceived(reply.getProblemId()))
            throw new CustomException("该问题已解决，不可再提交回答");

        replyMapper.create(reply);
    }

    public List<Reply> getReplyList(int problemId) {
        return replyMapper.getReplyList(problemId);
    }

    @Transactional
    public void updateReply(int userId, Reply reply) {
        Problem problem = problemMapper.getProblem(reply.getProblemId());

        if(problem == null || problem.getUserId() != userId)
            throw new CustomException("无法执行采纳答案操作");

        if(this.hasReceived(reply.getProblemId()) || problem.getStatus().equals("1"))
            throw new CustomException("该问题已解决，不可再采纳答案");

        replyMapper.updateStatus(reply);
        problemMapper.updateStatus(reply.getProblemId());
    }

    private boolean hasReceived(int problemId) {
        return replyMapper.getReplyWithReceived(problemId) != null;
    }
}
