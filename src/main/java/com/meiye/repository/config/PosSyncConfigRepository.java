package com.meiye.repository.config;

import  com.meiye.model.config.PosSyncConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * table name:  pos_sync_config
 * author name: ryne
 * create time: 2018-09-02 01:49:11
 */ 
@Repository
public interface PosSyncConfigRepository extends JpaRepository<PosSyncConfig,Long>{

}

