package com.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.dto.AppStatusDetailsDTO;
import com.application.dto.ApplicationDamagedDto;
import com.application.dto.CampusDto;
import com.application.dto.EmployeeDto;
import com.application.dto.GenericDropdownDTO;
import com.application.entity.AppStatus;
import com.application.entity.AppStatusTrackView;
import com.application.entity.ApplicationStatus;
import com.application.entity.Campus;
import com.application.entity.CampusProView;
import com.application.entity.Dgm;
import com.application.entity.Employee;
import com.application.entity.Status;
import com.application.entity.Zone;
import com.application.repository.AppStatusRepository;
import com.application.repository.AppStatusTrackViewRepository;
import com.application.repository.ApplicationStatusRepository;
import com.application.repository.CampusProViewRepository;
import com.application.repository.CampusRepository;
import com.application.repository.DgmRepository;
import com.application.repository.EmployeeRepository;
import com.application.repository.StatusRepository;
import com.application.repository.ZoneRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
public class ApplicationDamagedService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired private AppStatusRepository appStatusRepository;
    @Autowired private StatusRepository statusRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private CampusRepository campusRepository;
    @Autowired private CampusProViewRepository campusProViewRepository;
    @Autowired private AppStatusTrackViewRepository appStatusTrackViewRepository;
    @Autowired public ApplicationStatusRepository applicationStatusRepository;
    @Autowired public DgmRepository dgmRepository;
    @Autowired public ZoneRepository zoneRepository;

    // ---------------------- READ METHODS (CACHEABLE) ----------------------

    @Cacheable(value = "appStatusTrackView", key = "#num")
    public Optional<AppStatusTrackView> getDetailsByApplicationNo(int num) {
        return appStatusTrackViewRepository.findById(num);
    }

    @Cacheable(value = "zoneEmployees")
    public List<Employee> getAllZoneEmployees() {
        return employeeRepository.findAll();
    }

    @Cacheable(value = "allCampuses")
    public List<GenericDropdownDTO> getActiveCampusesDropdown() {
        return campusRepository.findAllActiveCampusesForDropdown();
    }

    @Cacheable(value = "dgmCampuses")
    public List<GenericDropdownDTO> getDgmCampusesDropdown() {
        return dgmRepository.findDistinctActiveCampusesByDgm();
    }

    @Cacheable(value = "allStatuses")
    public List<ApplicationStatus> getAllStatus() {
        return applicationStatusRepository.findAll();
    }

    @Cacheable(value = "zonesDropdown")
    public List<GenericDropdownDTO> getAllZones() {
        return zoneRepository.findAllActiveZonesForDropdown();
    }

    @Cacheable(value = "campusesByDgm", key = "#dgmId")
    public List<Campus> getCampusesByDgmId(int dgmId) {
        List<Dgm> dgmEntries = dgmRepository.findByDgmId(dgmId);
        if (dgmEntries.isEmpty()) return List.of();
        return dgmEntries.stream().map(Dgm::getCampus).collect(Collectors.toList());
    }

    @Cacheable(value = "appStatusDetails", key = "#appNo")
    public Optional<AppStatusDetailsDTO> getAppStatusDetails(int appNo) {
        Optional<AppStatus> appStatusOptional = appStatusRepository.findByApplicationNumber(appNo);
        if (appStatusOptional.isEmpty()) return Optional.empty();

        AppStatus appStatus = appStatusOptional.get();
        AppStatusDetailsDTO dto = new AppStatusDetailsDTO();

        if (appStatus.getEmployee() != null) {
            dto.setProId(appStatus.getEmployee().getEmp_id());
            dto.setProName(appStatus.getEmployee().getFirst_name() + " " + appStatus.getEmployee().getLast_name());
        }
        if (appStatus.getZone() != null) {
            dto.setZoneId(appStatus.getZone().getZoneId());
            dto.setZoneName(appStatus.getZone().getZoneName());
        }
        if (appStatus.getEmployee2() != null) {
            dto.setDgmEmpId(appStatus.getEmployee2().getEmp_id());
            dto.setDgmEmpName(appStatus.getEmployee2().getFirst_name() + " " + appStatus.getEmployee2().getLast_name());
        }
        if (appStatus.getCampus() != null) {
            dto.setCampusId(appStatus.getCampus().getCampusId());
            dto.setCampusName(appStatus.getCampus().getCampusName());
        }
        if (appStatus.getStatus() != null) {
            dto.setStatus(appStatus.getStatus().getStatus_type());
        }

        dto.setReason(appStatus.getReason());

        return Optional.of(dto);
    }

    @Cacheable(value = "campusesByZone", key = "#zoneId")
    public List<CampusDto> getCampusDtosByZoneId(int zoneId) {
        Optional<Zone> zoneOptional = zoneRepository.findById(zoneId);
        return zoneOptional.map(zone -> campusRepository.findByZoneAsDto(zone))
                           .orElse(List.of());
    }

    // ---------------------- WRITE METHODS ----------------------

    @Transactional
    public AppStatus saveOrUpdateApplicationStatus(ApplicationDamagedDto dto) {
        if (dto == null) throw new IllegalArgumentException("DTO cannot be null");

        Optional<AppStatus> existingAppStatusOpt =
                dto.getApplicationNo() != null ? appStatusRepository.findByAppNo(dto.getApplicationNo())
                                               : Optional.empty();

        AppStatus appStatus = existingAppStatusOpt.orElse(new AppStatus());

        Status status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("Status not found"));
        Campus campus = campusRepository.findById(dto.getCampusId())
                .orElseThrow(() -> new EntityNotFoundException("Campus not found"));
        Employee proEmployee = employeeRepository.findById(dto.getProId())
                .orElseThrow(() -> new EntityNotFoundException("PRO Employee not found"));
        Zone zone = zoneRepository.findById(dto.getZoneId())
                .orElseThrow(() -> new EntityNotFoundException("Zone not found"));
        Employee dgmEmployee = employeeRepository.findById(dto.getDgmEmpId())
                .orElseThrow(() -> new EntityNotFoundException("DGM Employee not found"));

        appStatus.setApp_no(dto.getApplicationNo());
        appStatus.setReason(dto.getReason());
        appStatus.setStatus(status);
        appStatus.setCampus(campus);
        appStatus.setEmployee(proEmployee);
        appStatus.setZone(zone);
        appStatus.setEmployee2(dgmEmployee);
        appStatus.setIs_active(dto.getStatusId() == 1 ? 1 : 0);
        appStatus.setCreated_by(2);

        return appStatusRepository.save(appStatus);
    }

    // ---------------------- SUPPORT METHODS ----------------------

    @Cacheable(value = "dgmNamesByZone", key = "#zoneId")
    public List<EmployeeDto> getDgmNamesByZoneId(int zoneId) {
        List<Dgm> dgmList = dgmRepository.findByZoneId(zoneId);
        List<EmployeeDto> dgmEmployees = new ArrayList<>();

        for (Dgm dgm : dgmList) {
            if (dgm.getEmployee() != null) {
                EmployeeDto dto = new EmployeeDto();
                dto.setEmpId(dgm.getEmployee().getEmp_id());
                dto.setName(dgm.getEmployee().getFirst_name() + " " + dgm.getEmployee().getLast_name());
                dgmEmployees.add(dto);
            }
        }
        return dgmEmployees;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "campusEmployees", key = "#campusId")
    public List<EmployeeDto> getEmployeeNamesByCampusId(int campusId) {
        entityManager.clear();
        List<CampusProView> views = campusProViewRepository.findByCampusId(campusId);

        return views.stream()
                .map(view -> {
                    int empId = view.getCmps_emp_id();
                    Employee employee = employeeRepository.findById(empId).orElse(null);
                    if (employee != null) {
                        EmployeeDto dto = new EmployeeDto();
                        dto.setEmpId(empId);
                        dto.setName(employee.getFirst_name() + " " + employee.getLast_name());
                        return dto;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public AppStatusTrackView getAppStatusByCampusAndNumber(int appNo, String campusName) {
        return appStatusTrackViewRepository.findByNumAndCmps_name(appNo, campusName)
                .orElseThrow(() -> new NoSuchElementException(
                        "No status record found for App No: " + appNo + " and Campus: " + campusName));
    }
}
