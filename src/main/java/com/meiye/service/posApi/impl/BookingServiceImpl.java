package com.meiye.service.posApi.impl;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.accounting.InternalApiResult;
import com.meiye.bo.booking.BookingBo;
import com.meiye.bo.booking.BookingTradeItemBo;
import com.meiye.bo.booking.BookingTradeItemUserBo;
import com.meiye.bo.booking.dto.*;
import com.meiye.bo.customer.CustomerApiResult;
import com.meiye.bo.trade.OrderDto.OrderRequestDto;
import com.meiye.bo.trade.OrderDto.OrderResponseDto;
import com.meiye.bo.trade.OrderDto.TradeRequestDto;
import com.meiye.bo.trade.TradeCustomerBo;
import com.meiye.exception.BusinessException;
import com.meiye.model.booking.Booking;
import com.meiye.model.booking.BookingTradeItem;
import com.meiye.model.booking.BookingTradeItemUser;
import com.meiye.model.customer.Customer;
import com.meiye.model.trade.TradeCustomer;
import com.meiye.repository.booking.BookingRepository;
import com.meiye.repository.booking.BookingTradeItemRepository;
import com.meiye.repository.booking.BookingTradeItemUserRepository;
import com.meiye.repository.trade.TradeCustomerRepository;
import com.meiye.service.posApi.BookingService;
import com.meiye.service.posApi.OrderService;
import com.meiye.system.util.WebUtil;
import com.meiye.util.Constants;
import com.meiye.util.MeiYeInternalApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Author: ryner
 * @Description:
 * @Date: Created in 11:19 2018/9/10
 * @Modified By:
 */
@Service
public class BookingServiceImpl implements BookingService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BookingTradeItemUserRepository bookingTradeItemUserRepository;
    @Autowired
    BookingTradeItemRepository bookingTradeItemRepository;
    @Autowired
    TradeCustomerRepository tradeCustomerRepository;
    @Autowired
    OrderService orderService;

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public BookingResponseDto addBooking(BookingRequestDto bookingRequestDto) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        BookingBo content = bookingRequestDto.getContent();
        if(Objects.isNull(content) || Objects.isNull(content)){
            logger.error("创建预定接口-预定订单数据为空！");
            throw new BusinessException("创建预定接口-预定订单数据为空！");
        }
        List<BookingTradeItemBo> bookingTradeItemBos = content.getBookingTradeItems();
        List<BookingTradeItemUserBo> bookingTradeItemUserBos = content.getBookingTradeItemUsers();
        List<TradeCustomerBo> tradeCustomerBos = content.getTradeCustomerBos();
        //1.booking
        Booking booking = content.copyTo(Booking.class);
        logger.info("创建预定接口-新增预定记录主单开始");
        logger.info("创建预定接口-预定订单数据："+ JSON.toJSON(booking).toString());
        try{
            CustomerApiResult customerApiResult = MeiYeInternalApi.registCustomer(WebUtil.getCurrentBrandId(),WebUtil.getCurrentStoreId(),booking.getCreatorId(),booking.getCreatorName(),booking.getCommercialName(), booking.getCommercialPhone(), booking.getCommercialGender(), new Long(000));
            Customer data = customerApiResult.getData();
            booking.setCommercialId(data.getId());
        }catch(Exception e){
            logger.error("创建预定接口-调会员接口失败！"+e.getMessage());
        }
        booking = bookingRepository.save(booking);
        bookingResponseDto.setBooking(booking);
        logger.info("创建预定接口-新增预定记录主单结束");
        Long bookingId = booking.getId();
        List<BookingTradeItem>  bookingTradeItems = null;
        //2.booking trade item
        if(bookingTradeItemBos!=null &&bookingTradeItemBos.size()>0){
            bookingTradeItemBos.stream().forEach(bo->bo.setBookingId(bookingId));
            logger.info("创建预定接口-新增预定记录明细开始");
            logger.info("创建预定接口-预定交易明细数据："+ JSON.toJSON(bookingTradeItemBos).toString());
            bookingTradeItems = this.modifyBookingTradeItemBos(bookingTradeItemBos);
            bookingResponseDto.setBookingTradeItems(bookingTradeItems);
            logger.info("创建预定接口-新增预定记录明细结束");
        }

        //3.booking trade item user
        if(bookingTradeItemUserBos!=null &&bookingTradeItemUserBos.size()>0&&bookingTradeItemBos!=null &&bookingTradeItemBos.size()>0){
            List<BookingTradeItem> finalBookingTradeItems = bookingTradeItems;
            bookingTradeItemUserBos.stream().forEach(bo->{
                bo.setBookingId(bookingId);
                String bookingTradeItemUuid = bo.getBookingTradeItemUuid();
                BookingTradeItem bookIngTradeItemByUuid = findBookIngTradeItemByUuid(finalBookingTradeItems, bookingTradeItemUuid);
                bo.setBookingTradeItemId(bookIngTradeItemByUuid.getId());
            });
            logger.info("创建预定接口-新增预定订单销售员与订单商品关系开始");
            logger.info("创建预定接口-预定订单销售员与订单商品关系数据："+ JSON.toJSON(bookingTradeItemUserBos).toString());
            List<BookingTradeItemUser> bookingTradeItemUserList = this.modifyBookingTradeItemUserBos(bookingTradeItemUserBos);
            bookingResponseDto.setBookingTradeItemUsers(bookingTradeItemUserList);
            logger.info("创建预定接口-新增预定订单销售员与订单商品关系结束");
        }
        //4.trade_customer  只有一个会员
