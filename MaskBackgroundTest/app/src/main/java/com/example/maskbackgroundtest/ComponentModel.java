package com.example.maskbackgroundtest;

public class ComponentModel {

    private final int viewID;

    private final String className;

    private final boolean isClickable;

    private final boolean isFocusable;

    private final String addicionalInfo;

    private final CoordinateDTO coordinates;


    public ComponentModel(int viewId, String className, boolean isClickable, boolean isFocusable, String addicionalInfo, CoordinateDTO coordinates) {
        this.viewID = viewId;
        this.className = className;
        this.isClickable = isClickable;
        this.isFocusable = isFocusable;
        this.addicionalInfo = addicionalInfo;
        this.coordinates = coordinates;
    }
}
