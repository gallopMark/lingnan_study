package com.haoyu.app.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haoyu.app.adapter.DictEntryAdapter;
import com.haoyu.app.adapter.RegistCourseAdapter;
import com.haoyu.app.base.BaseActivity;
import com.haoyu.app.basehelper.BaseRecyclerAdapter;
import com.haoyu.app.entity.CourseMobileEntity;
import com.haoyu.app.entity.DictEntryMobileEntity;
import com.haoyu.app.entity.DictEntryResult;
import com.haoyu.app.entity.Paginator;
import com.haoyu.app.entity.RegistCourseListResult;
import com.haoyu.app.lingnan.student.R;
import com.haoyu.app.utils.Constants;
import com.haoyu.app.utils.OkHttpClientManager;
import com.haoyu.app.view.AppToolBar;
import com.haoyu.app.view.LoadFailView;
import com.haoyu.app.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Request;

/**
 * 创建日期：2017/1/6 on 16:08
 * 描述:选课中心
 * 作者:马飞奔 Administrator
 */
public class RegistCouseActivity extends BaseActivity implements View.OnClickListener, XRecyclerView.LoadingListener {
    private RegistCouseActivity context = this;
    @BindView(R.id.toolBar)
    AppToolBar toolBar;
    @BindView(R.id.tv_subject)
    TextView tv_subject;// 学科
    @BindView(R.id.tv_stage)
    TextView tv_stage; // 学段
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;
    @BindView(R.id.loadFailView)
    LoadFailView loadFailView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    private List<CourseMobileEntity> coursesList = new ArrayList<>();  //我的课程集合
    private RegistCourseAdapter mAdapter;
    private int page = 1;
    private boolean isRefresh, isLoadMore, isReload;
    private List<DictEntryMobileEntity> subjectList; // 学科集合
    private List<DictEntryMobileEntity> stageList; // 学段集合
    private String subjectType, stageType;

    @Override
    public int setLayoutResID() {
        return R.layout.activity_regist_course;
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setArrowImageView(R.drawable.refresh_arrow);
        mAdapter = new RegistCourseAdapter(context, coursesList);
        xRecyclerView.setAdapter(mAdapter);
        xRecyclerView.setLoadingListener(context);
    }

