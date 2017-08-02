package com.android.zhhr.utils;

import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * Created by 皓然 on 2017/8/1.
 */

public class ShowErrorTextUtil {
    private static boolean isShowReason  = true;
    public static String ShowErrorText(Throwable throwable){
        String error = "未知错误"+ throwable.toString();
        if(throwable instanceof ConnectException){
            error ="无法访问服务器接口";
        }else if(throwable instanceof UnknownHostException){
            error = "未知的域名，请检查网络是否连接";
        }
        if(isShowReason){
            error= error+throwable.toString();
        }
        return error;
    }
}
