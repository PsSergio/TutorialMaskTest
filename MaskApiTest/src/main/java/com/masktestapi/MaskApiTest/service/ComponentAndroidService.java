package com.masktestapi.MaskApiTest.service;

import com.masktestapi.MaskApiTest.dto.BoundsRecordDTO;
import com.masktestapi.MaskApiTest.dto.ComponentAndroidRecordDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ComponentAndroidService {

    public BoundsRecordDTO sortComponent(List<ComponentAndroidRecordDTO> components) {
        Random random = new Random();
        int randomIndex = random.nextInt(components.size());

        return components.get(randomIndex).bounds();
    }

}
