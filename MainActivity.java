package cn.gowild.robotlife.eve;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gowild.aidl.AidlEvent;
import cn.gowild.robotlife.eve.bean.ButtonTextEvent;
import cn.gowild.robotlife.eve.bean.MenuBean;
import cn.gowild.robotlife.eve.utils.DpUtils;
import cn.gowild.robotlife.eve.utils.UIUtils;
import cn.gowild.robotlife.eve.view.SpacesItemDecoration;
import cn.gowild.robotlife.sdk.RobotActivity;
import cn.gowild.robotlife.sdk.UnityHelper;


public class MainActivity extends RobotActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CHANGE_TIPS = 1;
    @BindView(R.id.menu_second_ll)
    LinearLayout mMenuFirstLl;
    @BindView(R.id.menu_firs_ll)
    LinearLayout mMenuSecondLl;
    @BindView(R.id.home_menu)
    LinearLayout mHomemenu;
    @BindView(R.id.bg_click_rl)
    RelativeLayout mBg;
    @BindView(R.id.menu_third_ll)
    LinearLayout mMenuThirdLl;
    @BindView(R.id.menu_left)
    RecyclerView mMenuView;
    @BindView(R.id.robot_logo)
    ImageView mLogo;
    //    @BindView(R.id.menu_new)
    //    LinearLayout mMenuNew;
    @BindView(R.id.home_back)
    ImageView mHomeBack;
    @BindView(R.id.home_tip)
    TextView mTips;
    private LinearLayoutManager mLayoutManager;
    private MenuAdapter mMenuAdapter;
    private List<MenuBean> mMenuData;
    private boolean isTipOne;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch (what) {
                case CHANGE_TIPS:
                    mTips.setText(/*isTipOne ? getString(R.string.home_tip1) : */getString(R.string.home_tip2));
                    isTipOne = !isTipOne;
                    mHandler.sendEmptyMessageDelayed(CHANGE_TIPS,5000);
                break;
                default:
                    break;
            }
        }
    };
    private FrameLayout mContent;
    private boolean loadSuccess;

    @Override
    public FrameLayout getUnityContainer() {
        EventBus.getDefault().register(this);
        initData();
        setContentView(R.layout.activity_main);
        initWindow();
        initView();

        return mContent;
    }

    private void initWindow() {
        //?????? windowManager ?????? ?????? WINDOW_SERVICE ????????????
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        //??????FrameLayout ?????? ??????
        FrameLayout mLayout = new FrameLayout(this);
        //???????????????????????? ??????  ?????? ?????????
        WindowManager.LayoutParams lp = new
                WindowManager.LayoutParams();
        // ??????
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        //?????? ??????????????? ???????????? ??? ??????????????????????????????
        lp.flags |=WindowManager. LayoutParams.FLAG_NOT_TOUCH_MODAL |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //???
        lp.width = 400;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //???
        lp.height = 900;
        lp.gravity = Gravity.TOP;
        //???????????? ??????  Id ???????????????
        // LayoutInflater inflater1 = LayoutInflater.from(this);
//LayoutInflater inflater2 = getLayoutInflater();
//LayoutInflater inflater3 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.activity_main, mLayout);
        mContent = mLayout.findViewById(R.id.content);
        ButterKnife.bind(this,mLayout);
        wm.addView(mLayout, lp);
    }

    private void initView() {
        mLogo.setVisibility(View.GONE);
        mMenuView.setVisibility(View.GONE);
        mHomeBack.setVisibility(View.GONE);
        mHomemenu.setVisibility(View.GONE);
        mTips.setVisibility(View.GONE);

        mLayoutManager = new GridLayoutManager(this,2);
        mMenuView.setLayoutManager(mLayoutManager);
        mMenuAdapter = new MenuAdapter<>(this);
        mMenuAdapter.setData(mMenuData);
        mMenuView.setAdapter(mMenuAdapter);

//        ViewTreeObserver viewTreeObserver = mMenuView.getViewTreeObserver();
//        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                //???????????????????????????????????????
//                mMenuView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                mMenuView.
//                mMenuView.addItemDecoration(new SpacesItemDecoration(item));
//
//            }
//        });

        DisplayMetrics mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics );
        Log.d(TAG,"screen " + mMetrics.toString());
        int item = DpUtils.dip2px(this,85);
        Log.d(TAG,"item space(dp2px) " + item );
        int space = UIUtils.getDimens(R.dimen.dp_85);
        Log.d(TAG,"item space(dimens) " + space );

        int width = getWindowManager().getDefaultDisplay().getWidth()* 22 / 100;
        Log.d(TAG,"item space(percent) " + width );
        mMenuView.addItemDecoration(new SpacesItemDecoration(width));
        mBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WakeUp();
                showLeftMenu();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mMenuData = new ArrayList<>();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(final ButtonTextEvent messageEvent) {
        if (!TextUtils.isEmpty(messageEvent.text)) {
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                   
                }
            });
        }
    }

    /**
     * ?????????????????????????????????(???voice????????????)
     * @param aidlEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(AidlEvent aidlEvent) {
        if ("wakeup".equals(aidlEvent.adilMsg)) {
            Log.d(TAG,"wakeup robot");
            setPosition("2.2,0,0");
            showLeftMenu();
            return;
        }

        //voice?????????????????????
        if (!TextUtils.isEmpty(aidlEvent.adilMsg)) {
            try {
                JSONObject jsonObject = new JSONObject(aidlEvent.adilMsg);
                String module = jsonObject.has("module") ? jsonObject.getString("module") : "";
                JSONObject data = jsonObject.getJSONObject("data");
                switch (module) {
                    //?????? {"data":{"button":"wakeup"},"module":"menu"} ?????????????????????????????????
//                    case "menu":
//                        if (data != null && data.has("button")
//                                && "wakeup".equals(data.getString("button"))) {
//                            Log.d(TAG,"wakeup robot");
//                            showLeftMenu();
//                        }
//                        break;
                    case "chatMenu":
                        JSONArray buttons = data.getJSONArray("buttons");
                        //                    List<MenuBean> menuDatas = new ArrayList<>();
                        List<MenuBean> list = new ArrayList<>();
                        for (int i = 0; i < buttons.length(); i++) {
                            JSONObject menuData = buttons.getJSONObject(i);
                            String menu = menuData.has("question") ? menuData.getString("question") : "";
                            String name = menuData.has("name") ? menuData.getString("name") : "";
                            if (!TextUtils.isEmpty(menu) || !TextUtils.isEmpty(name)) {
                                list.add(new MenuBean(menu,name));
                            }
                        }
                        Log.d(TAG, "menu " + list.size());
                        if (list.size() > 0) {
                            mMenuAdapter.setData(list);
                            showNextMenu();
                        } else {
                            showLeftMenu();
                        }
                        break;
                    case "recordingTipUI":
                        //?????????????????? ??????????????????????????????????????????????????????????????????
                        setPersonLeft();
                        String state = data.has("state") ? data.getString("state") : "";
                        Log.d(TAG,"recordingTipUI state " + state);
//                        if (state.equals("open") || state) {
                            mTips.setVisibility(View.GONE);
//                        }
                        break;
                    case "machineTipUI":
                        String mState = data.has("state") ? data.getString("state") : "";
                        Log.d(TAG,"machineTipUI state " + mState);
//                        if (mState.equals("open")) {
//                            mTips.setVisibility(View.GONE);
//                        } else {
//                            mTips.setVisibility(View.VISIBLE);
//                            showLeftMenu();
//                        }
                        //???????????????????????????????????????????????????????????????????????????????????????
                        if (mState.equals("close") && loadSuccess) {
//                            setPosition("0,0,0");
                            mTips.setVisibility(View.VISIBLE);
                            hideLeftMenu();
                        }
                        break;
                    case "robot":
                        String position = data.has("position") ? data.getString("position") : "";
                        if (!TextUtils.isEmpty(position) ) {
                            Log.d(TAG,"robot position " + position);
                            setPosition(position);
                        }
                        break;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPersonLeft() {
        setPosition("2.2,0,0");
    }

    private void setPosition(String position) {
        if (!TextUtils.isEmpty(position)) {
            String[] positions = position.split(",");
            if ( positions.length >= 3) {
                //				JSONObject jsonObject = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    JSONObject pos = new JSONObject();
                    pos.put("x",positions[0]);
                    pos.put("y",positions[1]);
                    pos.put("z",positions[2]);
                    data.put("position",pos);
                    //					jsonObject.put("data",data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UnityHelper.getInstance().SendToU3D("transform",data);
                Log.d(TAG,"position " + data.toString());
            }
        }
    }

    @Override
    public void onSceneShow() {
        super.onSceneShow();
        loadSuccess = true;
//        ????????????????????????
//        String position = GlobalProperty.getPropertyDefault(RobotConstants.ROBOT_POSITION, "");
        setPosition("2.2,0,0");
        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                mLogo.setVisibility(View.VISIBLE);
                // ??????????????????????????????tip??????
                mTips.setVisibility(View.VISIBLE);
//                mHandler.sendEmptyMessageDelayed(CHANGE_TIPS,50/00);
                ObjectAnimator mObObjectAnimator = new ObjectAnimator();
                ObjectAnimator alpha = mObObjectAnimator.ofFloat(mTips, "Alpha", 0.0F, 1.0F, 0.0F, 1.0F);
                alpha.setDuration(5000);
                alpha.setRepeatCount(ValueAnimator.INFINITE);//????????????
                alpha.setRepeatMode(ValueAnimator.RESTART);//
                alpha.start();
//                showLeftMenu();
            }
        });
    }

    @OnClick({R.id.menu_second_ll, R.id.menu_firs_ll, R.id.menu_third_ll, R.id.home_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_second_ll:
                sendTextToVoice("???????????????????????????");
                break;
            case R.id.menu_firs_ll:
                sendTextToVoice("?????????????????????");
                break;
            case R.id.menu_third_ll:
                Intent starter = new Intent(MainActivity.this, GowildHtmlActivity.class);
                starter.putExtra(Constants.LOAD_URL, Constants.MENU_URL);
                startActivity(starter);
                break;
            //            case R.id.menu_new_first:
            //                sendTextToVoice(getString(R.string.menu_new_first));
            //                break;
            //            case R.id.menu_new_second:
            //                sendTextToVoice(getString(R.string.menu_new_second));
            //                break;
            case R.id.home_back:
                showLeftMenu();
                clickBack();
                break;
        }
    }

    //????????????????????????????????????
    private void clickBack() {
        JSONObject raw = new JSONObject();
        try {
            raw.put("module", "menu");
            JSONObject data = new JSONObject();
            data.put("button", "back");
            raw.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UnitySendAndroid(raw.toString());
        WakeUp();
    }

    /**
     * ????????????????????????
     */
    private void showLeftMenu() {
        // TODO: 2020/4/1 ??????????????????
//        mMenuView.setVisibility(View.GONE);
//        mHomeBack.setVisibility(View.GONE);
//        mHomemenu.setVisibility(View.VISIBLE);
    }

    /**
     * ?????????????????????????????????
     */
    private void hideLeftMenu() {
        mHomemenu.setVisibility(View.GONE);
        mMenuView.setVisibility(View.GONE);
        mHomeBack.setVisibility(View.GONE);
    }

    private void showNextMenu() {
//        mMenuView.setVisibility(View.VISIBLE);
//        mHomeBack.setVisibility(View.VISIBLE);
//        mHomemenu.setVisibility(View.GONE);
        mTips.setVisibility(View.GONE);
    }

    /**
     * ????????????????????????voice????????????????????????????????????????????????voice??????unity??????tts??????
     * @param text
     */
    private void sendTextToVoice(String text) {
        JSONObject raw = new JSONObject();
        try {
            raw.put("module", "menu");
            JSONObject data = new JSONObject();
            data.put("button", "text");
            data.put("content", text);
            raw.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UnitySendAndroid(raw.toString());
    }

    public void playTextAnimation() {
//        ValueAnimator animator = ObjectAnimator.ofInt(mTips, "backgroundColor", 0x00ff0000, 0x6600ff00);//???????????????????????????????????????????????????"backgroundColor",???????????????????????????????????????,????????????????????????????????????????????????
//        animator.setDuration(3000);
//        animator.setEvaluator(new ArgbEvaluator());//??????????????????????????????ArgbEvaluator??????????????????????????????????????????????????????????????????????????????
//        animator.start();
//        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
//        alphaAnimation.setDuration(2500);
//        mTips.startAnimation(alphaAnimation);
    }



}

