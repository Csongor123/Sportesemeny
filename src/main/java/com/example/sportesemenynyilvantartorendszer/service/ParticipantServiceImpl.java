package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.repository.EventRepository;
import com.example.sportesemenynyilvantartorendszer.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    @Autowired
    public ParticipantServiceImpl(ParticipantRepository participantRepository, EventRepository eventRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public Optional<Participant> getParticipantById(Long id) {
        return participantRepository.findById(id);
    }

    @Override
    public Optional<Participant> getParticipantByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public Participant createParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    @Transactional
    public Participant updateParticipant(Long id, Participant participantDetails) {
        Optional<Participant> optionalParticipant = participantRepository.findById(id);

        if (optionalParticipant.isPresent()) {
            Participant existingParticipant = optionalParticipant.get();

            if (participantDetails.getFirstName() != null) {
                existingParticipant.setFirstName(participantDetails.getFirstName());
            }

            if (participantDetails.getLastName() != null) {
                existingParticipant.setLastName(participantDetails.getLastName());
            }

            if (participantDetails.getEmail() != null) {
                existingParticipant.setEmail(participantDetails.getEmail());
            }

            if (participantDetails.getPhone() != null) {
                existingParticipant.setPhone(participantDetails.getPhone());
            }

            if (participantDetails.getDateOfBirth() != null) {
                existingParticipant.setDateOfBirth(participantDetails.getDateOfBirth());
            }

            return participantRepository.save(existingParticipant);
        }

        return null;
    }

    @Override
    @Transactional
    public boolean deleteParticipant(Long id) {
        if (participantRepository.existsById(id)) {
            participantRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Participant> getParticipantsByLastName(String lastName) {
        return participantRepository.findByLastName(lastName);
    }

    @Override
    public List<Participant> getParticipantsByEventId(Long eventId) {
        return participantRepository.findByEventId(eventId);
    }

    @Override
    @Transactional
    public boolean registerParticipantForEvent(Long participantId, Long eventId) {
        Optional<Participant> optionalParticipant = participantRepository.findById(participantId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalParticipant.isPresent() && optionalEvent.isPresent()) {
            Participant participant = optionalParticipant.get();
            Event event = optionalEvent.get();

            if (event.isRegistrationOpen() && event.getAvailableSpots() > 0) {
                return participant.registerForEvent(event) &&
                        (participantRepository.save(participant) != null);
            }
        }

        return false;
    }

    @Override
    @Transactional
    public boolean cancelRegistration(Long participantId, Long eventId) {
        Optional<Participant> optionalParticipant = participantRepository.findById(participantId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalParticipant.isPresent() && optionalEvent.isPresent()) {
            Participant participant = optionalParticipant.get();
            Event event = optionalEvent.get();

            boolean canceled = participant.cancelRegistration(event);
            if (canceled) {
                participantRepository.save(participant);
                return true;
            }
        }

        return false;
    }

    @Override
    public int countParticipantsByEventId(Long eventId) {
        return participantRepository.countParticipantsByEventId(eventId);
    }

    @Override
    public List<Participant> searchParticipants(String keyword) {
        return participantRepository.searchParticipants(keyword);
    }
}