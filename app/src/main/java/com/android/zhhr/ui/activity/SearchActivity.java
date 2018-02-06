package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.data.entity.SearchResult;
import com.android.zhhr.presenter.SearchPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.SearchDynamicAdapter;
import com.android.zhhr.ui.adapter.SearchResultAdapter;
import com.android.zhhr.ui.adapter.SearchTopAdapter;
import com.android.zhhr.ui.adapter.base.BaseRecyclerAdapter;
import com.android.zhhr.ui.custom.NoScrollStaggeredGridLayoutManager;
import com.android.zhhr.ui.view.ISearchView;
import com.android.zhhr.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 张皓然 on 2018/2/1.
 */

public class SearchActivity extends BaseActivity<SearchPresenter> implements ISearchView<List<Comic>>{
    @Bind(R.id.et_search)
    EditText mSearchText;
    @Bind(R.id.iv_dynamic_recycle)
    RecyclerView mDynamicRecycle;
    @Bind(R.id.iv_clear)
    ImageView mClearText;

    @Bind(R.id.iv_result_recycle)
    RecyclerView mResultRecycle;
    @Bind(R.id.iv_top_search_recycle)
    RecyclerView mTopRecycle;

    SearchDynamicAdapter mDynaicAdapter;
    SearchResultAdapter mResultAdapter;
    SearchTopAdapter mTopAdapter;


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
        LinearLayoutManager manager = new LinearLayoutManager(this);

        mDynaicAdapter = new SearchDynamicAdapter(this,R.layout.item_dynamic_search);
        mDynamicRecycle.setLayoutManager(manager);
        mDynamicRecycle.setAdapter(mDynaicAdapter);

        mResultAdapter = new SearchResultAdapter(this,R.layout.item_search_result);
        manager = new LinearLayoutManager(this);
        mResultRecycle.setLayoutManager(manager);
        mResultRecycle.setAdapter(mResultAdapter);

        NoScrollStaggeredGridLayoutManager staggeredGridLayoutManager = new NoScrollStaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL);
        staggeredGridLayoutManager.setScrollEnabled(false);
        mTopAdapter = new SearchTopAdapter(this,R.layout.item_top_search);
        mTopRecycle.setLayoutManager(staggeredGridLayoutManager);
        mTopRecycle.setAdapter(mTopAdapter);
        mPresenter.getTopSearch();


        setListener();
    }
    //设置监听器
    private void setListener() {
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
                mResultRecycle.setVisibility(View.GONE);
                if(s.length()!=0){
                    //文字改变，动态获取搜索结果
                    mResultAdapter.setKey(s.toString());
                    mDynaicAdapter.setKey(s.toString());
                    mDynamicRecycle.setVisibility(View.VISIBLE);
                    mPresenter.getDynamicResult(s.toString());
                    mClearText.setVisibility(View.VISIBLE);
                }else{
                    mDynamicRecycle.setVisibility(View.GONE);
                    mClearText.setVisibility(View.GONE);
                }
            }
        });
        mTopAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mTopAdapter.getItems(position);
                IntentUtil.ToComicDetail(SearchActivity.this,comic.getId()+"",comic.getTitle());
            }
        });
        mDynaicAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                SearchBean searchBean = mDynaicAdapter.getItems(position);
                IntentUtil.ToComicDetail(SearchActivity.this,searchBean.getId()+"",searchBean.getTitle());
            }
        });
        mResultAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Comic comic = mResultAdapter.getItems(position);
                IntentUtil.ToComicDetail(SearchActivity.this,comic.getId()+"",comic.getTitle());
            }
        });
        //设置搜索监听事件
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    //关闭软键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mPresenter.getSearchResult();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    @OnClick(R.id.iv_clear)
    public void clearText() {
        mSearchText.setText("");
        mResultRecycle.setVisibility(View.GONE);
        mDynamicRecycle.setVisibility(View.GONE);
    }

    @Override
    public void fillDynamicResult(SearchResult searchResult) {
        mDynamicRecycle.setVisibility(View.VISIBLE);
        List<SearchBean> list = searchResult.getData();
        if(list!=null&&list.size()!=0){
            mDynaicAdapter.updateWithClear(searchResult.getData());
        }
    }

    @Override
    public void fillResult(List<Comic> comics) {
        mResultRecycle.setVisibility(View.VISIBLE);
        if(comics!=null&&comics.size()!=0){
            mResultAdapter.updateWithClear(comics);
            mResultAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void fillTopSearch(List<Comic> comics) {
        if(comics!=null&&comics.size()!=0){
            mTopAdapter.updateWithClear(comics);
            mTopAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getSearchText() {
        return mSearchText.getText().toString();
    }


    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<Comic> data) {

    }


    @Override
    public void getDataFinish() {
        mDynaicAdapter.notifyDataSetChanged();
    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @OnClick(R.id.tv_cancel)
    public void Cancel(View view){
        finish();
    }

}
