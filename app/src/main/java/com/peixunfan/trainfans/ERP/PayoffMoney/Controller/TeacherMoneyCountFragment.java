package com.peixunfan.trainfans.ERP.PayoffMoney.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.infrastructure.utils.IntentUtil;
import com.peixunfan.trainfans.Api.ApiProvider;
import com.peixunfan.trainfans.Base.BaseFragment;
import com.peixunfan.trainfans.ERP.PayoffMoney.View.TeacherMoneyCountAdapter;
import com.peixunfan.trainfans.R;
import com.peixunfan.trainfans.Recovery.Model.Article;
import com.peixunfan.trainfans.Recovery.Model.ArticleList;
import com.peixunfan.trainfans.Widgt.RefreshableRecyclerView.RefreshableRecyclerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * Created by chengyanfang on 2016/11/29.
 */

public class TeacherMoneyCountFragment extends BaseFragment implements Observer<ArticleList>{

    @Bind(R.id.container_layout)
    RelativeLayout mContainer;

    RefreshableRecyclerView mRefreshableRecyclerView;

    TeacherMoneyCountAdapter mAdapter;

    ArrayList<Article> mTeachMoneyCountList = new ArrayList();

    /**
     * 创建页面
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (mView == null)
        {
            mView = inflater.inflate(R.layout.fragment_teacher_moneycount_layout, container, false);
            ButterKnife.bind(this,mView);
        }
        isCreateView = true;
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected void initView() {
        //设置刷新列表
        mRefreshableRecyclerView = new RefreshableRecyclerView(getActivity(), new RefreshableRecyclerView.RefreshCallback() {
            @Override
            public void refresh() {
                mPage = 1;
                loadData();
            }

            @Override
            public void loadMore() {
                mPage = mPage + 1;
                loadData();
            }
        });
        mContainer.addView(mRefreshableRecyclerView.getView());
    }

    @Override
    protected void lazyLoad() {
        mRefreshableRecyclerView.autoRefresh();
        loadData();
    }

    @Override
    protected void initData(){
    }


    private void loadData()
    {
        ApiProvider.getInstance().getRecommendBorrow(this,String.valueOf(mPage));

        setApapter();
    }


    private void setApapter(){
        if(mAdapter == null){
            mAdapter = new TeacherMoneyCountAdapter(getActivity(),mTeachMoneyCountList);
            mAdapter.setOnItemClickListener((adapterView, view, i, l) -> {
                IntentUtil.forwordToActivity(getActivity(),TeacherMoneyCountDetailAct.class);
            });
            mAdapter.setLoadMoreListener(() -> {
                mPage++;
                loadData();
            });
            mRefreshableRecyclerView.setAdapter(mAdapter);
        }else {
            new Handler().post(() -> mAdapter.notifyDataSetChanged());
        }
    }

    @Override
    public void onCompleted() {
        mRefreshableRecyclerView.setRefreshing(false);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(ArticleList articleList) {
        if(mPage == 1){
            mTeachMoneyCountList.clear();
        }
        mTeachMoneyCountList.addAll(articleList.borrowList);

        setApapter();

        if (articleList.borrowList.size() < 10)
            mAdapter.canLoadMore(false);
        else
            mAdapter.canLoadMore(true);
    }
}
