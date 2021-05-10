package com.cct.onlineteaching.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Classes {
    private int id;
    private String name;
    private int studentNum;
    private int categoryId;
    private Timestamp createTime;
    private Timestamp updateTime;
}
