package com.ticket.ui.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.bean.BalanceChangeVo;
import com.ticket.bean.BalanceChangeVoResp;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/3/28.
 */
public class ExpensesDetailsActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.lv_expenses_details)
    ListView lv_expenses_details;
    private Call<BalanceChangeVoResp<List<BalanceChangeVo>>> changeVoRespCall;
    private ListViewDataAdapter<BalanceChangeVo> listViewDataAdapter;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.expenses_details;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_expenses_details;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("收支明细");
         listViewDataAdapter = new ListViewDataAdapter<BalanceChangeVo>(new ViewHolderCreator<BalanceChangeVo>() {
            @Override
            public ViewHolderBase<BalanceChangeVo> createViewHolder(int position) {

                return new ViewHolderBase<BalanceChangeVo>() {
                    TextView tv_trade_type;
                    TextView tv_trade_date;
                    TextView tv_trade_total_amount;
                    TextView tv_trade_amount;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.expenses_details_item, null);
                        tv_trade_type = ButterKnife.findById(view, R.id.tv_trade_type);
                        tv_trade_date = ButterKnife.findById(view, R.id.tv_trade_date);
                        tv_trade_total_amount = ButterKnife.findById(view, R.id.tv_trade_total_amount);
                        tv_trade_amount = ButterKnife.findById(view, R.id.tv_trade_amount);
                        return view;
                    }

                    @Override
                    public void showData(int position, BalanceChangeVo itemData) {
                        tv_trade_type.setText(itemData.getDescription());
                        tv_trade_amount.setText(itemData.getChangePrice());
                        tv_trade_date.setText(itemData.getChangeDateTime());
                    }
                };
            }
        });
        lv_expenses_details.setAdapter(listViewDataAdapter);
        changeVoRespCall = getApis().getBanlanceChanges(AppPreferences.getString("userId"));
        changeVoRespCall.enqueue(new Callback<BalanceChangeVoResp<List<BalanceChangeVo>>>() {
            @Override
            public void onResponse(Response<BalanceChangeVoResp<List<BalanceChangeVo>>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null & response.body().isSuccessfully()) {
                    listViewDataAdapter.getDataList().addAll(response.body().getBalanceChangeList());
                    listViewDataAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (changeVoRespCall != null) {
            changeVoRespCall.cancel();
        }
    }
}
