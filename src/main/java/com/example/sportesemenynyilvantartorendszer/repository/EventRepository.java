package com.example.sportesemenynyilvantartorendszer.repository;


import com.example.sportesemenynyilvantartorendszer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventDateAfter(LocalDateTime date);

    List<Event> findByRegistrationDeadlineAfter(LocalDateTime date);

    List<Event> findByLocation(String location);

    @Query("SELECT e FROM Event e WHERE SIZE(e.participants) < e.maxParticipants AND e.registrationDeadline > CURRENT_TIMESTAMP")
    List<Event> findAvailableEvents();

    @Query("SELECT e FROM Event e WHERE e.registrationDeadline BETWEEN ?1 AND ?2")
    List<Event> findByRegistrationDeadlineBetween(LocalDateTime start, LocalDateTime end);
}
