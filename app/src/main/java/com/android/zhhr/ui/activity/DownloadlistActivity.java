package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.data.entity.db.DBDownloadItems;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.presenter.DownloadlistPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.adapter.base.DownloadlistAdapter;
import com.android.zhhr.ui.view.IDownloadlistView;
import com.android.zhhr.utils.FileUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.android.zhhr.net.MainFactory.comicService;

/**
 * Created by DELL on 2018/2/12.
 */

public class DownloadlistActivity extends BaseActivity<DownloadlistPresenter> implements IDownloadlistView<List<DBDownloadItems>>{
    @Bind(R.id.tv_title)
    TextView mTitle;
    @Bind(R.id.rv_downloadlist)
    RecyclerView mRecyclerview;

    private DownloadlistAdapter mAdapter;


    @Override
    protected void initPresenter(Intent intent) {
        mPresenter = new DownloadlistPresenter(this,this,intent);
        mTitle.setText(((Comic)intent.getSerializableExtra(Constants.COMIC)).getTitle());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_downloadlist;
    }

    @Override
    protected void initView() {
        //mPresenter.getComic();
        mAdapter = new DownloadlistAdapter(this,R.layout.item_downloadlist);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(manager);
        mRecyclerview.setAdapter(mAdapter);
        mPresenter.initData();
    }

    @Override
    public void onStartDownload(int chapters) {

    }

    @Override
    public void onPausedDownload(int chapters) {

    }

    @Override
    public void onStartAll() {

    }

    @Override
    public void onPauseAll() {

    }

    @Override
    public void onSelectALL() {

    }

    @Override
    public void onDeleteAll() {

    }

    @Override
    public void ShowToast(String t) {
        showToast(t);
    }

    @OnClick(R.id.iv_back_color)
    public void finish(View view){
        this.finish();
    }

    @Override
    public void showErrorView(String throwable) {

    }

    @Override
    public void fillData(List<DBDownloadItems> data) {
        mAdapter.updateWithClear(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDataFinish() {

    }
}
