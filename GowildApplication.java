package cn.gowild.robotlife.eve;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import cn.gowild.robotlife.sdk.Robot;

/**
 * @author liudu
 * @version 1.0a
 * <p><strong>Features draft description.主要功能介绍</strong></p>
 * @since 2020/1/6 18:21
 */
public class GowildApplication extends Application {

    private static Handler mHandler;
    private static Thread mMainThread;
    private static long mMainTreadId;
    private static Looper mMainLooper;
    static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
//        try {
//            if (checkProcessIsMain(this, Process.myPid())) {
                mContext = getApplicationContext();
                // 主线程
        mHandler = new Handler();
                mMainThread = Thread.currentThread();
                mMainTreadId = android.os.Process.myTid();
                mMainLooper = getMainLooper();
                Robot.CONTROLLER.init(this);
//            }
//        } catch (
//                Exception e) {
//            e.printStackTrace();
//        }
    }


    public static Handler getHandler() {
        return mHandler;
    }


    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainTreadId() {
        return mMainTreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }


    public static Context getContext() {
        return mContext;
    }



}
