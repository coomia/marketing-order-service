package com.meiye.repository.store;

import  com.meiye.model.store.ShopDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * table name:  shop_device
 * author name: ryne
 * create time: 2018-09-05 21:24:59
 */ 
@Repository
public interface ShopDeviceRepository extends JpaRepository<ShopDevice,Long>{

}

