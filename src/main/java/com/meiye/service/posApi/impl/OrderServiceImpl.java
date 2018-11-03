package com.meiye.service.posApi.impl;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.trade.*;
import com.meiye.bo.trade.CancelTrade.CancelTradeBo;
import com.meiye.bo.trade.CancelTrade.ReturnInventoryItem;
import com.meiye.bo.trade.OrderDto.*;
import com.meiye.exception.BusinessException;
import com.meiye.model.part.DishShop;
import com.meiye.model.pay.Payment;
import com.meiye.model.pay.PaymentItem;
import com.meiye.model.pay.PaymentItemExtra;
import com.meiye.model.setting.Tables;
import com.meiye.model.trade.*;
import com.meiye.repository.part.DishShopRepository;
import com.meiye.repository.pay.PaymentItemExtraRepository;
import com.meiye.repository.pay.PaymentItemRepository;
import com.meiye.repository.pay.PaymentRepository;
import com.meiye.repository.setting.TablesRepository;
import com.meiye.repository.trade.*;
import com.meiye.service.pay.PayService;
import com.meiye.service.posApi.OrderService;
import com.meiye.system.util.WebUtil;
import com.meiye.util.Constants;
import com.meiye.util.ObjectUtil;
import com.meiye.util.StringUtil;
import com.meiye.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentItemRepository paymentItemRepository;

    @Autowired
    PaymentItemExtraRepository paymentItemExtraRepository;

    @Autowired
    PayService payService;

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
        if (!tradeOptional.isPresent()) {
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
            tradeItems.stream().forEach(bo -> {
                        if(Objects.isNull(bo.getTradeId())){
                            bo.setTradeId(tradeId);
                            if (Objects.isNull(bo.getRecycleStatus())) {
                                bo.setRecycleStatus(1);
                            }
                            if (Objects.isNull(bo.getBatchNo())) {
                                bo.setBatchNo("");
                            }
                        }
                    }
            );
            logger.info("改单接口-修改交易明细开始");
            logger.info("改单接口-交易记录主单：" + JSON.toJSON(tradeItems).toString());
            List<TradeItem> tradeItemList = this.modifyTradeItem(tradeItems);
            orderResponseDto.setTradeItems(tradeItemList);
            logger.info("改单接口-修改交易明细结束");
        }

        //3.修改 交易的顾客信息 数据
        if (Objects.nonNull(tradeCustomers) && tradeCustomers.size() > 0) {
            tradeCustomers.stream().forEach(bo ->{
                if(Objects.isNull(bo.getTradeId())){
                    bo.setTradeId(tradeId);
                }
            });
            logger.info("改单接口-修改交易的顾客信息开始");
            logger.info("改单接口-交易的顾客信息：" + JSON.toJSON(tradeCustomers).toString());
            List<TradeCustomer> tradeCustomerList = this.modifyTradeCustomer(tradeCustomers);
            orderResponseDto.setTradeCustomers(tradeCustomerList);
            logger.info("改单接口-修改交易的顾客信息结束");
        }

        //4.修改 交易桌台 数据
        if (Objects.nonNull(tradeTables) && tradeTables.size() > 0) {
            tradeTables.stream().forEach(bo ->{
                if(Objects.isNull(bo.getTradeId())){
                    bo.setTradeId(tradeId);
                }
            });
            logger.info("改单接口-修改交易桌台开始");
            logger.info("改单接口-交易桌台信息：" + JSON.toJSON(tradeTables).toString());
            List<TradeTable> tradeTableList = this.modifyTradeTable(tradeTables,orderResponseDto);
            orderResponseDto.setTradeTables(tradeTableList);
            logger.info("改单接口-修改交易桌台结束");
        }

        //5.修改 优惠信息 数据
        if (Objects.nonNull(tradePrivileges) && tradePrivileges.size() > 0) {
            tradePrivileges.stream().forEach(bo ->{
                if(Objects.isNull(bo.getTradeId())){
                    bo.setTradeId(tradeId);
                }
            });
            logger.info("改单接口-修改优惠信息开始");
            logger.info("改单接口-优惠信息：" + JSON.toJSON(tradePrivileges).toString());
            List<TradePrivilege> tradePrivilegeList = this.modifyTradePrivileges(tradeItems,tradePrivileges);
            orderResponseDto.setTradePrivileges(tradePrivilegeList);
            logger.info("改单接口-修改优惠信息结束");
        }

        //6.修改 交易明细特征 数据
        if (Objects.nonNull(tradeItemProperties) && tradeItemProperties.size() > 0) {
            tradeItemProperties.stream().forEach(bo ->{
                if(Objects.isNull(bo.getTradeId())){
                    bo.setTradeId(tradeId);
                }
            });
            logger.info("改单接口-修改交易明细特征开始");
            logger.info("改单接口-交易明细特征：" + JSON.toJSON(tradeItemProperties).toString());
            List<TradeItemProperty> tradeItemPropertiesList = this.modifyTradeItemProperties(tradeItems,tradeItemProperties);
            orderResponseDto.setTradeItemProperties(tradeItemPropertiesList);
            logger.info("改单接口-修改交易明细特征结束");
        }

        //7.修改 订单用户关联表 数据
        if (Objects.nonNull(tradeUsers) && tradeUsers.size() > 0) {
            tradeUsers.stream().forEach(bo ->{
                if(Objects.isNull(bo.getTradeId())){
                    bo.setTradeId(tradeId);
                }
            });
            logger.info("改单接口-修改订单用户关联表开始");
            logger.info("改单接口-订单用户关联表：" + JSON.toJSON(tradeUsers).toString());
            List<TradeUser> tradeUserList = this.modifyTradeUser(tradeItems,tradeUsers);
            orderResponseDto.setTradeUsers(tradeUserList);
            logger.info("改单接口-修改订单用户关联表结束");
        }


        //8.修改 会员次卡表 数据
        if (Objects.nonNull(customerCardTimes) && customerCardTimes.size() > 0) {
            customerCardTimes.stream().forEach(bo ->{
                if(Objects.isNull(bo.getTradeId())){
                    bo.setTradeId(tradeId);
                }
            });
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
                        if (Objects.isNull(bo.getRecycleStatus())) {
                            bo.setRecycleStatus(1);
                        }
                        if (Objects.isNull(bo.getBatchNo())) {
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
            List<TradeTable> tradeTableList = this.modifyTradeTable(tradeTables,orderResponseDto);
            orderResponseDto.setTradeTables(tradeTableList);
            logger.info("下单接口-新增交易桌台结束");
        }

        //5.新增 优惠信息 数据
        if (Objects.nonNull(tradePrivileges) && tradePrivileges.size() > 0) {
            tradePrivileges.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增优惠信息开始");
            logger.info("下单接口-优惠信息：" + JSON.toJSON(tradePrivileges).toString());
            List<TradePrivilege> tradePrivilegeList = this.modifyTradePrivileges(tradeItems,tradePrivileges);
            orderResponseDto.setTradePrivileges(tradePrivilegeList);
            logger.info("下单接口-新增优惠信息结束");
        }

        //6.新增 交易明细特征 数据
        if (Objects.nonNull(tradeItemProperties) && tradeItemProperties.size() > 0) {
            tradeItemProperties.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增交易明细特征开始");
            logger.info("下单接口-交易明细特征：" + JSON.toJSON(tradeItemProperties).toString());
            List<TradeItemProperty> tradeItemPropertiesList = this.modifyTradeItemProperties(tradeItems,tradeItemProperties);
            orderResponseDto.setTradeItemProperties(tradeItemPropertiesList);
            logger.info("下单接口-新增交易明细特征结束");
        }

        //7.新增 订单用户关联表 数据
        if (Objects.nonNull(tradeUsers) && tradeUsers.size() > 0) {
            tradeUsers.stream().forEach(bo -> bo.setTradeId(tradeId));
            logger.info("下单接口-新增订单用户关联表开始");
            logger.info("下单接口-订单用户关联表：" + JSON.toJSON(tradeUsers).toString());
            List<TradeUser> tradeUserList = this.modifyTradeUser(tradeItems,tradeUsers);
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
    public OrderResponseDto getOrderResponse(Long tradeId, boolean needDelData) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        Optional<Trade> optional = tradeRepository.findById(tradeId);
        if (!optional.isPresent()) {
            logger.error("根据ID未找到交易记录主单,ID: :" + tradeId);
            throw new BusinessException("根据ID未找到交易记录主单 ,ID:" + tradeId);
        }
        Trade trade = optional.get();
        List<TradeItem> tradeItemList = null;
        List<TradeCustomer> tradeCustomerList = null;
        List<TradeTable> tradeTableList = null;
        List<TradePrivilege> tradePrivilegeList = null;
        List<TradeItemProperty> tradeItemPropertiesList = null;
        List<TradeUser> tradeUserList = null;
        List<CustomerCardTime> customerCardTimeList = null;
        if (needDelData) {
            tradeItemList = tradeItemRepository.findAllByTradeId(tradeId);
            tradeCustomerList = tradeCustomerRepository.findAllByTradeId(tradeId);
            tradeTableList = tradeTableRepository.findAllByTradeId(tradeId);
            tradePrivilegeList = tradePrivilegeRepository.findAllByTradeId(tradeId);
            tradeItemPropertiesList = tradeItemPropertyRepository.findAllByTradeId(tradeId);
            tradeUserList = tradeUserRepository.findAllByTradeId(tradeId);
            customerCardTimeList = customerCardTimeRepository.findAllByTradeId(tradeId);
        } else {
            tradeItemList = tradeItemRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
            tradeCustomerList = tradeCustomerRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
            tradeTableList = tradeTableRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
            tradePrivilegeList = tradePrivilegeRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
            tradeItemPropertiesList = tradeItemPropertyRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
            tradeUserList = tradeUserRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
            customerCardTimeList = customerCardTimeRepository.findAllByTradeIdAndStatusFlag(tradeId, Constants.DATA_ENABLE);
        }
        List<Tables> tablesList = this.getRelatedTablesByTrade(tradeTableList, needDelData);
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
        return null;
    }

    @Transactional(rollbackOn = {Exception.class})
    @Override
    public Long returnTrade(CancelTradeBo cancelTradeBo) {
        if (cancelTradeBo != null && cancelTradeBo.getContent() != null
                && cancelTradeBo.getContent().getTradeId() != null) {

            OrderResponseDto order = getOrderResponse(cancelTradeBo.getContent().getTradeId(), false);
            if (order == null || order.getTrade() == null){
                throw new BusinessException("退货订单接口- 未找到退货订单");
            }
            Trade trade = order.getTrade();
            if(trade.getTradePayStatus()!=3)
                throw new BusinessException("退货订单接口- 订单未支付，不能退货");
            List<Trade> returnTrads=tradeRepository.findByRelateTradeIdAndTradeType(trade.getId(),2);
            if(!ObjectUtils.isEmpty(returnTrads)){
//                for(Trade returnTrade:returnTrads){
//                    if(returnTrade.getTradePayStatus()==4||returnTrade.getTradePayStatus()==5)
                throw new BusinessException("存在已退货的订单或正在退款中的退货单");
//                }
            }

            //copy trade and save trade

            Trade tradeNew = returnTradeByCopyAndSave(cancelTradeBo, trade);
            Long tradeNewId = tradeNew.getId();
            String tradeUuid = tradeNew.getUuid();

            //copy tradeCustomers and save
            returnTradeCustomerByCopyAndSave(order, tradeNewId, tradeUuid);

            //copy tradeTables and save
            returnTradeTableByCopyAndSave(order, tradeNewId, tradeUuid);

            //copy tradeItems and save
            returnTradeItemsByCopyAndSave(order, tradeNewId, tradeUuid);

            // copy customerCardTime
            returnCustomerCardTimeByCopyAndSave(order, tradeNewId, tradeUuid);

            //copy payment
            returnPayment(trade.getId(),tradeNewId,tradeUuid);
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
            return tradeNew.getId();
        } else {
            throw new BusinessException("退货订单接口- trade数据校验不通过");
        }
    }


    private void returnPayment(Long oldTradeId, Long tradeNewId, String tradeUuid){
        List<Payment> payments=paymentRepository.findByRelateId(oldTradeId);
        if(!ObjectUtils.isEmpty(payments)){
            for(Payment payment:payments){
                if(ObjectUtil.notEqual(payment.getIsPaid(),1))
                    continue;
                Payment newPayment=new Payment();
                BeanUtils.copyProperties(payment,newPayment);
                newPayment.setId(null);
                if(payment.getPaymentType()==1)
                    newPayment.setPaymentType(2);
                else if(payment.getPaymentType()==3)
                    newPayment.setPaymentType(4);
                else if(payment.getPaymentType()==5)
                    newPayment.setPaymentType(6);
                newPayment.setRelateUuid(tradeUuid);
                newPayment.setRelateId(tradeNewId);
                newPayment.setUuid(UUIDUtil.randomUUID());
                paymentRepository.save(newPayment);

                List<PaymentItem> paymentItems=paymentItemRepository.findByPaymentId(payment.getId());
                if(!ObjectUtils.isEmpty(paymentItems)){
                    for(PaymentItem paymentItem:paymentItems){
                        if(ObjectUtil.notEqual(paymentItem.getPayStatus(),3))
                            continue;

                        PaymentItem newPaymentItem=new PaymentItem();
                        BeanUtils.copyProperties(paymentItem,newPaymentItem);
                        newPaymentItem.setId(null);
                        newPaymentItem.setPaymentUuid(newPayment.getUuid());
                        newPaymentItem.setPaymentId(newPayment.getId());
                        newPaymentItem.setUuid(UUIDUtil.randomUUID());
                        newPaymentItem.setReturnCode(UUIDUtil.randomUUID());
                        paymentItemRepository.save(newPaymentItem);

                        PaymentItemExtra paymentItemExtra=paymentItemExtraRepository.findOneByPaymentItemId(paymentItem.getId());
                        if(!ObjectUtils.isEmpty(paymentItemExtra)){
                            PaymentItemExtra newPaymentItemExtra=new PaymentItemExtra();
                            BeanUtils.copyProperties(paymentItemExtra,newPaymentItemExtra);
                            newPaymentItemExtra.setId(null);
                            newPaymentItemExtra.setPaymentItemId(newPaymentItem.getId());
                            newPaymentItemExtra.setUuid(UUIDUtil.randomUUID());
                            paymentItemExtraRepository.save(newPaymentItemExtra);
                        }
                        payService.refundByPaymentItemId(newPaymentItem.getId());
                    }
                }
            }
        }
    }

    private void returnCustomerCardTimeByCopyAndSave(OrderResponseDto order, Long tradeNewId, String tradeUuid) {
        List<CustomerCardTime> customerCardTimes = order.getCustomerCardTimes();
        if(customerCardTimes != null && customerCardTimes.size()>0){
            customerCardTimes.forEach(customerCardTime ->{
                CustomerCardTime newCustomerCardTime = new CustomerCardTime();
                BeanUtils.copyProperties(customerCardTime, newCustomerCardTime);
                newCustomerCardTime.setId(null);
                newCustomerCardTime.setTradeId(tradeNewId);
                newCustomerCardTime.setTradeUuid(tradeUuid);
                customerCardTimeRepository.save(newCustomerCardTime);
            });
        }
    }

    private void returnTradeItemsByCopyAndSave(OrderResponseDto order, Long tradeNewId, String tradeUuid) {
        List<TradeItem> tradeItems = order.getTradeItems();
        if(tradeItems != null && tradeItems.size()>0){
            tradeItems.forEach(tradeItem->{
                TradeItem newTradeItem = new TradeItem();
                BeanUtils.copyProperties(tradeItem, newTradeItem);
                newTradeItem.setId(null);
                newTradeItem.setTradeId(tradeNewId);
                newTradeItem.setTradeUuid(tradeUuid);
                newTradeItem.setUuid(UUID.randomUUID().toString().substring(0, 32));
                tradeItemRepository.save(newTradeItem);
                    //copy tradeItemProperties
                List<TradeItemProperty> tradeItemProperties = order.getTradeItemProperties();
                if (tradeItemProperties != null && tradeItemProperties.size()>0){
                    tradeItemProperties.forEach(tradeItemPropertie->{
                        if (tradeItemPropertie.getTradeItemId() == tradeItem.getId()){
                            TradeItemProperty newTradeItemProperty = new TradeItemProperty();
                            BeanUtils.copyProperties(tradeItem, newTradeItem);
                            newTradeItemProperty.setId(null);
                            newTradeItemProperty.setTradeId(tradeNewId);
                            newTradeItemProperty.setTradeUuid(tradeUuid);
                            newTradeItemProperty.setTradeItemId(newTradeItem.getId());
                            newTradeItemProperty.setTradeItemUuid(newTradeItem.getUuid());
                            newTradeItemProperty.setUuid(UUID.randomUUID().toString().substring(0, 32));
                            tradeItemPropertyRepository.save(newTradeItemProperty);
                        }
                    });
                }

                //copy tradePrivileges and save
                List<TradePrivilege> tradePrivileges = order.getTradePrivileges();
                if (tradePrivileges != null && tradePrivileges.size()>0){
                    tradePrivileges.forEach(tradePrivilege ->{
                                if (tradePrivilege.getTradeItemId() == tradeItem.getId()) {
                                    TradePrivilege newTradePrivilege = new TradePrivilege();
                                    BeanUtils.copyProperties(tradePrivilege, newTradePrivilege);
                                    newTradePrivilege.setId(null);
                                    newTradePrivilege.setTradeId(tradeNewId);
                                    newTradePrivilege.setTradeUuid(tradeUuid);
                                    newTradePrivilege.setTradeItemId(newTradeItem.getId());
                                    newTradePrivilege.setTradeItemUuid(newTradeItem.getUuid());
                                    newTradePrivilege.setUuid(UUID.randomUUID().toString().substring(0, 32));
                                    tradePrivilegeRepository.save(newTradePrivilege);
                                }
                    });
                }

                // copy tradeUsers and save
                List<TradeUser> tradeUsers = order.getTradeUsers();
                if (tradeUsers != null && tradeUsers.size()>0){
                    tradeUsers.forEach(tradeUser ->{
                        if (tradeUser.getTradeItemId() == tradeItem.getId()) {
                            TradeUser newTradeUser = new TradeUser();
                            BeanUtils.copyProperties(tradeUser, newTradeUser);
                            newTradeUser.setId(null);
                            newTradeUser.setTradeId(tradeNewId);
                            newTradeUser.setTradeUuid(tradeUuid);
                            newTradeUser.setTradeItemId(newTradeItem.getId());
                            newTradeUser.setTradeItemUuid(newTradeItem.getUuid());
                            tradeUserRepository.save(newTradeUser);
                        }
                    });
                }

            });
        }
        List<TradePrivilege> tradePrivileges = order.getTradePrivileges();
        if (tradePrivileges != null && tradePrivileges.size()>0){
            tradePrivileges.forEach(tradePrivilege ->{
                if (tradePrivilege.getTradeItemId()==null||tradePrivilege.getTradeItemId()<1l) {
                    TradePrivilege newTradePrivilege = new TradePrivilege();
                    BeanUtils.copyProperties(tradePrivilege, newTradePrivilege);
                    newTradePrivilege.setId(null);
                    newTradePrivilege.setTradeId(tradeNewId);
                    newTradePrivilege.setTradeUuid(tradeUuid);
                    newTradePrivilege.setUuid(UUID.randomUUID().toString().substring(0, 32));
                    tradePrivilegeRepository.save(newTradePrivilege);
                }
            });
        }

        List<TradeUser> tradeUsers = order.getTradeUsers();
        if (tradeUsers != null && tradeUsers.size()>0){
            tradeUsers.forEach(tradeUser ->{
                if (tradeUser.getTradeItemId()==null||tradeUser.getTradeItemId()<1l) {
                    TradeUser newTradeUser = new TradeUser();
                    BeanUtils.copyProperties(tradeUser, newTradeUser);
                    newTradeUser.setId(null);
                    newTradeUser.setTradeId(tradeNewId);
                    newTradeUser.setTradeUuid(tradeUuid);
                    tradeUserRepository.save(newTradeUser);
                }
            });
        }

    }

    private void returnTradeTableByCopyAndSave(OrderResponseDto order, Long tradeNewId, String tradeUuid) {
        List<TradeTable> tradeTables = order.getTradeTables();
        if(tradeTables != null && tradeTables.size()>0){
            tradeTables.forEach(tradeTable->{
                TradeTable newTradeTable = new TradeTable();
                BeanUtils.copyProperties(tradeTable, newTradeTable);
                newTradeTable.setId(null);
                newTradeTable.setTradeId(tradeNewId);
                newTradeTable.setTradeUuid(tradeUuid );
                newTradeTable.setUuid(UUID.randomUUID().toString().substring(0, 32));
                tradeTableRepository.save(newTradeTable);
            });
        }
    }

    private void returnTradeCustomerByCopyAndSave(OrderResponseDto order, Long tradeNewId, String tradeUuid) {
        List<TradeCustomer> tradeCustomers = order.getTradeCustomers();
        if (tradeCustomers != null && tradeCustomers.size()>0){
            tradeCustomers.forEach(tradeCustomer->{
                TradeCustomer newTradeCustomer = new TradeCustomer();
                BeanUtils.copyProperties(tradeCustomer, newTradeCustomer);
                newTradeCustomer.setId(null);
                newTradeCustomer.setTradeId(tradeNewId);
                newTradeCustomer.setTradeUuid(tradeUuid);
                newTradeCustomer.setUuid(UUID.randomUUID().toString().substring(0, 32));
                tradeCustomerRepository.save(newTradeCustomer);
            });
        }
    }

    private Trade returnTradeByCopyAndSave(CancelTradeBo cancelTradeBo, Trade trade) {
        Trade tradeNew = new Trade();
        BeanUtils.copyProperties(trade, tradeNew);
        tradeNew.setId(null);
        tradeNew.setRelateTradeId(cancelTradeBo.getContent().getTradeId());
        tradeNew.setTradeType(2);
        if(tradeNew.getTradePayStatus()==3)
            tradeNew.setTradePayStatus(4);
        String oldTradeNo=trade.getTradeNo();
        String newTradeNo=oldTradeNo.substring(0,3)+ StringUtil.getCurrentTime(null)+oldTradeNo.substring(oldTradeNo.length()-7);
        tradeNew.setUuid(UUIDUtil.randomUUID());
        tradeNew.setTradeNo(newTradeNo);
        tradeRepository.save(tradeNew);
        return tradeNew;
    }

    private List<Tables> getRelatedTablesByTrade(List<TradeTable> tradeTables, boolean needDelData) {
        List<Tables> tablesList = null;
        List<Long> tableIds = null;
        for (TradeTable tradeTable : tradeTables) {
            tableIds = new ArrayList<>();
            tableIds.add(tradeTable.getTableId());
        }
        if (tableIds != null && tableIds.size() > 0) {
            if(needDelData){
                tablesList = tablesRepository.findAllById(tableIds);
            }else{
                tablesList = tablesRepository.findAllByIdInAndStatusFlagAndBrandIdentyAndShopIdenty(tableIds, Constants.DATA_ENABLE, WebUtil.getCurrentBrandId(),WebUtil.getCurrentStoreId());
            }
        }
        return tablesList;
    }

    private void modifyInventory(List<InventoryItemsDto> deductInventoryItems, boolean isAddQty) {
        for (InventoryItemsDto dto : deductInventoryItems) {
            Long dishId = dto.getDishId();
            String dishName = dto.getDishName();
            Double price = dto.getPrice();
            Double quantity = dto.getQuantity();
            Optional<DishShop> optional = dishShopRepository.findById(dishId);
            if (!optional.isPresent()) {
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

    private List<TradeUser> modifyTradeUser(List<TradeItemBo> tradeItemBoList,List<TradeUserBo> tradeUserBos) {
        List<TradeUser> tradeUsers = new ArrayList<TradeUser>();
        for (TradeUserBo bo : tradeUserBos) {
            TradeUser tradeUser = bo.copyTo(TradeUser.class);
            for (TradeItemBo itemBo : tradeItemBoList) {
                String uuid = itemBo.getUuid();
                String tradeItemUuid = tradeUser.getTradeItemUuid();
                if(Objects.nonNull(tradeItemUuid)&&Objects.nonNull(uuid)
                        &&tradeItemUuid.equals(uuid)){
                    tradeUser.setTradeItemId(itemBo.getId());
                    break;
                }
            }
            tradeUser = tradeUserRepository.save(tradeUser);
            tradeUsers.add(tradeUser);
        }
        return tradeUsers;
    }

    private List<TradeItemProperty> modifyTradeItemProperties(List<TradeItemBo> tradeItemBoList,List<TradeItemPropertyBo> tradeItemPropertieBos) {
        List<TradeItemProperty> tradeItemPropertiesList = new ArrayList<TradeItemProperty>();
        for (TradeItemPropertyBo bo : tradeItemPropertieBos) {
            TradeItemProperty tradeItemProperty = bo.copyTo(TradeItemProperty.class);
            for (TradeItemBo itemBo : tradeItemBoList) {
                String uuid = itemBo.getUuid();
                String tradeItemUuid = tradeItemProperty.getTradeItemUuid();
                if(Objects.nonNull(tradeItemUuid)&&Objects.nonNull(uuid)
                        &&tradeItemUuid.equals(uuid)){
                    tradeItemProperty.setTradeItemId(itemBo.getId());
                    break;
                }
            }
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

    private List<TradePrivilege> modifyTradePrivileges(List<TradeItemBo> tradeItemBoList,List<TradePrivilegeBo> tradePrivilegeBos) {
        List<TradePrivilege> tradePrivileges = new ArrayList<TradePrivilege>();
        for (TradePrivilegeBo bo : tradePrivilegeBos) {
            TradePrivilege tradePrivilege = bo.copyTo(TradePrivilege.class);
            for (TradeItemBo itemBo : tradeItemBoList) {
                String uuid = itemBo.getUuid();
                String tradeItemUuid = tradePrivilege.getTradeItemUuid();
                if(Objects.nonNull(tradeItemUuid)&&Objects.nonNull(uuid)
                        &&tradeItemUuid.equals(uuid)){
                    tradePrivilege.setTradeItemId(itemBo.getId());
                    break;
                }
            }
            tradePrivilege = tradePrivilegeRepository.save(tradePrivilege);
            tradePrivileges.add(tradePrivilege);
        }
        return tradePrivileges;
    }

    private List<TradeTable> modifyTradeTable(List<TradeTableBo> tradeTableBos,OrderResponseDto orderResponseDto) {
        List<TradeTable> tradeTableList = new ArrayList<TradeTable>();
        List<Tables> tablesList = new ArrayList<>();
        for (TradeTableBo bo : tradeTableBos) {
            //1.修改桌台信息trade_table
            TradeTable tradeTable = bo.copyTo(TradeTable.class);
            tradeTable = tradeTableRepository.save(tradeTable);
            tradeTableList.add(tradeTable);
            //2.维护桌台信息tables
            Optional<Tables> optional = tablesRepository.findById(bo.getTableId());
            if (!optional.isPresent()) {
                logger.error("根据ID未找到可维护桌台信息,TABLE_ID:" + bo.getTableId());
                throw new BusinessException("根据ID未找到可维护桌台信息,TABLE_ID:" + bo.getTableId());
            }
            Tables tables = optional.get();
            if (Constants.DATA_ENABLE.equals(bo.getStatusFlag()) && Constants.SELF_TABLE_STATUS_LOCK.equals(bo.getSelfTableStatus())) {
                tables.setTableStatus(Constants.SELF_TABLE_STATUS_LOCK);
            } else {
                tables.setTableStatus(Constants.SELF_TABLE_STATUS_UNLOCK);
            }
            Tables tables1 = tablesRepository.save(tables);
            tablesList.add(tables1);
        }
        orderResponseDto.setTables(tablesList);
        return tradeTableList;
    }

    @Override
    public Trade getTradeByTradeNo(String tradeNo){
        return tradeRepository.findByTradeNo(tradeNo);
    }

    @Override
    public TradeBo getTradeByTradeId(Long tradeId){
        Optional<Trade> trade=tradeRepository.findById(tradeId);
        if(!trade.isPresent())
            return null;
        else
            return trade.get().copyTo(TradeBo.class);
    }


    @Override
    public Long getCustomerIdByType(Long tradeId,Integer type){
        List<TradeCustomer> tradeCustomers=tradeCustomerRepository.findAllByTradeIdAndStatusFlag(tradeId,1);
        if(!ObjectUtils.isEmpty(tradeCustomers)){
            for(TradeCustomer tradeCustomer:tradeCustomers){
                if(type.equals(tradeCustomer.getCustomerType()))
                    return tradeCustomer.getCustomerId();
            }
        }
        return null;
    }

}
