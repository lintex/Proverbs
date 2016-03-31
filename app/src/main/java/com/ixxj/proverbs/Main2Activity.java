package com.ixxj.proverbs;

import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.yokeyword.indexablelistview.IndexEntity;
import com.yokeyword.indexablelistview.IndexableStickyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private IndexableStickyListView mIndexableStickyListView;
    private SearchView mSearchView;

    private CityAdapter mAdapter;
    private List<CityEntity> mCities = new ArrayList<>();
    private DataBaseHelper myDbHelper = new DataBaseHelper(null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mIndexableStickyListView = (IndexableStickyListView) findViewById(R.id.listView2);

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

        mAdapter = new CityAdapter(this);
        mIndexableStickyListView.setAdapter(mAdapter);



        Cursor c = myDbHelper.myDataBase.rawQuery("select * from proverbtable", null);
        if (c != null) {
            while (c.moveToNext()) {
                CityEntity cityEntity = new CityEntity(c.getInt(c.getColumnIndex("id")),c.getString(c.getColumnIndex("english")),c.getString(c.getColumnIndex("chinese")));
                cityEntity.setName(c.getString(c.getColumnIndex("english")));
                mCities.add(cityEntity);
            }
            c.close();
        }
        myDbHelper.myDataBase.close();

        mIndexableStickyListView.bindDatas(mCities);

        // item点击事件监听
        mIndexableStickyListView.setOnItemContentClickListener(new IndexableStickyListView.OnItemContentClickListener() {
            @Override
            public void onItemClick(View v, IndexEntity indexEntity) {
                CityEntity item = (CityEntity) indexEntity;
                Toast.makeText(Main2Activity.this, item.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
