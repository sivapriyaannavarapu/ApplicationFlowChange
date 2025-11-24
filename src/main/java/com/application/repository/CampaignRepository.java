package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.Campaign;
import com.application.entity.Campus;
import com.application.entity.Zone;

@Repository
public interface CampaignRepository  extends JpaRepository<Campaign, Integer>{
	 List<Campaign> findByCity_CityId(int cityId);
	 
	 Campaign findByCampus_CampusId(int campusId);
	 
}
