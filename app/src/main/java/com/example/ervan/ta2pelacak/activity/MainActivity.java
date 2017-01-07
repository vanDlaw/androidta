package com.example.ervan.ta2pelacak.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ervan.ta2pelacak.R;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Lc", "create");


        Button btn = (Button) findViewById(R.id.keluarbutton);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO:
                // This function closes Activity Two
                // Hint: use Context's finish() method
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lc", "pause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lc", "resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Lc","stop");
    }

    /** Called when the user clicks the Send button */
    public void GotoMulai(View view) {
        Intent intent = new Intent(this, MulaiActivity.class);
        startActivity(intent);
    }
    public void GotoSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    public void Keluar(View view) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        moveTaskToBack(true);
    }
}