//        if(tradeCustomerBos!=null &&tradeCustomerBos.size()>0){
//            TradeCustomerBo tradeCustomerBo = tradeCustomerBos.get(0);
//            CustomerApiResult customerApiResult = MeiYeInternalApi.registCustomer(tradeCustomerBo.getCustomerName(), tradeCustomerBo.getCustomerPhone(), tradeCustomerBo.getCustomerSex(), bookingId);
//            Customer data = customerApiResult.getData();
//            tradeCustomerBos.stream().forEach(bo->{
//                bo.setCustomerId(data.getId());
//            });
//            logger.info("创建预定接口-新增订单用户开始");
//            logger.info("创建预定接口-新增订单用户开始："+ JSON.toJSON(tradeCustomerBos).toString());
//            List<TradeCustomer> tradeCustomers = this.modifyTradeCustomer(tradeCustomerBos);
//            bookingResponseDto.setTradeCustomers(tradeCustomers);
//            logger.info("创建预定接口-新增订单用户开始");
//        }

        return bookingResponseDto;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public BookingResponseDto updateBooking(BookingRequestDto bookingRequestDto) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        BookingBo content = bookingRequestDto.getContent();
        if(Objects.isNull(content) || Objects.isNull(content)){
            logger.error("修改预定接口-修改预定订单数据为空！");
            throw new BusinessException("修改预定接口-修改预定订单数据为空！");
        }
        List<BookingTradeItemBo> bookingTradeItemBos = content.getBookingTradeItems();
        List<BookingTradeItemUserBo> bookingTradeItemUserBos = content.getBookingTradeItemUsers();
        List<TradeCustomerBo> tradeCustomerBos = content.getTradeCustomerBos();
        Booking booking = content.copyTo(Booking.class);
        //校验版本号
        Long bookingId = booking.getId();
        if(Objects.isNull(bookingId)){
            logger.error("修改预定接口-预订单booking_Id is null！");
            throw new BusinessException("修改预定接口-预订单booking_Id is null！");
        }
        Optional<Booking> Optional = bookingRepository.findById(bookingId);
        if(Objects.isNull(Optional)){
            logger.error("修改预定接口-未找到需要修改的预订单！booking_id:"+bookingId);
            throw new BusinessException("修改预定接口-未找到需要修改的预订单！booking_id:"+bookingId);
        }
        Booking booking1 = Optional.get();
        Timestamp clientUpdateTime = booking.getServerUpdateTime();
        Timestamp serverUpdateTime = booking1.getServerUpdateTime();
        if(clientUpdateTime.equals(serverUpdateTime)){
            booking.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
        }else{
            logger.error("修改预定接口-版本校验失败！");
            throw new BusinessException("修改预定接口-版本校验失败！");
        }

        //1.booking
        logger.info("修改预定接口-修改预定记录主单开始");
        logger.info("修改预定接口-预定订单数据："+ JSON.toJSON(booking).toString());
        booking = bookingRepository.save(booking);
        bookingResponseDto.setBooking(booking);
        logger.info("修改预定接口-修改预定记录主单结束");
        //2.booking trade item
        List<BookingTradeItem> bookingTradeItems = null;
        if(bookingTradeItemBos!=null &&bookingTradeItemBos.size()>0){
            logger.info("修改预定接口-修改预定交易明细开始");
            logger.info("修改预定接口-预定交易明细数据："+ JSON.toJSON(bookingTradeItemBos).toString());
            bookingTradeItems = this.modifyBookingTradeItemBos(bookingTradeItemBos);
            bookingResponseDto.setBookingTradeItems(bookingTradeItems);
            logger.info("修改预定接口-修改预定记录明细结束");
        }
        //3.booking trade item user
        if(bookingTradeItemUserBos!=null &&bookingTradeItemUserBos.size()>0){
            logger.info("修改预定接口-修改预定订单销售员与订单商品关系开始");
            logger.info("修改预定接口-预定订单销售员与订单商品关系数据："+ JSON.toJSON(bookingTradeItemUserBos).toString());
            List<BookingTradeItem> finalBookingTradeItems = bookingTradeItems;
            bookingTradeItemUserBos.stream().forEach(bo->{
                if(Objects.isNull(bo.getBookingTradeItemId())){
                    bo.setBookingId(bookingId);
                    String bookingTradeItemUuid = bo.getBookingTradeItemUuid();
                    BookingTradeItem bookIngTradeItemByUuid = findBookIngTradeItemByUuid(finalBookingTradeItems, bookingTradeItemUuid);
                    bo.setBookingTradeItemId(bookIngTradeItemByUuid.getId());
                }
            });

            List<BookingTradeItemUser> bookingTradeItemUserList = this.modifyBookingTradeItemUserBos(bookingTradeItemUserBos);
            bookingResponseDto.setBookingTradeItemUsers(bookingTradeItemUserList);
            logger.info("修改预定接口-修改预定订单销售员与订单商品关系结束");
        }
        //4.trade_customer  只有一个会员
