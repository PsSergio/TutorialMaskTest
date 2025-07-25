package com.masktestapi.MaskApiTest.service;

import com.masktestapi.MaskApiTest.dto.BoundsRecordDTO;
import com.masktestapi.MaskApiTest.dto.ComponentAndroidRecordDTO;
import com.masktestapi.MaskApiTest.dto.ComponentIndenfidorRecordDTO;
import com.masktestapi.MaskApiTest.dto.OutputResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ComponentAndroidService {

    public OutputResponse sortComponent(List<ComponentAndroidRecordDTO> components) {
        Random random = new Random();
        int randomIndex = random.nextInt(components.size());
        var component = components.get(randomIndex);
        var identifidor = new ComponentIndenfidorRecordDTO(component.viewID(), component.bounds());
        return new OutputResponse(identifidor, components);
    }

}
