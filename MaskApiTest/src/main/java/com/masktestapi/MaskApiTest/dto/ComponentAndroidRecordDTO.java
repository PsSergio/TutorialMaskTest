package com.masktestapi.MaskApiTest.dto;

import java.util.List;

public record ComponentAndroidRecordDTO(BoundsRecordDTO bounds, int viewID, String className, Boolean isClickable, Boolean isFocusable, String addicionalInfo) {
}
