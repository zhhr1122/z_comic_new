package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.data.entity.SearchResult;
import com.android.zhhr.presenter.SearchPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.SearchDynamicAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.view.ISearchView;
import com.android.zhhr.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView<SearchResult>{
    @Bind(R.id.et_search)
    EditText mSearchText;
    @Bind(R.id.iv_dynamic_recycle)
    RecyclerView mDynamicRecycle;
    @Bind(R.id.iv_clear)
    ImageView mClearText;

    SearchDynamicAdapter mDynaicAdapter;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new SearchPresenter(this,this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        mDynaicAdapter = new SearchDynamicAdapter(this,R.layout.item_dynamic_search);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mDynamicRecycle.setLayoutManager(manager);
        mDynamicRecycle.setAdapter(mDynaicAdapter);
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d("zhhr1122","beforeTextChanged="+s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("zhhr1122","onTextChanged="+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d("zhhr1122","Editable="+s.toString());
                 if(s.length()!=0){
                    //文字改变，动态获取搜索结果
                    mDynamicRecycle.setVisibility(View.VISIBLE);
                    mPresenter.getDynamicResult(s.toString());
                    mClearText.setVisibility(View.VISIBLE);
                }else{
                    mDynamicRecycle.setVisibility(View.GONE);
                    mClearText.setVisibility(View.GONE);
                }

            }
        });
        mDynaicAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                SearchBean searchBean = new SearchBean();
                if(mPresenter.getmDynamicResult()!=null&&mPresenter.getmDynamicResult().getData().size()!=0){
                    searchBean = mPresenter.getmDynamicResult().getData().get(position);
                }
                IntentUtil.ToComicDetail(SearchActivity.this,searchBean.getId()+"",searchBean.getTitle());
                SearchActivity.this.finish();
            }
        });

    }

    @Override
    @OnClick(R.id.iv_clear)
    public void clearText() {
        mSearchText.setText("");
    }

    @Override
    public void fillDynamicResult(SearchResult searchResult) {
        List<SearchBean> list = searchResult.getData();
        if(list!=null&&list.size()!=0){
            mDynaicAdapter.updateWithClear(searchResult.getData());
        }
    }

    @Override
    public void fillResult(SearchResult searchResult) {

    }


    @Override
    public void fillHotRank(List ranks) {

    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(SearchResult data) {

    }


    @Override
    public void getDataFinish() {
        mDynaicAdapter.notifyDataSetChanged();
    }

    @Override
    public void ShowToast(String t) {
        ShowToast(t);
    }

    @OnClick(R.id.tv_cancel)
    public void Cancel(View view){
        finish();
    }

}
