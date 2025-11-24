package com.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO (Data Transfer Object) to hold the orientation information.
 * This object will be automatically converted to JSON by Spring.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrientationInfo {

    private int orientationId;
    private String orientationName;
}
