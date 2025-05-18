package com.example.maskbackgroundtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GettingCache";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);

        }
//        showOverlay();

        sharedPreferences = getSharedPreferences("LocalCache", MODE_PRIVATE);

        listener = (sharedPreferences, key) -> {
            String cache = sharedPreferences.getString(key, null);

            modifyMaskPosition(cache);
            Log.e(TAG, cache);

            showOverlay();

        };

    }

    public void modifyMaskPosition(String cache){


        if(cache != null){
            Gson gson = new Gson();
            ComponentModel model = gson.fromJson(cache, ComponentModel.class);

            MaskView mask = findViewById(R.id.mask);
            mask.setPositions(model.getRect());

            }
    }

    private void showOverlay(){

        var windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.FILL_PARENT,
                WindowManager.LayoutParams.FILL_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        var overlayView = inflater.inflate(R.layout.activity_main, null);

        windowManager.addView(overlayView, params);

    }

}