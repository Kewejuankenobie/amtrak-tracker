package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.gtfs.RouteAmtrak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<RouteAmtrak, String> {
}
