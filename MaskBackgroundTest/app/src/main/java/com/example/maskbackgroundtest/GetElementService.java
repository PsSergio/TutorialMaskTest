package com.example.maskbackgroundtest;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GetElementService extends AccessibilityService {

    private final List<ComponentModel> components = new ArrayList<>();
    private static final String TAG = "GetElementService";
    private final GetElementAPIService service = new GetElementAPIService(this);
    private ComponentIdentidorModel identidorModel;
    private int idComponent = 0;
    private List<AccessibilityNodeInfo> nodesSent = new ArrayList<>();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getViewEvent();
            Gson gson = new Gson();
            String json = gson.toJson(components);

            service.saveInFile(json);

        }
    };


    public boolean filterComponents(AccessibilityNodeInfo node){
        if(!node.isVisibleToUser()) return false;
        if(!node.isClickable()) return false;

        if(node.getClassName().toString().equals("android.widget.ImageView") && node.getContentDescription() == null)
            return false;

        if(node.getClassName().toString().equals("android.widget.TextView") && node.getText() == null)
            return false;

        return true;
    }


    public List<String> setAddicionalInfo(AccessibilityNodeInfo node){

        var addicionalInfo = new ArrayList<String>();

        if(node == null) return null;
        if(node.getText() != null){
            addicionalInfo.add(node.getText().toString());
        }
        if(node.getContentDescription() != null){
            addicionalInfo.add(node.getContentDescription().toString());
        }

        for(int i = 0; i < node.getChildCount(); i++){
            addicionalInfo.addAll(setAddicionalInfo(node.getChild(i)));
        }

        return addicionalInfo;

    }

    public void addComponent(AccessibilityNodeInfo rootNodes){
        Rect rect = new Rect();
        rootNodes.getBoundsInScreen(rect);

        var className = rootNodes.getClassName().toString();

        var addinfo = setAddicionalInfo(rootNodes);
        String addinfoInString = addinfo.stream()
                .collect(Collectors.joining(" - ", "", ""));

        var isClickable = rootNodes.isClickable();
        var isFocusable = rootNodes.isFocusable();
        isFocusable = false;
        this.idComponent = this.idComponent+1;

//        Log.e(TAG, "addComponent: " + id);
        var componentModel = new ComponentModel(this.idComponent, className, isClickable, isFocusable, addinfoInString);

        components.add(componentModel);
        nodesSent.add(rootNodes);
        printComponents(componentModel);

    }

    public void printComponents(ComponentModel model){
        Gson json = new Gson();

        Log.e(TAG, json.toJson(model));

    }

    public void getComponent(AccessibilityNodeInfo node){

        if(node == null) {
            Log.e(TAG, "NULO!");
            return;
        }
//
//        CharSequence pkg = node.getPackageName();
//        if (pkg != null && pkg.toString().equals(APOIODIGITAL_PACKAGE)) {
//            return;
//        }

        if(filterComponents(node)) {
//            Log.e(TAG, "getComponent: ");

            addComponent(node);
        }

        for(int i = 0; i < node.getChildCount(); i++){


            getComponent(node.getChild(i));

        }
    }



    public void getViewEvent(){
        var gson = new Gson();
        components.clear();
        nodesSent.clear();
        idComponent = 0;
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
//        rootNode.findAccessibilityNodeInfosByViewId()

        getComponent(rootNode);

    }

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


        super.onDestroy();


    }
}
