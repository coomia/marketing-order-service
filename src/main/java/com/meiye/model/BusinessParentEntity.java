package com.meiye.model;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by Administrator on 2018/8/18 0018.
 */
@Data
@MappedSuperclass
public class BusinessParentEntity extends ParentEntity {
    protected Long brandIdenty;
    protected Long shopIdenty;
}
