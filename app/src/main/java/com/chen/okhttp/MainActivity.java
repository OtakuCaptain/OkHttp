package com.chen.okhttp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NewsAdapter mAdapter;
    private ArrayList<NewsDetail> mData;
    private SwipeRefreshLayout mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initData();
        loadData();
    }

    private void initData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String newsString = prefs.getString("News", null);
        if (newsString != null) {
            mData = processData(newsString);
            initRecyclerView();
        }

    }

    private void initRecyclerView() {
        mAdapter = new NewsAdapter(MainActivity.this, mData);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initView() {
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_news);

        mRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }


    public void loadData() {

//        String url = "http://192.168.1.109:8080/zhbj/categories.json";
        String url = "http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取新闻失败", Toast.LENGTH_SHORT).show();
                        mRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.i("新闻", responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                        editor.putString("News", responseText);
                        editor.apply();
                        initRecyclerView();
                        mRefresh.setRefreshing(false);
                    }
                });

//                ArrayList<NewsModel> newsModels = NewsJsonUtils.readJsonNewsBeans(responseText);
//                listener.onSuccess(newsModels);
            }
        });

    }

    private static ArrayList<NewsDetail> processData(String json) {
        Gson gson = new Gson();
//        NewsMenu newsMenu =  gson.fromJson(json, NewsMenu.class);
        NewsBean newsMenu = gson.fromJson(json, NewsBean.class);
        Log.i("Pager", "解析结果：" + String.valueOf(newsMenu));
        return newsMenu.data;
    }
}
