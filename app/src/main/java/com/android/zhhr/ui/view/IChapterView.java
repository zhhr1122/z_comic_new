package com.android.zhhr.ui.view;

import java.util.List;

/**
 * Created by 皓然 on 2017/7/20.
 */

public interface IChapterView<T> extends IBaseView{
    //获取数据完成
    void getDataFinish();
    //未获取到数据
    void showEmptyView();
    //展示错误页面
    void showErrorView(Throwable throwable);
    //填充数据
    void fillData(T data);
    //弹出菜单
    void showMenu();
    //下一章
    void nextChapter(T data,int loadingPosition);
    //前一章
    void preChapter(T data,int loadingPosition);
    //切换预览模式
    void SwitchModel(int a);
    //前一页
    void prePage();
    //下一页
    void nextPage();

    void setTitle(String name);
}
