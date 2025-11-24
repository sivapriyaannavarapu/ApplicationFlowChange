package com.application.service;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.application.dto.AppStatusDTO;
import com.application.entity.AppStatusTrackView;
import com.application.repository.AppStatusTrackViewRepository;
 
@Service
public class ApplicationStatusViewService {
 
    @Autowired
    private AppStatusTrackViewRepository appStatusTrackViewRepository;

//    @Cacheable(value = "appStatusByCampus", key = "#cmpsId")
    public List<AppStatusTrackView> getApplicationStatusByCampus(int cmpsId) {
        return appStatusTrackViewRepository.findByCmps_id(cmpsId);
    }
    
    public List<AppStatusTrackView> getApplicationStatusByEmployeeCampus(int empId) {
        try {
            List<AppStatusTrackView> result = appStatusTrackViewRepository.findByEmployeeCampus(empId);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Cacheable(value = "allstatustable")
    public List<AppStatusDTO> getAllStatus() {
        return appStatusTrackViewRepository.getAllStatusData();
    }
}