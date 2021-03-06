package com.haoyu.app.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;


import com.haoyu.app.basehelper.BaseArrayRecyclerAdapter;

import com.haoyu.app.entity.MEvaluateEntity;
import com.haoyu.app.lingnan.student.R;
import com.haoyu.app.imageloader.GlideImgManager;
import com.haoyu.app.utils.TimeUtil;
import com.haoyu.app.view.StarBar;

import java.util.List;

/**
 * Created by acer1 on 2017/2/21.
 * 听课评课查看结果评价及建议列表
 */
public class TeachingStudyResultDetial extends BaseArrayRecyclerAdapter<MEvaluateEntity> {

    private Context context;

    public TeachingStudyResultDetial(Context context, List<MEvaluateEntity> mDatas) {
        super(mDatas);
        this.context = context;
    }


    @Override
    public void onBindHoder(RecyclerHolder holder, MEvaluateEntity entity, int position) {
        ImageView iv_img = holder.obtainView(R.id.iv_img);
        TextView tv_name = holder.obtainView(R.id.tv_name);
        StarBar ratingBar1 = holder.obtainView(R.id.ratingBar1);
        TextView tv_content = holder.obtainView(R.id.tv_content);
        TextView tv_time = holder.obtainView(R.id.tv_time);
        if (entity.getCreator() != null && entity.getCreator().getAvatar() != null) {
            GlideImgManager.loadCircleImage(context, entity.getCreator().getAvatar(), R.drawable.user_default,
                    R.drawable.user_default, iv_img);
        } else {
            iv_img.setImageResource(R.drawable.user_default);
        }
        if (entity.getCreator() != null && entity.getCreator().getRealName() != null) {
            tv_name.setText(entity.getCreator().getRealName());
        } else {
            tv_name.setText("");
        }
        if (entity.getComment() != null) {
            Spanned spanned = Html.fromHtml(entity.getComment());
            tv_content.setText(spanned);
        } else {
            tv_content.setText("");
        }

        tv_time.setText(TimeUtil.getDateHR(entity.getCreateTime()));
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.teaching_study_more;
    }
}
