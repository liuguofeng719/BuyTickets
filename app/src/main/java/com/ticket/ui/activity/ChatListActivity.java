package com.ticket.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ticket.R;
import com.ticket.bean.BaseInfoVo;
import com.ticket.bean.MessageListResp;
import com.ticket.bean.MessagesVo;
import com.ticket.bean.UserVo;
import com.ticket.ui.adpater.base.ListViewDataAdapter;
import com.ticket.ui.adpater.base.ViewHolderBase;
import com.ticket.ui.adpater.base.ViewHolderCreator;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.AppPreferences;
import com.ticket.utils.CommonUtils;
import com.ticket.utils.TLog;
import com.ticket.widgets.CircleImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/4/13.
 */
public class ChatListActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.lv_message)
    ListView lv_message;
    @InjectView(R.id.edit_content)
    EditText edit_content;
    @InjectView(R.id.iv_reply)
    ImageView iv_reply;

    private ListViewDataAdapter<MessagesVo> messageDataAdapter;
    private Call<MessageListResp<List<MessagesVo>>> messageListRespCall;
    private String orderId;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @OnClick(R.id.iv_reply)
    public void reply() {
        if (!TextUtils.isEmpty(edit_content.getText())) {
            UserVo userVo = AppPreferences.getObject(UserVo.class);
            MessagesVo messagesVo = new MessagesVo();
            messagesVo.setContent(edit_content.getText().toString());
            messagesVo.setNickName(userVo.getNickName());
            messagesVo.setHeadPicture(userVo.getHeadPicture());
            messagesVo.setPublishDateTime(CommonUtils.getCurrentDate());
            messageDataAdapter.getDataList().add(messagesVo);
            messageDataAdapter.notifyDataSetChanged();
            publishMessage(userVo.getUserId(), edit_content.getText().toString());
        }
    }

    private void publishMessage(String userId, String content) {
        Call<BaseInfoVo> infoVoCall = getApis().publishMessage(orderId, userId, content).clone();
        infoVoCall.enqueue(new Callback<BaseInfoVo>() {
            @Override
            public void onResponse(Response<BaseInfoVo> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    CommonUtils.make(ChatListActivity.this, CommonUtils.getCodeToStr(response.code()));
                }else{
                    edit_content.setText("");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        orderId = extras.getString("orderId");
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.message_list;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_message;
    }

    @Override
    protected void initViewsAndEvents() {
        this.messageDataAdapter = new ListViewDataAdapter<MessagesVo>(new ViewHolderCreator<MessagesVo>() {
            @Override
            public ViewHolderBase<MessagesVo> createViewHolder(int position) {
                return new ViewHolderBase<MessagesVo>() {
                    CircleImageView iv_face;
                    TextView tv_nickname;
                    TextView tv_send_date;
                    TextView tv_msg;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.order_student_msg_item, null);
                        iv_face = ButterKnife.findById(view, R.id.iv_face);
                        tv_nickname = ButterKnife.findById(view, R.id.tv_nickname);
                        tv_send_date = ButterKnife.findById(view, R.id.tv_send_date);
                        tv_msg = ButterKnife.findById(view, R.id.tv_msg);
                        return view;
                    }

                    @Override
                    public void showData(int position, MessagesVo itemData) {
                        TLog.d(TAG_LOG, itemData.toString());

                        DisplayImageOptions options = new DisplayImageOptions
                                .Builder()
                                .showImageForEmptyUri(R.drawable.face)
                                .showImageOnFail(R.drawable.face)
                                .showImageOnLoading(R.drawable.face)
                                .cacheInMemory(true)
                                .cacheOnDisk(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .build();

                        ImageLoader.getInstance().displayImage(itemData.getHeadPicture(), iv_face, options);
                        tv_nickname.setText(itemData.getNickName());
                        tv_send_date.setText(itemData.getPublishDateTime());
                        tv_msg.setText(itemData.getContent());
                    }
                };
            }
        });
        lv_message.setAdapter(messageDataAdapter);
        getMessageList();
    }

    private void getMessageList() {
        showLoading(getString(R.string.common_loading_message));
        messageListRespCall = getApis().getTravelChatMessages(orderId).clone();
        messageListRespCall.enqueue(new Callback<MessageListResp<List<MessagesVo>>>() {
            @Override
            public void onResponse(Response<MessageListResp<List<MessagesVo>>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null && response.body().isSuccessfully()) {
                    List<MessagesVo> listResp = response.body().getMessages();
                    messageDataAdapter.getDataList().addAll(listResp);
                } else {
                    MessageListResp<List<MessagesVo>> messageListResp = response.body();
                    CommonUtils.make(ChatListActivity.this, messageListResp.getErrorMessage().equals("") ? messageListResp.getErrorMessage() : CommonUtils.getCodeToStr(response.code()));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if ("timeout".equals(t.getMessage())) {
                    CommonUtils.make(ChatListActivity.this, "网络请求超时");
                }
                hideLoading();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (messageListRespCall != null) {
            messageListRespCall.cancel();
        }
    }
}
