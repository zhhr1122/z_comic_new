package com.android.zhhr.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件处理工具类
 * Created by DELL on 2018/2/12.
 */

public class FileUtil {
    public static final String SDPATH = Environment.getExternalStorageDirectory().getPath() + "/z_comic/";// 获取文件夹
    public static final String CACHE = "cache/";
    public static final String COMIC = "comic/";
    public static void init(){
        //创建文件夹
        LogUtil.d("初始化创建文件夹");
        createDir(SDPATH+CACHE);
        createDir(SDPATH+COMIC);
    }

    public static File createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();//mkdirs可以创建多级目录下的文件夹
            LogUtil.d(file.getAbsolutePath() + "文件夹创建成功");
        }else{
            LogUtil.d(file.getAbsolutePath() + "文件夹已经存在");
        }
        return file;

    }
    // 保存图片
    public static boolean saveBitmap(Bitmap mBitmap, String path, String imgName) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return false;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + imgName; //
        delFile(path, imgName);//删除本地旧图
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 保存图片到SD卡
     * @param is
     * @param path
     * @param imgName
     * @return
     */
    public static boolean saveImgToSdCard(InputStream is,String path,String imgName){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return false;
        }
        createDir(path);
        delFile(path, imgName);//删除本地旧图
        try {
            File file = new File(path, imgName);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
            fos.close();
            bis.close();
            is.close();
            LogUtil.d("下载图片成功"+path+imgName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(e.toString());
            return false;
        }
    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }
    // 删除文件
    public static void delFile(String path, String fileName) {
        File file = new File(path + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }
    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
    // 删除目录本身
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

