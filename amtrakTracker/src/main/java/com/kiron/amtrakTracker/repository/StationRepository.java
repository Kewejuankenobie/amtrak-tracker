package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.gtfs.StationAmtrak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<StationAmtrak, String> {
}
