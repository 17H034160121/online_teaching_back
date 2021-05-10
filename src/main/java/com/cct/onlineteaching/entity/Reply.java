package com.cct.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Reply {
    private int id;
    private String content;
    private String status;
    private int problemId;
    private int userId;
    private Timestamp createTime;
    private Timestamp updateTime;
}
