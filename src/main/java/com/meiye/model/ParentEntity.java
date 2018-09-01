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
    protected String creatorName;
    @Column(updatable = false)
    protected Long creatorId;
    protected String updatorName;
    protected Long updatorId;
    @Column(updatable = false)
    protected Timestamp serverCreateTime;
    protected Timestamp serverUpdateTime;
    protected Integer statusFlag=1;

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
