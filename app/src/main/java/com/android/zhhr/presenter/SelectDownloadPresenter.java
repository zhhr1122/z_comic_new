package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Chapters;
import com.android.zhhr.ui.view.ISelectDownloadView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张皓然 on 2018/1/24.
 */

public class SelectDownloadPresenter extends BasePresenter<ISelectDownloadView>{
    /*//已经选中的章节
    private ArrayList<Integer> SelectedChapters;
    //已经下载的章节
    private ArrayList<Integer> DownloadedChapters;*/

    private ArrayList<String> mChapters;

    private HashMap<Integer,Integer> map;
    private boolean isSelectedAll ;
    private int SelectedNum = 0;

    public int getSelectedNum() {
        return SelectedNum;
    }

    public SelectDownloadPresenter(Activity context, ISelectDownloadView view, ArrayList<String> mChapters) {
        super(context, view);
        this.mChapters = mChapters;
        isSelectedAll  = false;
        initData();
    }

    private void initData() {
        /*SelectedChapters = new ArrayList<>();
        DownloadedChapters = new ArrayList<>();*/
        map = new HashMap<>();
        if(mChapters!=null&&mChapters.size()!=0){
            for(int i=0;i<mChapters.size();i++){
                map.put(i, Constants.CHAPTER_FREE);
            }
        }
    }

    public void uptdateToSelected(int position){
        //SelectedChapters.add(position);
        if(map.get(position).equals(Constants.CHAPTER_FREE)){
            SelectedNum++;
            map.put(position,Constants.CHAPTER_SELECTED);
        }else if(map.get(position).equals(Constants.CHAPTER_SELECTED)){
            map.put(position,Constants.CHAPTER_FREE);
            SelectedNum--;
            isSelectedAll = false;
            mView.removeAll();
        }
        mView.updateDownloadList(map);
    }

    public void SelectOrMoveAll(){
        if(!isSelectedAll){
            if(mChapters!=null&&mChapters.size()!=0){
                for(int i=0;i<mChapters.size();i++){
                    if(map.get(i) == Constants.CHAPTER_FREE){
                        map.put(i, Constants.CHAPTER_SELECTED);
                        SelectedNum++;
                    }
                }
                mView.addAll();
            }
        }else{
            if(mChapters!=null&&mChapters.size()!=0){
                for(int i=0;i<mChapters.size();i++){
                    if(map.get(i) == Constants.CHAPTER_SELECTED){
                        map.put(i, Constants.CHAPTER_FREE);
                    }
                }
                SelectedNum = 0;
                mView.removeAll();
            }
        }
        isSelectedAll = !isSelectedAll;
        mView.updateDownloadList(map);
    }

}
