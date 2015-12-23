package com.ticket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ticket.api.Apis;
import com.ticket.bean.CityListResp;
import com.ticket.bean.CityVo;
import com.ticket.bean.MessageVo;
import com.ticket.bean.OrderDeatilResp;
import com.ticket.bean.OrderDetailVo;
import com.ticket.bean.OrderVo;
import com.ticket.bean.OrderVoResp;
import com.ticket.bean.PassengerListResp;
import com.ticket.bean.PassengerVo;
import com.ticket.utils.RetrofitUtils;
import com.ticket.utils.TLog;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Apis apis = RetrofitUtils.getRfHttpsInstance(Apis.SMS_BAO).create(Apis.class);
        Apis apis = RetrofitUtils.getRfHttpsInstance(Apis.TROYCD).create(Apis.class);
        getOrders(apis);
    }

    private void getOrders(Apis apis) {
        Call<OrderVoResp<List<OrderVo>>> orderResp = apis.getOrders("57F0E4E9-0E46-4047-83BD-127F22E646B3",0);
        orderResp.enqueue(new Callback<OrderVoResp<List<OrderVo>>>() {
            @Override
            public void onResponse(Response<OrderVoResp<List<OrderVo>>> response, Retrofit retrofit) {
                if(response.body().isSuccessfully()){
                    TLog.d("OrderVoResp", response.body().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getOrderDetails(Apis apis) {
        Call<OrderDeatilResp<OrderDetailVo>> orderDetails = apis.getOrderDetails("525DD424-908E-4DBB-9D95-02528889CE5F").clone();
        orderDetails.enqueue(new Callback<OrderDeatilResp<OrderDetailVo>>() {
            @Override
            public void onResponse(Response<OrderDeatilResp<OrderDetailVo>> response, Retrofit retrofit) {
                if(response.body().isSuccessfully()) {
                    TLog.d("getOrderDetails", response.body().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCityListResp(Apis apis) {
        Call<CityListResp<List<CityVo>>> destCitys = apis.getDestinationCities("BB833180-93F9-40A7-B422-9B98B067CF58").clone();
        destCitys.enqueue(new Callback<CityListResp<List<CityVo>>>() {
            @Override
            public void onResponse(Response<CityListResp<List<CityVo>>> response, Retrofit retrofit) {
                if(response.body().isSuccessfully()){
                    TLog.d("CityListResp", response.body().getCityList().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPassenners(Apis apis) {
        Call<PassengerListResp<List<PassengerVo>>> passenners = apis.getPassengers("57f0e4e9-0e46-4047-83bd-127f22e646b3").clone();
        passenners.enqueue(new Callback<PassengerListResp<List<PassengerVo>>>() {
            @Override
            public void onResponse(Response<PassengerListResp<List<PassengerVo>>> response, Retrofit retrofit) {
                TLog.d("passenners", response.body().toString());
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendMsg(Apis apis) {
        Call<MessageVo> callMsg = apis.getVerifyCode("15882035337").clone();
        callMsg.enqueue(new Callback<MessageVo>() {
            @Override
            public void onResponse(Response<MessageVo> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}