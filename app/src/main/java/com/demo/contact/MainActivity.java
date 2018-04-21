package com.demo.contact;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lib.kalu.adapter.BaseCommonAdapter;
import lib.kalu.adapter.holder.RecyclerHolder;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<ContactBean> datas = new ArrayList<>();
    private final List<String> index = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertData();

        final RecyclerView recycler = findViewById(R.id.recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);


        final ContactBar bar = findViewById(R.id.sidebar);
        bar.setOnBarChangeListener(new ContactBar.OnBarChangeListener() {
            @Override
            public void onBarChange(String letter) {

                int position = 0;
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getIndex().equals(letter)) {
                        position = i;
                        break;
                    }
                }

                moveToPosition(linearLayoutManager, position);
            }
        });

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                bar.scrollLetter(datas.get(firstVisibleItemPosition).getIndex());
            }
        });
    }

    private final void moveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

    private final void insertData() {

        for (int i = 0; i < index.size(); i++) {
            ContactBean contactBean1 = new ContactBean();
            contactBean1.setIndex(index.get(i));
            contactBean1.setName("小张");
            contactBean1.setShow(true);
            datas.add(contactBean1);

            ContactBean contactBean2 = new ContactBean();
            contactBean2.setIndex(index.get(i));
            contactBean2.setName("小张");
            contactBean2.setShow(false);
            datas.add(contactBean2);
        }
    }

    private final BaseCommonAdapter<ContactBean> adapter = new BaseCommonAdapter<ContactBean>(datas, R.layout.activity_main_child) {
        @Override
        protected void onNext(RecyclerHolder holder, ContactBean model, int position) {
            holder.setText(R.id.index, model.getIndex());
            holder.setText(R.id.name, model.getName());
            holder.setVisible(R.id.index, model.isShow() ? View.VISIBLE : View.GONE);
        }
    };
}
