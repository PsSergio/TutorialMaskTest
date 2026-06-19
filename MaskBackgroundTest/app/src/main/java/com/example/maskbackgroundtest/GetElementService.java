package com.example.maskbackgroundtest;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GetElementService extends AccessibilityService {

    private static final String TAG = "GetElementService";
    private final GetElementAPIService service = new GetElementAPIService(this);
    private ComponentIdentidorModel identidorModel;
    
    // Background executor to avoid freezing the Main Thread
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScreenStateExtractor screenExtractor = new ScreenStateExtractor();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            
            if (rootNode == null) {
                Log.e(TAG, "Root node is null. Cannot extract screen.");
                return;
            }

            // Offload heavy processing to the background thread
            executor.execute(() -> {
                try {
                    List<ComponentModel> extractedComponents = screenExtractor.extractScreen(rootNode);
                    
                    Gson gson = new Gson();

                    for (ComponentModel model : extractedComponents) {
                        Log.e(TAG, "onReceive: " + gson.toJson(model));
                    }

//                    String json = gson.toJson(extractedComponents);
//                    Log.e(TAG, "onReceive: " + json );

//                    service.saveInFile(json);
                } catch (Exception e) {
                    Log.e(TAG, "Error processing screen state", e);
                }
            });
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {


    }

    @Override
    protected void onServiceConnected() {

        super.onServiceConnected();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.maskbackgroundtest.GET_ALL_UI_COMPONENTS");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED);
        }

//        getViewEvent();

        var info = new AccessibilityServiceInfo();
        info.flags |= AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        this.setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        
        if (executor != null) {
            executor.shutdown();
        }

        super.onDestroy();
    }
}
