package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.gtfs.StopTimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StopTimeRepository extends JpaRepository<StopTimes, Long> {

    @Transactional
    @Query("select st from StopTimes st where st.stop_id = ?1")
    List<StopTimes> findAllByStop_Id(String id);
}
