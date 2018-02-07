package com.android.zhhr.utils;

/**
 * Created by 张皓然 on 2018/2/7.
 */

public class ApiException extends RuntimeException {
    private static final int EMPTY = 0;
    private static String message;

    public ApiException(int status){
        this(getApiExceptionMessage(status));
    }



    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        return message;
    }

    private static String getApiExceptionMessage(int code){
        switch (code) {
            case EMPTY:
                message = "未获取到";
                break;
            default:
                message = "未知错误";
        }
        return message;
    }
}
