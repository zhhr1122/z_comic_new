package com.android.zhhr.presenter;

import android.app.Activity;

import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.Type;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.ICategoryView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by zhhr on 2018/3/16.
 */

public class CategoryPresenter extends BasePresenter<ICategoryView>{
    private List<Type> mSelectList;
    private Map<String,Integer> mSelectMap;
    protected ComicModule mModel;
    private int page;

    public CategoryPresenter(Activity context, ICategoryView view) {
        super(context, view);
        this.mSelectList = new ArrayList<>();
        this.mSelectMap =new HashMap<>();
        this.mModel = new ComicModule(context);
        this.page = 1;
    }

    public void loadData() {
       String[] themes = new String[]{"全部","爆笑","热血","冒险","恐怖","科幻","魔幻","玄幻","校园","悬疑","推理","萌系","穿越","后宫"};
        for(int i=0;i<14;i++){
            Type item = new Type("theme",themes[i],i);
            mSelectList.add(item);
        }
        String[] finish =  new String[]{"全部","连载","完结",null,null,null,null};
        for(int i=0;i<7;i++){
            Type item = new Type("finish",finish[i],i);
            mSelectList.add(item);
        }
        String[] audience =  new String[]{"全部","少年","少女","青年","少儿",null,null};
        for(int i=0;i<7;i++){
            Type item = new Type("audience",audience[i],i);
            mSelectList.add(item);
        }
        String[] nation =  new String[]{"全部","内地","港台","韩国","日本",null,null};
        for(int i=0;i<7;i++){
            Type item = new Type("nation",nation[i],i);
            mSelectList.add(item);
        }

        mSelectMap.put("theme",0);
        mSelectMap.put("finish",0);
        mSelectMap.put("audience",0);
        mSelectMap.put("nation",0);

        mView.fillSelectData(mSelectList,mSelectMap);
        loadCategoryList();
    }

    public void loadCategoryList(){
        mModel.getCategroyList(mSelectMap,page, new DisposableObserver<List<Comic>>() {
            @Override
            public void onNext(@NonNull List<Comic> comics) {
                mView.fillData(comics);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void onItemClick(Type type, int position) {
        if(type.getTitle()!=null){
            mSelectMap.put(type.getType(),type.getValue());
            mView.setMap(mSelectMap);
            loadCategoryList();
        }
    }
}
