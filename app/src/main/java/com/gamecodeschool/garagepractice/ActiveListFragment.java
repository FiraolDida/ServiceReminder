package com.gamecodeschool.garagepractice;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActiveListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private TextView emptyText2;
    public RecyclerView.Adapter adapter;
    private List<String> listItems;
    private MyDBHandler myDBHandler;
    private View activeListFragment;
    public SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activeListFragment = inflater.inflate(R.layout.fragment_active_list, container, false);

        recyclerView = (RecyclerView) activeListFragment.findViewById(R.id.recyclerView);
        emptyText2 = (TextView) activeListFragment.findViewById(R.id.emptyText2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = (SwipeRefreshLayout) activeListFragment.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        getData();
        adapter.notifyDataSetChanged();

        return activeListFragment;
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

        recyclerView = (RecyclerView) activeListFragment.findViewById(R.id.recyclerView);
        //emptyText = (TextView) findViewById(R.id.emptyText);
        myDBHandler = new MyDBHandler(getContext());

        ArrayList<ListItem> arrayList = new ArrayList<ListItem>();
        listItems = new ArrayList<>();
        Cursor cursor = myDBHandler.getAllRows();

        long start = 0, finish = 0;

        if(cursor.getCount() == 0){
            Toast.makeText(getContext(), "The database is empty " , Toast.LENGTH_LONG).show();
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
                listItem.set_phone2(cursor.getInt(9));
                arrayList.add(listItem);
            }

            emptyText2.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }



        adapter = new MyAdapter(getContext() , arrayList);
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
