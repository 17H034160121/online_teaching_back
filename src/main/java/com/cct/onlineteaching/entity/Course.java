package com.cct.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Course {
    private int id;
    private String name;
    private String introduction;
    private String poster;
    private String link;
    private String priority;
    private int userId;
    private int categoryId;
    private String categoryName;
    private Timestamp createTime;
    private Timestamp updateTime;
}
