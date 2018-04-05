package com.gamecodeschool.garagepractice;

import android.database.Cursor;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WaitingList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView2;
    private TextView emptyText2;
    public RecyclerView.Adapter adapter;
    private List<String> listItems;
    private MyDBHandler myDBHandler;
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);

        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_name_list);
        swipeRefreshLayout.setOnRefreshListener(this);

        getData();
        adapter.notifyDataSetChanged();
    }

    public void getData(){
        try{
            swipeRefreshLayout.setRefreshing(true);
            populate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void populate(){
        myDBHandler = new MyDBHandler(this);

        ArrayList<ListItem> arrayList = new ArrayList<ListItem>();
        listItems = new ArrayList<>();
        Cursor cursor = myDBHandler.getFlagInfo();

        if(cursor.getCount() == 0){
            Toast.makeText(this, "The database is empty " , Toast.LENGTH_LONG).show();
        }else {

            while(cursor.moveToNext()){
                ListItem listItem = new ListItem();
                listItem.set_nameList(cursor.getString(1));
                //listItem.set_phone("Phone: "+(cursor.getString(2)));
                listItem.set_phone(cursor.getInt(2));
                listItem.set_desc(cursor.getString(3));
                listItem.set_date(Integer.parseInt(cursor.getString(4)));
                listItem.set_month(cursor.getInt(5));
                listItem.set_km(cursor.getInt(6));
                listItem.set_plateNumber(cursor.getString(7));
                arrayList.add(listItem);
            }
//            emptyText2.setVisibility(View.GONE);
//            recyclerView2.setVisibility(View.VISIBLE);
        }



        adapter = new MyAdapter2(this , arrayList);
        recyclerView2.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onRefresh() {
        Log.d("WaitingList", "On swipe refresh called.");
        getData();
    }
}
