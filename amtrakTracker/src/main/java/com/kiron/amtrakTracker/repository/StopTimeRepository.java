package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.gtfs.StopTimesAmtrak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopTimeRepository extends JpaRepository<StopTimesAmtrak, Long> {
}
