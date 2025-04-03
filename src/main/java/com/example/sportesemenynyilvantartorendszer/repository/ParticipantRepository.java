package com.example.sportesemenynyilvantartorendszer.repository;

import com.example.sportesemenynyilvantartorendszer.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByEmail(String email);

    List<Participant> findByLastName(String lastName);

    @Query("SELECT p FROM Participant p JOIN p.events e WHERE e.id = ?1")
    List<Participant> findByEventId(Long eventId);

    @Query("SELECT COUNT(p) FROM Participant p JOIN p.events e WHERE e.id = ?1")
    int countParticipantsByEventId(Long eventId);

    @Query("SELECT p FROM Participant p WHERE p.firstName LIKE %?1% OR p.lastName LIKE %?1% OR p.email LIKE %?1%")
    List<Participant> searchParticipants(String keyword);
}