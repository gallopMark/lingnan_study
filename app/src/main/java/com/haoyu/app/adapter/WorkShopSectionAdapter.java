package com.haoyu.app.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoyu.app.basehelper.BaseArrayRecyclerAdapter;
import com.haoyu.app.entity.MWorkshopActivity;
import com.haoyu.app.entity.MWorkshopSection;
import com.haoyu.app.lingnan.student.R;
import com.haoyu.app.utils.TimeUtil;
import com.haoyu.app.view.FullyLinearLayoutManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建日期：2017/1/3 on 15:09
 * 描述:
 * 作者:马飞奔 Administrator
 */
public class WorkShopSectionAdapter extends BaseArrayRecyclerAdapter<MWorkshopSection> {
    private Context context;
    private String pressId;
    private Map<Integer, Boolean> hashMap = new HashMap<>();
    private OnActivityClickCallBack onActivityClickCallBack;

    public WorkShopSectionAdapter(Context context, List<MWorkshopSection> mDatas) {
        super(mDatas);
        this.context = context;
        for (int i = 0; i < mDatas.size(); i++) {
            if (i == 0)
                hashMap.put(i, true);
            else
                hashMap.put(i, false);
        }
    }

    private void setSelected(String pressId) {
        this.pressId = pressId;
        notifyDataSetChanged();
    }

    public void setOnActivityClickCallBack(OnActivityClickCallBack onActivityClickCallBack) {
        this.onActivityClickCallBack = onActivityClickCallBack;
    }

    @Override
    public void onBindHoder(RecyclerHolder holder, final MWorkshopSection entity, final int position) {
        if (position + 1 < 10) {
            holder.setText(R.id.tv_position, "0" + (position + 1));
        } else if (position + 1 >= 10 && position + 1 <= 99) {
            holder.setText(R.id.tv_position, String.valueOf(position + 1));
        } else {
            holder.setText(R.id.tv_position, "..");
        }
        if (entity.getTitle() != null && entity.getTitle().trim().length() > 0) {
            holder.setText(R.id.tv_title, entity.getTitle());
        } else
            holder.setText(R.id.tv_title, "无标题");
        if (entity.getTimePeriod() != null) {
            holder.setText(R.id.tv_researchTime, "研修时间：" +
                    TimeUtil.getDateYM(entity.getTimePeriod().getStartTime()) + "-"
                    + TimeUtil.getDateYM(entity.getTimePeriod().getEndTime()));
        } else {
            holder.setText(R.id.tv_researchTime, "研修时间：未知");
        }
        final RecyclerView recyclerView = holder.obtainView(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(context);
        layoutManager.setOrientation(FullyLinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        final LinearLayout ll_visiable = holder.obtainView(R.id.ll_visiable);
        WorkShopActivityAdapter adapter = new WorkShopActivityAdapter(entity.getActivities());
        recyclerView.setAdapter(adapter);
        if (hashMap.get(position) != null && hashMap.get(position) == true)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
        if (entity.getActivities() != null && entity.getActivities().size() > 0
                && hashMap.get(position) != null && hashMap.get(position) == false) {
            ll_visiable.setVisibility(View.VISIBLE);
        } else {
            ll_visiable.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    hashMap.put(position, false);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    hashMap.put(position, true);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if (entity.getActivities() != null && entity.getActivities().size() > 0
                        && hashMap.get(position) != null && hashMap.get(position) == false) {
                    ll_visiable.setVisibility(View.VISIBLE);
                } else {
                    ll_visiable.setVisibility(View.GONE);
                }
            }
        });
    }

    private class WorkShopActivityAdapter extends BaseArrayRecyclerAdapter<MWorkshopActivity> {

        public WorkShopActivityAdapter(List<MWorkshopActivity> mDatas) {
            super(mDatas);
        }

        @Override
        public int bindView(int viewtype) {
            return R.layout.workshop_activity_item;
        }

