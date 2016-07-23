package com.ticket.ui.activity;

import android.app.Dialog;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.bigkoo.pickerview.TimePickerView;
import com.ticket.R;
import com.ticket.TicketsApplication;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.LocationService;
import com.ticket.utils.TLog;
import com.ticket.widgets.SlideShowView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by liuguofeng719 on 2016/7/22.
 */
public class TravelCharteredActivity extends BaseActivity implements CloudListener {

    @InjectView(R.id.btn_back)
    ImageView btnBack;
    @InjectView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @InjectView(R.id.edit_start_location)
    TextView editStartLocation;
    @InjectView(R.id.iv_change_location)
    ImageView ivChangeLocation;
    @InjectView(R.id.edit_end_location)
    TextView editEndLocation;
    @InjectView(R.id.edit_date)
    TextView editDate;
    @InjectView(R.id.edit_person_number)
    EditText editPersonNumber;
    @InjectView(R.id.slideShowView)
    SlideShowView slideShowView;
    @InjectView(R.id.tv_car_type)
    TextView tvCarType;
    @InjectView(R.id.tv_price_rule)
    TextView tvPriceRule;
    @InjectView(R.id.tv_valuation_rule)
    TextView tvValuationRule;
    @InjectView(R.id.btn_key_charted)
    Button btnKeyCharted;
    private TimePickerView pvTime;
    private LocationService locationService;

    private String startLatitude;//维度
    private String startLongitude;//经度
    private String startPoi;//poi

    private String endLatitude;//维度
    private String endLongitude;//经度
    private String endPoi;//poi

    private String provinceName;
    private String cityName;
    private Dialog mDialog;
    private RoutePlanSearch mSearch;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    //开始出发地点
    @OnClick(R.id.edit_start_location)
    public void editStartLocation() {

    }

    //开始出发地点
    @OnClick(R.id.edit_end_location)
    public void editEndLocation() {

    }

    //切换开始和结束地点
    @OnClick(R.id.iv_change_location)
    public void ivChangeLocation() {
        swapCity();
    }

    //一键包车
    @OnClick(R.id.btn_key_charted)
    public void btnKeyCharted() {
        LocalSearchInfo info = new LocalSearchInfo();
        info.ak = "LSMWIqXRNR0Xk0bfyLDZr3zrIrdWyDUS";
        info.q = "天安门";
        info.region = "北京";
        CloudManager.getInstance().localSearch(info);
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "龙泽");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "西单");
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));

    }

    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            //获取步行线路规划结果
        }

        public void onGetTransitRouteResult(TransitRouteResult result) {
            //获取公交换乘路径规划结果
        }

        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            //获取驾车线路规划结果
            List<TaxiInfo> taxiInfos = result.getTaxiInfos();
            for (TaxiInfo taxiInfo : taxiInfos) {
                TLog.d(TAG_LOG, taxiInfo.getTotalPrice() + "元");
            }
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    /**
     * 切换城市
     */
    private void swapCity() {

        //变换poi
        String tempCity = startPoi;
        startPoi = endPoi;
        endPoi = tempCity;

        //切换维度
        String tempLatitude = startLatitude;
        startLatitude = endLatitude;
        endLatitude = tempLatitude;

        //切换经度
        String tempLongitude = startLongitude;
        startLongitude = endLongitude;
        endLongitude = tempLongitude;

        editStartLocation.setText(startPoi);
        editEndLocation.setText(endPoi);
    }

    //计价规则
    @OnClick(R.id.tv_valuation_rule)
    public void tvValuationRule() {
        mDialog = CommonUtils.createDialog(this);
        mDialog.setContentView(R.layout.travel_chartered_dialog);
        ImageView imageView = (ImageView) mDialog.findViewById(R.id.iv_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.dismiss(mDialog);
            }
        });
        mDialog.show();
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

    /*****
     * @see copy funtion to you project
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
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\nProvince : ");
                sb.append(location.getProvince());
                provinceName = location.getProvince();
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                cityName = location.getCity();
                AppPreferences.putString("provinceName", provinceName);
                AppPreferences.putString("cityName", cityName);
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
                List<Poi> list = location.getPoiList();// POI数据
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }
//                Toast.makeText(TravelCharteredActivity.this,sb.toString(), Toast.LENGTH_LONG);
                editStartLocation.setText(list.get(0).getName());
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

    @Override
    protected void initViewsAndEvents() {
        tvHeaderTitle.setText("包车出行");
        CloudManager.getInstance().init(TravelCharteredActivity.this);
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(listener);
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
    }

    @Override
    public void onGetSearchResult(CloudSearchResult result, int i) {
        if (result != null && result.poiList != null
                && result.poiList.size() > 0) {
            Log.d(TAG_LOG, "onGetSearchResult, result length: " + result.poiList.size());
        }
    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {

    }
}
