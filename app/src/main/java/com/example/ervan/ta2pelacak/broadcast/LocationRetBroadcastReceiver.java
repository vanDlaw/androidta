package com.example.ervan.ta2pelacak.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.UserDictionary;
import android.util.Log;

import com.example.ervan.ta2pelacak.service.LocationRetrieval;

/**
 * Created by Ervan on 07/01/2017.
 */

public class LocationRetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LocationRetrieval.class.getSimpleName(), "Service Stop!!!");
        final String appString= "com.example.ervan.ta2pelacak";

        // GET DATA FROM CONTENT PROVIDER
        String[] mProjection = {
                UserDictionary.Words.APP_ID,
                UserDictionary.Words._ID,
                UserDictionary.Words.WORD
        };
        Cursor mCursor = context.getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,
                mProjection,null,null,null
        );
        Log.i("QUERY", " " + mCursor.getCount());
        String pin= "";
        while(mCursor.moveToNext()){
            String item = mCursor.getString(mCursor.getColumnIndex(UserDictionary.Words.WORD));
            Log.i("cursor-item", item);
            pin  = item.substring(appString.length() - 7);
        }
        mCursor.close();

        Log.i("broadcastReceiver-PIN", "#" + pin + "#");
        Intent i = new Intent(context, LocationRetrieval.class);
        i.putExtra("pin",pin);
        context.startService(i);
    }
}