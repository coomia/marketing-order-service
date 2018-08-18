package com.meiye.model;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/8/9 0009.
 */
@Data
@MappedSuperclass
public class ParentEntity{
    @Column(updatable = false)
    private String creatorName;
    @Column(updatable = false)
    private Long creatorId;
    private String updatorName;
    private Long updatorId;
    @Column(updatable = false)
    private Timestamp serverCreateTime;
    private Timestamp serverUpdateTime;
    private Integer statusFlag=1;

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
