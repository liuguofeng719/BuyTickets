package com.ticket.api;

import com.ticket.bean.AlipayVo;
import com.ticket.bean.BalanceChangeVo;
import com.ticket.bean.BalanceChangeVoResp;
import com.ticket.bean.BalanceVo;
import com.ticket.bean.BankAccountVo;
import com.ticket.bean.BankAccountVoResp;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.CityInfoResp;
import com.ticket.bean.CityListResp;
import com.ticket.bean.CityVo;
import com.ticket.bean.FrequencyListResp;
import com.ticket.bean.FrequencyVo;
import com.ticket.bean.LeasedVehicleListResp;
import com.ticket.bean.LeasedVehicleOrder;
import com.ticket.bean.MessageListResp;
import com.ticket.bean.MessageVo;
import com.ticket.bean.MessagesVo;
import com.ticket.bean.OrderCreateVoResp;
import com.ticket.bean.OrderDeatilResp;
import com.ticket.bean.OrderDetailVo;
import com.ticket.bean.OrderVo;
import com.ticket.bean.OrderVoResp;
import com.ticket.bean.PassengerListResp;
import com.ticket.bean.PassengerVo;
import com.ticket.bean.PicturesVo;
import com.ticket.bean.PicturesVoResp;
import com.ticket.bean.PlaceListResp;
import com.ticket.bean.PlaceVo;
import com.ticket.bean.ProvincesResp;
import com.ticket.bean.ProvincesVo;
import com.ticket.bean.ShareMessageVo;
import com.ticket.bean.TravelOrderDetailVoResp;
import com.ticket.bean.TravelOrdersVo;
import com.ticket.bean.TravelOrdersVoResp;
import com.ticket.bean.TravelRoutingListVo;
import com.ticket.bean.TravelRoutingListVoResp;
import com.ticket.bean.TravelRoutingVo;
import com.ticket.bean.TravelRoutingVoResp;
import com.ticket.bean.UserVo;
import com.ticket.bean.VehicleOrdersDetailsVoResp;
import com.ticket.bean.VersionVo;
import com.ticket.bean.WXPayVo;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Apis {

    //购票系统BaseUri
    String TROYCD = "http://api.troycd.com:9010/API/";

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
     *
     * @Param ProvinceID
     */
    @GET("Bus/GetOriginatingCity.ashx")
    Call<CityListResp<List<CityVo>>> getOriginatingCity(@Query("ProvinceID") String provinceID);

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
    Call<CityListResp<List<CityVo>>> getHotDestinationCities(@Query("startCityID") String startCityID);

    /**
     * 通过省市和开始城市筛选 到达城市
     *
     * @param startCityID
     * @param provinceID
     */
    @GET("Bus/GetDestinationCitiesByProvinceID.ashx")
    Call<CityListResp<List<CityVo>>> GetDestinationCitiesByProvinceID(
            @Query("startCityID") String startCityID,
            @Query("provinceID") String provinceID
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
     *
     * @param userID
     * @return
     */
    @GET("Orders/GetTicketOrders.ashx")
    Call<OrderVoResp<List<OrderVo>>> GetTicketOrders(
            @Query("userID") String userID
    );

    /**
     * 订单详情
     *
     * @param orderID
     * @return
     */
    @GET("Orders/GetOrderDetails.ashx")
    Call<OrderDeatilResp<OrderDetailVo>> getOrderDetails(
            @Query("orderID") String orderID
    );

    /**
     * 退票
     *
     * @param orderDetailID
     * @return
     */
    @GET("Orders/RefundTicket.ashx")
    Call<BaseInfoVo> refundTicket(
            @Query("orderDetailID") String orderDetailID
    );

    /**
     * 获取支付宝签名信息
     *
     * @param orderID
     * @return
     */
    @GET("Orders/SignAlipay.ashx")
    Call<AlipayVo> signAlipay(
            @Query("orderID") String orderID
    );

    /**
     * 获取微信支付签名信息
     *
     * @param orderID
     * @return
     */
    @GET("Orders/PayOrderByWeiChat.ashx")
    Call<WXPayVo> payOrderByWeiChat(
            @Query("orderID") String orderID
    );

    /**
     * 学生出行支付宝支付
     *
     * @param orderID
     * @return
     */
    @GET("Orders/PayTravelOrderByAlipay.ashx")
    Call<AlipayVo> payTravelOrderByAlipay(
            @Query("orderID") String orderID
    );

    /**
     * 学生出行微信支付
     *
     * @param orderID
     * @return
     */
    @GET("Orders/PayTravelOrderByWeiChat.ashx")
    Call<WXPayVo> payTravelOrderByWeiChat(
            @Query("orderID") String orderID
    );

    /**
     * 使用余额支付
     *
     * @param orderType 订单类型（说明：汽车票传入字符串"ticket"，学生出行传入字符串"travel"，包车出行传入字符串"leasedVehicle"）
     * @return
     */
    @GET("Orders/PaymentByBanlance.ashx")
    Call<BaseInfoVo> paymentByBanlance(
            @Query("orderType") String orderType,
            @Query("orderID") String orderID,
            @Query("userID") String userID
    );


    /**
     * 获取轮播图接口
     */
    @GET("Advertisement/GetAdvertisementPictures.ashx")
    Call<PicturesVoResp<List<PicturesVo>>> getAdvertisementPictures();

    /**
     * 根据GPS定位得到的城市或者城市ID
     *
     * @return
     */
    @GET("Bus/GetGPSCity.ashx")
    Call<CityInfoResp<CityVo>> getGPSCity(
            @Query("provinceName") String provinceName,
            @Query("cityName") String cityName);

    /**
     * 获取省级城市
     *
     * @return
     */
    @GET("Bus/GetOriginatingProvinces.ashx")
    Call<ProvincesResp<List<ProvincesVo>>> getOriginatingProvinces();

    /**
     * 学生出行 平台发布路线预览
     *
     * @return
     */
    @GET("Travel/GetTravelRoutingPreview.ashx")
    Call<TravelRoutingVoResp<List<TravelRoutingVo>>> getTravelRoutingPreview();

    /**
     * 学生出行 路线详情列表
     *
     * @param startCityID 开始城市
     * @param stopCityID  结束城市
     * @param goDate      出发日期
     * @return
     */
    @GET("Travel/GetTravelRoutingList.ashx")
    Call<TravelRoutingListVoResp<List<TravelRoutingListVo>>> getTravelRoutingList(
            @Query("startCityID") String startCityID,
            @Query("stopCityID") String stopCityID,
            @Query("goDate") String goDate
    );

    /**
     * 发布众筹
     *
     * @param startPlaceID 出发城市ID
     * @param stopPlaceID  到达城市ID
     * @param userID       用户ID
     * @param goDate       出发日期(格式:2015-11-12)
     * @return
     */
    @GET("Travel/CreateTravel.ashx")
    Call<BaseInfoVo> createTravel(
            @Query("startPlaceID") String startPlaceID,
            @Query("stopPlaceID") String stopPlaceID,
            @Query("userID") String userID,
            @Query("goDate") String goDate
    );

    /**
     * 获取账户余额
     *
     * @param userID 用户ID
     * @return
     */
    @GET("User/GetBanlance.ashx")
    Call<BalanceVo> getBanlance(@Query("userID") String userID);

    /**
     * 获取账单情况
     *
     * @param userID
     * @return
     */
    @GET("User/GetBanlanceChanges.ashx")
    Call<BalanceChangeVoResp<List<BalanceChangeVo>>> getBanlanceChanges(
            @Query("userID") String userID
    );


    /**
     * 账户充值使用支付宝签名
     *
     * @param userID
     * @return
     */
    @GET("Orders/RechargeByAlipay.ashx")
    Call<AlipayVo> rechargeByAlipay(
            @Query("userID") String userID,
            @Query("price") String price
    );

    /**
     * 账户充值使用微信签名
     *
     * @param userID
     * @return
     */
    @GET("Orders/RechargeByWeiXin.ashx")
    Call<WXPayVo> rechargeByWeiXin(
            @Query("userID") String userID,
            @Query("price") String price
    );

    /**
     * 获取包车支付宝签名
     *
     * @param orderID
     * @return
     */
    @GET("Orders/PayLeasedVehicleOrderByAlipay.ashx")
    Call<AlipayVo> payLeasedVehicleOrderByAlipay(
            @Query("orderID") String orderID
    );

    /**
     * 获取包车微信签名
     *
     * @param orderID
     * @return
     */
    @GET("Orders/PayLeasedVehicleOrderByWeiChat.ashx")
    Call<WXPayVo> payLeasedVehicleOrderByWeiChat(
            @Query("orderID") String orderID
    );

    /**
     * 获取用户银行帐号
     */
    @GET("User/GetBankAccount.ashx")
    Call<BankAccountVoResp<BankAccountVo>> getBankAccount(@Query("userID") String userID);

    /**
     * 加入API
     */
    @GET("Travel/JoinTravel.ashx")
    Call<BaseInfoVo> joinTravel(
            @Query("travelID") String travelID,
            @Query("userID") String userID
    );

    /**
     * 提现API
     *
     * @param userID
     * @param withdrawalPrice
     * @param bankName
     * @param bankAccount
     * @param bankRealName
     * @return
     */
    @GET("User/ApplicationForWithdrawal.ashx")
    Call<BaseInfoVo> applicationForWithdrawal(
            @Query("userID") String userID,
            @Query("withdrawalPrice") String withdrawalPrice,
            @Query("bankName") String bankName,
            @Query("bankAccount") String bankAccount,
            @Query("bankRealName") String bankRealName
    );

    /**
     * 获取包车出行地市
     *
     * @param cityID
     * @return
     */
    @GET("Travel/GetPlaceList.ashx")
    Call<PlaceListResp<List<PlaceVo>>> getPlaceList(@Query("cityID") String cityID);

    /**
     * 关联第三方登陆
     *
     * @return
     */
    @GET("User/ExternalSystemAuthentication.ashx")
    Call<UserVo> externalSystemAuthentication(
            @Query("userID") String userID,
            @Query("headPicture") String headPicture,
            @Query("platform") String platform,
            @Query("openID") String openID,
            @Query("nickName") String nickName
    );

    /**
     * 关联手机号
     *
     * @return
     */
    @GET("User/BindingUserMobileNumber.ashx")
    Call<UserVo> bindingUserMobileNumber(
            @Query("userID") String userID,
            @Query("mobileNumber") String mobileNumber
    );

    /**
     * @return
     * @desc 创建包车出行订单
     * userID：用户ID
     * goDate：出发日期
     * passengerAmount：出发人数
     * trip：行程(格式: 1,成都-安岳|1,安岳-自贡|2,自贡-宜宾|2,宜宾-成都 )  1：代表出行的第几天，成都-安岳代表出行的目的地和到达地
     */
    @GET("LeasedVehicle/CreateLeasedVehicleOrder.ashx")
    Call<BaseInfoVo> CreateLeasedVehicleOrder(
            @Query("userID") String userID,
            @Query("goDate") String goDate,
            @Query("passengerAmount") String passengerAmount,
            @Query("trip") String trip
    );

    /**
     * 包车订单列表
     *
     * @param userID
     * @return
     */
    @GET("Orders/GetLeasedVehicleOrders.ashx")
    Call<LeasedVehicleListResp<List<LeasedVehicleOrder>>> getLeasedVehicleOrders(
            @Query("userID") String userID
    );

    /**
     * 包车订单详情
     *
     * @param orderID
     * @return
     */
    @GET("Orders/GetLeasedVehicleOrdersDetails.ashx")
    Call<VehicleOrdersDetailsVoResp> getLeasedVehicleOrdersDetails(
            @Query("orderID") String orderID
    );

    /**
     * 学生创建订单
     *
     * @param userID
     * @param travelID
     * @param passengers 乘客ID和是否为学生票（格式： passengerID,1|passengerID,0 passengerID:乘客ID，0和1 代表是否为学生）
     * @return
     */
    @GET("Orders/CreateTravelOrder.ashx")
    Call<OrderCreateVoResp> CreateTravelOrder(
            @Query("userID") String userID,
            @Query("travelID") String travelID,
            @Query("passengers") String passengers
    );

    /**
     * 学生出行订单列表
     *
     * @param userID
     * @return
     */
    @GET("Orders/GetTravelOrders.ashx")
    Call<TravelOrdersVoResp<List<TravelOrdersVo>>> getTravelOrders(
            @Query("userID") String userID
    );

    /**
     * 学生出行订单详情
     *
     * @param orderID
     * @return
     */
    @GET("Orders/GetTravelOrderDetails.ashx")
    Call<TravelOrderDetailVoResp> getTravelOrderDetails(
            @Query("orderID") String orderID
    );

    /**
     * 选择包车报价
     *
     * @param quoteID
     * @return
     */
    @GET("Orders/ChooseCompanyQuote.ashx")
    Call<BaseInfoVo> chooseCompanyQuote(
            @Query("quoteID") String quoteID
    );

    /**
     * 学生出行退票
     *
     * @param orderItemID
     * @return
     */
    @GET("Orders/RefundTravel.ashx")
    Call<BaseInfoVo> refundTravel(
            @Query("orderItemID") String orderItemID
    );

    /**
     * 学生出行订单详情留言板消息获取
     *
     * @param orderID
     * @return
     */
    @GET("Travel/GetTravelChatMessages.ashx")
    Call<MessageListResp<List<MessagesVo>>> getTravelChatMessages(
            @Query("orderID") String orderID
    );

    /**
     * 学生出行订单详情添加留言板消息
     *
     * @param orderID
     * @return
     */
    @GET("Travel/PublishMessage.ashx")
    Call<BaseInfoVo> publishMessage(
            @Query("orderID") String orderID,
            @Query("userID") String userID,
            @Query("message") String message
    );

    /**
     * 订单分享
     *
     * @param ordernumber
     * @return
     */
    @GET("orders/share.ashx")
    Call<ShareMessageVo> share(
            @Query("ordernumber") String ordernumber
    );

    /**
     * 获取版本信息
     * @return
     */
    @GET("common/checkversion.ashx?platform=android")
    Call<VersionVo> getVersion();
}
