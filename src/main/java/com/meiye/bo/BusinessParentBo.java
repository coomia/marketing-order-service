package com.meiye.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.meiye.bo.user.UserBo;
import com.meiye.model.BusinessParentEntity;
import com.meiye.system.util.WebUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by Administrator on 2018/8/18 0018.
 */
@Data
public class BusinessParentBo extends ParentBo {
    protected Long brandIdenty=WebUtil.getCurrentBrandId();
    protected Long shopIdenty=WebUtil.getCurrentStoreId();
}
