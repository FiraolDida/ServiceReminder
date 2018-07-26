package com.gamecodeschool.garagepractice;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by FIRAOL DIDA on 25-Mar-18.
 */

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private MyDBHandler myDBHandler;
    private Calendar calendar;
    private int km = 0;

    public MyAdapter2(Context context, List<ListItem> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public MyAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_item, parent, false);
        return  new MyAdapter2.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter2.ViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);
        String phone = "0"+String.valueOf(listItem.get_phone());

        holder.textViewHead2.setText(listItem.get_nameList());
        holder.textViewPhone2.setText("Phone: " + phone);
        holder.textViewDesc2.setText(listItem.get_desc());
        holder.textViewDate2.setText("Date: NULL/NULL");

        holder.linearLayout2.setOnLongClickListener(new View.OnLongClickListener() {
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


    public void createDialog(View view, final String name, final int id, final String plateNumber, final int position){
        final CharSequence[] items = new CharSequence[]{"Update date", "Delete"};
        myDBHandler = new MyDBHandler(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Action on " + name);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    kilometerAlertDialog(id, name, plateNumber);
                    listItems.remove(position);
                    notifyDataSetChanged();
                }
                else {
                    //Toast.makeText(context, "DELETED " , Toast.LENGTH_LONG).show();
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

        public TextView textViewHead2;
        public TextView textViewDesc2;
        public TextView textViewPhone2;
        public LinearLayout linearLayout2;
        public TextView textViewDate2;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewHead2 = (TextView) itemView.findViewById(R.id.textViewHead2);
            textViewDesc2 = (TextView) itemView.findViewById(R.id.textViewDesc2);
            textViewPhone2 = (TextView) itemView.findViewById(R.id.textViewPhone2);
            linearLayout2 = (LinearLayout) itemView.findViewById(R.id.linearLayout2);
            textViewDate2 = (TextView) itemView.findViewById(R.id.textViewDate2);
        }
    }
}
