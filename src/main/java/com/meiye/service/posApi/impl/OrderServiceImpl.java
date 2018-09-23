package com.meiye.service.posApi.impl;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.trade.*;
import com.meiye.bo.trade.CancelTrade.CancelTradeBo;
import com.meiye.bo.trade.CancelTrade.ReturnInventoryItem;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
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
     *
     * @param orderRequestDto
     * @return
     */
    @Override
    @Transactional(rollbackOn = {Exception.class})
    public OrderResponseDto modifyOrderData(OrderRequestDto orderRequestDto) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        TradeRequestDto tradeRequestDto = orderRequestDto.getContent();
        TradeBo tradeBo = tradeRequestDto.getTradeRequest();
        InventoryRequestDto inventoryRequest = tradeRequestDto.getInventoryRequest();
        if (Objects.isNull(tradeBo)) {
            logger.error("改单接口-订单数据为空！");
            throw new BusinessException("改单接口-订单数据为空！");
        }
        List<CustomerCardTimeBo> customerCardTimes = tradeBo.getCustomerCardTimes();
        List<TradeCustomerBo> tradeCustomers = tradeBo.getTradeCustomers();
        List<TradeItemPropertyBo> tradeItemProperties = tradeBo.getTradeItemProperties();
        List<TradeItemBo> tradeItems = tradeBo.getTradeItems();
        List<TradePrivilegeBo> tradePrivileges = tradeBo.getTradePrivileges();
        List<TradeUserBo> tradeUsers = tradeBo.getTradeUsers();
        List<TradeTableBo> tradeTables = tradeBo.getTradeTables();
        Trade trade = tradeBo.copyTo(Trade.class);

        //校验版本号
        Long tradeId = trade.getId();
        if (Objects.isNull(tradeId)) {
            logger.error("改单接口-交易记录主单tradeId is null！");
            throw new BusinessException("改单接口-交易记录主单tradeId is null！");
        }
        Optional<Trade> tradeOptional = tradeRepository.findById(tradeId);
        if (Objects.isNull(tradeOptional)) {
            logger.error("改单接口-未找到交易记录主单！tradeId:" + tradeId);
            throw new BusinessException("改单接口-未找到交易记录主单！tradeId:" + tradeId);
        }
        Trade trade1 = tradeOptional.get();
        Timestamp clientUpdateTime = trade.getServerUpdateTime();
        Timestamp serverUpdateTime = trade1.getServerUpdateTime();
        if (clientUpdateTime.equals(serverUpdateTime)) {
            trade.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
        } else {
            logger.error("改单接口-版本校验失败！");
            throw new BusinessException("改单接口-版本校验失败！");
        }

        //1.修改 交易记录主单 数据
        logger.info("改单接口-修改交易记录主单开始");
        logger.info("改单接口-交易记录主单：" + JSON.toJSON(trade).toString());
        trade = tradeRepository.save(trade);
        orderResponseDto.setTrade(trade);
        logger.info("改单接口-修改交易记录主单结束");

        //2.修改 交易明细 数据
        if (Objects.nonNull(tradeItems) && tradeItems.size() > 0) {
            logger.info("改单接口-修改交易明细开始");
            logger.info("改单接口-交易记录主单：" + JSON.toJSON(tradeItems).toString());
            List<TradeItem> tradeItemList = this.modifyTradeItem(tradeItems);
            orderResponseDto.setTradeItems(tradeItemList);
            logger.info("改单接口-修改交易明细结束");
        }

        //3.修改 交易的顾客信息 数据
        if (Objects.nonNull(tradeCustomers) && tradeCustomers.size() > 0) {
            logger.info("改单接口-修改交易的顾客信息开始");
            logger.info("改单接口-交易的顾客信息：" + JSON.toJSON(tradeCustomers).toString());
            List<TradeCustomer> tradeCustomerList = this.modifyTradeCustomer(tradeCustomers);
            orderResponseDto.setTradeCustomers(tradeCustomerList);
            logger.info("改单接口-修改交易的顾客信息结束");
        }

        //4.修改 交易桌台 数据
        if (Objects.nonNull(tradeTables) && tradeTables.size() > 0) {
            logger.info("改单接口-修改交易桌台开始");
            logger.info("改单接口-交易桌台信息：" + JSON.toJSON(tradeTables).toString());
            List<TradeTable> tradeTableList = this.modifyTradeTable(tradeTables);
            orderResponseDto.setTradeTables(tradeTableList);
            logger.info("改单接口-修改交易桌台结束");
        }

        //5.修改 优惠信息 数据
        if (Objects.nonNull(tradePrivileges) && tradePrivileges.size() > 0) {
            logger.info("改单接口-修改优惠信息开始");
            logger.info("改单接口-优惠信息：" + JSON.toJSON(tradePrivileges).toString());
            List<TradePrivilege> tradePrivilegeList = this.modifyTradePrivileges(tradePrivileges);
            orderResponseDto.setTradePrivileges(tradePrivilegeList);
            logger.info("改单接口-修改优惠信息结束");
        }

        //6.修改 交易明细特征 数据
        if (Objects.nonNull(tradeItemProperties) && tradeItemProperties.size() > 0) {
            logger.info("改单接口-修改交易明细特征开始");
            logger.info("改单接口-交易明细特征：" + JSON.toJSON(tradeItemProperties).toString());
            List<TradeItemProperty> tradeItemPropertiesList = this.modifyTradeItemProperties(tradeItemProperties);
            orderResponseDto.setTradeItemProperties(tradeItemPropertiesList);
            logger.info("改单接口-修改交易明细特征结束");
        }

        //7.修改 订单用户关联表 数据
        if (Objects.nonNull(tradeUsers) && tradeUsers.size() > 0) {
            logger.info("改单接口-修改订单用户关联表开始");
            logger.info("改单接口-订单用户关联表：" + JSON.toJSON(tradeUsers).toString());
            List<TradeUser> tradeUserList = this.modifyTradeUser(tradeUsers);
            orderResponseDto.setTradeUsers(tradeUserList);
            logger.info("改单接口-修改订单用户关联表结束");
        }


        //8.修改 会员次卡表 数据
        if (Objects.nonNull(customerCardTimes) && customerCardTimes.size() > 0) {
            logger.info("改单接口-修改会员次卡表开始");
            logger.info("改单接口-修改会员次卡表：" + JSON.toJSON(customerCardTimes).toString());
            List<CustomerCardTime> customerCardTimeList = this.modifyCustomerCardTimeBo(customerCardTimes);
            orderResponseDto.setCustomerCardTimes(customerCardTimeList);
            logger.info("改单接口-修改会员次卡表结束");
        }

        //9.修改 商品 库存信息
        if (Objects.nonNull(inventoryRequest)) {
            List<InventoryItemsDto> deductInventoryItems = inventoryRequest.getDeductInventoryItems();
            if (Objects.nonNull(deductInventoryItems) && deductInventoryItems.size() > 0) {
                this.modifyInventory(deductInventoryItems, false);
            }
            List<InventoryItemsDto> returnInventoryItems = inventoryRequest.getReturnInventoryItems();
            if (Objects.nonNull(returnInventoryItems) && returnInventoryItems.size() > 0) {
                this.modifyInventory(returnInventoryItems, true);
            }
        }
        return orderResponseDto;
    }

    /**
     * 下单订单接口 - Ryne 2018/09/02
     *
     * @param addOrderRequestDto
     * @return
     */
    @Override
    @Transactional(rollbackOn = {Exception.class})
    public OrderResponseDto addOrderData(OrderRequestDto addOrderRequestDto) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        TradeRequestDto tradeRequestDto = addOrderRequestDto.getContent();
        TradeBo tradeBo = tradeRequestDto.getTradeRequest();
        InventoryRequestDto inventoryRequest = tradeRequestDto.getInventoryRequest();
        if (Objects.isNull(tradeBo)) {
            logger.error("下单接口-订单数据为空！");
            throw new BusinessException("下单接口-订单数据为空！");
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
        if (Objects.isNull(trade.getBizDate())) {
            trade.setBizDate(new Date());
        }
        trade.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
        //1.新增 交易记录主单 数据
        logger.info("下单接口-新增交易记录主单开始");
        logger.info("下单接口-交易记录主单：" + JSON.toJSON(trade).toString());
        //流水号从1自增
        long count = tradeRepository.count();
        long serialNumber = count + 1;
        trade.setSerialNumber(Long.toString(serialNumber));
        trade = tradeRepository.save(trade);
        orderResponseDto.setTrade(trade);
        logger.info("下单接口-新增交易记录主单结束");
        //关联trade_ID给其他订单相关表
        Long tradeId = trade.getId();

        //2.新增 交易明细 数据
        if (Objects.nonNull(tradeItems) && tradeItems.size() > 0) {
            tradeItems.stream().forEach(bo -> {
                bo.setTradeId(tradeId);
                if(Objects.isNull(bo.getRecycleStatus())){
                    bo.setRecycleStatus(1);
                }
                if(Objects.isNull(bo.getBatchNo())){
                    bo.setBatchNo("");
                }
            }
            );
            logger.info("下单接口-新增交易明细开始");
            logger.info("下单接口-交易记录主单：" + JSON.toJSON(tradeItems).toString());
            List<TradeItem> tradeItemList = this.modifyTradeItem(tradeItems);
            orderResponseDto.setTradeItems(tradeItemList);
            logger.info("下单接口-新增交易明细结束");
        }

        //3.新增 交易的顾客信息 数据
        if (Objects.nonNull(tradeCustomers) && tradeCustomers.size() > 0) {
            tradeCustomers.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增交易的顾客信息开始");
            logger.info("下单接口-交易的顾客信息：" + JSON.toJSON(tradeCustomers).toString());
            List<TradeCustomer> tradeCustomerList = this.modifyTradeCustomer(tradeCustomers);
            orderResponseDto.setTradeCustomers(tradeCustomerList);
            logger.info("下单接口-新增交易的顾客信息结束");
        }

        //4.新增 交易桌台 数据
        if (Objects.nonNull(tradeTables) && tradeTables.size() > 0) {
            tradeTables.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增交易桌台开始");
            logger.info("下单接口-交易桌台信息：" + JSON.toJSON(tradeTables).toString());
            List<TradeTable> tradeTableList = this.modifyTradeTable(tradeTables);
            orderResponseDto.setTradeTables(tradeTableList);
            logger.info("下单接口-新增交易桌台结束");
        }

        //5.新增 优惠信息 数据
        if (Objects.nonNull(tradePrivileges) && tradePrivileges.size() > 0) {
            tradePrivileges.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增优惠信息开始");
            logger.info("下单接口-优惠信息：" + JSON.toJSON(tradePrivileges).toString());
            List<TradePrivilege> tradePrivilegeList = this.modifyTradePrivileges(tradePrivileges);
            orderResponseDto.setTradePrivileges(tradePrivilegeList);
            logger.info("下单接口-新增优惠信息结束");
        }

        //6.新增 交易明细特征 数据
        if (Objects.nonNull(tradeItemProperties) && tradeItemProperties.size() > 0) {
            tradeItemProperties.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增交易明细特征开始");
            logger.info("下单接口-交易明细特征：" + JSON.toJSON(tradeItemProperties).toString());
            List<TradeItemProperty> tradeItemPropertiesList = this.modifyTradeItemProperties(tradeItemProperties);
            orderResponseDto.setTradeItemProperties(tradeItemPropertiesList);
            logger.info("下单接口-新增交易明细特征结束");
        }

        //7.新增 订单用户关联表 数据
        if (Objects.nonNull(tradeUsers) && tradeUsers.size() > 0) {
            tradeUsers.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增订单用户关联表开始");
            logger.info("下单接口-订单用户关联表：" + JSON.toJSON(tradeUsers).toString());
            List<TradeUser> tradeUserList = this.modifyTradeUser(tradeUsers);
            orderResponseDto.setTradeUsers(tradeUserList);
            logger.info("下单接口-新增订单用户关联表结束");
        }

        //8.新增 会员次卡表 数据
        if (Objects.nonNull(customerCardTimes) && customerCardTimes.size() > 0) {
            customerCardTimes.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增会员次卡表开始");
            logger.info("下单接口-新增会员次卡表：" + JSON.toJSON(customerCardTimes).toString());
            List<CustomerCardTime> customerCardTimeList = this.modifyCustomerCardTimeBo(customerCardTimes);
            orderResponseDto.setCustomerCardTimes(customerCardTimeList);
            logger.info("下单接口-新增会员次卡表结束");
        }

        //9.修改 商品 库存信息
        if (Objects.nonNull(inventoryRequest)) {
            List<InventoryItemsDto> deductInventoryItems = inventoryRequest.getDeductInventoryItems();
            if (Objects.nonNull(deductInventoryItems) && deductInventoryItems.size() > 0) {
                this.modifyInventory(deductInventoryItems, false);
            }
            List<InventoryItemsDto> returnInventoryItems = inventoryRequest.getReturnInventoryItems();
            if (Objects.nonNull(returnInventoryItems) && returnInventoryItems.size() > 0) {
                this.modifyInventory(returnInventoryItems, true);
            }
        }
        return orderResponseDto;
    }

    @Override
    public OrderResponseDto getOrderResponse(Long tradeId) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        Optional<Trade> optional = tradeRepository.findById(tradeId);
        if (Objects.isNull(optional)) {
            logger.error("根据ID未找到交易记录主单,ID: :" + tradeId);
            throw new BusinessException("根据ID未找到交易记录主单 ,ID:" + tradeId);
        }
        Trade trade = optional.get();
        List<TradeItem> tradeItemList = tradeItemRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
        List<TradeCustomer> tradeCustomerList = tradeCustomerRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
        List<TradeTable> tradeTableList = tradeTableRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
        List<TradePrivilege> tradePrivilegeList = tradePrivilegeRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
        List<TradeItemProperty> tradeItemPropertiesList = tradeItemPropertyRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
        List<TradeUser> tradeUserList = tradeUserRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
        List<CustomerCardTime> customerCardTimeList = customerCardTimeRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
        List<Tables> tablesList = this.getRelatedTablesByTrade(tradeTableList);
        orderResponseDto.setTables(tablesList);
        orderResponseDto.setCustomerCardTimes(customerCardTimeList);
        orderResponseDto.setTrade(trade);
        orderResponseDto.setTradeCustomers(tradeCustomerList);
        orderResponseDto.setTradeItemProperties(tradeItemPropertiesList);
        orderResponseDto.setTradeItems(tradeItemList);
        orderResponseDto.setTradePrivileges(tradePrivilegeList);
        orderResponseDto.setTradeUsers(tradeUserList);
        orderResponseDto.setTradeTables(tradeTableList);
        return orderResponseDto;
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public OrderResponseDto deleteTrade(CancelTradeBo cancelTradeBo) {
        if (cancelTradeBo != null && cancelTradeBo.getContent() != null &&
                cancelTradeBo.getContent().getObsoleteRequest() != null &&
                cancelTradeBo.getContent().getObsoleteRequest().getTradeId() != null) {
            Long tradeId = cancelTradeBo.getContent().getObsoleteRequest().getTradeId();
            Trade one = tradeRepository.getOne(tradeId);
            if (one == null || one.getTradePayStatus() != 1) {
                throw new BusinessException("作废订单接口- trade数据校验不通过");
            }
            tradeRepository.deleteTradeById(cancelTradeBo.getContent().getObsoleteRequest().getTradeId(), new Timestamp(new Date().getTime()));
            //下面根据trade id拿到 订单数据 然后返回。
            if (cancelTradeBo.getContent().getReviseStock() != null && cancelTradeBo.getContent().getReviseStock() == true) {
                List<ReturnInventoryItem> returnInventoryItems = cancelTradeBo.getContent().getReturnInventoryItems();
                if (returnInventoryItems != null && returnInventoryItems.size() > 0) {
                    for (int i = 0; i < returnInventoryItems.size(); i++) {
                        ReturnInventoryItem returnInventoryItem = returnInventoryItems.get(i);
                        if (returnInventoryItem.getQuantity() != null && returnInventoryItem.getQuantity() > 0) {
                            Long dishId = returnInventoryItem.getDishId();
                            DishShop dishShop = dishShopRepository.findByIdAndShopIdenty(dishId, cancelTradeBo.getShopID());
                            if (dishShop != null) {
                                dishShop.setDishQty(dishShop.getDishQty() + returnInventoryItem.getQuantity());
                                dishShopRepository.save(dishShop);
                            }
                        }
                    }
                }
            }

        } else {
            throw new BusinessException("作废订单接口- trade数据校验不通过");
        }
        return getOrderResponse(cancelTradeBo.getContent().getObsoleteRequest().getTradeId());
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public OrderResponseDto returnTrade(CancelTradeBo cancelTradeBo) {
        if (cancelTradeBo != null && cancelTradeBo.getContent() != null
                && cancelTradeBo.getContent().getTradeId() != null) {
            Trade trade = tradeRepository.findByIdAndBrandIdentyAndTradeStatusIsNot(cancelTradeBo.getContent().getTradeId(), cancelTradeBo.getBrandID(), 6);
            if (trade == null) {
                throw new BusinessException("退货订单接口- trade数据校验不通过");
            }
            Trade tradeNew = new Trade();
            BeanUtils.copyProperties(trade, tradeNew);
            tradeNew.setId(null);
            tradeNew.setRelateTradeId(cancelTradeBo.getContent().getTradeId());
            tradeNew.setUuid(UUID.randomUUID().toString().substring(0, 32));
            tradeRepository.save(tradeNew);
            //归还库存
            if (cancelTradeBo.getContent().getReturnInventoryItems() != null && cancelTradeBo.getContent().getReturnInventoryItems().size() > 0) {
                List<ReturnInventoryItem> returnInventoryItems = cancelTradeBo.getContent().getReturnInventoryItems();
                for (int i = 0; i < returnInventoryItems.size(); i++) {
                    ReturnInventoryItem returnInventoryItem = returnInventoryItems.get(i);
                    if (returnInventoryItem.getQuantity() != null && returnInventoryItem.getQuantity() > 0) {
                        Long dishId = returnInventoryItem.getDishId();
                        DishShop dishShop = dishShopRepository.findByIdAndShopIdenty(dishId, cancelTradeBo.getShopID());
                        if (dishShop != null) {
                            dishShop.setDishQty(dishShop.getDishQty() + returnInventoryItem.getQuantity());
                        }
                    }
                }
            }
        } else {
            throw new BusinessException("退货订单接口- trade数据校验不通过");
        }

        return getOrderResponse(cancelTradeBo.getContent().getTradeId());
    }

    private  List<Tables> getRelatedTablesByTrade( List<TradeTable> tradeTables){
        List<Tables> tablesList= null;
        List<Long> tableIds = null;
        for(TradeTable tradeTable : tradeTables){
            tableIds = new ArrayList<>();
            tableIds.add(tradeTable.getTableId());
        }
        if(tableIds!=null &&tableIds.size()>0){
            tablesList = tablesRepository.findAllByIdInAndStatusFlag(tableIds,Constants.DATA_ENABLE);
        }
        return  tablesList;
    }

    private void modifyInventory(List<InventoryItemsDto> deductInventoryItems, boolean isAddQty) {
        for (InventoryItemsDto dto : deductInventoryItems) {
            Long dishId = dto.getDishId();
            String dishName = dto.getDishName();
            Double price = dto.getPrice();
            Double quantity = dto.getQuantity();
            Optional<DishShop> optional = dishShopRepository.findById(dishId);
            if (Objects.isNull(optional)) {
                logger.error("改单接口- 根据ID未找到商品,Dish_ID:" + dishId);
                throw new BusinessException("改单接口- 根据ID未找到商品,Dish_ID:" + dishId);
            }
            DishShop dishShop = optional.get();
            Double dishQty = dishShop.getDishQty();
            if (isAddQty) {
                dishQty = dishQty + quantity;
            } else {
                dishQty = dishQty - quantity;
            }
            dishShop.setDishQty(dishQty);
//            dishShop.setName(dishName);
//            dishShop.setMarketPrice(price);
            dishShopRepository.save(dishShop);
        }
    }

    private List<CustomerCardTime> modifyCustomerCardTimeBo(List<CustomerCardTimeBo> customerCardTimes) {
        List<CustomerCardTime> customerCardTimeList = new ArrayList<CustomerCardTime>();
        for (CustomerCardTimeBo bo : customerCardTimes) {
            CustomerCardTime customerCardTime = bo.copyTo(CustomerCardTime.class);
            customerCardTime = customerCardTimeRepository.save(customerCardTime);
            customerCardTimeList.add(customerCardTime);
        }
        return customerCardTimeList;
    }

    private List<TradeUser> modifyTradeUser(List<TradeUserBo> tradeUserBos) {
        List<TradeUser> tradeUsers = new ArrayList<TradeUser>();
        for (TradeUserBo bo : tradeUserBos) {
            TradeUser tradeUser = bo.copyTo(TradeUser.class);
            tradeUser = tradeUserRepository.save(tradeUser);
            tradeUsers.add(tradeUser);
        }
        return tradeUsers;
    }

    private List<TradeItemProperty> modifyTradeItemProperties(List<TradeItemPropertyBo> tradeItemPropertieBos) {
        List<TradeItemProperty> tradeItemPropertiesList = new ArrayList<TradeItemProperty>();
        for (TradeItemPropertyBo bo : tradeItemPropertieBos) {
            TradeItemProperty tradeItemProperty = bo.copyTo(TradeItemProperty.class);
            tradeItemProperty = tradeItemPropertyRepository.save(tradeItemProperty);
            tradeItemPropertiesList.add(tradeItemProperty);
        }
        return tradeItemPropertiesList;
    }

    private List<TradeItem> modifyTradeItem(List<TradeItemBo> tradeItemBoList) {
        List<TradeItem> tradeItemList = new ArrayList<TradeItem>();
        for (TradeItemBo bo : tradeItemBoList) {
            TradeItem tradeItem = bo.copyTo(TradeItem.class);
            tradeItem = tradeItemRepository.save(tradeItem);
            tradeItemList.add(tradeItem);
        }
        return tradeItemList;
    }

    private List<TradeCustomer> modifyTradeCustomer(List<TradeCustomerBo> tradeCustomers) {
        List<TradeCustomer> tradeCustomerList = new ArrayList<TradeCustomer>();
        for (TradeCustomerBo bo : tradeCustomers) {
            TradeCustomer tradeCustomer = bo.copyTo(TradeCustomer.class);
            tradeCustomer = tradeCustomerRepository.save(tradeCustomer);
            tradeCustomerList.add(tradeCustomer);
        }
        return tradeCustomerList;
    }

    private List<TradePrivilege> modifyTradePrivileges(List<TradePrivilegeBo> tradePrivilegeBos) {
        List<TradePrivilege> tradePrivileges = new ArrayList<TradePrivilege>();
        for (TradePrivilegeBo bo : tradePrivilegeBos) {
            TradePrivilege tradePrivilege = bo.copyTo(TradePrivilege.class);
            tradePrivilege = tradePrivilegeRepository.save(tradePrivilege);
            tradePrivileges.add(tradePrivilege);
        }
        return tradePrivileges;
    }

    private List<TradeTable> modifyTradeTable(List<TradeTableBo> tradeTableBos) {
        List<TradeTable> tradeTableList = new ArrayList<TradeTable>();
        for (TradeTableBo bo : tradeTableBos) {
            //1.修改桌台信息trade_table
            TradeTable tradeTable = bo.copyTo(TradeTable.class);
            tradeTable = tradeTableRepository.save(tradeTable);
            tradeTableList.add(tradeTable);
            //2.维护桌台信息tables
            Optional<Tables> optional = tablesRepository.findById(bo.getTableId());
            if (Objects.isNull(optional)) {
                logger.error("根据ID未找到可维护桌台信息,TABLE_ID:" + bo.getTableId());
                throw new BusinessException("根据ID未找到可维护桌台信息,TABLE_ID:" + bo.getTableId());
            }
            Tables tables = optional.get();
            if (Constants.DATA_ENABLE.equals(bo.getStatusFlag()) && Constants.SELF_TABLE_STATUS_LOCK.equals(bo.getSelfTableStatus())) {
                tables.setTableStatus(Constants.SELF_TABLE_STATUS_LOCK);
            } else {
                tables.setTableStatus(Constants.SELF_TABLE_STATUS_UNLOCK);
            }
            tablesRepository.save(tables);
        }
        return tradeTableList;
    }

    @Override
    public Trade getTradeByTradeNo(String tradeNo){
        return tradeRepository.findByTradeNo(tradeNo);
    }


}
