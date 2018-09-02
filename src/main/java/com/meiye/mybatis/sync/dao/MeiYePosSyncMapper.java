package com.meiye.mybatis.sync.dao;

import com.meiye.bo.config.AppConfigBo;
import com.meiye.bo.config.PosSyncConfigBo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/9/1 0001.
 */
@Mapper
public interface MeiYePosSyncMapper {
    @Select("<script>" +
            "select * from ${tableName} " +
            " where 1=1" +
            "<if test='isInit'>" +
            " and status_flag=1 " +
            "</if>" +
            "<if test='!isInit'>" +
            " and id>#{id} and IFNULL(server_update_time,server_create_time)>=#{serverUpdateTime} " +
            "</if> " +
            "<if test='syncConfigBo!=null'>" +
            " <if test='syncConfigBo.filterShopIdenty=Y'>" +
            "  and brand_identy=#{shopIdenty}" +
            "</if>" +
            "<if test='syncConfigBo.filterBrandIdenty=Y'>" +
            " and brand_identy = #{brandIdenty}" +
            "</if>" +
            "<if test='syncConfigBo.syncRecentDays>0'>" +
            " and IFNULL(server_update_time,server_create_time) > DATE_ADD(NOW(),INTERVAL #{syncConfigBo.syncRecentDays} DAY) " +
            "</if>" +
            "</if>" +
            "<if test='syncConfigBo==null'>" +
            " and brand_identy=#{shopIdenty} and brand_identy = #{brandIdenty} " +
            "</if>" +
            "order by  IFNULL(server_update_time,server_create_time),id" +
            "</script>")
    public List<HashMap<String,Object>> selectDataByTable(@Param("tableName") String tableName,@Param("brandIdenty") Long brandIdenty, @Param("shopIdenty") Long shopIdenty,@Param("isInit") boolean isInit, @Param("id") Long id, @Param("serverUpdateTime") Long serverUpdateTime,
                                                          @Param("syncConfigBo") PosSyncConfigBo syncConfigBo);

    @Select("select * from pos_sync_config")
    public List<PosSyncConfigBo> findAllConfigs();
}
