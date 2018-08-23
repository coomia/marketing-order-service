package com.meiye.service.store;

import com.meiye.bo.store.CommercialBo;

/**
 * Created by Administrator on 2018/8/23 0023.
 */
public interface StoreService {
    CommercialBo findStoreById(Long storeId);
}
