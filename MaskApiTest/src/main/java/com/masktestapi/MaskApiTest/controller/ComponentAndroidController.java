package com.masktestapi.MaskApiTest.controller;

import com.masktestapi.MaskApiTest.dto.BoundsRecordDTO;
import com.masktestapi.MaskApiTest.dto.ComponentAndroidRecordDTO;
import com.masktestapi.MaskApiTest.dto.ComponentIndenfidorRecordDTO;
import com.masktestapi.MaskApiTest.dto.OutputResponse;
import com.masktestapi.MaskApiTest.service.ComponentAndroidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("component/")
public class ComponentAndroidController {

    private final ComponentAndroidService service;

    public ComponentAndroidController(ComponentAndroidService service) {
        this.service = service;
    }

    @PostMapping("getBounds")
    public ResponseEntity<OutputResponse> getBounds(
            @RequestBody List<ComponentAndroidRecordDTO> components){
        OutputResponse response = service.sortComponent(components);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }   

}
