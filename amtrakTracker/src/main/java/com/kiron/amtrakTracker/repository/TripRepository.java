package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.gtfs.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, String> {
}
