package com.ixxj.proverbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yokeyword.indexablelistview.IndexBarAdapter;

/**
 * Created by lintex on 2016/3/28.
 */
public class CityAdapter extends IndexBarAdapter<CityEntity> {
    private Context mContext;

    public CityAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected TextView onCreateTitleViewHolder(ViewGroup parent) {
        // 创建 Sticky字母的Header布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tv_title_city, parent, false);
        return (TextView) view.findViewById(R.id.tv_title);
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent) {
        // 创建 Item布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, CityEntity cityEntity) {
        // 为Item布局绑定数据
        CityViewHolder cityViewHolder = (CityViewHolder) holder;
        cityViewHolder.tvCity.setText(cityEntity.ItemEnglish);
        cityViewHolder.tvID.setText(cityEntity.ItemId.toString());
        cityViewHolder.tvChinese.setText(cityEntity.ItemChinese);
    }

    // ViewHolder需要继承IndexBarAdapter.ViewHolder
    class CityViewHolder extends IndexBarAdapter.ViewHolder {
        TextView tvCity;
        TextView tvID;
        TextView tvChinese;
        public CityViewHolder(View view) {
            super(view);
            tvCity = (TextView) view.findViewById(R.id.tv_name);
            tvID = (TextView) view.findViewById(R.id.tv_id);
            tvChinese = (TextView) view.findViewById(R.id.tv_chinese);

        }
    }
}