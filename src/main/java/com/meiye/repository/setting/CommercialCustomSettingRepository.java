package com.meiye.repository.setting;

import com.meiye.model.setting.CommercialCustomSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 18:13 2018/8/26
 * @Modified By:
 */
@Repository
public interface CommercialCustomSettingRepository extends JpaRepository<CommercialCustomSettings,Long> {

    CommercialCustomSettings findByBrandIdentityAndShopIdentityAndSettingKey(Long brandIdentity,Long shopIdentity,String key);

}
