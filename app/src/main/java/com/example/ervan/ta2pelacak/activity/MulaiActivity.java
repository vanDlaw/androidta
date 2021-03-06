package com.example.ervan.ta2pelacak.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ervan.ta2pelacak.R;

public class MulaiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulai);

        final Button dev1=(Button) findViewById(R.id.LacakDev1);
        final Button dev2=(Button) findViewById(R.id.LacakDev2);
        final Button dev3=(Button) findViewById(R.id.LacakDev3);
        final Context Ctx = this.getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Button dev1=(Button) findViewById(R.id.LacakDev1);
        final Button dev2=(Button) findViewById(R.id.LacakDev2);
        final Button dev3=(Button) findViewById(R.id.LacakDev3);

        final String Mypref="mypref";
        SharedPreferences sharedPref = this.getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        String nama1=sharedPref.getString("Device1" + "-NAMA","Device 1");
        String nama2=sharedPref.getString("Device2" + "-NAMA","Device 2");
        String nama3=sharedPref.getString("Device3" + "-NAMA","Device 3");
        String pin1=sharedPref.getString("Device1" + "-PIN","");
        String pin2=sharedPref.getString("Device2" + "-PIN","");
        String pin3=sharedPref.getString("Device3" + "-PIN","");

        dev1.setText(nama1+" ("+pin1+")");
        if(pin1=="()"){
            dev1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = getApplicationContext();
                    CharSequence text = "Data Kosong";
                    int duration = Toast.LENGTH_SHORT;

                    final Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        } else {
            dev1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("device", "1");
                    startActivity(intent);

                }
            });
        }
        dev2.setText(nama2+" ("+pin2+")");
        if(pin2=="()"){
            dev2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = getApplicationContext();
                    CharSequence text = "Data Kosong";
                    int duration = Toast.LENGTH_SHORT;

                    final Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        } else {
            dev2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("device", "2");
                    startActivity(intent);
                }
            });
        }
        dev3.setText(nama3+" ("+pin3+")");
        if(pin3=="()"){
            dev3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = getApplicationContext();
                    CharSequence text = "Data Kosong";
                    int duration = Toast.LENGTH_SHORT;

                    final Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        }else{
            dev3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    intent.putExtra("device", "3");
                    startActivity(intent);
                }
            });
        }
    }
}

