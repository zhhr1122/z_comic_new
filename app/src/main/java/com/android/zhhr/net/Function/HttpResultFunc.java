package com.android.zhhr.net.Function;

import com.android.zhhr.data.entity.HttpResult;
import com.android.zhhr.utils.ApiException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * Created by 张皓然 on 2018/2/7.
 */

public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

    @Override
    public T apply(@NonNull HttpResult<T> tHttpResult) throws Exception {
        if (tHttpResult.getStatus() != 2) {
            throw new ApiException(tHttpResult.getStatus());
        }
        return tHttpResult.getData();
    }
}