        @Override
        public void onBindHoder(RecyclerHolder holder, final MWorkshopActivity entity, int position) {
            ImageView ic_type = holder.obtainView(R.id.ic_type);
            TextView tv_typeName = holder.obtainView(R.id.tv_typeName);
            TextView tv_title = holder.obtainView(R.id.tv_title);
            if (entity.getType() != null && entity.getType().equals("discussion")) {
                if (pressId != null && pressId.equals(entity.getId()))
                    ic_type.setImageResource(R.drawable.ws_discuss_press);
                else
                    ic_type.setImageResource(R.drawable.ws_discuss_default);
                tv_typeName.setText("教学研讨");
            } else if (entity.getType() != null && entity.getType().equals("survey")) {
                if (pressId != null && pressId.equals(entity.getId()))
                    ic_type.setImageResource(R.drawable.ws_questionnaire_press);
                else
                    ic_type.setImageResource(R.drawable.ws_questionnaire_default);
                tv_typeName.setText("调查问卷");
            } else if (entity.getType() != null && entity.getType().equals("debate")) {
                if (pressId != null && pressId.equals(entity.getId()))
                    ic_type.setImageResource(R.drawable.ws_bianlun_press);
                else
                    ic_type.setImageResource(R.drawable.ws_bianlun_default);
                tv_typeName.setText("在线辩论");
            } else if (entity.getType() != null && entity.getType().equals("lcec")) {
                if (pressId != null && pressId.equals(entity.getId()))
                    ic_type.setImageResource(R.drawable.ws_tingke_press);
                else
                    ic_type.setImageResource(R.drawable.ws_tingke_default);
                tv_typeName.setText("听课评课");
            } else if (entity.getType() != null && entity.getType().equals("lesson_plan")) {
                if (pressId != null && pressId.equals(entity.getId()))
                    ic_type.setImageResource(R.drawable.ws_beike_press);
                else
                    ic_type.setImageResource(R.drawable.ws_beike_default);
                tv_typeName.setText("集体备课");
            } else if (entity.getType() != null && entity.getType().equals("test")) {
                if (pressId != null && pressId.equals(entity.getId()))
                    ic_type.setImageResource(R.drawable.progress_test_press);
                else
                    ic_type.setImageResource(R.drawable.progress_test_default);
                tv_typeName.setText("在线测验");
            } else if (entity.getType() != null && entity.getType().equals("video")) {
                if (pressId != null && pressId.equals(entity.getId()))
                    ic_type.setImageResource(R.drawable.progress_video_press);
                else
                    ic_type.setImageResource(R.drawable.progress_video_default);
                tv_typeName.setText("教学观摩");
            } else {
                if (pressId != null && pressId.equals(entity.getId()))
                    ic_type.setImageResource(R.drawable.course_word_selected);
                else
                    ic_type.setImageResource(R.drawable.course_word_default);
                tv_typeName.setText("类型未知");
            }
            if (pressId != null && pressId.equals(entity.getId())) {
                tv_typeName.setTextColor(ContextCompat.getColor(context, R.color.defaultColor));
                tv_title.setTextColor(ContextCompat.getColor(context, R.color.defaultColor));
            } else {
                tv_typeName.setTextColor(ContextCompat.getColor(context, R.color.blow_gray));
                tv_title.setTextColor(ContextCompat.getColor(context, R.color.line_bottom));
            }
            if (entity.getTitle() != null && entity.getTitle().trim().length() > 0) {
                tv_title.setText(entity.getTitle());
            } else
                tv_title.setText("无标题");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelected(entity.getId());
                    if (onActivityClickCallBack != null) {
                        onActivityClickCallBack.onActivityClick(entity);
                    }
                }
            });
        }
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.workshop_section_item;
    }

    public interface OnActivityClickCallBack {
        void onActivityClick(MWorkshopActivity activity);
    }
}
