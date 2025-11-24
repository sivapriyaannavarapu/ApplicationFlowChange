package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.ProgramName;

@Repository
public interface ProgramNameRepository extends JpaRepository<ProgramName, Integer>{
	
	Optional<ProgramName> findByProgramName(String programName);
//	List<ProgramName> findByStreamId(int streamId);
}
