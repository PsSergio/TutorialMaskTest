package com.example.maskbackgroundtest;

import java.util.List;

public class ResponseModel {

    private final ComponentIdentidorModel identifidor;

    private final List<ComponentModel> components;

    public ComponentIdentidorModel getIdentifidor() {
        return identifidor;
    }

    public List<ComponentModel> getComponents() {
        return components;
    }

    public ResponseModel(ComponentIdentidorModel identifidor, List<ComponentModel> components) {
        this.identifidor = identifidor;
        this.components = components;
    }
}
