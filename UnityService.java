package cn.gowild.robotlife.eve;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.unity3d.player.UnityPlayer;

import java.security.Provider;

import butterknife.ButterKnife;
import cn.gowild.property.GlobalProperty;
import cn.gowild.robotlife.sdk.Robot;
import cn.gowild.robotlife.sdk.RobotActivity;
import cn.gowild.robotlife.sdk.RobotConstants;
import cn.gowild.robotlife.sdk.UnityHelper;
import cn.gowild.robotlife.sdk.utils.ExceptionCallback;
import cn.gowild.robotlife.sdk.utils.FileUtils;
import cn.gowild.robotlife.sdk.utils.HandleMessageHelper;
import cn.gowild.sound.SoundEffect;

public class UnityService extends Service {
    FrameLayout mUnityContent;
    UnityPlayer mUnityPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUnityPlayer = new UnityPlayer(getApplicationContext());
//        initWindow();
        testView();
        mUnityPlayer.requestFocus();
        mUnityPlayer.windowFocusChanged(true);
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        //registerReceiver(mUsbReceiver, usbDeviceStateFilter);

        IntentFilter usbDiskFilter = new IntentFilter();
        usbDiskFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        usbDiskFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        // 必须要有此行，否则无法收到广播
        usbDiskFilter.addDataScheme("file");
        //registerReceiver(mUsbDiskReceiver, usbDiskFilter);

        IntentFilter apkComplete =  new IntentFilter();
        return super.onStartCommand(intent, flags, startId);
    }

    private void testView() {
        WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        //检查版本，注意当type为TYPE_APPLICATION_OVERLAY时，铺满活动窗口，但在关键的系统窗口下面，如状态栏或IME
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
//        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
//        layoutParams.token = this.getWindow().getDecorView().getWindowToken();  //这样设置，在activity中打开悬浮框可绕过权限；
        layoutParams.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.format = PixelFormat.TRANSLUCENT;  //透明
        layoutParams.gravity = Gravity.TOP | Gravity.RIGHT;  //右上角显示
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        windowManager.addView(view,layoutParams);
        mUnityContent = view.findViewById(R.id.content);
        mUnityContent.addView(mUnityPlayer);
        mUnityContent.post(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mUnityPlayer.resume();
                        mUnityPlayer.requestFocus();
                    }
                }).start();
            }
        });

        Robot.CONTROLLER.bindService();
//        handleMessageHelper = new HandleMessageHelper(RobotActivity.this,  new ExceptionCallback(){
//            @Override
//            public void handleRuntimeException() {
//                startVoiceService();
//            }
//
//            @Override
//            public void handleRemoteException() {
//            }
//        });
//        checkU3DRun();
        mUnityPlayer.resume();
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }


    private void initWindow() {
        //创建 windowManager 对象 获得 WINDOW_SERVICE 系统服务
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        //创建FrameLayout 布局 对象
        FrameLayout mLayout = new FrameLayout(getApplicationContext());
        //创建一个布局参数 对象  宽高 类型等
        WindowManager.LayoutParams lp = new
                WindowManager.LayoutParams();
        // 类型
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        //设置 这里将解决 无返回键 和 无法弹出输入框的问题
        lp.flags |=WindowManager. LayoutParams.FLAG_NOT_TOUCH_MODAL |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //宽
        lp.width = 400;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //高
        lp.height = 900;
        lp.gravity = Gravity.TOP;
        //要加载的 资源  Id 三种实例化
        // LayoutInflater inflater1 = LayoutInflater.from(this);
//LayoutInflater inflater2 = getLayoutInflater();
//LayoutInflater inflater3 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.activity_main, mLayout);
//        mUnityContent = mLayout.findViewById(R.id.content);
//        mUnityContent.addView(mUnityPlayer);
        wm.addView(mLayout, lp);

    }
}
