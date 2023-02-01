package cn.gowild.robotlife.eve;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gowild.robotlife.eve.base.AdapterDelegate;
import cn.gowild.robotlife.eve.bean.ButtonTextEvent;
import cn.gowild.robotlife.eve.bean.MenuBean;

/**
 * @author liudu
 * @version 1.0a
 * <p><strong>Features draft description.主要功能介绍</strong></p>
 * @since 2020/1/14 16:19
 */
public class MenuDelegate extends AdapterDelegate<List<MenuBean>> {

    private final MenuAdapter mMenuAdapter;
    private final Activity mActivity;
    private final LayoutInflater mLayoutInflater;

    public MenuDelegate(Activity activity, MenuAdapter adapter) {
        mActivity = activity;
        mMenuAdapter = adapter;
        mLayoutInflater = activity.getLayoutInflater();
    }

    @Override
    protected boolean isForViewType(List<MenuBean> items, int postion) {
        return true;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new MenuHolder(mLayoutInflater.inflate(R.layout.menu_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<MenuBean> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        final MenuBean bean = items.get(position);
        if (bean == null) {
            return;
        }

        MenuHolder menuHolder = (MenuHolder) holder;
        menuHolder.itemView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(bean.name)) {
            menuHolder.name.setText(bean.name);
        } else if (!TextUtils.isEmpty(bean.question)) {
            menuHolder.name.setText(bean.question);
        } else {
            //menuHolder.itemView.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(bean.question)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ButtonTextEvent(bean.question));
                }
            });
        }
    }


    public class MenuHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_menu_name)
        public TextView name;

        public MenuHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
