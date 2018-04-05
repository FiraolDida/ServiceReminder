package com.gamecodeschool.garagepractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by FIRAOL DIDA on 14-Mar-18.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
         Intent background = new Intent(context, BackgroundService.class);
         context.startService(background);
    }
}
