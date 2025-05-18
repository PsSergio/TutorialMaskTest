package com.example.maskbackgroundtest;

import android.graphics.Rect;

public class ComponentModel {

    private final String className;

    private final String contentDescription;

    private final String text;

    private final boolean isClickable;

    private final boolean isFocusable;

    private final Rect rect;

    public Rect getRect() {
        return rect;
    }

    public ComponentModel(String className, String contentDescription, String text, boolean isClickable, boolean isFocusable, Rect rect) {
        this.className = className;
        this.contentDescription = contentDescription;
        this.text = text;
        this.isClickable = isClickable;
        this.isFocusable = isFocusable;
        this.rect = rect;
    }
}
