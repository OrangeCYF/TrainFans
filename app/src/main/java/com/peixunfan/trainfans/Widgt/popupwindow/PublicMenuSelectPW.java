package com.peixunfan.trainfans.Widgt.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.infrastructure.utils.AppUtil;
import com.peixunfan.trainfans.Base.BaseAdapter;
import com.peixunfan.trainfans.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/24.
 */

public class PublicMenuSelectPW  {
    Context mContext;

    View mView;

    PopupWindow popupWindow;

    RecyclerView recyclerView;

    String mCurrentType;
    ArrayList<String> mTypes = new ArrayList<>();

    TypeSelectAdapter mTypeSelectAdapter;

    float alpha = 1.0f;

    AdapterView.OnItemClickListener mOnItemClickListener;

    public PublicMenuSelectPW(Context context, ArrayList<String> types, String currentSelected, AdapterView.OnItemClickListener onItemClickListener){
        mContext = context;
        mTypes.addAll(types);
        mCurrentType = currentSelected;
        mOnItemClickListener = onItemClickListener;
        initUI();
    }

    private void initUI(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_typeselect_type,null);
        recyclerView =(RecyclerView)mView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mTypeSelectAdapter = new TypeSelectAdapter(mContext,mTypes,mCurrentType);
        recyclerView.setAdapter(mTypeSelectAdapter);
        mTypeSelectAdapter.setOnItemClickListener((adapterView, view, i, l) -> {
            popupWindow.dismiss();
            if (i < mTypes.size()-1){
                mOnItemClickListener.onItemClick(null,null,i,l);
            }
        });
    }

    public void show(View view){
        float popwindowHeight = 0;
        popwindowHeight = AppUtil.dip2px(mContext,50)*mTypes.size()+ AppUtil.dip2px(mContext,10);

        popupWindow = new PopupWindow(mView, AppUtil.getWidth(mContext), (int)popwindowHeight, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);

        while (alpha>0.6){
            alpha = alpha - 0.1f;
            new Handler().postDelayed(() -> AppUtil.backgroundAlpha((Activity) mContext,alpha),150);
        }

        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);

        popupWindow.setOnDismissListener(() -> {
            popupWindow = null;
            AppUtil.backgroundAlpha((Activity) mContext,1f);
        });
    }

    public class TypeSelectAdapter extends BaseAdapter<String> {

        String mCurrentType;

        public TypeSelectAdapter(Context context, ArrayList<String> datas,String currentType) {
            super(context, datas);
            mCurrentType = currentType;
        }

        @Override
        protected int getResourceId() {
            return R.layout.adapter_base_type_select_item;
        }

        @Override
        protected RecyclerView.ViewHolder onCreateItemHolderViewHolder(View arg0) {
            return new ItemViewHolder(arg0, this.mContext);
        }

        @Override
        protected void onBindContentViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            final ItemViewHolder aItemViewHolder = (ItemViewHolder) viewHolder;

            if (mItemClickListener != null) {
                aItemViewHolder.mView.setOnClickListener(v -> mItemClickListener.onItemClick(null, null, position, 0));
            }

            if(position == mDatas.size() - 1)
            {
                aItemViewHolder.bottomLine.setVisibility(View.GONE);
                aItemViewHolder.topblank.setVisibility(View.VISIBLE);
            }else{
                aItemViewHolder.topblank.setVisibility(View.GONE);
                aItemViewHolder.bottomLine.setVisibility(View.VISIBLE);
            }

            aItemViewHolder.mTypeTv.setText(mDatas.get(position));

            if (mDatas.get(position).equals(mCurrentType)){
                aItemViewHolder.mTypeTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }else{
                aItemViewHolder.mTypeTv.setTextColor(mContext.getResources().getColor(R.color.color_a0a0a0));
            }
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_type)
        TextView mTypeTv;
        @Bind(R.id.iv_bottom_line)
        View bottomLine;
        @Bind(R.id.rlv_topblank)
        View topblank;

        View mView;

        public ItemViewHolder(View arg0, Context aContext) {
            super(arg0);
            ButterKnife.bind(this,arg0);
            mView = arg0;
        }
    }

}
