package com.ticket.api;

import com.ticket.bean.AlipayVo;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.CityListResp;
import com.ticket.bean.CityVo;
import com.ticket.bean.FrequencyListResp;
import com.ticket.bean.FrequencyVo;
import com.ticket.bean.MessageVo;
import com.ticket.bean.OrderCreateVoResp;
import com.ticket.bean.OrderDeatilResp;
import com.ticket.bean.OrderDetailVo;
import com.ticket.bean.OrderVo;
import com.ticket.bean.OrderVoResp;
import com.ticket.bean.PassengerListResp;
import com.ticket.bean.PassengerVo;
import com.ticket.bean.UserVo;
import com.ticket.bean.WXPayVo;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Apis {

    //购票系统BaseUri
    String TROYCD = "http://api.troycd.com:9000/API/";

    /**
     * 短信接口
     *
     * @param mobileNumber
     * @return {"VerifyCode":"652821","IsSuccessfully":true,"ErrorMessage":""}
     */
    @GET("Messages/SendVerifyCode.ashx")
    Call<MessageVo> getVerifyCode(
            @Query("mobileNumber") String mobileNumber
    );


    /**
     * 注册账号
     *
     * @param mobileNumber
     * @param verifyCode
     * @param password
     * @return {"IsSuccessfully":true,"ErrorMessage":""}
     */
    @GET("User/Register.ashx")
    Call<BaseInfoVo> register(
            @Query("mobileNumber") String mobileNumber,
            @Query("password") String password,
            @Query("verifyCode") String verifyCode
    );

    /**
     * 重置密码 & 找回密码
     *
     * @param mobileNumber
     * @param verifyCode
     * @param password
     * @return {"IsSuccessfully":true,"ErrorMessage":""}
     */
    @GET("User/Resetpassword.ashx")
    Call<BaseInfoVo> resetPassWord(
            @Query("mobileNumber") String mobileNumber,
            @Query("password") String password,
            @Query("verifyCode") String verifyCode
    );


    /**
     * 登陆
     *
     * @param mobileNumber
     * @param password
     * @return {"UserID":"57f0e4e9-0e46-4047-83bd-127f22e646b3","IsSuccessfully":true,"ErrorMessage":""}
     */
    @GET("User/Authentication.ashx")
    Call<UserVo> login(
            @Query("mobileNumber") String mobileNumber,
            @Query("password") String password
    );

    /**
     * 添加乘客
     *
     * @param userID
     * @param mobileNumber
     * @param idCard
     * @param fullName
     * @return {"IsSuccessfully":true,"ErrorMessage":""}
     */
    @GET("User/AddPassengers.ashx")
    Call<BaseInfoVo> addPassengers(
            @Query("userID") String userID,
            @Query("mobileNumber") String mobileNumber,
            @Query("idCard") String idCard,
            @Query("fullName") String fullName
    );

    /**
     * 获取乘客
     *
     * @param userID
     * @return {"Passengers":[{"FullName":"李四","IDCard":"510123198208120316","MobileNumber":"13800138000"}],"IsSuccessfully":true,"ErrorMessage":""}
     */
    @GET("User/GetPassengers.ashx")
    Call<PassengerListResp<List<PassengerVo>>> getPassengers(
            @Query("userID") String userID
    );

    /**
     * 删除乘客
     *
     * @param passengerID
     * @return
     */
    @GET("User/DeletePassenger.ashx")
    Call<PassengerListResp> deletePassenger(
            @Query("passengerID") String passengerID
    );

    /**
     * 获取出发城市列表
     */
    @GET("Bus/GetOriginatingCity.ashx")
    Call<CityListResp<List<CityVo>>> getOriginatingCity();

    /**
     * 获取出发热门城市
     */
    @GET("Bus/GetHotOriginatingCity.ashx")
    Call<CityListResp<List<CityVo>>> getHotOriginatingCity();

    /**
     * 获取到达城市列表
     *
     * @param startCityID
     */
    @GET("Bus/GetDestinationCities.ashx")
    Call<CityListResp<List<CityVo>>> getDestinationCities(
            @Query("startCityID") String startCityID
    );

    /**
     * 获取到达热门城市
     *
     * @param startCityID
     */
    @GET("Bus/GetHotDestinationCities.ashx")
    Call<CityListResp<List<CityVo>>> getHotDestinationCities
            (@Query("startCityID") String startCityID
    );

    /**
     * 获取车辆班次
     *
     * @param startCityID 开始城市
     * @param stopCityID  到达城市
     * @param goDate      发车时间 格式 2015-11-12
     * @return
     */
    @GET("Bus/GetFrequencyList.ashx")
    Call<FrequencyListResp<List<FrequencyVo>>> getFrequencyList(
            @Query("startCityID") String startCityID,
            @Query("stopCityID") String stopCityID,
            @Query("goDate") String goDate
    );

    /**
     * 创建订单
     *
     * @param routingID      班次编码
     * @param userID         用户编码
     * @param passengers     乘客编码 多个以逗号分割
     * @param isBuyInsurance 是否买保险
     * @return
     */
    @GET("Orders/CreateOrder.ashx")
    Call<OrderCreateVoResp> createOrder(
            @Query("routingID") String routingID,
            @Query("userID") String userID,
            @Query("passengers") String passengers,
            @Query("isBuyInsurance") int isBuyInsurance
    );


    /**
     * 获取订单
     * @param userID
     * @return
     */
    @GET("Orders/GetOrders.ashx")
    Call<OrderVoResp<List<OrderVo>>> getOrders(
            @Query("userID") String userID,
            @Query("isPaid") int isPaid
    );

    /**
     * 订单详情
     * @param orderID
     * @return
     */
    @GET("Orders/GetOrderDetails.ashx")
    Call<OrderDeatilResp<OrderDetailVo>> getOrderDetails(
            @Query("orderID") String orderID
    );

    /**
     * 退票
     * @param orderDetailID
     * @return
     */
    @GET("Orders/RefundTicket.ashx")
    Call<BaseInfoVo> refundTicket(
            @Query("orderDetailID") String orderDetailID
    );

    /**
     * 获取支付宝签名信息
     * @param orderID
     * @return
     */
    @GET("Orders/SignAlipay.ashx")
    Call<AlipayVo> signAlipay(
            @Query("orderID") String orderID
    );

    /**
     * 获取微信支付签名信息
     * @param orderID
     * @return
     */
    @GET("Orders/PayOrderByWeiChat.ashx")
    Call<WXPayVo> payOrderByWeiChat(
            @Query("orderID") String orderID
    );
}
