package com.example.maskbackgroundtest;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class GetElementService extends AccessibilityService {

    private final List<ComponentModel> components = new ArrayList<>();
    private static final String TAG = "GetElementService";

    public boolean isNodeComponentLayout(AccessibilityNodeInfo node){
        var layoutsComponents = new String[]{
                "android.view.View",
                "android.widget.RelativeLayout",
                "android.widget.LinearLayout",
                "android.widget.ScrollView",
                "android.widget.HorizontalScrollView",
                "android.widget.GridView",
                "android.view.ViewGroup",
                "android.widget.ListView",
                "android.widget.FrameLayout"
        };
        for (String layoutsComponent : layoutsComponents) {
            if (Objects.equals(node.getClassName(), layoutsComponent)) return true;
        }
        return false;
    }

    public boolean isComponentValidLayout(AccessibilityNodeInfo node){

        if(!isNodeComponentLayout(node)) return true;

        if((node.getContentDescription() != null || node.getText() != null) && (node.isClickable() || node.isFocusable())) return true;

        return false;
    }

    public boolean filterComponents(AccessibilityNodeInfo node){
        if(!isComponentValidLayout(node)) return false;

        if(!node.isVisibleToUser()) return false;

        if(node.getText() == null && node.getContentDescription() == null && !node.isFocusable() && !node.isClickable()) return false;

        if(node.getClassName().toString().equals("android.widget.ImageView") && node.getContentDescription() == null)
            return false;

        if(node.getClassName().toString().equals("android.widget.TextView") && node.getText() == null)
            return false;

        return true;
    }

    public void addComponent(AccessibilityNodeInfo rootNodes){
        Rect rect = new Rect();
        rootNodes.getBoundsInScreen(rect);

        var className = rootNodes.getClassName().toString();

        var contentDesc = "null";
        var text = "null";
        try{
            contentDesc = rootNodes.getContentDescription().toString();
        }catch (Exception e){
            contentDesc = "null";
        }
        try{
            text = rootNodes.getText().toString();
        }catch (Exception e){
            text = "null";
        }

        var isClickable = rootNodes.isClickable();
        var isFocusable = rootNodes.isFocusable();
        var componentModel = new ComponentModel(className, contentDesc, text, isClickable, isFocusable, rect);

        components.add(componentModel);

    }


    public void getComponent(AccessibilityNodeInfo node){
        if(node == null) return;

        if(filterComponents(node)) {
            addComponent(node);
        }

        for(int i = 0; i < node.getChildCount(); i++){

            getComponent(node.getChild(i));

        }
    }

    public void toLocalCache(ComponentModel model){
        var json = new Gson();
        String text = json.toJson(model);

        SharedPreferences sharedPreferences = getSharedPreferences("LocalCache", MODE_PRIVATE);
        sharedPreferences.edit().putString("component", text).apply();
        Log.e(TAG, text);


    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

            getComponent(rootNode);
            Random random = new Random();
            if(!components.isEmpty()){
                int randomIndex = random.nextInt(components.size());
                ComponentModel component = components.get(randomIndex);
                toLocalCache(component);
            }

            Log.e(TAG, "onAccessibilityEvent: Getting element");

        }


    }

    protected void onServiceConnected() {
        super.onServiceConnected();
        var info = new AccessibilityServiceInfo();

        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {

    }
}
