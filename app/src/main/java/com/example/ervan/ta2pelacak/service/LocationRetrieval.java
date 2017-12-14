package com.example.ervan.ta2pelacak.service;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
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
    String Latitude = "";
    Timer timer;
    TimerTask timerTask;
    String Longitude = "";
    String Pin ="";
    Context ctx;

    public LocationRetrieval(){
        Log.i("LocRetService","Started");
    }

    public LocationRetrieval(Context AppsContext){
        this.ctx = AppsContext;
        Log.i("LocRetService","Started");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        Pin = intent.getExtras().getString("pin");
        Log.i("LRS-PIN", Pin);
        defineTimerTask();
        return Service.START_STICKY;
    }

    public void defineTimerTask() {
        Log.i("execMethod","getLoc");
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Latitude = location.getLatitude() + "";
                Longitude = location.getLongitude() + "";

                Log.i("LAT", Latitude);
                Log.i("LONG", Longitude);

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("latitude",Latitude);
                params.put("longitude",Longitude);
                params.put("pin",Pin);

                Log.i("SendToServer", Latitude+" "+Longitude+" " + Pin);

                client.post("http://128.199.190.244/index.php/user/lokasi", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("Retval",String.valueOf(response));
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

        // 900000 = 15 menit
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 900000, 0, locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("Service", "onDestroy!!!");
        Intent broadIntent = new Intent("com.example.ervan.ta2.pelacak.RestartService");
        sendBroadcast(broadIntent);
    }
}
