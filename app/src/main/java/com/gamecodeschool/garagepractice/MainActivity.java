package com.gamecodeschool.garagepractice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText contactName;
    EditText contactNumber;
    EditText kilometer;
    EditText plateNumber;
    MyDBHandler myDBHandler;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactName = (EditText) findViewById(R.id.contactName);
        contactNumber = (EditText) findViewById(R.id.contactNumber);
        kilometer = (EditText) findViewById(R.id.kilometer);
        plateNumber = (EditText) findViewById(R.id.plateNumber);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        myDBHandler = new MyDBHandler(this);

    }

    public void handleContact(View v){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent , 2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
        //Toast.makeText(this, "WORKING WELL", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == resultCode){
            switch (requestCode){
                case 2:
                    contactPicked(data);
                    break;
            }
        }
        else {
            Log.i("contactPractice", "Error fetching contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String num = null ;
            String name = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            num = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            if (num.contains("+251")){
                num = num.replace("+251", "0");
            }
            if (num.contains("-")){
                num = num.replace("-", "");
            }if (num.contains(" ")){
                num = num.replace(" ", "");
            }


            contactNumber.setText(num);
            contactName.setText(name);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleAdd(View v){
        String name = contactName.getText().toString();
        String p_Number = plateNumber.getText().toString();
        int phone = 0;
        int _km = 0;
        int km = 0;

        if(contactName.length() !=0 && contactNumber.length() !=0 && kilometer.length() !=0 && plateNumber.length() !=0){
            if (contactNumber.length() == 10){
                if (kilometer.length() <= 6){
                    phone = Integer.parseInt(contactNumber.getText().toString());
                    _km = Integer.parseInt(kilometer.getText().toString());
                    km = addOnKilometer(_km);
                    if (km < 1000000){
                        checkInDatabase(name, phone, km, p_Number);
                    }else {
                        Toast.makeText(MainActivity.this, "Kilometer exceed maximum number", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    kilometer.setError("Exceed maximum number");
                    Toast.makeText(MainActivity.this, "Exceed maximum number", Toast.LENGTH_LONG).show();
                }
            }
            else {
                contactNumber.setError("Invalid format");
                Toast.makeText(MainActivity.this, "Invalid phone number", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(MainActivity.this, "Please fill in all the field!", Toast.LENGTH_LONG).show();
        }
    }

    private void checkInDatabase(String name, int phone, int km, String _plateNumber) {
        boolean status = myDBHandler.checkForExistance(name, phone);
        if (status){
            contactName.setText("");
            contactNumber.setText("");
            kilometer.setText("");
            plateNumber.setText("");
            Toast.makeText(this, "The content you are adding already exists in the Database", Toast.LENGTH_SHORT).show();
        }
        else {
            addData(name, phone, km, _plateNumber);
        }
    }

    public int addOnKilometer(int num){
        int kilo_M = num + 5000;
        return kilo_M;
    }

    public void addData(String name, int phone, int km, String _plateNumber){
        boolean insertData;
        String desc = "Dear " + name + ", it's your car service time. Plate number: " + _plateNumber + ", Kilometer: " + km + ".\nThank you, Daniel Garage";
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 3);
        int time = calendar.get(Calendar.DAY_OF_MONTH);
        Date date = calendar.getTime();
        String month = (String) DateFormat.format("MM", date);
        int f_month = Integer.parseInt(month);

        Log.i("TEST", "date: " + time + "month: " + f_month);

        try{
            insertData = myDBHandler.addData(name, phone, desc, time, f_month, km, _plateNumber);
            if (insertData) {
                Toast.makeText(MainActivity.this, "Successfully entered", Toast.LENGTH_LONG).show();
                contactName.setText("");
                contactNumber.setText("");
                kilometer.setText("");
                plateNumber.setText("");
                helperfunction();
            }
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleShowList(View v){
        Intent intent = new Intent(MainActivity.this, NameList.class);
        startActivity(intent);
    }

    public void handleWaitingList(View v){
        Intent intent = new Intent(MainActivity.this, WaitingList.class );
        startActivity(intent);
    }

    public void helperfunction(){
        Intent intent = new Intent(this, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmRunning == false){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
            Toast.makeText(this, "Alarm scheduled ", Toast.LENGTH_SHORT).show();
        }
    }
}
