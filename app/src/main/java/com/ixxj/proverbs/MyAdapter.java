package com.ixxj.proverbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lintex on 2016/3/24.
 */
public class MyAdapter extends BaseAdapter {
    private List<ItemBean> mList;
    private Context context;
    private boolean hideChinese;
    //初始化列表项分组字母
//    private String [] mListTag = {"A","B","C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "Y", "Z"};


    public MyAdapter(Context context, List<ItemBean> list, boolean hideChinese) {
        mList = list;
        this.context = context;
        this.hideChinese = hideChinese;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.id = (TextView) convertView.findViewById(R.id.id_tv_id);
            viewHolder.english = (TextView) convertView.findViewById(R.id.id_tv_english);
            viewHolder.chinese = (TextView) convertView.findViewById(R.id.id_tv_chinese);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ItemBean bean = mList.get(position);
        viewHolder.id.setText(bean.ItemId.toString());
        viewHolder.english.setText(bean.ItemEnglish);
        viewHolder.chinese.setText(bean.ItemChinese);

        //viewHolder.tagText.setText("以字母" + bean.ItemEnglish + "开头的谚语，共有" + bean.ItemChinese + "条。");
        if (hideChinese) {
            viewHolder.chinese.setVisibility(View.GONE);
        } else {
            viewHolder.chinese.setVisibility(View.VISIBLE);
        }
        return convertView;
    }


    class ViewHolder {
        public TextView id;
        public TextView english;
        public TextView chinese;
    }
}
