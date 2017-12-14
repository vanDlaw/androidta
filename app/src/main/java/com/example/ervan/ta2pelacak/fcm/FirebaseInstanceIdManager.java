package com.example.ervan.ta2pelacak.fcm;

import android.database.Cursor;
import android.provider.UserDictionary;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpClient;

/**
 * Created by Ervan on 14/01/2017.
 */

public class FirebaseInstanceIdManager extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String newToken = FirebaseInstanceId.getInstance().getToken();
        sendToServer(newToken);
    }

    private void sendToServer(String refreshedToken){
        String appString= "com.example.ervan.ta2pelacak";
        String[] mProjection = {
                UserDictionary.Words.APP_ID,
                UserDictionary.Words._ID,
                UserDictionary.Words.WORD
        };
        Cursor mCursor = this.getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,
                mProjection,null,null,null
        );
        String pin = "";
        while(mCursor.moveToNext()){
            String item = mCursor.getString(mCursor.getColumnIndex(UserDictionary.Words.WORD));
            Log.i("cursor-item", item);
            pin  = item.substring(appString.length() - 7);
        }
        mCursor.close();

        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("token", refreshedToken);
        params.put("pin", pin);
        Log.i("FCM-token", refreshedToken+" #" + pin + "#");

        client.post("http://128.199.190.244/index.php/user/token", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("sent token",statusCode+"");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("sent token",statusCode+"");
            }
        });
    }
}