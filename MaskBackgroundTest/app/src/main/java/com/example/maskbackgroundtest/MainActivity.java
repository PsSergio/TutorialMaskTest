package com.example.maskbackgroundtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GettingCache";
//    private final MyReceiver receiver = new MyReceiver();
    private final BroadcastReceiver receiverIdentifidor = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        Gson gson = new Gson();
        String response = intent.getStringExtra("identifidor");

        ComponentIdentidorModel identifidor = gson.fromJson(response, ComponentIdentidorModel.class);

        mask.setPositions(identifidor.getBounds());
    }
};

    private MaskView mask;
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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.maskbackgroundtest.GET_IDENTIFOR_ELEMENT");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiverIdentifidor, intentFilter, RECEIVER_EXPORTED);
        }

        showOverlay();

        mask.setContext(this);

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
        mask = overlayView.findViewById(R.id.mask);
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
        unregisterReceiver(receiverIdentifidor);
        super.onDestroy();
    }
}