//        List<TradeCustomer> list = new ArrayList<TradeCustomer>();
//        if(tradeCustomerBos!=null &&tradeCustomerBos.size()>0){
//            for(TradeCustomerBo bo:tradeCustomerBos){
//                TradeCustomer tradeCustomer = bo.copyTo(TradeCustomer.class);
//                tradeCustomer = tradeCustomerRepository.save(tradeCustomer);
//                list.add(tradeCustomer);
//            }
//        }
//        bookingResponseDto.setTradeCustomers(list);
        return bookingResponseDto;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public Booking delBooking(CacelBookingDto cacelBookingDto) {
        CacelDetailDto content = cacelBookingDto.getContent();
        if(Objects.isNull(content.getBookingId())){
            logger.error("删除预定接口-删除预定接口数据为空！");
            throw new BusinessException("删除预定接口-booking Id为空，请检查！");
        }
        Optional<Booking> byId = bookingRepository.findById(content.getBookingId());
        if(!byId.isPresent()){
            logger.error("删除预定接口-数据库未找到需要删除的预订单！");
            throw new BusinessException("删除预定接口-数据库未找到需要删除的预订单！");
        }
        Booking booking = byId.get();
       // booking.setStatusFlag(Constants.DATA_DISABLE); 改预订单状态为9，不做物理删除
        if(Objects.nonNull(content.getReason())){
            booking.setRemark(content.getReason());
        }
        if(Objects.nonNull(content.getBrandIdenty())){
            booking.setBrandIdenty(content.getBrandIdenty());
        }
        if(Objects.nonNull(content.getShopIdenty())){
            booking.setShopIdenty(content.getShopIdenty());
        }
        if(Objects.nonNull(content.getCancelOrderUser())){
            booking.setUpdatorId(content.getCancelOrderUser());
        }
        booking.setOrderStatus(Constants.CANCEL_STATUS);
        Booking booking1 = bookingRepository.save(booking);
        return booking1;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public BookToOrderResponseDto updateBookingToOrder(OrderRequestDto orderRequestDto) {
        BookToOrderResponseDto bookToOrderResponseDto = new BookToOrderResponseDto();
        TradeRequestDto content = orderRequestDto.getContent();
        BookingInfoDto bookingInfoDto = content.getBookingInfo();
        if(Objects.isNull(bookingInfoDto)){
            logger.error("预订转订单接口-预订单数据为空！");
            throw new BusinessException("预订转订单接口-预订单数据为空，请检查！");
        }

        //校验版本号
        Long bookingId = bookingInfoDto.getBookingId();
        if(Objects.isNull(bookingId)){
            logger.error("预订转订单接口-预订单booking_Id is null！");
            throw new BusinessException("预订转订单接口-预订单booking_Id is null！");
        }
        Optional<Booking> Optional = bookingRepository.findById(bookingId);
        if(!Optional.isPresent()){
            logger.error("预订转订单接口-未找到需要修改的预订单！booking_id:"+bookingId);
            throw new BusinessException("预订转订单接口-未找到需要修改的预订单！booking_id:"+bookingId);
        }
        Booking booking1 = Optional.get();
        Timestamp clientUpdateTime = new Timestamp(bookingInfoDto.getBookingServerUpdateTime());
        Timestamp serverUpdateTime = booking1.getServerUpdateTime();
        if(clientUpdateTime.equals(serverUpdateTime)){
            booking1.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
        }else{
            logger.error("预订转订单接口-版本校验失败！");
            throw new BusinessException("预订转订单接口-版本校验失败！");
        }
        //修改预订单状态
        booking1.setOrderStatus(Constants.ARRIVE_THE_STORE__STATUS);
        booking1 = bookingRepository.save(booking1);
        //插入新订单信息
        OrderResponseDto orderResponseDto = orderService.addOrderData(orderRequestDto);
        BeanUtils.copyProperties(orderResponseDto, bookToOrderResponseDto);
        bookToOrderResponseDto.setBooking(booking1);
        return bookToOrderResponseDto;
    }

    private List<BookingTradeItem> modifyBookingTradeItemBos(List<BookingTradeItemBo> bookingTradeItemBos){
        List<BookingTradeItem> bookingTradeItemList = new ArrayList<BookingTradeItem>();
        for(BookingTradeItemBo bo:bookingTradeItemBos){
            BookingTradeItem bookingTradeItem = bo.copyTo(BookingTradeItem.class);
            bookingTradeItem = bookingTradeItemRepository.save(bookingTradeItem);
            bookingTradeItemList.add(bookingTradeItem);
        }
        return bookingTradeItemList;
    }

    private  List<TradeCustomer>  modifyTradeCustomer( List<TradeCustomerBo> tradeCustomerBos){
        List<TradeCustomer> list = new ArrayList<TradeCustomer>();
        for(TradeCustomerBo bo:tradeCustomerBos){
            TradeCustomer tradeCustomer = bo.copyTo(TradeCustomer.class);
            tradeCustomer = tradeCustomerRepository.save(tradeCustomer);
            list.add(tradeCustomer);
        }
        return list;
    }

    private List<BookingTradeItemUser> modifyBookingTradeItemUserBos(List<BookingTradeItemUserBo> bookingTradeItemBos){
        List<BookingTradeItemUser> bookingTradeItemUserList = new ArrayList<BookingTradeItemUser>();
        for(BookingTradeItemUserBo bo:bookingTradeItemBos){
            BookingTradeItemUser bookingTradeItemUser = bo.copyTo(BookingTradeItemUser.class);
            bookingTradeItemUser = bookingTradeItemUserRepository.save(bookingTradeItemUser);
            bookingTradeItemUserList.add(bookingTradeItemUser);
        }
        return bookingTradeItemUserList;
    }

    private BookingTradeItem findBookIngTradeItemByUuid(List<BookingTradeItem> bookingTradeItems, String uuid){
        if(Objects.isNull(uuid)){
            logger.error("创建/修改预定接口-uuid为空，技师无法关联交易明细单。");
            throw new BusinessException("创建/修改预定接口-uuid为空，技师无法关联交易明细单。");
        }
        BookingTradeItem bookingTradeItemByUuid = null;
        for(BookingTradeItem bookingTradeItem :bookingTradeItems){
            if(uuid.equals(bookingTradeItem.getUuid())){
                bookingTradeItemByUuid = bookingTradeItem;
                break;
            }
        }
        if(Objects.isNull(bookingTradeItemByUuid)){
            logger.error("创建/修改预定接口-通过uuid：\"+uuid+\"，未找到技师所关联的交易明细单。");
            throw new BusinessException("创建/修改预定接口-通过uuid："+uuid+"，未找到技师所关联的交易明细单。");
        }
        return bookingTradeItemByUuid;
    }

    @Override
    public ReadyBookedUserResponseDto getAllAlreadyBookedUser(ReadyBookedUserRequestDto readyBookedUserRequestDto) {
        if(readyBookedUserRequestDto.getBrandID() == null || readyBookedUserRequestDto.getShopID() ==null
                || readyBookedUserRequestDto.getContent() == null || readyBookedUserRequestDto.getContent().getStartTime() ==null
                || readyBookedUserRequestDto.getContent().getEndTime() == null || readyBookedUserRequestDto.getDeviceID() ==null){
            logger.error("检查技师列表接口-接口数据验证失败");
            throw new BusinessException("检查技师列表接口-接口数据验证失败");
        }

        Set<Long> userIDs = bookingTradeItemUserRepository.getAllAlreadyBookedUser(readyBookedUserRequestDto.getShopID(), readyBookedUserRequestDto.getBrandID()
                , readyBookedUserRequestDto.getContent().getStartTime(), readyBookedUserRequestDto.getContent().getEndTime(),readyBookedUserRequestDto.getDeviceID());

        ReadyBookedUserResponseDto readyBookedUserResponseDto= new ReadyBookedUserResponseDto();
        readyBookedUserResponseDto.setStartTime(readyBookedUserRequestDto.getContent().getStartTime());
        readyBookedUserResponseDto.setEndTime(readyBookedUserRequestDto.getContent().getEndTime());
        readyBookedUserResponseDto.setUserIds(userIDs);
        return readyBookedUserResponseDto;
    }

    @Override
    public Page<Booking> getBookPageByCriteria(Integer pageNum, Integer pageSize, BookingPageRequestDto bookingPageRequestDto) {
        Pageable pageable = new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "serverCreateTime");
        Page<Booking> usersPage = bookingRepository.findAll(new Specification<Booking>() {
            @Override
            public Predicate toPredicate(Root<Booking> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if (null != bookingPageRequestDto.getBrandID()) {
                    list.add(criteriaBuilder.equal(root.get("brandIdenty").as(Long.class), bookingPageRequestDto.getBrandID()));
                }
                if (null != bookingPageRequestDto.getShopID()) {
                    list.add(criteriaBuilder.equal(root.get("shopIdenty").as(Long.class), bookingPageRequestDto.getShopID()));
                }
                if(2 == bookingPageRequestDto.getContent().getType() ){//已超时
                    if (null != bookingPageRequestDto.getContent().getStartTime()) {
                        list.add(criteriaBuilder.equal(root.get("orderStatus").as(Integer.class),Constants.NOT_TO_THE_STORE__STATUS));
                        list.add(criteriaBuilder.lessThan(root.get("startTime").as(Date.class),bookingPageRequestDto.getContent().getStartTime()));
                    }
                }else{
                    if (null != bookingPageRequestDto.getContent().getStartTime()) {
                        list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime").as(Date.class),bookingPageRequestDto.getContent().getStartTime()));
                    }
                    if (null != bookingPageRequestDto.getContent().getEndTime()) {
                        list.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime").as(Date.class),bookingPageRequestDto.getContent().getEndTime()));
                    }
                    if (null != bookingPageRequestDto.getContent().getType()) {
                        if(1 == bookingPageRequestDto.getContent().getType()){ //待服务
                            list.add(criteriaBuilder.equal(root.get("orderStatus").as(Integer.class),Constants.NOT_TO_THE_STORE__STATUS));
                        }else if (3 == bookingPageRequestDto.getContent().getType()){//已取消
                            list.add(criteriaBuilder.equal(root.get("orderStatus").as(Integer.class),Constants.CANCEL_STATUS));
                        }else if (4 == bookingPageRequestDto.getContent().getType()){//未处理订单
                            list.add(criteriaBuilder.equal(root.get("orderStatus").as(Integer.class),Constants.PENNDING_OPERATE_STATUS));
                        }
                    }
                }

                list.add(criteriaBuilder.equal(root.get("statusFlag").as(Long.class), 1));
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);
        return usersPage;
    }

    @Override
    public BookingPageResponseDto getPageBooking(Integer page, Integer pageCount, BookingPageRequestDto bookingPageRequestDto) {
        if(page-1<= 0){
            page=0;
        }else {
            page = page -1;
        }
        Page<Booking> pageByCriteria = getBookPageByCriteria(page, pageCount, bookingPageRequestDto);
        BookingPageResponseDto bookingPageResponseDto = new BookingPageResponseDto();
        List<Booking> content = pageByCriteria.getContent();
        if(content != null && content.size()>0){
            List<BookingTradeItem> bookingTradeItems = new ArrayList<>();
            List<BookingTradeItemUser> bookingTradeItemUsers = new ArrayList<>();
            for (int i = 0; i < content.size(); i++) {
                Long id = content.get(i).getId();
                //得到TradeItem
                List<BookingTradeItem> byBookingId = bookingTradeItemRepository.findByBookingIdAndStatusFlag(id,1);
                if(byBookingId!=null && byBookingId.size()>0){
                    bookingTradeItems.addAll(byBookingId);
                }
                //得到User
                List<BookingTradeItemUser> byBookingIdAndStatusFlag = bookingTradeItemUserRepository.findByBookingIdAndStatusFlag(id, 1);
                if (byBookingIdAndStatusFlag != null && byBookingIdAndStatusFlag.size()>0){
                    bookingTradeItemUsers.addAll(byBookingIdAndStatusFlag);
                }
            }
            bookingPageResponseDto.setBookings(content);
            bookingPageResponseDto.setBookingTradeItems(bookingTradeItems);
            bookingPageResponseDto.setBookingTradeItemUsers(bookingTradeItemUsers);
        }



        return bookingPageResponseDto;
    }

    @Override
    public Booking updateWxBookingStatus(WxBookingRequestDto wxBookingRequestDto) {
        WxBookingContentDto content = wxBookingRequestDto.getContent();
        if(Objects.isNull(content)){
            logger.error("更新微信预订单接口-更新微信预订单状态数据为空！");
            throw new BusinessException("更新微信预订单接口-更新微信预订单状态数据为空，请检查！");
        }
        //校验版本号
        Long bookingId = content.getBookingId();
        if(Objects.isNull(bookingId)){
            logger.error("更新微信预订单接口-预订单booking_Id is null！");
            throw new BusinessException("更新微信预订单接口-预订单booking_Id is null！");
        }
        Optional<Booking> Optional = bookingRepository.findById(bookingId);
        if(!Optional.isPresent()){
            logger.error("更新微信预订单接口-未找到需要修改的预订单！booking_id:"+bookingId);
            throw new BusinessException("更新微信预订单接口-未找到需要修改的预订单！booking_id:"+bookingId);
        }
        Booking booking = Optional.get();
        Timestamp clientUpdateTime = new Timestamp(content.getBookingServerUpdateTime());
        Timestamp serverUpdateTime = booking.getServerUpdateTime();
        if(clientUpdateTime.equals(serverUpdateTime)){
            booking.setServerUpdateTime(new Timestamp(System.currentTimeMillis()));
        }else{
            logger.error("更新微信预订单接口-版本校验失败！");
            throw new BusinessException("更新微信预订单接口-版本校验失败！");
        }
        Integer orderStatus = booking.getOrderStatus();
        if(Constants.PENNDING_OPERATE_STATUS != orderStatus){
            logger.error("更新微信预订单接口-该订单状态不允许执行此操作！");
            throw new BusinessException("更新微信预订单接口-该订单状态不允许执行此操作！");
        }
        booking.setOrderStatus(content.getToOrderStatus());
        Booking saveBooking = bookingRepository.save(booking);
        return saveBooking;
    }


}
