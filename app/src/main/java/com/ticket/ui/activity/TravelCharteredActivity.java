package com.ticket.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bigkoo.pickerview.TimePickerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ticket.R;
import com.ticket.TicketsApplication;
import com.ticket.bean.CalculatePriceResp;
import com.ticket.bean.CalculatePriceVo;
import com.ticket.bean.CarTypeListResp;
import com.ticket.bean.CarTypeVo;
import com.ticket.bean.PicturesVo;
import com.ticket.bean.SuggestionInfoVo;
import com.ticket.common.Constants;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.LocationService;
import com.ticket.utils.TLog;
import com.ticket.widgets.SlideShowView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/7/22.
 */
public class TravelCharteredActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.tv_price_rule)
    TextView tvPriceRule;
    @InjectView(R.id.edit_start_location)
    TextView editStartLocation;
    @InjectView(R.id.iv_change_location)
    ImageView ivChangeLocation;
    @InjectView(R.id.edit_end_location)
    TextView editEndLocation;
    @InjectView(R.id.edit_date)
    TextView editDate;
    @InjectView(R.id.edit_days)
    EditText editDays;
    @InjectView(R.id.slideShowView)
    SlideShowView slideShowView;
    @InjectView(R.id.tv_car_type)
    TextView tvCarType;
    @InjectView(R.id.tv_valuation_rule)
    TextView tvValuationRule;
    @InjectView(R.id.btn_key_charted)
    Button btnKeyCharted;

    private TimePickerView pvTime;
    private LocationService locationService;

    private double startLatitude;//维度
    private double startLongitude;//经度
    private String startPoi;//poi

    private double endLatitude;//维度
    private double endLongitude;//经度
    private String endPoi;//poi

    private String cityStartName;
    private String cityEndName;

    private Dialog mDialog;
    private List<CarTypeVo> carTypeList;
    private CalculatePriceResp<CalculatePriceVo> calculatePriceResp;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //开始出发地点
    @OnClick(R.id.edit_start_location)
    public void editStartLocation() {
        Bundle bundle = new Bundle();
        bundle.putString("cityName", cityStartName);
        bundle.putString("city", "start");
        readyGoForResult(SuggestionActivity.class, 1, bundle);
    }

    //开始出发地点
    @OnClick(R.id.edit_end_location)
    public void editEndLocation() {
        Bundle bundle = new Bundle();
        bundle.putString("cityName", cityStartName);
        bundle.putString("city", "end");
        readyGoForResult(SuggestionActivity.class, 2, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.comm.START_CITY_SUCCESS) {
            SuggestionInfoVo suggestionInfo = (SuggestionInfoVo) data.getSerializableExtra("suggestionInfo");
            startLatitude = suggestionInfo.getPt().latitude;
            startLongitude = suggestionInfo.getPt().longitude;
            startPoi = suggestionInfo.getKey();
            cityStartName = suggestionInfo.getCity();
            editStartLocation.setText(startPoi);
        } else if (resultCode == Constants.comm.END_CITY_SUCCESS) {
            SuggestionInfoVo suggestionInfo = (SuggestionInfoVo) data.getSerializableExtra("suggestionInfo");
            endLatitude = suggestionInfo.getPt().latitude;
            endLongitude = suggestionInfo.getPt().longitude;
            endPoi = suggestionInfo.getKey();
            cityEndName = suggestionInfo.getCity();
            editEndLocation.setText(endPoi);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //切换开始和结束地点
    @OnClick(R.id.iv_change_location)
    public void ivChangeLocation() {
        swapCity();
    }

    /**
     * 切换城市
     */
    private void swapCity() {
        //变换poi
        String tempPoi = startPoi;
        startPoi = endPoi;
        endPoi = tempPoi;

        //切换维度
        double tempLatitude = startLatitude;
        startLatitude = endLatitude;
        endLatitude = tempLatitude;

        //切换经度
        double tempLongitude = startLongitude;
        startLongitude = endLongitude;
        endLongitude = tempLongitude;

        //切换城市名字
        String tempCityName = cityStartName;
        cityStartName = cityEndName;
        cityEndName = tempCityName;

        editStartLocation.setText(startPoi);
        editEndLocation.setText(endPoi);
    }

    //一键包车
    @OnClick(R.id.btn_key_charted)
    public void btnKeyCharted() {
        if (calculatePriceResp != null) {
            Bundle bundle = new Bundle();
            bundle.putString("totalPrice", calculatePriceResp.getTotalPrice() + "");
            bundle.putString("carType", tvCarType.getTag().toString());
            readyGo(TravelOrderCharteredActivity.class, bundle);
        } else {
            CommonUtils.make(this, "请选择出发地和目的，在选择计价规则");
        }
    }

    //校验信息
    private boolean validate() {
        if (TextUtils.isEmpty(editStartLocation.getText())) {
            CommonUtils.make(this, "请输入出发地");
            return false;
        }
        if (TextUtils.isEmpty(editEndLocation.getText())) {
            CommonUtils.make(this, "请输入目的地");
            return false;
        }
        if (TextUtils.isEmpty(editDate.getText())) {
            CommonUtils.make(this, "请输入出发日期");
            return false;
        }
        if (TextUtils.isEmpty(editDays.getText())) {
            CommonUtils.make(this, "请输入包车天数");
            return false;
        }
        return true;
    }


    //计价规则
    @OnClick(R.id.tv_valuation_rule)
    public void tvValuationRule() {
        mDialog = CommonUtils.createDialog(this);
        mDialog.setContentView(R.layout.travel_chartered_dialog);
        if (validate()) {
            //计算p1、p2两点之间的直线距离，单位：米
            LatLng p1 = new LatLng(startLatitude, startLongitude);
            LatLng p2 = new LatLng(endLatitude, endLongitude);
            double distance = DistanceUtil.getDistance(p1, p2);//单位米
            double distanceV = Math.round(distance / 100d) / 10d;//米转换成公里
            final String carType = tvCarType.getTag().toString();//车的类型

            Call<CalculatePriceResp<CalculatePriceVo>> respCall = getApis().calculatePrice(
                    carType,
                    Integer.parseInt(editDays.getText().toString()),
                    editDate.getText().toString(),
                    distanceV + "",
                    cityStartName,
                    startLatitude + "," + startLongitude,
                    startPoi,
                    cityEndName,
                    endLatitude + "," + endLongitude,
                    endPoi
            ).clone();

            respCall.enqueue(new Callback<CalculatePriceResp<CalculatePriceVo>>() {
                @Override
                public void onResponse(Response<CalculatePriceResp<CalculatePriceVo>> response, Retrofit retrofit) {

                    if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                        calculatePriceResp = response.body();
                        CalculatePriceVo priceDetails = calculatePriceResp.getPriceDetails();
                        ImageView iv_car_image = (ImageView) mDialog.findViewById(R.id.iv_car_image);
                        ImageLoader.getInstance().displayImage(priceDetails.getCarTypePicture(), iv_car_image);
                        TextView tv_calculate_time = (TextView) mDialog.findViewById(R.id.tv_calculate_time);
                        TextView tv_car_type = (TextView) mDialog.findViewById(R.id.tv_car_type);
                        tv_car_type.setText(priceDetails.getCarTypeName());
                        tv_calculate_time.setText(priceDetails.getAppearanceFeeContent());
                        TextView tv_calculate_time_total = (TextView) mDialog.findViewById(R.id.tv_calculate_time_total);
                        tv_calculate_time_total.setText(Integer.parseInt(priceDetails.getAppearanceFee()) + "元");

                        TextView tv_course_time = (TextView) mDialog.findViewById(R.id.tv_course_time);
                        tv_course_time.setText(priceDetails.getTravelCostContent());
                        TextView tv_calculate_course_total = (TextView) mDialog.findViewById(R.id.tv_calculate_course_total);
//                        BigDecimal b1 = new BigDecimal(priceDetails.getTravelCost());
//                        double f11 = b1.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                        tv_calculate_course_total.setText(priceDetails.getTravelCost().substring(0, priceDetails.getTravelCost().lastIndexOf(".")) + "元");

                        ImageView iv_close = (ImageView) mDialog.findViewById(R.id.iv_close);
                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvPriceRule.setText("约" + (int) calculatePriceResp.getTotalPrice() + "元");
                                CommonUtils.dismiss(mDialog);
                            }
                        });
                        mDialog.show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.travel_chartered_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onPause() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onPause();
    }

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("包车出行");

        //选择出发日期
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String format = sdf.format(date);
                editDate.setText(format);
            }
        });

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // -----------location config ------------
                locationService = ((TicketsApplication) (mContext.getApplicationContext())).locationService;
                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                locationService.registerListener(mListener);
                locationService.start();
            }
        });
        getCarTypes();
    }

    private void getCarTypes() {
        Call<CarTypeListResp<List<CarTypeVo>>> carTypeListRespCall = getApis().getCarTypes().clone();
        carTypeListRespCall.enqueue(new Callback<CarTypeListResp<List<CarTypeVo>>>() {
            @Override
            public void onResponse(Response<CarTypeListResp<List<CarTypeVo>>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    CarTypeListResp<List<CarTypeVo>> carTypeListResp = response.body();
                    carTypeList = carTypeListResp.getCarTypeList();

                    List<PicturesVo> picturesVoList = new ArrayList<PicturesVo>();
                    for (CarTypeVo carTypeVo : carTypeList) {
                        PicturesVo picturesVo = new PicturesVo();
                        picturesVo.setPictureUrl(carTypeVo.getPictureUrl());
                        picturesVo.setNavigateUrl("");
                        picturesVoList.add(picturesVo);
                    }

                    slideShowView.setImageUrlList(picturesVoList);
                    tvCarType.setText(carTypeList.get(0).getCarTypeName());
                    tvCarType.setTag(carTypeList.get(0).getCarTypeId());

                    slideShowView.setOnImageSelectedListener(new SlideShowView.OnImageSelectedListener() {
                        @Override
                        public void onImageSelected(int position) {
                            tvCarType.setText(carTypeList.get(position).getCarTypeName());
                            tvCarType.setTag(carTypeList.get(position).getCarTypeId());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    /*****
     * @desc copy funtion to you project
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                final StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlongitude : ");
                sb.append(location.getLongitude());
                startLatitude = location.getLatitude();
                startLongitude = location.getLongitude();
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\nProvince : ");
                sb.append(location.getProvince());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                cityStartName = location.getCity();
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                final List<Poi> list = location.getPoiList();// POI数据
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                    if (list.size() > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startPoi = list.get(new Random().nextInt(list.size())).getName();
                                editStartLocation.setText(list.get(new Random().nextInt(list.size())).getName());
                            }
                        });
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                TLog.d(TAG_LOG, sb.toString());
                locationService.stop();
            }
        }
    };
}
