package com.example.maskbackgroundtest;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScreenStateExtractor {

    private final ComponentFilter componentFilter = new ComponentFilter();

    public List<ComponentModel> extractScreen(AccessibilityNodeInfo rootNode) {
        List<ComponentModel> components = new ArrayList<>();
        List<AccessibilityNodeInfo> nodesToRecycle = new ArrayList<>();
        
        // Array acts as a mutable integer wrapper to increment recursively without global state
        int[] idCounter = {0};

        if (rootNode != null) {
            getComponent(rootNode, components, nodesToRecycle, idCounter);
        }

        // Clean up memory before returning the extracted data
        for (AccessibilityNodeInfo node : nodesToRecycle) {
            if (node != null) {
                try {
                    node.recycle();
                } catch (IllegalStateException e) {
                    // Node already recycled
                }
            }
        }

        return components;
    }

    private void getComponent(AccessibilityNodeInfo node, List<ComponentModel> components, List<AccessibilityNodeInfo> nodesToRecycle, int[] idCounter) {
        if (node == null) {
            return;
        }

        nodesToRecycle.add(node);

        if (componentFilter.filterComponents(node)) {
            addComponent(node, components, idCounter);
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            getComponent(node.getChild(i), components, nodesToRecycle, idCounter);
        }
    }

    private void addComponent(AccessibilityNodeInfo node, List<ComponentModel> components, int[] idCounter) {
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);

        CoordinateDTO coordinates = new CoordinateDTO(rect.centerX(), rect.centerY());

        String className = node.getClassName() != null ? node.getClassName().toString() : "";

        List<String> addinfo = new ArrayList<>();
        componentFilter.extractNodeText(node, addinfo);
        String addinfoInString = addinfo.stream()
                .collect(Collectors.joining(" - ", "", ""));

        boolean isClickable = node.isClickable();
        boolean isFocusable = node.isFocusable();
        
        idCounter[0]++;
        int idComponent = idCounter[0];

        ComponentModel componentModel = new ComponentModel(idComponent, className, isClickable, isFocusable, addinfoInString, coordinates);
        components.add(componentModel);
    }
}
