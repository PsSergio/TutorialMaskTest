package com.masktestapi.MaskApiTest.controller;

import com.masktestapi.MaskApiTest.dto.BoundsRecordDTO;
import com.masktestapi.MaskApiTest.dto.ComponentAndroidRecordDTO;
import com.masktestapi.MaskApiTest.service.ComponentAndroidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("component/")
public class ComponentAndroidController {

    private final ComponentAndroidService service;

    public ComponentAndroidController(ComponentAndroidService service) {
        this.service = service;
    }

    @GetMapping("getBounds")
    public ResponseEntity<BoundsRecordDTO> getBounds(
            @RequestBody List<ComponentAndroidRecordDTO> components){
        BoundsRecordDTO bounds = service.sortComponent(components);
        return ResponseEntity.status(HttpStatus.OK).body(bounds);
    }

}
