package com.ixxj.proverbs;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private DataBaseHelper myDbHelper = new DataBaseHelper(null);
    private ListView mListView;
    private TextView tv_chinese;
    private boolean hideChinese = true;
    private List<ItemBean> itemBeanList;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(this);
        tv_chinese = (TextView) findViewById(R.id.id_tv_chinese);

        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        mListView.setAdapter(new MyAdapter(this, getAllData(), hideChinese));
    }

    // 查询所有数据
    public List<ItemBean> getAllData() {
        itemBeanList = new ArrayList<>();

        Cursor c = myDbHelper.myDataBase.rawQuery("select * from proverbtable where english like 'A%' OR english like '\"A%' LIMIT 100", null);
        if (c != null) {
            while (c.moveToNext()) {
                itemBeanList.add(new ItemBean(c.getInt(c.getColumnIndex("id")),
                        c.getString(c.getColumnIndex("english")),
                        c.getString(c.getColumnIndex("chinese"))
                ));
            }
            c.close();
        }
        myDbHelper.myDataBase.close();
        return itemBeanList;
    }

    //创建浮动菜单项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity, menu);
        return true;
    }

    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_menu_hideChinese:
                if (hideChinese) {
                    item.setTitle("隐藏中文");
                    hideChinese = false;
                } else {
                    item.setTitle("显示中文");
                    hideChinese = true;
                }
                mListView.setAdapter(new MyAdapter(this, itemBeanList, hideChinese));
                break;
            case R.id.id_menu_setting:
                startActivity(new Intent(this, Main2Activity.class));
                break;
            case R.id.id_menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.id_menu_search:
                break;
            case R.id.id_menu_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View v = view.findViewById(R.id.id_tv_chinese);
        if (v.getVisibility() == View.GONE) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
