package com.meiye.mybatis.sync.dao;

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
//            "<if test='!isInit'>" +
            " and id>#{id} and IFNULL(server_update_time,server_create_time)>=FROM_UNIXTIME(#{serverUpdateTime}) " +
//            "</if> " +
            "<if test='syncConfigBo!=null'>" +
            " <if test='syncConfigBo.filterShopIdenty!=\"N\"'>" +
            "  and shop_identy=#{shopIdenty}" +
            "</if>" +
            "<if test='syncConfigBo.filterBrandIdenty!=\"N\"'>" +
            " and brand_identy = #{brandIdenty}" +
            "</if>" +
            "<if test='syncConfigBo.syncRecentDays!=0'>" +
            " and IFNULL(server_update_time,server_create_time) > DATE_ADD(NOW(),INTERVAL #{syncConfigBo.syncRecentDays} DAY) " +
            "</if>" +
            "</if>" +
            "<if test='syncConfigBo==null'>" +
            " and shop_identy=#{shopIdenty} and brand_identy = #{brandIdenty} " +
            "</if>" +
            "order by  IFNULL(server_update_time,server_create_time),id" +
            "</script>")
    public List<HashMap<String,Object>> selectDataByTable(@Param("tableName") String tableName,@Param("brandIdenty") Long brandIdenty, @Param("shopIdenty") Long shopIdenty,@Param("isInit") boolean isInit, @Param("id") Long id, @Param("serverUpdateTime") Long serverUpdateTime,
                                                          @Param("syncConfigBo") PosSyncConfigBo syncConfigBo);

    @Select("select * from pos_sync_config")
    public List<PosSyncConfigBo> findAllConfigs();

    @Select("SELECT a.`device_mac` device_id,b.`commercial_id` shop_id,c.`id` brand_id,c.`name` brand_name,b.`commercial_name` shop_name,b.`commercial_phone` shop_phone,\n" +
            "b.`commercial_adress` shop_address,b.`commercial_logo` shop_logo,b.`longitude`,b.`latitude`,b.open_time \n" +
            "FROM `shop_device` a,`commercial` b,`brand` c\n" +
            "WHERE a.`shop_identy`=b.`commercial_id` AND a.`brand_identy`=c.`id`\n" +
            "AND a.`device_mac`=#{deviceId}")
    public HashMap<String,Object> getShopInfoByDeviceMac(@Param("deviceId") String deviceId);
}
