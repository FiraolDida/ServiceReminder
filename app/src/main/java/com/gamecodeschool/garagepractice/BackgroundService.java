package com.gamecodeschool.garagepractice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by FIRAOL DIDA on 18-Mar-18.
 */

public class BackgroundService extends Service{
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private MyDBHandler myDBHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
        myDBHandler = new MyDBHandler(this);
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            String name, desc;
            int phone;
            int phone2;

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            Log.i("Today's DAY", "run: " + day);
            Date date = calendar.getTime();
            String month = (String) DateFormat.format("MM", date);
            int todays_month = Integer.parseInt(month);

            Cursor cursor = myDBHandler.getPhone(day, todays_month);

            if (cursor.getCount() == 0) {
                Log.i("CODE IN CURSOR COUNTER", " The database is empty");
            } else {
                while (cursor.moveToNext()) {
                    name = cursor.getString(1);
                    phone = cursor.getInt(2);
                    desc = cursor.getString(3);
                    phone2 = cursor.getInt(9);
                    Log.i("phone2", "run: " + phone2);

                    sendMessage(phone, name, desc, phone2);
                }
            }

            Log.i("CODE IN RUN BLOCK", " CODE IN RUN BLOCK");
            stopSelf();
        }
    };

    public void sendMessage(int phone, String name, String desc, int phone2){
        SmsManager smsManager = SmsManager.getDefault();
        String _phone = "0" + phone;
        String _phone2 = "0" + phone2;

        if (_phone2.equals("00")){
            smsManager.sendTextMessage(_phone, null, desc, null, null);
            Log.i("SEND_TEST", "sendMessage: " + _phone + ", " + desc);
        }else {
            smsManager.sendTextMessage(_phone, null, desc, null, null);
            smsManager.sendTextMessage(_phone2, null, desc, null, null);
            Log.i("SEND_TEST", "sendMessage: " + _phone + " | " + _phone2 + ", " + desc);
        }


        setFlagRecord(name);
    }

    public void setFlagRecord(String name){
        myDBHandler.updateFlag(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning){
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }
}
