package com.ticket.ui.activity;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticket.R;
import com.ticket.ui.base.BaseActivity;
import com.ticket.utils.CommonUtils;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 学生出行
 */
public class StudentTripActivity extends BaseActivity {

    @InjectView(R.id.btn_back)
    ImageView btn_back;
    @InjectView(R.id.tv_header_title)
    TextView tv_header_title;
    @InjectView(R.id.tv_text_info)
    TextView tv_text_info;

    @OnClick(R.id.btn_back)
    public void btnBack() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.student_trip;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText("学生出行");
        SpannableString sbs = new SpannableString("没有您想去的地方？发布一条众筹路线试试");
        String s = sbs.toString();
        int start = s.indexOf("众");
        int end = s.indexOf("线");
        sbs.setSpan(new NoLineClickSpan(), start, end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text_info.setMovementMethod(LinkMovementMethod.getInstance());
        tv_text_info.setHighlightColor(getResources().getColor(R.color.transparent));
        tv_text_info.setLinkTextColor(getResources().getColor(R.color.common_text_color));
        tv_text_info.setText(sbs);
    }

    class NoLineClickSpan extends ClickableSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            CommonUtils.make(StudentTripActivity.this, "123456");
        }
    }
}
