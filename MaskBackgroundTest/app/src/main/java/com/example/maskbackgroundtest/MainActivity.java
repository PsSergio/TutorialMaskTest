package com.example.maskbackgroundtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GettingCache";
//    private final MyReceiver receiver = new MyReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.example.maskbackgroundtest.GET_ALL_UI_COMPONENTS");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED);
//        }

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);

        }

        showOverlay();



//        button.setOnClickListener(e -> {
//            Intent intent = new Intent("com.example.maskbackgroundtest.GET_ELEMENTS");
//            sendBroadcast(intent);
//        });
    }

    private void showOverlay(){

        var windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        var overlayView = inflater.inflate(R.layout.overlay_layout, null);

        Button button = overlayView.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: Clicou!");
                Intent intent = new Intent("com.example.maskbackgroundtest.GET_ALL_UI_COMPONENTS");
                sendBroadcast(intent);
            }
        });

        windowManager.addView(overlayView, params);


    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(receiver);
        super.onDestroy();
    }
}