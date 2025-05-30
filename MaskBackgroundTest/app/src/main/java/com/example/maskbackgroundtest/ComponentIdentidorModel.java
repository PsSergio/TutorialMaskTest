package com.example.maskbackgroundtest;

public class ComponentIdentidorModel {

    private final int viewID;

    private final BoundsModel boundsRecordDTO;

    public ComponentIdentidorModel(int viewID, BoundsModel bounds) {
        this.viewID = viewID;
        this.boundsRecordDTO = bounds;
    }

    public int getViewID() {
        return viewID;
    }

    public BoundsModel getBounds() {
        return boundsRecordDTO;
    }
}
