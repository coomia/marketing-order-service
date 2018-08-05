package com.meiye.bo.user;

import lombok.Data;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
@Data
public class UserBo {
    private Long id;
    private String name;
    private Long updateId;
    private Date updateDatetime;
    private Long deleteId;
    private Date deleteDateTime;
    private Long entryId;
    private Date entryDatetime;
    private Long version;
}
