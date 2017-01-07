package com.example.ervan.ta2pelacak.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

        dev1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ctx, MapsActivity.class);
                intent.putExtra("device", "1");
                startActivity(intent);
            }
        });
        dev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ctx, MapsActivity.class);
                intent.putExtra("device", "2");
                startActivity(intent);
            }
        });
        dev3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ctx, MapsActivity.class);
                intent.putExtra("device", "3");
                startActivity(intent);
            }
        });
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

        String PINdevice1=sharedPref.getString("Device1","()");
        String PINdevice2=sharedPref.getString("Device2","()");
        String PINdevice3=sharedPref.getString("Device3","()");

        dev1.setText("Device1 ("+PINdevice1+")");
        dev2.setText("Device2 ("+PINdevice2+")");
        dev3.setText("Device3 ("+PINdevice3+")");
    }
}

