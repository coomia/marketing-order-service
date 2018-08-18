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
public class SimpleParentEntity {
    @Column(updatable = false)
    private Long creatorId;
    private Long updatorId;
    @Column(updatable = false)
    private java.util.Date serverCreateTime;
    private java.util.Date serverUpdateTime;

    public <T> T copyTo(Class<T> target){
        try {
            T entityBo = target.newInstance();
            BeanUtils.copyProperties(this, entityBo);
            return entityBo;
        }catch (Exception exp){
            exp.printStackTrace();
            return null;
        }
    }
}
