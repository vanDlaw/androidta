package com.example.ervan.ta2pelacak.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ervan.ta2pelacak.R;
import com.example.ervan.ta2pelacak.service.LocationRetrieval;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final String Mypref="mypref";
        SharedPreferences sharedPref = this.getSharedPreferences(Mypref,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        Button simpan=(Button) findViewById(R.id.simpan_setting);
        final Spinner device=(Spinner) findViewById(R.id.spinner_device);

        final EditText Inputpin=(EditText) findViewById(R.id.editPIN);

        Context context = getApplicationContext();
        CharSequence text = "Data Berhasil Disimpan";
        int duration = Toast.LENGTH_SHORT;

        final Toast toast = Toast.makeText(context, text, duration);


        TextView pin=(TextView) findViewById(R.id.setting_pin);
        String defaultValue = getResources().getString(R.string.pin);
        String PIN = sharedPref.getString(getString(R.string.pin), defaultValue);
        Log.i(defaultValue, PIN);
        pin.setText(PIN);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.devices, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        device.setAdapter(adapter);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String spinnervalue= device.getSelectedItem().toString();
                final String inputpinvalue= Inputpin.getText().toString();
                Log.i("spiner", spinnervalue+" "+inputpinvalue);

                AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://128.199.190.244/index.php/user/get/"+inputpinvalue, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.i("response", String.valueOf(response));
                        try {

                            Log.i("status", response.getString("status"));
                            if(response.getString("status").equals("200")){
                                toast.setText("data berhasil disimpan");
                                JSONObject data = response.getJSONObject("data");
                                editor.putString(spinnervalue+"_lat",data.getString("latitude"));
                                editor.putString(spinnervalue+"_long",data.getString("longitude"));
                                editor.putString(spinnervalue,inputpinvalue);
                                editor.apply();
                            }else {
                                toast.setText("data yang dimasukan salah");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        toast.show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.i(statusCode+"",responseString);
                    }
                });


            }
        });
    }


}
