package cn.gowild.robotlife.eve;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.gowild.robotlife.eve.base.AdapterDelegatesManager;
import cn.gowild.robotlife.eve.bean.MenuBean;


public class MenuAdapter<T extends MenuBean> extends RecyclerView.Adapter {
    List<MenuBean> mItemList;
    private AdapterDelegatesManager<List<MenuBean>> delegatesManager;

    public MenuAdapter(Activity activity) {
        this.mItemList = new ArrayList<>();
        delegatesManager = new AdapterDelegatesManager<>();
        delegatesManager.addDelegate(new MenuDelegate(activity, this));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        delegatesManager.onBindViewHolder(mItemList, position, holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        delegatesManager.onBindViewHolder(mItemList, position, holder, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(mItemList, position);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setData(List<MenuBean> list) {
        mItemList.clear();
        Log.d("setData ", "list " + list.size());
        for (int i = 0; i < list.size(); i++) {
            //菜单展示最大数量
            if (i < 9) {
                mItemList.add(list.get(i));
                Log.d("setData ", "list " + list.get(i).name + " add ");
                //占位改变竖行顺序
                if (list.size() <= 5) {
                    Log.d("setData ", "list " + i + " add empty");
                    mItemList.add(new MenuBean(""));
                }
            }
        }
        notifyDataSetChanged();
    }
}
