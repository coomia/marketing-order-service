package com.meiye.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.user.UserBo;
import com.meiye.model.ParentEntity;
import com.meiye.system.util.WebUtil;
import com.meiye.util.Constants;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2018/8/9 0009.
 */
@Data
public class ParentBo{
    private String creatorName;
    private Long creatorId;
    @JSONField(serialize=false)
    private String updatorName;
    @JSONField(serialize=false)
    private Long updatorId;
    private Timestamp serverCreateTime;
    private Timestamp serverUpdateTime;
    private Integer  statusFlag= Constants.DATA_ENABLE;

    public <T extends ParentEntity> T copyTo(Class<T> target){
        try {
            T entity = target.newInstance();
            BeanUtils.copyProperties(this, entity);
            UserBo userBo= WebUtil.getCurrentUser();
            if(entity.getCreatorId()==null)
                entity.setCreatorId(userBo==null?null:userBo.getId());
            if(entity.getServerCreateTime()==null)
                entity.setServerCreateTime(new Timestamp(System.currentTimeMillis()));
            entity.setCreatorName(userBo==null?null:userBo.getUsername());
            entity.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
            entity.setUpdatorId(userBo==null?null:userBo.getId());
            entity.setUpdatorName(userBo==null?null:userBo.getUsername());
            return entity;
        }catch (Exception exp){
            exp.printStackTrace();
            return null;
        }
    }
}
