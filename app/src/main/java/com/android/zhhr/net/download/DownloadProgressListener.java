package com.android.zhhr.net.download;

/**
 * Created by 张皓然 on 2018/2/13.
 * 成功回调处理
 */

public interface DownloadProgressListener {
    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}
