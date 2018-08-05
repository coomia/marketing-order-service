package com.meiye.repository.config;

import com.meiye.model.config.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2018/8/5 0005.
 */
public interface AppConfigRepository extends JpaRepository<AppConfig,Long> {
}
