package com.meiye.service.posApi.impl;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.trade.*;
import com.meiye.bo.trade.OrderDto.*;
import com.meiye.exception.BusinessException;
import com.meiye.model.part.DishShop;
import com.meiye.model.setting.Tables;
import com.meiye.model.trade.*;
import com.meiye.repository.part.DishShopRepository;
import com.meiye.repository.setting.TablesRepository;
import com.meiye.repository.trade.*;
import com.meiye.service.posApi.OrderService;
import com.meiye.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 23:27 2018/9/1
 * @Modified By:
 */
@Service
public class OrderServiceImpl implements OrderService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CustomerCardTimeRepository customerCardTimeRepository;
    @Autowired
    TradeCustomerRepository tradeCustomerRepository;
    @Autowired
    TradeItemPropertyRepository tradeItemPropertyRepository;
    @Autowired
    TradeItemRepository tradeItemRepository;
    @Autowired
    TradePrivilegeRepository tradePrivilegeRepository;
    @Autowired
    TradeRepository tradeRepository;
    @Autowired
    TradeTableRepository tradeTableRepository;
    @Autowired
    TradeUserRepository tradeUserRepository;
    @Autowired
    TablesRepository tablesRepository;
    @Autowired
    DishShopRepository dishShopRepository;

    @Override
    public void testLog() {
        logger.info("order service info ---");
        logger.error("order service error ---");
    }

    /**
     * 修改订单接口 - Ryne 2018/09/02
     * @param modifyOrderRequestDto
     * @return
     */
    @Override
    @Transactional(rollbackOn = {Exception.class})
    public ModifyOrderResponseDto modifyOrderData(ModifyOrderRequestDto modifyOrderRequestDto) {
        ModifyOrderResponseDto modifyOrderResponseDto = new ModifyOrderResponseDto();
        TradeRequestDto tradeRequestDto = modifyOrderRequestDto.getContent();
        TradeBo tradeBo = tradeRequestDto.getTradeRequest();
        InventoryRequestDto inventoryRequest = tradeRequestDto.getInventoryRequest();
        if(Objects.isNull(tradeBo) || Objects.isNull(inventoryRequest)){
            logger.error("改单接口-订单数据或库存数据为空！");
            throw new BusinessException("改单接口-订单数据或库存数据为空！");
        }
        List<CustomerCardTimeBo> customerCardTimes = tradeBo.getCustomerCardTimes();
        List<TradeCustomerBo> tradeCustomers = tradeBo.getTradeCustomers();
        List<TradeItemPropertyBo> tradeItemProperties = tradeBo.getTradeItemProperties();
        List<TradeItemBo> tradeItems = tradeBo.getTradeItems();
        List<TradePrivilegeBo> tradePrivileges = tradeBo.getTradePrivileges();
        List<TradeUserBo> tradeUsers = tradeBo.getTradeUsers();
        List<TradeTableBo> tradeTables = tradeBo.getTradeTables();
        Trade trade = tradeBo.copyTo(Trade.class);

        //1.修改 交易记录主单 数据
        logger.info("改单接口-修改交易记录主单开始");
        logger.info("改单接口-交易记录主单："+ JSON.toJSON(trade).toString());
        trade = tradeRepository.save(trade);
        modifyOrderResponseDto.setTrade(trade);
        logger.info("改单接口-修改交易记录主单结束");

        //2.修改 交易明细 数据
        logger.info("改单接口-修改交易明细开始");
        logger.info("改单接口-交易记录主单："+ JSON.toJSON(tradeItems).toString());
        List<TradeItem> tradeItemList = this.modifyTradeItem(tradeItems);
        modifyOrderResponseDto.setTradeItems(tradeItemList);
        logger.info("改单接口-修改交易明细结束");

        //3.修改 交易的顾客信息 数据
        logger.info("改单接口-修改交易的顾客信息开始");
        logger.info("改单接口-交易的顾客信息："+ JSON.toJSON(tradeCustomers).toString());
        List<TradeCustomer> tradeCustomerList = this.modifyTradeCustomer(tradeCustomers);
        modifyOrderResponseDto.setTradeCustomers(tradeCustomerList);
        logger.info("改单接口-修改交易的顾客信息结束");

        //4.修改 交易桌台 数据
        logger.info("改单接口-修改交易桌台开始");
        logger.info("改单接口-交易桌台信息："+ JSON.toJSON(tradeTables).toString());
        List<TradeTable> tradeTableList = this.modifyTradeTable(tradeTables);
        modifyOrderResponseDto.setTables(tradeTableList);
        logger.info("改单接口-修改交易桌台结束");

        //5.修改 优惠信息 数据
        logger.info("改单接口-修改优惠信息开始");
        logger.info("改单接口-优惠信息："+ JSON.toJSON(tradePrivileges).toString());
        List<TradePrivilege> tradePrivilegeList = this.modifyTradePrivileges(tradePrivileges);
        modifyOrderResponseDto.setTradePrivileges(tradePrivilegeList);
        logger.info("改单接口-修改优惠信息结束");

        //6.修改 交易明细特征 数据
        logger.info("改单接口-修改交易明细特征开始");
        logger.info("改单接口-交易明细特征："+ JSON.toJSON(tradeItemProperties).toString());
        List<TradeItemProperty> tradeItemPropertiesList = this.modifyTradeItemProperties(tradeItemProperties);
        modifyOrderResponseDto.setTradeItemProperties(tradeItemPropertiesList);
        logger.info("改单接口-修改交易明细特征结束");

        //7.修改 订单用户关联表 数据
        logger.info("改单接口-修改订单用户关联表开始");
        logger.info("改单接口-订单用户关联表："+ JSON.toJSON(tradeUsers).toString());
        List<TradeUser> tradeUserList = this.modifyTradeUser(tradeUsers);
        modifyOrderResponseDto.setTradeUsers(tradeUserList);
        logger.info("改单接口-修改订单用户关联表结束");

        //8.修改 会员次卡表 数据
        logger.info("改单接口-修改会员次卡表开始");
        logger.info("改单接口-修改会员次卡表："+ JSON.toJSON(customerCardTimes).toString());
        List<CustomerCardTime> customerCardTimeList = this.modifyCustomerCardTimeBo(customerCardTimes);
        modifyOrderResponseDto.setCustomerCardTimes(customerCardTimeList);
        logger.info("改单接口-修改会员次卡表结束");

        //9.修改 商品 库存信息
        List<InventoryItemsDto> deductInventoryItems = inventoryRequest.getDeductInventoryItems();
        this.modifyInventory(deductInventoryItems,false);
        List<InventoryItemsDto> returnInventoryItems = inventoryRequest.getReturnInventoryItems();
       this.modifyInventory(returnInventoryItems,true);
        return modifyOrderResponseDto;
    }

    /**
     * 下单订单接口 - Ryne 2018/09/02
     * @param addOrderRequestDto
     * @return
     */
    @Override
    @Transactional(rollbackOn = {Exception.class})
    public ModifyOrderResponseDto addOrderData(AddOrderRequestDto addOrderRequestDto) {
        ModifyOrderResponseDto modifyOrderResponseDto = new ModifyOrderResponseDto();
        TradeBo tradeBo = addOrderRequestDto.getContent();
        if(Objects.isNull(tradeBo)){
            logger.error("下单接口-订单数据为空！");
            throw new BusinessException("下单接口-下单数据为空！");
        }
        List<CustomerCardTimeBo> customerCardTimes = tradeBo.getCustomerCardTimes();
        List<TradeCustomerBo> tradeCustomers = tradeBo.getTradeCustomers();
        List<TradeItemPropertyBo> tradeItemProperties = tradeBo.getTradeItemProperties();
        List<TradeItemBo> tradeItems = tradeBo.getTradeItems();
        List<TradePrivilegeBo> tradePrivileges = tradeBo.getTradePrivileges();
        List<TradeUserBo> tradeUsers = tradeBo.getTradeUsers();
        List<TradeTableBo> tradeTables = tradeBo.getTradeTables();
        Trade trade = tradeBo.copyTo(Trade.class);
        //开单接口该数据为null需要服务器创建
        if(Objects.isNull(trade.getBizDate())){
            trade.setBizDate(new Date());
        }

        //1.新增 交易记录主单 数据
        logger.info("下单接口-新增交易记录主单开始");
        logger.info("下单接口-交易记录主单："+ JSON.toJSON(trade).toString());
        trade = tradeRepository.save(trade);
        modifyOrderResponseDto.setTrade(trade);
        logger.info("下单接口-新增交易记录主单结束");

        //2.新增 交易明细 数据
        logger.info("下单接口-新增交易明细开始");
        logger.info("下单接口-交易记录主单："+ JSON.toJSON(tradeItems).toString());
        List<TradeItem> tradeItemList = this.modifyTradeItem(tradeItems);
        modifyOrderResponseDto.setTradeItems(tradeItemList);
        logger.info("下单接口-新增交易明细结束");

        //3.新增 交易的顾客信息 数据
        logger.info("下单接口-新增交易的顾客信息开始");
        logger.info("下单接口-交易的顾客信息："+ JSON.toJSON(tradeCustomers).toString());
        List<TradeCustomer> tradeCustomerList = this.modifyTradeCustomer(tradeCustomers);
        modifyOrderResponseDto.setTradeCustomers(tradeCustomerList);
        logger.info("下单接口-新增交易的顾客信息结束");

        //4.新增 交易桌台 数据
        logger.info("下单接口-新增交易桌台开始");
        logger.info("下单接口-交易桌台信息："+ JSON.toJSON(tradeTables).toString());
        List<TradeTable> tradeTableList = this.modifyTradeTable(tradeTables);
        modifyOrderResponseDto.setTables(tradeTableList);
        logger.info("下单接口-新增交易桌台结束");

        //5.新增 优惠信息 数据
        logger.info("下单接口-新增优惠信息开始");
        logger.info("下单接口-优惠信息："+ JSON.toJSON(tradePrivileges).toString());
        List<TradePrivilege> tradePrivilegeList = this.modifyTradePrivileges(tradePrivileges);
        modifyOrderResponseDto.setTradePrivileges(tradePrivilegeList);
        logger.info("下单接口-新增优惠信息结束");

        //6.新增 交易明细特征 数据
        logger.info("下单接口-新增交易明细特征开始");
        logger.info("下单接口-交易明细特征："+ JSON.toJSON(tradeItemProperties).toString());
        List<TradeItemProperty> tradeItemPropertiesList = this.modifyTradeItemProperties(tradeItemProperties);
        modifyOrderResponseDto.setTradeItemProperties(tradeItemPropertiesList);
        logger.info("下单接口-新增交易明细特征结束");

        //7.新增 订单用户关联表 数据
        logger.info("下单接口-新增订单用户关联表开始");
        logger.info("下单接口-订单用户关联表："+ JSON.toJSON(tradeUsers).toString());
        List<TradeUser> tradeUserList = this.modifyTradeUser(tradeUsers);
        modifyOrderResponseDto.setTradeUsers(tradeUserList);
        logger.info("下单接口-新增订单用户关联表结束");

        //8.新增 会员次卡表 数据
        logger.info("下单接口-新增会员次卡表开始");
        logger.info("下单接口-新增会员次卡表："+ JSON.toJSON(customerCardTimes).toString());
        List<CustomerCardTime> customerCardTimeList = this.modifyCustomerCardTimeBo(customerCardTimes);
        modifyOrderResponseDto.setCustomerCardTimes(customerCardTimeList);
        logger.info("下单接口-新增会员次卡表结束");
        return modifyOrderResponseDto;
    }

    private void modifyInventory(List<InventoryItemsDto> deductInventoryItems, boolean isAddQty){
        for(InventoryItemsDto dto : deductInventoryItems){
            Long dishId = dto.getDishId();
            String dishName = dto.getDishName();
            Double price = dto.getPrice();
            Double quantity = dto.getQuantity();
            Optional<DishShop> optional = dishShopRepository.findById(dishId);
            if(Objects.isNull(optional)){
                logger.error("改单接口- 根据ID未找到商品,Dish_ID:"+dishId);
                throw new BusinessException("改单接口- 根据ID未找到商品,Dish_ID:"+dishId);
            }
            DishShop dishShop = optional.get();
            Double dishQty = dishShop.getDishQty();
            if(isAddQty){
                dishQty = dishQty + quantity;
            }else{
                dishQty = dishQty - quantity;
            }
            dishShop.setDishQty(dishQty);
//            dishShop.setName(dishName);
//            dishShop.setMarketPrice(price);
            dishShopRepository.save(dishShop);
        }
    }

    private List<CustomerCardTime> modifyCustomerCardTimeBo(List<CustomerCardTimeBo> customerCardTimes){
        List<CustomerCardTime> customerCardTimeList = new ArrayList<CustomerCardTime>();
        for(CustomerCardTimeBo bo:customerCardTimes){
            CustomerCardTime customerCardTime = bo.copyTo(CustomerCardTime.class);
            customerCardTime = customerCardTimeRepository.save(customerCardTime);
            customerCardTimeList.add(customerCardTime);
        }
        return customerCardTimeList;
    }

    private List<TradeUser> modifyTradeUser(List<TradeUserBo> tradeUserBos){
        List<TradeUser> tradeUsers = new ArrayList<TradeUser>();
        for(TradeUserBo bo:tradeUserBos){
            TradeUser tradeUser = bo.copyTo(TradeUser.class);
            tradeUser = tradeUserRepository.save(tradeUser);
            tradeUsers.add(tradeUser);
        }
        return tradeUsers;
    }

    private List<TradeItemProperty> modifyTradeItemProperties(List<TradeItemPropertyBo> tradeItemPropertieBos){
        List<TradeItemProperty> tradeItemPropertiesList = new ArrayList<TradeItemProperty>();
        for(TradeItemPropertyBo bo:tradeItemPropertieBos){
            TradeItemProperty tradeItemProperty = bo.copyTo(TradeItemProperty.class);
            tradeItemProperty = tradeItemPropertyRepository.save(tradeItemProperty);
            tradeItemPropertiesList.add(tradeItemProperty);
        }
        return tradeItemPropertiesList;
    }

    private List<TradeItem> modifyTradeItem(List<TradeItemBo> tradeItemBoList){
        List<TradeItem> tradeItemList = new ArrayList<TradeItem>();
        for(TradeItemBo bo:tradeItemBoList){
            TradeItem tradeItem = bo.copyTo(TradeItem.class);
            tradeItem = tradeItemRepository.save(tradeItem);
            tradeItemList.add(tradeItem);
        }
        return tradeItemList;
    }

    private List<TradeCustomer> modifyTradeCustomer(List<TradeCustomerBo> tradeCustomers){
        List<TradeCustomer> tradeCustomerList = new ArrayList<TradeCustomer>();
        for(TradeCustomerBo bo:tradeCustomers){
            TradeCustomer tradeCustomer = bo.copyTo(TradeCustomer.class);
            tradeCustomer = tradeCustomerRepository.save(tradeCustomer);
            tradeCustomerList.add(tradeCustomer);
        }
        return tradeCustomerList;
    }

    private List<TradePrivilege> modifyTradePrivileges(List<TradePrivilegeBo> tradePrivilegeBos){
        List<TradePrivilege> tradePrivileges = new ArrayList<TradePrivilege>();
        for(TradePrivilegeBo bo:tradePrivilegeBos){
            TradePrivilege tradePrivilege = bo.copyTo(TradePrivilege.class);
            tradePrivilege = tradePrivilegeRepository.save(tradePrivilege);
            tradePrivileges.add(tradePrivilege);
        }
        return tradePrivileges;
    }

    private List<TradeTable> modifyTradeTable(List<TradeTableBo> tradeTableBos){
        List<TradeTable> tradeTableList = new ArrayList<TradeTable>();
        for(TradeTableBo bo:tradeTableBos){
            //1.修改桌台信息trade_table
            TradeTable tradeTable = bo.copyTo(TradeTable.class);
            tradeTable = tradeTableRepository.save(tradeTable);
            tradeTableList.add(tradeTable);
            //2.维护桌台信息tables
            Optional<Tables> optional = tablesRepository.findById(bo.getTableId());
            if(Objects.isNull(optional)){
                logger.error("根据ID未找到可维护桌台信息,TABLE_ID:"+bo.getTableId());
                throw new BusinessException("根据ID未找到可维护桌台信息,TABLE_ID:"+bo.getTableId());
            }
            Tables tables = optional.get();
            if(Constants.DATA_ENABLE.equals(bo.getStatusFlag()) && Constants.SELF_TABLE_STATUS_LOCK.equals(bo.getSelfTableStatus())){
                tables.setTableStatus(Constants.SELF_TABLE_STATUS_LOCK);
            }else{
                tables.setTableStatus(Constants.SELF_TABLE_STATUS_UNLOCK);
            }
            tablesRepository.save(tables);
        }
        return tradeTableList;
    }


    public static void main(String[] args) {
        String s = "allright";
       // Assert s =="allright";


    }

}
