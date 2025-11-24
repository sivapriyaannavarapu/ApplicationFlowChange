package com.application.dto;
 
/**
* A DTO (Data Transfer Object) to hold the aggregated results from AppStatusTrack.
* This is used in a JPQL 'SELECT NEW' constructor expression.
*/
public record MetricsAggregateDTO(
    long totalApp,
    long appSold,
    long appConfirmed,
    long appAvailable,
    long appUnavailable,
    long appDamaged,
    long appIssued
) {
    /**
     * No-args constructor in case of proxying, though JPQL should use the canonical constructor.
     * A default "empty" DTO.
     */
    public MetricsAggregateDTO() {
        this(0, 0, 0, 0, 0, 0, 0);
    }
}