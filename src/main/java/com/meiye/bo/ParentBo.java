package com.meiye.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.user.UserBo;
import com.meiye.model.ParentEntity;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/9 0009.
 */
@Data
public class ParentBo{
    @JSONField(serialize=false)
    private String creatorName;
    @JSONField(serialize=false)
    private Long creatorId;
    @JSONField(serialize=false)
    private String updatorName;
    @JSONField(serialize=false)
    private Long updatorId;
    @JSONField(serialize=false)
    private java.util.Date serverCreateTime;
    @JSONField(serialize=false)
    private java.util.Date serverUpdateTime;
    private Long  statusFlag;

    public <T extends ParentEntity> T copyTo(Class<T> target){
        try {
            T entity = target.newInstance();
            BeanUtils.copyProperties(this, entity);
            UserBo userBo= WebUtil.getCurrentUser();
            Date now=new Date();
            if(entity.getCreatorId()==null)
                entity.setCreatorId(userBo==null?null:userBo.getId());
            if(entity.getServerCreateTime()==null)
                entity.setServerCreateTime(now);
            entity.setCreatorName(userBo==null?null:userBo.getUsername());
            entity.setServerUpdateTime(now);
            entity.setUpdatorId(userBo==null?null:userBo.getId());
            entity.setUpdatorName(userBo==null?null:userBo.getUsername());
            return entity;
        }catch (Exception exp){
            exp.printStackTrace();
            return null;
        }
    }
}
