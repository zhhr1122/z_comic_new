package com.android.zhhr.utils;

/**
 * Created by 张皓然 on 2018/2/2.
 */

public class TextUtils {
    public static String getSearchText(String key,String BaseTitle){
        String title = BaseTitle;
        if(key!=null){
            title = "";
            if(!key.equals(BaseTitle)){
                String[] titles = BaseTitle.split(key);
                if(titles.length!=1){
                    title = titles[0]+"<font color='#ff9a6a'>"+key+"</font>"+titles[1];
                }else{
                    if(BaseTitle.indexOf(key) == 0){
                        title = "<font color='#ff9a6a'>"+key+"</font>" + titles[0];
                    }else if(!titles[0].equals(BaseTitle)){
                        title = titles[0] + "<font color='#ff9a6a'>"+key+"</font>";
                    }else{
                        title = BaseTitle;
                    }
                }
            }else{
                title =  "<font color='#ff9a6a'>"+BaseTitle+"</font>";
            }
        }
        return title;
    }

    public static String getSearchErrorText(String key){
        String title = "没有找到"+"<font color='#ff9a6a'>"+key+"</font>"+"相关的漫画";
        return title;
    }
}
