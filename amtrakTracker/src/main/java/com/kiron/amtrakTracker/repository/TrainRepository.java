package com.kiron.amtrakTracker.repository;

import com.kiron.amtrakTracker.model.TrainParsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<TrainParsed, String> {
    @Transactional
    List<TrainParsed> findByNameContainsIgnoreCase(String name);
    @Transactional
    List<TrainParsed> findByNumber(Integer number);

    @Transactional
    @Modifying
    @Query("delete TrainParsed t where t.is_active = ?1")
    void deleteByIs_active(boolean b);
}
