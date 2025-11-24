package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.CmpsOrientation;
import com.application.entity.Orientation;
import com.application.entity.OrientationBatch;

@Repository
public interface CmpsOrientationRepository extends JpaRepository<CmpsOrientation, Integer>{
	
	Optional<CmpsOrientation> findByOrientation_OrientationId(int orientationId);

    // Corrected method to find by the related Orientation entity itself.
    Optional<CmpsOrientation> findByOrientation(Orientation orientation);

    // Corrected custom query to find by Orientation, Campus, and Academic Year IDs.
    @Query("SELECT c FROM CmpsOrientation c WHERE c.orientation.orientationId = :orientationId AND c.cmpsId = :cmpsId AND c.acdcYearId = :acdcYearId")
    Optional<CmpsOrientation> findByOrientationAndCmpsAndYear(
        @Param("orientationId") Integer orientationId,
        @Param("cmpsId") Integer cmpsId,
        @Param("acdcYearId") Integer acdcYearId);

    // Corrected custom query to find the orientation fee by the orientation ID.
    @Query("SELECT c.orientation_fee FROM CmpsOrientation c WHERE c.orientation.orientationId = :orientationId")
    Float findOrientationFeeByOrientationId(@Param("orientationId") int orientationId);

    // Corrected method to find by Campus ID, Orientation ID, and Orientation Batch ID.
    Optional<CmpsOrientation> findByCmpsIdAndOrientation_OrientationIdAndOrientationBatch_OrientationBatchId(
        int cmpsId, int orientationId, int orientationBatchId);

    // Corrected custom query to find all OrientationBatch entities related to a specific Orientation ID.
    @Query("SELECT c.orientationBatch FROM CmpsOrientation c WHERE c.orientation.orientationId = :orientationId")
    List<OrientationBatch> findOrientationBatchesByOrientationId(int orientationId);
    
    List<CmpsOrientation> findByCmpsIdAndOrientationOrientationId(int cmpsId, int orientationId);

//    /**
//     * Finds all associated OrientationBatch entities for a given orientation.
//     * This is a useful method for populating dependent dropdowns.
//     */
//    @Query("SELECT c.orientationBatch FROM CmpsOrientation c WHERE c.orientation.orientationId = :orientationId")
//    List<OrientationBatch> findOrientationBatchesByOrientationId(@Param("orientationId") int orientationId);
    

	 
	 
}
