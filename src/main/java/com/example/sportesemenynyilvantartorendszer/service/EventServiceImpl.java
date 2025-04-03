package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        Optional<Event> optionalEvent = eventRepository.findById(id);

        if (optionalEvent.isPresent()) {
            Event existingEvent = optionalEvent.get();

            if (eventDetails.getName() != null) {
                existingEvent.setName(eventDetails.getName());
            }

            if (eventDetails.getDescription() != null) {
                existingEvent.setDescription(eventDetails.getDescription());
            }

            if (eventDetails.getEventDate() != null) {
                existingEvent.setEventDate(eventDetails.getEventDate());
            }

            if (eventDetails.getLocation() != null) {
                existingEvent.setLocation(eventDetails.getLocation());
            }

            if (eventDetails.getMaxParticipants() > 0) {
                existingEvent.setMaxParticipants(eventDetails.getMaxParticipants());
            }

            if (eventDetails.getRegistrationDeadline() != null) {
                existingEvent.setRegistrationDeadline(eventDetails.getRegistrationDeadline());
            }

            return eventRepository.save(existingEvent);
        }

        return null;
    }

    @Override
    @Transactional
    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDateTime.now());
    }

    @Override
    public List<Event> getEventsWithOpenRegistration() {
        return eventRepository.findByRegistrationDeadlineAfter(LocalDateTime.now());
    }

    @Override
    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocation(location);
    }

    @Override
    public boolean isEventFull(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        return optionalEvent.map(event -> event.getParticipants().size() >= event.getMaxParticipants()).orElse(false);
    }

    @Override
    public int getAvailableSpotsForEvent(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        return optionalEvent.map(Event::getAvailableSpots).orElse(0);
    }

    @Override
    public List<Event> getEventsByDeadlineBetween(LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByRegistrationDeadlineBetween(start, end);
    }
}

