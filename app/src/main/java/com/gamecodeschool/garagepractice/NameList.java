package com.gamecodeschool.garagepractice;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NameList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private TextView emptyText;
    public RecyclerView.Adapter adapter;
    private List<String> listItems;
    private MyDBHandler myDBHandler;
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //emptyText = (TextView) findViewById(R.id.emptyText);
        myDBHandler = new MyDBHandler(this);

        ArrayList<ListItem> arrayList = new ArrayList<ListItem>();
        listItems = new ArrayList<>();
        Cursor cursor = myDBHandler.getAllRows();

        long start = 0, finish = 0;

        if(cursor.getCount() == 0){
            Toast.makeText(this, "The database is empty " , Toast.LENGTH_LONG).show();
        }else {

            while(cursor.moveToNext()){
                ListItem listItem = new ListItem();
                listItem.set_nameList(cursor.getString(1));
                listItem.set_phone(cursor.getInt(2));
                listItem.set_desc(cursor.getString(3));
                listItem.set_date(Integer.parseInt(cursor.getString(4)));
                Log.d("Populate", "populate: " + cursor.getString(3));
                listItem.set_month(cursor.getInt(5));
                listItem.set_km(cursor.getInt(6));
                listItem.set_plateNumber(cursor.getString(7));
                arrayList.add(listItem);
            }

            //emptyText.setVisibility(View.GONE);
            //recyclerView.setVisibility(View.VISIBLE);
        }



        adapter = new MyAdapter(this , arrayList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        Log.d("NameList", "On swipe refresh called.");
        getData();
    }
}
