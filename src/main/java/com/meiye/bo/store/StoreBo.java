package com.meiye.bo.store;

import com.meiye.bo.ParentBo;
import lombok.Data;

/**
 * Created by jonyh on 08/17/18.
 */
@Data
public class StoreBo extends ParentBo {
    private Long id=1l;
    private String name="test 门店";
}
