package com.android.zhhr.net;

import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.utils.ApiException;

import rx.functions.Func1;

/**
 * Created by 张皓然 on 2018/2/7.
 */

public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    @Override
    public T call(HttpResult<T> httpResult) {
        if (httpResult.getStatus() != 2) {
             throw new ApiException(httpResult.getStatus());
        }
        return httpResult.getData();
    }
}
