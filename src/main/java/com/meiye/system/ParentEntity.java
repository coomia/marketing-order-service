package com.meiye.system;

import lombok.*;

import java.util.Date;

/**
 * Created by jonyh on 08/08/18.
 */
@Data
@Getter
@Setter
public class ParentEntity {
    private Long updateId;
    private Date updateDatetime;
    private Long deleteId;
    private Date deleteDateTime;
    private Long entryId;
    private Date entryDatetime;
}
