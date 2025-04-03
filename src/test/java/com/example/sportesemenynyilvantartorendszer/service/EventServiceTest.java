package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event testEvent;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setName("Test Event");
        testEvent.setDescription("Test Description");
        testEvent.setEventDate(now.plusDays(30));
        testEvent.setLocation("Test Location");
        testEvent.setMaxParticipants(100);
        testEvent.setRegistrationDeadline(now.plusDays(15));
    }

    @Test
    void getAllEvents_ShouldReturnAllEvents() {
        // Arrange
        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");

        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("Event 2");

        List<Event> expectedEvents = Arrays.asList(event1, event2);

        when(eventRepository.findAll()).thenReturn(expectedEvents);

        // Act
        List<Event> actualEvents = eventService.getAllEvents();

        // Assert
        assertEquals(expectedEvents.size(), actualEvents.size());
        assertEquals(expectedEvents, actualEvents);
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void getEventById_WhenEventExists_ShouldReturnEvent() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));

        // Act
        Optional<Event> result = eventService.getEventById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testEvent, result.get());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getEventById_WhenEventDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Event> result = eventService.getEventById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(eventRepository, times(1)).findById(99L);
    }

    @Test
    void createEvent_ShouldSaveAndReturnEvent() {
        // Arrange
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // Act
        Event savedEvent = eventService.createEvent(testEvent);

        // Assert
        assertNotNull(savedEvent);
        assertEquals(testEvent, savedEvent);
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    void updateEvent_WhenEventExists_ShouldUpdateAndReturnEvent() {
        // Arrange
        Event eventToUpdate = new Event();
        eventToUpdate.setName("Updated Name");
        eventToUpdate.setDescription("Updated Description");
        eventToUpdate.setLocation("Updated Location");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Event updatedEvent = eventService.updateEvent(1L, eventToUpdate);

        // Assert
        assertNotNull(updatedEvent);
        assertEquals("Updated Name", updatedEvent.getName());
        assertEquals("Updated Description", updatedEvent.getDescription());
        assertEquals("Updated Location", updatedEvent.getLocation());
        verify(eventRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void updateEvent_WhenEventDoesNotExist_ShouldReturnNull() {
        // Arrange
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Event result = eventService.updateEvent(99L, new Event());

        // Assert
        assertNull(result);
        verify(eventRepository, times(1)).findById(99L);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void deleteEvent_WhenEventExists_ShouldReturnTrue() {
        // Arrange
        when(eventRepository.existsById(1L)).thenReturn(true);
        doNothing().when(eventRepository).deleteById(1L);

        // Act
        boolean result = eventService.deleteEvent(1L);

        // Assert
        assertTrue(result);
        verify(eventRepository, times(1)).existsById(1L);
        verify(eventRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEvent_WhenEventDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(eventRepository.existsById(99L)).thenReturn(false);

        // Act
        boolean result = eventService.deleteEvent(99L);

        // Assert
        assertFalse(result);
        verify(eventRepository, times(1)).existsById(99L);
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    void getUpcomingEvents_ShouldReturnFutureEvents() {
        // Arrange
        List<Event> upcomingEvents = Arrays.asList(testEvent);
        when(eventRepository.findByEventDateAfter(any(LocalDateTime.class))).thenReturn(upcomingEvents);

        // Act
        List<Event> result = eventService.getUpcomingEvents();

        // Assert
        assertEquals(upcomingEvents, result);
        verify(eventRepository, times(1)).findByEventDateAfter(any(LocalDateTime.class));
    }

    @Test
    void getEventsWithOpenRegistration_ShouldReturnEventsWithFutureDeadlines() {
        // Arrange
        List<Event> eventsWithOpenRegistration = Arrays.asList(testEvent);
        when(eventRepository.findByRegistrationDeadlineAfter(any(LocalDateTime.class)))
                .thenReturn(eventsWithOpenRegistration);

        // Act
        List<Event> result = eventService.getEventsWithOpenRegistration();

        // Assert
        assertEquals(eventsWithOpenRegistration, result);
        verify(eventRepository, times(1)).findByRegistrationDeadlineAfter(any(LocalDateTime.class));
    }
}
