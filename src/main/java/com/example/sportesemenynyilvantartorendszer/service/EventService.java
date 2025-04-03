package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEvents();

    Optional<Event> getEventById(Long id);

    Event createEvent(Event event);

    Event updateEvent(Long id, Event eventDetails);

    boolean deleteEvent(Long id);

    List<Event> getUpcomingEvents();

    List<Event> getEventsWithOpenRegistration();

    List<Event> getEventsByLocation(String location);

    boolean isEventFull(Long eventId);

    int getAvailableSpotsForEvent(Long eventId);

    List<Event> getEventsByDeadlineBetween(LocalDateTime start, LocalDateTime end);
}

