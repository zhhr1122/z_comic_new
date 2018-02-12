package com.android.zhhr.ui.activity;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.android.zhhr.R;
import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.commons.Url;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.data.entity.SearchBean;
import com.android.zhhr.presenter.DownloadlistPresenter;
import com.android.zhhr.ui.activity.base.BaseActivity;
import com.android.zhhr.ui.view.IDownloadlistView;
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

public class DownloadlistActivity extends BaseActivity<DownloadlistPresenter> implements IDownloadlistView{
    @Bind(R.id.tv_title)
    TextView mTitle;

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
        comicService.downloadFile(Url.test_url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<ResponseBody> responseBody) {
                        try {
                            InputStream is = responseBody.body().byteStream();
                            File file = new File(Environment.getExternalStorageDirectory(), "text_img.png");
                            FileOutputStream fos = new FileOutputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(is);
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = bis.read(buffer)) != -1) {
                                fos.write(buffer, 0, len);
                                fos.flush();
                            }
                            fos.close();
                            bis.close();
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
}
