package com.example.maskbackgroundtest;

public class ComponentModel {

    private final int viewID;

    private final String className;

    private final String contentDescription;

    private final String text;

    private final boolean isClickable;

    private final boolean isFocusable;

    private final BoundsModel bounds;

    public BoundsModel getBounds() {
        return bounds;
    }

    public ComponentModel(int viewId, String className, String contentDescription, String text, boolean isClickable, boolean isFocusable, BoundsModel bounds) {
        this.viewID = viewId;
        this.className = className;
        this.contentDescription = contentDescription;
        this.text = text;
        this.isClickable = isClickable;
        this.isFocusable = isFocusable;
        this.bounds = bounds;
    }
}
