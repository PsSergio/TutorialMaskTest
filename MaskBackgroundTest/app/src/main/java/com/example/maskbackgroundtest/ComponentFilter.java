package com.example.maskbackgroundtest;

import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

public class ComponentFilter {

    public boolean filterComponents(AccessibilityNodeInfo node) {
        if (node == null || !node.isVisibleToUser()) return false;

        String className = node.getClassName() != null ? node.getClassName().toString() : "";
        boolean hasText = node.getText() != null && !node.getText().toString().trim().isEmpty();
        boolean hasContentDesc = node.getContentDescription() != null && !node.getContentDescription().toString().trim().isEmpty();

        if (node.isClickable()) return true;
        if (className.contains("TextView") && hasText) return true;
        if (className.contains("ImageView") && hasContentDesc) return true;

        return false;
    }

    public void extractNodeText(AccessibilityNodeInfo node, List<String> infoList) {
        if (node == null) return;

        if (node.getText() != null && !node.getText().toString().trim().isEmpty()) {
            infoList.add(node.getText().toString());
        }
        if (node.getContentDescription() != null && !node.getContentDescription().toString().trim().isEmpty()) {
            infoList.add(node.getContentDescription().toString());
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            extractNodeText(node.getChild(i), infoList);
        }
    }
}
