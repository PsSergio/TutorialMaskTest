package com.masktestapi.MaskApiTest.dto;

import java.util.List;

public record OutputResponse(ComponentIndenfidorRecordDTO identifidor, List<ComponentAndroidRecordDTO> components) {
}
