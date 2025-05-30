package com.masktestapi.MaskApiTest.dto;

public record ComponentAndroidRecordDTO(BoundsRecordDTO bounds, int viewID, String className, String contentDescription, String text, Boolean isClickable, Boolean isFocusable) {
}
