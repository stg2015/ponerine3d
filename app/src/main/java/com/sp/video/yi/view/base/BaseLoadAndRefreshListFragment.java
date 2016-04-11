package com.sp.video.yi.view.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nd.hy.android.hermes.frame.loader.BasicListLoader;
import com.nd.hy.android.hermes.frame.loader.IUpdateListener;
import com.sp.video.yi.demo.R;
import com.sp.views.adapter.recycler.CommonRcvAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Weichao Wang on 2016/4/1.
 */
public abstract class BaseLoadAndRefreshListFragment<T, V> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IUpdateListener<List<V>>, View.OnClickListener {
    public final String TAG = getClass().getSimpleName();

    @Bind(R.id.rv_list)
    RecyclerView       mRecyclerView;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.pb_empty_loader)
    ProgressBar        mPbEmptyLoader;
    @Bind(R.id.tv_empty)
    TextView           mTvEmpty;
    @Bind(R.id.vg_empty_container)
    RelativeLayout     mVgEmptyContainer;

    private List<V> mDataList            = new ArrayList<>();//需求列表数据
    private List<T> mDataBaseList        = new ArrayList<>();//commonAdapter适配数据
    private List<T> mDataBaseContentList = new ArrayList<>();//需求列表数据转换为的commonAdapter适配数据

    public T dataHasNoMoreBase;//没有更多的commonAdapter适配数据

    private CommonRcvAdapter<T> mAdapter;
    private              int mPageIndex = 0;
    private static final int mPageSize  = 10;
    private int totalCount;

    private boolean loadFinished = false;
    private boolean reqFinished  = false;
    private int     mReqConError = 0;


    private boolean enableRefresh  = true;
    private boolean enableLoadMore = true;

    public boolean isEnableLoadMore() {
        return enableLoadMore;
    }

    public boolean isEnableRefresh() {
        return enableRefresh;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sp_frg_load_and_refresh_list;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        initView();
        showLoading();
        initLocalData();
        remoteData();
    }

    protected void remoteData() {
        reqFinished = false;
        mReqConError = 0;
        bindLifecycle(getRequestObservable(mPageIndex, mPageSize))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer count) {
                        reqFinished = true;
                        if (null != mSwipeRefreshLayout) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (null != count) {
                            totalCount = count;
                            if (totalCount <= 0) {
                                showEmptyTip(false);
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        reqFinished = true;
                        if (null != mSwipeRefreshLayout) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                      /*  if (throwable instanceof RxBizException) {
                            RxBizException bizException = (RxBizException) throwable;
                            if (bizException.getErrorKind() == RetrofitError.Kind.NETWORK) {
                                mReqConError++;
                                showEmptyTip(true);
                            }
                        }*/
                        mReqConError++;
                        showEmptyTip(true);
                    }
                });
    }

    protected abstract Observable<Integer> getRequestObservable(int mPageIndex, int mPageSize);

    protected abstract T genNoMoreBaseData();

    protected abstract T genBaseData(V data);

    protected abstract CommonRcvAdapter<T> getAdapter(List<T> mDataBaseList);

    protected abstract BasicListLoader genLoader();


    protected int getEmptyDrawableId() {
        return R.drawable.sp_ic_no_info;
    }

    protected int getEmptyStringId() {
        return R.string.sp_no_info;
    }

    private void initView() {
        //gen no more view
        dataHasNoMoreBase = genNoMoreBaseData();
        //swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.pltCourseStudyRefreshColorScheme);
        //enable or disable refresh
        mSwipeRefreshLayout.setEnabled(isEnableRefresh());
        //recycle view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setRecycleChildrenOnDetach(true);
        mRecyclerView.setLayoutManager(layoutManager);
        //enable or disable refresh
        if (isEnableLoadMore()) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                int lastVisibleItem;
                int totalItemCount;
                int scrollState;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    scrollState = newState;
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    totalItemCount = mAdapter.getItemCount();
                    //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                    // dy>0 表示向下滑动

                    if (lastVisibleItem >= totalItemCount - 4
                            && totalItemCount - 4 >= 0
                            && dy > 0
                            && totalCount > totalItemCount
                            && (mPageIndex + 1) * mPageSize <= totalCount) {
                        loadMore();
                    }
                }
            });
        }
        mDataBaseList.clear();
        mAdapter = getAdapter(mDataBaseList);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadMore() {
        mPageIndex++;
        bindLifecycle(getRequestObservable(mPageIndex, mPageSize))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer count) {
                        if (null != count) {
                            totalCount = count;
                            if (totalCount <= 0) {
                                showEmptyTip(false);
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                      /*  if (throwable instanceof RxBizException) {
                            RxBizException bizException = (RxBizException) throwable;
                            if (bizException.getErrorKind() == RetrofitError.Kind.NETWORK) {
                                mReqConError++;
                                showEmptyTip(true);
                            }
                        }*/
                        mReqConError++;
                        showEmptyTip(true);
                    }
                });
    }


    protected void showEmptyTip(boolean isNetError) {
        if (!loadFinished
                || !reqFinished
                || null == mVgEmptyContainer
                || null == mPbEmptyLoader
                || null == mTvEmpty
                || null == mRecyclerView
                ) {
            return;
        }
        if (mDataList == null || mDataList.size() == 0) {
            mVgEmptyContainer.setVisibility(View.VISIBLE);
            mPbEmptyLoader.setVisibility(View.GONE);
            mTvEmpty.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            if (isNetError || mReqConError > 0) {
                showMessage(getResources().getString(R.string.sp_no_connection));
                SpannableStringBuilder builder = new SpannableStringBuilder(getActivity().getResources().getString(R.string.sp_load_fail_and_retry));
                mTvEmpty.setText(builder);
                mTvEmpty.setOnClickListener(this);
                mTvEmpty.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sp_ic_connection_error, 0, 0);
            } else {
                mTvEmpty.setText(getResources().getString(getEmptyStringId()));
                mTvEmpty.setOnClickListener(null);
                mTvEmpty.setCompoundDrawablesWithIntrinsicBounds(0, getEmptyDrawableId(), 0, 0);
            }

        }

    }


    private void hideLoadingShowData() {
        //show data into recycleView
        mVgEmptyContainer.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    protected void showLoading() {
        mVgEmptyContainer.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mPbEmptyLoader.setVisibility(View.VISIBLE);
        mTvEmpty.setText(R.string.sp_wait_for_loading);
        mTvEmpty.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }


    @Override
    public void onRefresh() {
        mPageIndex = 0;
        remoteData();
    }

    private void initLocalData() {
        loadFinished = false;
        mDataList.clear();
        BasicListLoader loader = genLoader();
        getLoaderManager().restartLoader(getStaticLoaderId(), null, loader);
    }

    protected abstract int getStaticLoaderId();


    @Override
    public void onUpdate(List<V> datas) {
        if (datas == null) {
            return;
        }
        loadFinished = true;
        sortDataList(datas);
        mDataList = datas;
        mDataBaseList.removeAll(mDataBaseContentList);//清除需求数据
        mDataBaseList.remove(dataHasNoMoreBase);//清除没有更多
        mDataBaseContentList.clear();//刷新需求数据列表容器
        for (V data : mDataList) {
            T dataBase = genBaseData(data);
            mDataBaseContentList.add(dataBase);
        }
        mDataBaseList.addAll(mDataBaseContentList);//添加需求数据
        mAdapter.updateData(mDataBaseList);
        //加载完成后显示数据或者空页面
        if (mDataList == null || mDataList.size() == 0) {
            showEmptyTip(false);
        } else {
            hideLoadingShowData();
            if (isEnableLoadMore()) {
                addHasMoreWhenNeeded();//按需添加没有更多
            }
        }
    }

    protected abstract void sortDataList(List<V> datas);


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_empty:
                showLoading();
                remoteData();
                break;
        }
    }


    private void addHasMoreWhenNeeded() {
        //超过一屏幕，且数据条数已经到达服务端返回的总条数
        if (mRecyclerView == null || mDataBaseList == null || mDataBaseList.size() == 0) {
            return;
        }
        //没超过一屏幕判定方法
        // 1、不超过3条
        // 2、最后一条视图的底部坐标小于父view底部坐标   且   第一条视图是数据第一条
        if (mRecyclerView.getChildCount() <= 3) {
            return;
        }
        if (mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1) != null &&
                mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1).getBottom() <= mRecyclerView.getHeight() &&
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
            return;
        }
        if (mDataBaseContentList.size() == totalCount) {
            if (mDataBaseList.get(mDataBaseContentList.size() - 1) != dataHasNoMoreBase) {
                mDataBaseList.add(dataHasNoMoreBase);
                mAdapter.updateData(mDataBaseList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
