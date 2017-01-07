package com.example.ervan.ta2pelacak.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.example.ervan.ta2pelacak.R;
import com.example.ervan.ta2pelacak.service.LocationRetrieval;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        final TextView pin = (TextView) findViewById(R.id.pin);

        final String Mypref="mypref";
        SharedPreferences sharedPref = this.getSharedPreferences(Mypref,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        final Intent startMain = new Intent(this,MainActivity.class);

        AsyncHttpClient client = new AsyncHttpClient();
        TelephonyManager  tMgr=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String no= tMgr.getDeviceId();
        Log.d("nomerhp","IMEI HP:"+no);

        RequestParams params = new RequestParams();
        params.put("no",no);

        Log.d("LALAA",""+ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Log.d("SS","Should Show");
            }else{
                Log.d("SS","Should not Show");
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }else{
            Log.d("CHECK", "PERMISSION GRANTED");
        }


        client.post("http://192.168.1.6/TA/public/user/daftar", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("response", String.valueOf(response));
                try {
                    editor.putString(getString(R.string.pin), response.getString("data"));
                    editor.commit();

                    Intent i = new Intent(getApplicationContext(), LocationRetrieval.class);
                    i.putExtra("pin", response.getString("data"));
                    getApplicationContext().startService(i);
                    Log.d("SERVICE", "STARTED");


                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d(statusCode+"",responseString);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if(grantResults.length >0){
                Log.d("STAT", "DAPAT GPS AKSES");
            }else{
             Log.d("STAT", "GAGAL");
            }

            return;
        }else{
            Log.d("REQ CODE", "!= 100");
        }
    }
}

