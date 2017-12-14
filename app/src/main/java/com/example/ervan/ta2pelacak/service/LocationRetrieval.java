package com.example.ervan.ta2pelacak.service;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.UserDictionary;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ervan on 06/12/2016.
 */

public class LocationRetrieval extends Service {
    String Latitude     = "";
    String Longitude    = "";
    String Pin          = "";
    String PinNew       = "";
    Context ctx;

    public LocationRetrieval(){ Log.i("LocRetServiceDefault","Started"); }

    public LocationRetrieval(Context AppsContext){
        this.ctx = AppsContext;
        Log.i("LocRetService","Started");
        String[] mProjection = {
                UserDictionary.Words.APP_ID,
                UserDictionary.Words._ID,
                UserDictionary.Words.WORD
        };
        Cursor mCursor = AppsContext.getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,
                mProjection,null,null,null
        );
        Log.i("QUERY FROM SERVICE", " " + mCursor.getCount());
        while(mCursor.moveToNext()) {
            String item = mCursor.getString(mCursor.getColumnIndex(UserDictionary.Words.WORD));
            Log.i("CI-Service", item);
            PinNew  = item.substring(item.length() - 6);
            Log.i("PinNew", PinNew);
        }
        mCursor.close();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("INTENT", String.valueOf(intent));
        if (intent == null){
            Log.i("PinNew", "#" + PinNew + "#");
//            stopMyService();
        } else {
            Pin = intent.getExtras().getString("pin");
        }
        Log.i("LRS-PIN-BR", "#" + Pin + "#");
        defineTimerTask();
        return Service.START_STICKY_COMPATIBILITY;
    }

    private void stopMyService(){
        stopForeground(true);
        stopSelf();
    }

    public void defineTimerTask() {
        Log.i("execMethod","getLoc");
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Latitude    = location.getLatitude() + "";
                Longitude   = location.getLongitude() + "";

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params    = new RequestParams();
                params.put("latitude", Latitude);
                params.put("longitude", Longitude);
                params.put("pin", Pin);

                Log.i("STATUS||LAT||LONG||PIN", "NEW POSITION || " + Latitude + " || " + Longitude + " || " + Pin);

                client.post("http://128.199.190.244/index.php/user/lokasi", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
//                        Log.i("Retval",String.valueOf(response));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.i("res", responseString);
                        Log.i("res", statusCode+"");
                    }
                });
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Condition", "PERMISSION DENIED");
            return;
        }

        // 900000   = 15 menit
        // 10000    = 10 detik
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("LocRetService", "onDestroy!!!");
        Intent broadIntent = new Intent("com.example.ervan.ta2.pelacak.RestartService");
        sendBroadcast(broadIntent);
        Log.i("Broadcast", "Sent");
    }
}