    public void initData() {
        String url;
        if (subjectType != null && stageType != null) {
            url = Constants.OUTRT_NET + "/m/course_center?page=" + page + "&stage=" + stageType + "&subject=" + subjectType;
        } else if (subjectType != null) {
            url = Constants.OUTRT_NET + "/m/course_center?page=" + page + "&subject=" + subjectType;
        } else if (stageType != null) {
            url = Constants.OUTRT_NET + "/m/course_center?page=" + page + "&stage=" + stageType;
        } else {
            url = Constants.OUTRT_NET + "/m/course_center?page=" + page;
        }
        addSubscription(OkHttpClientManager.getAsyn(context, url, new OkHttpClientManager.ResultCallback<RegistCourseListResult>() {

            @Override
            public void onBefore(Request request) {
                if (isReload) {
                    showTipDialog();
                    xRecyclerView.setVisibility(View.GONE);
                }
                tv_empty.setVisibility(View.GONE);
            }

            @Override
            public void onError(Request request, Exception e) {
                hideTipDialog();
                xRecyclerView.setVisibility(View.GONE);
                tv_empty.setVisibility(View.GONE);
                if (isRefresh) {
                    xRecyclerView.refreshComplete(false);
                } else if (isLoadMore) {
                    page -= 1;
                    xRecyclerView.loadMoreComplete(false);
                } else {
                    loadFailView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onResponse(RegistCourseListResult response) {
                hideTipDialog();
                if (response != null && response.getResponseData() != null && response.getResponseData().getmCourses() != null && response.getResponseData().getmCourses().size() > 0) {
                    updateUI(response.getResponseData().getmCourses(), response.getResponseData().getPaginator());
                } else {
                    if (isRefresh) {
                        xRecyclerView.refreshComplete(true);
                    } else if (isLoadMore) {
                        xRecyclerView.loadMoreComplete(true);
                    } else {
                        xRecyclerView.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.VISIBLE);
                    }
                }
            }
        }));
    }

    private void updateUI(List<CourseMobileEntity> mList, Paginator paginator) {
        if (xRecyclerView.getVisibility() != View.VISIBLE)
            xRecyclerView.setVisibility(View.VISIBLE);
        if (tv_empty.getVisibility() != View.GONE) tv_empty.setVisibility(View.GONE);
        if (isReload) {
            coursesList.clear();
        } else if (isRefresh) {
            coursesList.clear();
            xRecyclerView.refreshComplete(true);
        } else if (isLoadMore) {
            xRecyclerView.loadMoreComplete(true);
        }
        coursesList.addAll(mList);
        mAdapter.notifyDataSetChanged();
        if (paginator != null && paginator.getHasNextPage()) {
            xRecyclerView.setLoadingMoreEnabled(true);
        } else {
            xRecyclerView.setLoadingMoreEnabled(false);
        }
    }

    @Override
    public void setListener() {
        toolBar.setOnLeftClickListener(new AppToolBar.OnLeftClickListener() {
            @Override
            public void onLeftClick(View view) {
                finish();
            }
        });
        tv_subject.setOnClickListener(context);
        tv_stage.setOnClickListener(context);
        loadFailView.setOnRetryListener(new LoadFailView.OnRetryListener() {
            @Override
            public void onRetry(View v) {
                initData();
            }
        });
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseRecyclerAdapter.RecyclerHolder holder, View view, int position) {
                if (position - 1 >= 0 && position - 1 < coursesList.size()) {
                    String courseId = coursesList.get(position - 1).getId();
                    Intent intent = new Intent(context, CourseDetailActivity.class);
                    intent.putExtra("courseId", courseId);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        isLoadMore = false;
        isReload = false;
        page = 1;
        initData();
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        isLoadMore = true;
        isReload = false;
        page += 1;
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_subject:
                if (subjectList == null) {
                    initSubject();
                } else {
                    showSubjectWindow();
                }
                break;
            case R.id.tv_stage:
                if (stageList == null) {
                    initStage();
                } else {
                    showStageWindow();
                }
                break;
        }
    }

    private void initSubject() {
        String url = Constants.OUTRT_NET + "/m/textBook?textBookTypeCode=SUBJECT";
        addSubscription(OkHttpClientManager.getAsyn(context, url, new OkHttpClientManager.ResultCallback<DictEntryResult>() {

            @Override
            public void onBefore(Request request) {
                showTipDialog();
            }

            @Override
            public void onError(Request request, Exception e) {
                hideTipDialog();
            }

            @Override
            public void onResponse(DictEntryResult response) {
                hideTipDialog();
                if (response != null && response.getResponseData() != null) {
                    subjectList = new ArrayList<>();
                    DictEntryMobileEntity entity = new DictEntryMobileEntity();
                    entity.setTextBookName("所有学科");
                    subjectList.add(entity);
                    subjectList.addAll(response.getResponseData());
                    showSubjectWindow();
                }
            }
        }));
    }

    /**
     * 访问学段条目
     */
    private void initStage() {
        String url = Constants.OUTRT_NET + "/m/textBook?textBookTypeCode=STAGE";
        addSubscription(OkHttpClientManager.getAsyn(context, url, new OkHttpClientManager.ResultCallback<DictEntryResult>() {

            @Override
            public void onBefore(Request request) {
                showTipDialog();
            }

            @Override
            public void onError(Request request, Exception e) {
                hideTipDialog();
            }

            @Override
            public void onResponse(DictEntryResult response) {
                hideTipDialog();
                if (response != null && response.getResponseData() != null) {
                    stageList = new ArrayList<>();
                    DictEntryMobileEntity entity = new DictEntryMobileEntity();
                    entity.setTextBookName("所有学段");
                    stageList.add(entity);
                    stageList.addAll(response.getResponseData());
                    showStageWindow();
                }
            }
        }));
    }

    private int stageSelect = 0;

    private void showStageWindow() {
        Drawable zhankai = ContextCompat.getDrawable(context, R.drawable.course_dictionary_shouqi);
        zhankai.setBounds(0, 0, zhankai.getMinimumWidth(), zhankai.getMinimumHeight());
        tv_stage.setCompoundDrawables(null, null, zhankai, null);
        View view = getLayoutInflater().inflate(R.layout.popupwindow_listview, null);
        ListView lv = view.findViewById(R.id.listView);
        final DictEntryAdapter adapter = new DictEntryAdapter(context, stageList, stageSelect);
        lv.setAdapter(adapter);
        final PopupWindow popupWindow = new PopupWindow(view, tv_stage.getWidth(), LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable shouqi = ContextCompat.getDrawable(context, R.drawable.course_dictionary_xiala);
                shouqi.setBounds(0, 0, shouqi.getMinimumWidth(), shouqi.getMinimumHeight());
                tv_stage.setCompoundDrawables(null, null, shouqi, null);
            }
        });
        lv.setSelection(stageSelect);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stageSelect = position;
                adapter.setSelectItem(stageSelect);
                tv_stage.setText(stageList.get(position).getTextBookName());
                popupWindow.dismiss();
                if (position > 0) {
                    stageType = stageList.get(position).getTextBookValue();
                } else {
                    stageType = null;
                }
                isRefresh = false;
                isLoadMore = false;
                isReload = true;
                page = 1;
                initData();
            }
        });
        popupWindow.showAsDropDown(tv_stage);
    }

    private int subjectSelect = 0;

    private void showSubjectWindow() {
        Drawable zhankai = ContextCompat.getDrawable(context, R.drawable.course_dictionary_shouqi);
        zhankai.setBounds(0, 0, zhankai.getMinimumWidth(), zhankai.getMinimumHeight());
        tv_subject.setCompoundDrawables(null, null, zhankai, null);
        View view = getLayoutInflater().inflate(R.layout.popupwindow_listview, null);
        ListView lv = view.findViewById(R.id.listView);
        final DictEntryAdapter adapter = new DictEntryAdapter(context, subjectList, subjectSelect);
        lv.setAdapter(adapter);
        final PopupWindow popupWindow = new PopupWindow(view, tv_subject.getWidth(), LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable shouqi = ContextCompat.getDrawable(context, R.drawable.course_dictionary_xiala);
                shouqi.setBounds(0, 0, shouqi.getMinimumWidth(), shouqi.getMinimumHeight());
                tv_subject.setCompoundDrawables(null, null, shouqi, null);
            }
        });
        lv.setSelection(subjectSelect);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subjectSelect = position;
                adapter.setSelectItem(subjectSelect);
                tv_subject.setText(subjectList.get(position).getTextBookName());
                popupWindow.dismiss();
                if (position > 0) {
                    subjectType = subjectList.get(position).getTextBookValue();
                } else {
                    subjectType = null;
                }
                isRefresh = false;
                isLoadMore = false;
                isReload = true;
                page = 1;
                initData();
            }
        });
        popupWindow.showAsDropDown(tv_subject);
    }
}
