package com.application.controller;
 
import com.application.dto.CombinedAnalyticsDTO;
import com.application.service.ApplicationAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
 
    @Autowired
    private ApplicationAnalyticsService analyticsService;
 
    /**
     * Endpoint for the "NORMAL" Zonal view.
     * Gets analytics for a single Zone ID.
     */
    @GetMapping("/zone/{id}")
    public ResponseEntity<CombinedAnalyticsDTO> getZoneAnalytics(@PathVariable Long id) {
        try {
            CombinedAnalyticsDTO data = analyticsService.getZoneAnalytics(id);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.err.println("Error in getZoneAnalytics: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
 
    /**
     * Endpoint for the "NORMAL" Campus view.
     * Gets analytics for a single Campus ID.
     */
    @GetMapping("/campus/{id}")
    public ResponseEntity<CombinedAnalyticsDTO> getCampusAnalytics(@PathVariable Long id) {
        try {
            CombinedAnalyticsDTO data = analyticsService.getCampusAnalytics(id);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            System.err.println("Error in getCampusAnalytics: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * "MASTER ROLLUP" ENDPOINT
     * This single endpoint handles both Zonal and DGM rollup views.
     * - If 'empId' is a Zonal Accountant, returns sum of their DGMs.
     * - If 'empId' is a DGM, returns sum of their Campuses.
     * - If 'empId' is a PRO, it returns a 400 error.
     *
     * @param empId The employee ID of the DGM or Zonal Accountant.
     * @return A CombinedAnalyticsDTO with the summed analytics.
     */
    @GetMapping("/{empId}")
    public ResponseEntity<CombinedAnalyticsDTO> getRollupAnalytics(
            @PathVariable("empId") Integer empId) {
        
        try {
            // Call the "master rollup" method in the service
            CombinedAnalyticsDTO analytics = analyticsService.getRollupAnalytics(empId);
 
            // Check if the service returned empty data (e.g., role is PRO)
            if (analytics.getGraphData() == null && analytics.getMetricsData() == null) {
                // This returns the JSON { "role": "PRO", "entityName": "This role does not have a rollup view", ... }
                return ResponseEntity.badRequest().body(analytics);
            }
            return ResponseEntity.ok(analytics);
            
        } catch (Exception e) {
            System.err.println("Error in getRollupAnalytics: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}