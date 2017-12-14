package com.example.ervan.ta2pelacak.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.UserDictionary;
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
    Intent serviceIntent;
    private LocationRetrieval LocRet;
    Context ctx;

    public Context getCtx(){
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_startup);

        LocRet = new LocationRetrieval(getCtx());
        serviceIntent = new Intent(getCtx(), LocRet.getClass());

        final TextView pin = (TextView) findViewById(R.id.pin);
        final String Mypref="mypref";
        SharedPreferences sharedPref = this.getSharedPreferences(Mypref,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        final Intent startMain = new Intent(this,MainActivity.class);

        AsyncHttpClient client = new AsyncHttpClient();
        TelephonyManager  tMgr=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei= tMgr.getDeviceId();

        RequestParams params = new RequestParams();
        params.put("imei",imei);

        Log.i("LALAA",""+ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Log.i("SS","Should Show");
            }else{
                Log.i("SS","Should not Show");
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }else{
            Log.i("CHECK", "PERMISSION GRANTED");
        }

        Log.i("params",params.toString());
        client.post("http://128.199.190.244/index.php/user/daftar", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("response", String.valueOf(response));
                try {
                    editor.putString(getString(R.string.pin), response.getString("data"));
                    editor.commit();

                    // DELETE ALL
                    String selClause    = UserDictionary.Words.WORD + " LIKE ? ";
                    String[] selArgs    = {"%com.ervan.ta2pelacak%"};
                    int deletedRows     = 0;
                    deletedRows         = getContentResolver().delete(
                            UserDictionary.Words.CONTENT_URI,
                            selClause,
                            selArgs
                    );
                    Log.i("clause", selClause);
                    Log.i("deleted",""+deletedRows);

                    // SAVE TO CONTENT PROVIDER
                    ContentValues newvalue = new ContentValues();
                    newvalue.put(UserDictionary.Words.APP_ID,"com.ervan.ta2pelacak");
                    newvalue.put(UserDictionary.Words.WORD,"com.ervan.ta2pelacak-" + response.getString("data"));
                    Uri resulturi;
                    resulturi= getContentResolver().insert(
                            UserDictionary.Words.CONTENT_URI,newvalue
                    );
                    Log.i("resultURI",resulturi.toString());

                    if(!isMyServiceRunning(LocRet.getClass())){
                        Intent i = new Intent(getApplicationContext(), LocationRetrieval.class);
                        i.putExtra("pin",response.getString("data"));
                        getApplicationContext().startService(i);
                        Log.i("SERVICE", "WILL START");
                    }

                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(statusCode+"",responseString);
                finish();
            }
        });

    }

    public boolean isMyServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service:manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        Log.i("STARTUP-ACTIVITY", "onDestroy!!!");
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if(grantResults.length >0){
                Log.i("STAT", "DAPAT GPS AKSES");
            }else{
             Log.i("STAT", "GAGAL");
            }

            return;
        }else{
            Log.i("REQ CODE", "!= 100");
        }
    }
}

