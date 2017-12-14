package com.example.ervan.ta2pelacak.activity;

import android.content.Intent;
import android.database.Cursor;
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ervan.ta2pelacak.R;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Log.i("Lc", "create");

        ImageButton btn = (ImageButton) findViewById(R.id.keluarbutton);
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
        Log.i("Lc", "pause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lc", "resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Lc","stop");
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
