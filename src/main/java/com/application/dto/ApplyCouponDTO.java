package com.application.dto;

import lombok.Data;

@Data
public class ApplyCouponDTO {
    private String couponCode;
    private int studentAdmissionId;
    private int employeeId;
}