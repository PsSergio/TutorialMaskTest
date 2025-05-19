package com.masktestapi.MaskApiTest.dto;

public record ComponentAndroidRecordDTO(BoundsRecordDTO bounds, String className, String contentDescription, String text, Boolean isClickable, Boolean isFocusable) {
}
