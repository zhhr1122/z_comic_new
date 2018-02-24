package com.android.zhhr.net.download;

import java.io.InputStream;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by 张皓然 on 2018/2/24.
 */

public class DownloadComicDisposableObserver extends DisposableObserver<InputStream> {

    @Override
    public void onNext(@NonNull InputStream inputStream) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
