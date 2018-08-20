package com.gamecodeschool.garagepractice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment implements View.OnClickListener {

    EditText contactName;
    EditText contactNumber;
    EditText kilometer;
    EditText plateNumber;
    EditText contactNumber2;
    View homeFragment;
    ImageButton contactButton;
    ImageButton contactButtonAdd;
    LinearLayout contactLayout;
    Button saveButton;
    MyDBHandler myDBHandler;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeFragment = inflater.inflate(R.layout.fragment_home, container, false);

        contactName = (EditText) homeFragment.findViewById(R.id.contactName);
        contactNumber = (EditText) homeFragment.findViewById(R.id.contactNumber);
        contactNumber2 = (EditText) homeFragment.findViewById(R.id.contactNumber2);
        contactLayout = (LinearLayout) homeFragment.findViewById(R.id.contact_layout);
        plateNumber = (EditText) homeFragment.findViewById(R.id.plateNumber);
        kilometer = (EditText) homeFragment.findViewById(R.id.kilometer);
        myDBHandler = new MyDBHandler(getContext());
        saveButton = (Button) homeFragment.findViewById(R.id.saveButton);
        contactButton = (ImageButton) homeFragment.findViewById(R.id.contactButton);
        contactButtonAdd = (ImageButton) homeFragment.findViewById(R.id.contactButtonAdd);
        contactButton.setOnClickListener(this);
        contactButtonAdd.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        copyFile();
        return homeFragment;
    }

    public void handleContact(View v){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent , 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
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

    public void handleContactAdd(View view){
        if (contactLayout.getVisibility() == View.VISIBLE){
            contactLayout.setVisibility(View.GONE);
            contactButtonAdd.setImageResource(R.drawable.ic_add_black_24dp);
        }
        else {
            contactLayout.setVisibility(View.VISIBLE);
            contactButtonAdd.setImageResource(R.drawable.ic_remove_black_24dp);
        }
    }

    public void handleAdd(View view){
        String name = contactName.getText().toString();

        int phone = 0;
        int phone2 = 0;
        int _km = 0;
        int km = 0;

        if(contactName.length() !=0 && contactNumber.length() !=0 && kilometer.length() !=0 && plateNumber.length() !=0 && contactLayout.getVisibility() == View.GONE){
            Log.d("after IF", "handleAdd: " + contactName.getText().toString() + ", "
            + contactNumber.getText().toString() + ", " + kilometer.getText().toString() + ", "
            + plateNumber.getText().toString());
            if (contactNumber.length() == 10){
                if (kilometer.length() <= 6){
                    String p_Number = plateNumber.getText().toString();
                    phone = Integer.parseInt(contactNumber.getText().toString());
                    _km = Integer.parseInt(kilometer.getText().toString());
                    km = addOnKilometer(_km);
                    if (km < 1000000){
                        checkInDatabase(name, phone, km, p_Number);
                    }else {
                        Toast.makeText(getContext(), "Kilometer exceed maximum number", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    kilometer.setError("Exceed maximum number");
                    Toast.makeText(getContext(), "Exceed maximum number", Toast.LENGTH_LONG).show();
                }
            }
            else {
                contactNumber.setError("Invalid format");
                Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
            }

        } else if (contactName.length() !=0 && contactNumber.length() !=0 && kilometer.length() !=0 && plateNumber.length() !=0 && contactLayout.getVisibility() == View.VISIBLE
                && contactNumber2.length() !=0 ){
            Log.d("after IF", "handleAdd: " + contactName.getText().toString() + ", "
                    + contactNumber.getText().toString() + ", " + kilometer.getText().toString() + ", "
                    + plateNumber.getText().toString());
            if (contactNumber.length() == 10 && contactNumber2.length() == 10){
                if (kilometer.length() <= 6){
                    String p_Number = plateNumber.getText().toString();
                    phone = Integer.parseInt(contactNumber.getText().toString());
                    phone2 = Integer.parseInt(contactNumber2.getText().toString());
                    _km = Integer.parseInt(kilometer.getText().toString());
                    km = addOnKilometer(_km);
                    if (km < 1000000){
                        checkInDatabase(name, phone, phone2, km, p_Number);
                    }else {
                        Toast.makeText(getContext(), "Kilometer exceed maximum number", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    kilometer.setError("Exceed maximum number");
                    Toast.makeText(getContext(), "Exceed maximum number", Toast.LENGTH_LONG).show();
                }
            }
            else {
                contactNumber.setError("Invalid format");
                contactNumber2.setText("Invalid format");
                Toast.makeText(getContext(), "Invalid phone number or unfilled phone number filled", Toast.LENGTH_LONG).show();
            }

        }
        else {
            Toast.makeText(getContext(), "Please fill in all the field!", Toast.LENGTH_LONG).show();
        }
    }

    public int addOnKilometer(int num){
        int kilo_M = num + 5000;
        return kilo_M;
    }

    private void checkInDatabase(String name, int phone, int phone2, int km, String _plateNumber) {
        boolean status = myDBHandler.checkForExistance(name, phone, phone2);
        if (status){
            contactName.setText("");
            contactNumber.setText("");
            kilometer.setText("");
            plateNumber.setText("");
            contactNumber2.setText("");
            Toast.makeText(getContext(), "The content you are adding already exists in the Database", Toast.LENGTH_SHORT).show();
        }
        else {
            addData(name, phone, phone2, km, _plateNumber);
        }
    }
    private void checkInDatabase(String name, int phone, int km, String _plateNumber) {
        boolean status = myDBHandler.checkForExistance(name, phone);
        if (status){
            contactName.setText("");
            contactNumber.setText("");
            kilometer.setText("");
            plateNumber.setText("");
            Toast.makeText(getContext(), "The content you are adding already exists in the Database", Toast.LENGTH_SHORT).show();
        }
        else {
            addData(name, phone, km, _plateNumber);
        }
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
                Toast.makeText(getContext(), "Successfully entered", Toast.LENGTH_LONG).show();
                contactName.setText("");
                contactNumber.setText("");
                kilometer.setText("");
                plateNumber.setText("");
                contactNumber2.setText("");
                helperfunction();
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void addData(String name, int phone, int phone2, int km, String _plateNumber){
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
            insertData = myDBHandler.addData(name, phone, phone2, desc, time, f_month, km, _plateNumber);
            if (insertData) {
                Toast.makeText(getContext(), "Successfully entered", Toast.LENGTH_LONG).show();
                contactName.setText("");
                contactNumber.setText("");
                kilometer.setText("");
                plateNumber.setText("");
                contactNumber2.setText("");
                helperfunction();
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void helperfunction(){
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmRunning == false){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
            Toast.makeText(getContext(), "Alarm scheduled ", Toast.LENGTH_SHORT).show();
        }
    }

    public void copyFile()
    {
        Log.i("CopyFile", "copyFile: before try");
        try
        {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite())
            {
                String currentDBPath = "/data/com.gamecodeschool.garagepractice/databases/GarageNameList.db";
                String backupDBPath = "GarageNameList";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                Log.i("CopyFile", "copyFile: in sd scan" + sd);

                if (currentDB.exists()) {
                    Log.i("CopyFile", "copyFile: current DB");
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getContext(), "Backup Complete", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Couldn't backup", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e) {
            Log.w("Settings Backup", e);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.contactButton:
                handleContact(view);
                break;
            case R.id.contactButtonAdd:
                handleContactAdd(view);
                break;
            case R.id.saveButton:
                handleAdd(view);
                break;
        }
    }
}
