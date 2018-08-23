package com.meiye.service.store.impl;

import com.meiye.bo.store.BrandBo;
import com.meiye.bo.store.CommercialBo;
import com.meiye.bo.store.StoreBo;
import com.meiye.model.store.Brand;
import com.meiye.model.store.Commercial;
import com.meiye.repository.store.BrandRepository;
import com.meiye.repository.store.StoreRepository;
import com.meiye.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/8/23 0023.
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    StoreRepository storeRepository;

    @Override
    public CommercialBo findStoreById(Long storeId){
        if(storeId!=null) {
            Commercial store =storeRepository.findById(storeId).get();
            if(store!=null) {
                CommercialBo commercialBo = store.copyTo(CommercialBo.class);
                if(commercialBo.getBrandId()!=null) {
                    Brand brand = brandRepository.findById(commercialBo.getBrandId()).get();
                    if(brand!=null)
                        commercialBo.setBrandBo(brand.copyTo(BrandBo.class));
                }
                return commercialBo;
            }else
                return  null;
        }else
            return null;

    }
}
