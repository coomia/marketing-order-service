package com.meiye.bo;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by Administrator on 2018/8/9 0009.
 */
@Data
public class ParentBo{
    private String creatorName;
    private Long creatorId;
    private String updatorName;
    private Long updatorId;
    private java.util.Date serverCreateTime;
    private java.util.Date serverUpdateTime;
    private Integer statusFlag;

    public <T> T copyTo(Class<T> target){
        try {
            T entity = target.newInstance();
            BeanUtils.copyProperties(this, entity);
            return entity;
        }catch (Exception exp){
            exp.printStackTrace();
            return null;
        }
    }
}
