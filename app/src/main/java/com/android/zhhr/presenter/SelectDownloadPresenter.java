package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.ui.view.ISelectDownloadView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张皓然 on 2018/1/24.
 */

public class SelectDownloadPresenter extends BasePresenter<ISelectDownloadView>{
    //已经选中的章节
    private ArrayList<Integer> SelectedChapters;
    //已经下载的章节
    private ArrayList<Integer> DownloadedChapters;

    private ArrayList<String> mChapters;

    private HashMap<Integer,Integer> map;

    public ArrayList<Integer> getSelectedChapters() {
        return SelectedChapters;
    }

    public void setSelectedChapters(ArrayList<Integer> selectedChapters) {
        SelectedChapters = selectedChapters;
    }

    public ArrayList<Integer> getDownloadedChapters() {
        return DownloadedChapters;
    }

    public void setDownloadedChapters(ArrayList<Integer> downloadedChapters) {
        DownloadedChapters = downloadedChapters;
    }

    public SelectDownloadPresenter(Activity context, ISelectDownloadView view,ArrayList<String> mChapters) {
        super(context, view);
        this.mChapters = mChapters;
        initData();
    }

    private void initData() {
        SelectedChapters = new ArrayList<>();
        DownloadedChapters = new ArrayList<>();
        map = new HashMap<Integer, Integer>();
        if(mChapters!=null&&mChapters.size()!=0){
            for(int i=0;i<mChapters.size();i++){
                map.put(i, Constants.CHAPTER_FREE);
            }
        }
    }

    public void addToSelected(int position){
        SelectedChapters.add(position);
        map.put(position,Constants.CHAPTER_SELECTED);
        mView.addToDownloadList(map);
    }
}
