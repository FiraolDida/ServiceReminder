package com.gamecodeschool.garagepractice;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by FIRAOL DIDA on 08-Mar-18.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;
    private MyDBHandler myDBHandler;
    private NameList nameList;
    private RecyclerView.Adapter adapter;
    private Calendar calendar;
    private int km = 0;
    private FragmentActivity fragmentActivity;

    public MyAdapter(Context context, List<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);
        String phone = "0" + String.valueOf(listItem.get_phone());
        String phone2 = "0" + String.valueOf(listItem.get_phone2());
        Log.i("phone 2", "onBindViewHolder: " + phone2);

        if (phone2.equals("00")){
            Log.i("phone200", "onBindViewHolder: ");
            holder.textViewHead.setText(listItem.get_nameList());
            holder.textViewPhone.setText("Phone: " +phone);
            holder.textViewDesc.setText(listItem.get_desc());
            holder.textViewDate.setText("Date: "+listItem.get_date() + "/" + listItem.get_month());
        }
        else {
            holder.textViewHead.setText(listItem.get_nameList());
            holder.textViewPhone.setText("Phone: " +phone + " | " + phone2);
            holder.textViewDesc.setText(listItem.get_desc());
            holder.textViewDate.setText("Date: "+listItem.get_date() + "/" + listItem.get_month());
        }



        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                String name = listItem.get_nameList();
                int phone = listItem.get_phone();
                int id = findId(name, phone);
                String plateNumber = listItem.get_plateNumber();
                //Toast.makeText(context, name, Toast.LENGTH_LONG).show();
                createDialog(view, name, id, plateNumber, position);
                return true;
            }
        });

    }

    public int findId(String name, int phone){
        int id = 0;
        myDBHandler = new MyDBHandler(context);
        Cursor cursor = myDBHandler.getId(name, phone);
        while (cursor.moveToNext()){
            id = cursor.getInt(0);
        }
        return id;
    }

    public void createDialog(final View view, final String name, final int id, final String plateNumber, final int position){
        final CharSequence[] items = new CharSequence[]{"Update", "Delete"};
        myDBHandler = new MyDBHandler(context);
        nameList = new NameList();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Action on " + name);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    kilometerAlertDialog(id, name, plateNumber);
                    notifyDataSetChanged();
                }
                else{
                    //Toast.makeText(context, "DELETED " + id, Toast.LENGTH_LONG).show();
                    myDBHandler.deleteName(id);
                    listItems.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateTime(int id){
        myDBHandler = new MyDBHandler(context);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 3);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Date date = calendar.getTime();
        String f_month = (String) DateFormat.format("MM", date);
        int month = Integer.parseInt(f_month);

        myDBHandler.updateDate(id, day, month);
    }

    public void updateDesc(int id, String name, String plateNumber, int km){
        myDBHandler = new MyDBHandler(context);
        myDBHandler.updateDesc(id, km, plateNumber, name);
    }

    public void kilometerAlertDialog(final int id, final String name, final String plateNumber){
        myDBHandler = new MyDBHandler(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Kilometer");
        // Set up the input
        final EditText input = new EditText(this.context);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                km = Integer.parseInt(input.getText().toString());
                int _km = km + 5000;
                if (_km < 1000000){
                    myDBHandler.updateKilometer(id, _km);
                    updateTime(id);
                    updateDesc(id, name, plateNumber, _km);
                    Log.i("TIME", "TIME: UPDATED");
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "Kilometer exceed maximum number", Toast.LENGTH_LONG).show();
                    kilometerAlertDialog(id, name, plateNumber);
                }
            } });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            } });
        builder.show();

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewPhone;
        public LinearLayout linearLayout;
        public TextView textViewDate;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            textViewPhone = (TextView) itemView.findViewById(R.id.textViewPhone);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
        }
    }
}

