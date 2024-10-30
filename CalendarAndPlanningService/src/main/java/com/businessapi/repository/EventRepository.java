package com.businessapi.repository;

import com.businessapi.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<List<Event>> findAllByAuthId(Long authId);
    Optional<Event> findById(Long id);

}
