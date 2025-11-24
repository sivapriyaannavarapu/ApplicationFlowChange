package com.application.repository;

import com.application.entity.ParentOccupationView;
import com.application.entity.ParentOccupationViewId;
// REMOVE: import com.application.dto.OccupationSectorDropdownDTO;
import org.springframework.data.jpa.repository.JpaRepository;
// REMOVE: import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParentOccupationViewRepository extends JpaRepository<ParentOccupationView, ParentOccupationViewId> {

    // --- REMOVE THE CUSTOM JPQL QUERIES ---
    // @Query("SELECT DISTINCT new ...")
    // List<OccupationSectorDropdownDTO> findDistinctOccupations();
    // @Query("SELECT DISTINCT new ...")
    // List<OccupationSectorDropdownDTO> findDistinctSectors();

    /**
     * --- ADD THIS METHOD ---
     * Finds all records from the view. We will filter duplicates in the service.
     * Use findByIsActive(1) if your view has an isActive field.
     */
    List<ParentOccupationView> findAll(); // Or List<ParentOccupationView> findByIsActive(int isActive);

}