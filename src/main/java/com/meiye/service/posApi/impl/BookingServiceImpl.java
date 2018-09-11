package com.meiye.service.posApi.impl;

import com.alibaba.fastjson.JSON;
import com.meiye.bo.booking.BookingBo;
import com.meiye.bo.booking.BookingTradeItemBo;
import com.meiye.bo.booking.BookingTradeItemUserBo;
import com.meiye.bo.booking.dto.BookingRequestDto;
import com.meiye.bo.booking.dto.BookingResponseDto;
import com.meiye.exception.BusinessException;
import com.meiye.model.booking.Booking;
import com.meiye.model.booking.BookingTradeItem;
import com.meiye.model.booking.BookingTradeItemUser;
import com.meiye.repository.booking.BookingRepository;
import com.meiye.repository.booking.BookingTradeItemRepository;
import com.meiye.repository.booking.BookingTradeItemUserRepository;
import com.meiye.service.posApi.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        //1.booking
        Booking booking = content.copyTo(Booking.class);
        logger.info("创建预定接口-新增预定记录主单开始");
        logger.info("创建预定接口-预定订单数据："+ JSON.toJSON(booking).toString());
        booking = bookingRepository.save(booking);
        bookingResponseDto.setBooking(booking);
        logger.info("创建预定接口-新增预定记录主单结束");
        //2.booking trade item
        Long bookingId = booking.getId();
        bookingTradeItemBos.stream().forEach(bo->bo.setBookingId(bookingId));
        logger.info("创建预定接口-新增预定记录明细开始");
        logger.info("创建预定接口-预定交易明细数据："+ JSON.toJSON(bookingTradeItemBos).toString());
        List<BookingTradeItem> bookingTradeItems = this.modifyBookingTradeItemBos(bookingTradeItemBos);
        bookingResponseDto.setBookingTradeItems(bookingTradeItems);
        logger.info("创建预定接口-新增预定记录明细结束");
        //3.booking trade item user
        bookingTradeItemUserBos.stream().forEach(bo->{
            bo.setBookingId(bookingId);
            String bookingTradeItemUuid = bo.getBookingTradeItemUuid();
            BookingTradeItem bookIngTradeItemByUuid = findBookIngTradeItemByUuid(bookingTradeItems, bookingTradeItemUuid);
            bo.setBookingTradeItemId(bookIngTradeItemByUuid.getId());
        });
        logger.info("创建预定接口-新增预定订单销售员与订单商品关系开始");
        logger.info("创建预定接口-预定订单销售员与订单商品关系数据："+ JSON.toJSON(bookingTradeItemUserBos).toString());
        List<BookingTradeItemUser> bookingTradeItemUserList = this.modifyBookingTradeItemUserBos(bookingTradeItemUserBos);
        bookingResponseDto.setBookingTradeItemUsers(bookingTradeItemUserList);
        logger.info("创建预定接口-新增预定订单销售员与订单商品关系结束");

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
        logger.info("修改预定接口-修改预定交易明细开始");
        logger.info("修改预定接口-预定交易明细数据："+ JSON.toJSON(bookingTradeItemBos).toString());
        List<BookingTradeItem> bookingTradeItems = this.modifyBookingTradeItemBos(bookingTradeItemBos);
        bookingResponseDto.setBookingTradeItems(bookingTradeItems);
        logger.info("修改预定接口-修改预定记录明细结束");
        //3.booking trade item user
        logger.info("修改预定接口-修改预定订单销售员与订单商品关系开始");
        logger.info("修改预定接口-预定订单销售员与订单商品关系数据："+ JSON.toJSON(bookingTradeItemUserBos).toString());
        List<BookingTradeItemUser> bookingTradeItemUserList = this.modifyBookingTradeItemUserBos(bookingTradeItemUserBos);
        bookingResponseDto.setBookingTradeItemUsers(bookingTradeItemUserList);
        logger.info("修改预定接口-修改预定订单销售员与订单商品关系结束");

        return bookingResponseDto;
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

}
