package com.ixxj.proverbs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.yokeyword.indexablelistview.IndexEntity;
import com.yokeyword.indexablelistview.IndexableStickyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainSearchActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private IndexableStickyListView mIndexableStickyListView;
    private SearchView mSearchView;
    private boolean hideChinese = true;
    private boolean showSearch = true;

    private long exitTime = 0;
    private CityAdapter mAdapter;
    private List<CityEntity> mCities = new ArrayList<>();
    private DataBaseHelper myDbHelper = new DataBaseHelper(null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        initView();
    }

    private void initView() {
        mSearchView = (SearchView) findViewById(R.id.id_searchView);
        mIndexableStickyListView = (IndexableStickyListView) findViewById(R.id.id_lv_searchListView);

        View headerView = getLayoutInflater().inflate(R.layout.header, null);
        mIndexableStickyListView.addHeaderView(headerView);

        mAdapter = new CityAdapter(this);
        mIndexableStickyListView.setAdapter(mAdapter);

        // 初始化数据
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

        Cursor c = myDbHelper.myDataBase.rawQuery("select * from proverbtable", null);
        if (c != null) {
            while (c.moveToNext()) {
                CityEntity cityEntity = new CityEntity(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("english")), c.getString(c.getColumnIndex("chinese")));
                cityEntity.setName(c.getString(c.getColumnIndex("english")));
                mCities.add(cityEntity);
            }
            c.close();
        }
        myDbHelper.myDataBase.close();

        mIndexableStickyListView.bindDatas(mCities);

        mIndexableStickyListView.setOnItemContentClickListener(new IndexableStickyListView.OnItemContentClickListener() {
            @Override
            public void onItemClick(View v, IndexEntity indexEntity) {
                //ContactEntity contactEntity = (ContactEntity) indexEntity;
                //Toast.makeText(PickContactActivity.this, "选择了" + contactEntity.getName() + " 手机号:" + contactEntity.getMobile(), Toast.LENGTH_SHORT).show();
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mIndexableStickyListView.searchTextChange(newText);
                return true;
            }
        });
    }

    private void hideSearch() {
        setTitle("英语谚语大全");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mSearchView.setVisibility(View.GONE);
        showSearch = true;
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
                //mListView.setAdapter(new MyAdapter(this, itemBeanList, hideChinese));
                break;
            case R.id.id_menu_setting:
                startActivity(new Intent(this, Main2Activity.class));
                break;
            case R.id.id_menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.id_menu_search:
                if (showSearch) {
                    setTitle("输入单词查找谚语");
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }
                    mSearchView.setVisibility(View.VISIBLE);
                    mSearchView.setFocusable(true);
                    mSearchView.requestFocus();
                    //SearchView获得焦点后没有显示键盘,强制显示出来
                    //InputMethodManager imm = (InputMethodManager) mSearchView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.showSoftInput(mSearchView, InputMethodManager.SHOW_FORCED);
                    //InputMethodManager inputManager = (InputMethodManager)mSearchView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    //inputManager.showSoftInput(mSearchView, 0);

                    InputMethodManager m = (InputMethodManager) mSearchView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    showSearch = false;
                } else {
                    hideSearch();
                }
                break;
            case R.id.id_menu_exit:
                finish();
                break;
            case android.R.id.home:
                hideSearch();
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
            if (!showSearch) {
                hideSearch();
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            }
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
