package com.ticket.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddPassengerActivity extends BaseActivity {

    @InjectView(R.id.edit_passName)
    EditText edit_passName;
    @InjectView(R.id.passenger_mobile)
    EditText passenger_mobile;
    @InjectView(R.id.passenger_idcard)
    EditText passenger_idcard;
    @InjectView(R.id.btn_add_passenger)
    Button btn_add_passenger;

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.add_passenger;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_header_title.setText(getString(R.string.add_custom_title));
        btn_add_passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = validate();
                if (TextUtils.isEmpty(msg)) {
                    Call<BaseInfoVo> callPassenger = getApis().addPassengers(AppPreferences.getString("userId"),
                            passenger_mobile.getText().toString(),
                            passenger_idcard.getText().toString(),
                            edit_passName.getText().toString());

                    callPassenger.enqueue(new Callback<BaseInfoVo>() {
                        @Override
                        public void onResponse(Response<BaseInfoVo> response, Retrofit retrofit) {
                            if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                                CommonUtils.make(AddPassengerActivity.this, "添加乘客成功");
                                finish();
                            } else {
                                CommonUtils.make(AddPassengerActivity.this,response.body().getErrorMessage());
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String validate() {
        if (TextUtils.isEmpty(edit_passName.getText())) {
            return "用户名不能为空";
        }
        if (TextUtils.isEmpty(passenger_mobile.getText())) {
            return "手机号不能为空";
        }
        if (!CommonUtils.isMobile(passenger_mobile.getText().toString())) {
            return "请输入正确的手机号";
        }
        if (TextUtils.isEmpty(passenger_idcard.getText().toString())) {
            return "身份证不能为空";
        }
        if (!CommonUtils.isIDcard(passenger_idcard.getText().toString())) {
            return "请输入正确的身份证号";
        }
        return "";
    }
}
