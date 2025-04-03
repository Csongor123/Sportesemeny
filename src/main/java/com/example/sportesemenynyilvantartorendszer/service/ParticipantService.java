package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Participant;

import java.util.List;
import java.util.Optional;

public interface ParticipantService {

    List<Participant> getAllParticipants();

    Optional<Participant> getParticipantById(Long id);

    Optional<Participant> getParticipantByEmail(String email);

    Participant createParticipant(Participant participant);

    Participant updateParticipant(Long id, Participant participantDetails);

    boolean deleteParticipant(Long id);

    List<Participant> getParticipantsByLastName(String lastName);

    List<Participant> getParticipantsByEventId(Long eventId);

    boolean registerParticipantForEvent(Long participantId, Long eventId);

    boolean cancelRegistration(Long participantId, Long eventId);

    int countParticipantsByEventId(Long eventId);

    List<Participant> searchParticipants(String keyword);
}
