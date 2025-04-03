package com.example.sportesemenynyilvantartorendszer.service;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.repository.EventRepository;
import com.example.sportesemenynyilvantartorendszer.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ParticipantServiceImpl participantService;

    private Participant testParticipant;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        testParticipant = new Participant();
        testParticipant.setId(1L);
        testParticipant.setFirstName("John");
        testParticipant.setLastName("Doe");
        testParticipant.setEmail("john.doe@example.com");
        testParticipant.setPhone("1234567890");
        testParticipant.setDateOfBirth(LocalDate.of(1990, 1, 1));

        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setName("Test Event");
        testEvent.setEventDate(now.plusDays(30));
        testEvent.setLocation("Test Location");
        testEvent.setMaxParticipants(100);
        testEvent.setRegistrationDeadline(now.plusDays(15));
    }

    @Test
    void getAllParticipants_ShouldReturnAllParticipants() {
        // Arrange
        Participant participant1 = new Participant();
        participant1.setId(1L);
        participant1.setFirstName("John");

        Participant participant2 = new Participant();
        participant2.setId(2L);
        participant2.setFirstName("Jane");

        List<Participant> expectedParticipants = Arrays.asList(participant1, participant2);

        when(participantRepository.findAll()).thenReturn(expectedParticipants);

        // Act
        List<Participant> actualParticipants = participantService.getAllParticipants();

        // Assert
        assertEquals(expectedParticipants.size(), actualParticipants.size());
        assertEquals(expectedParticipants, actualParticipants);
        verify(participantRepository, times(1)).findAll();
    }

    @Test
    void getParticipantById_WhenParticipantExists_ShouldReturnParticipant() {
        // Arrange
        when(participantRepository.findById(1L)).thenReturn(Optional.of(testParticipant));

        // Act
        Optional<Participant> result = participantService.getParticipantById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testParticipant, result.get());
        verify(participantRepository, times(1)).findById(1L);
    }

    @Test
    void getParticipantById_WhenParticipantDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(participantRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Participant> result = participantService.getParticipantById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(participantRepository, times(1)).findById(99L);
    }

    @Test
    void getParticipantByEmail_WhenParticipantExists_ShouldReturnParticipant() {
        // Arrange
        String email = "john.doe@example.com";
        when(participantRepository.findByEmail(email)).thenReturn(Optional.of(testParticipant));

        // Act
        Optional<Participant> result = participantService.getParticipantByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testParticipant, result.get());
        verify(participantRepository, times(1)).findByEmail(email);
    }

    @Test
    void createParticipant_ShouldSaveAndReturnParticipant() {
        // Arrange
        when(participantRepository.save(any(Participant.class))).thenReturn(testParticipant);

        // Act
        Participant savedParticipant = participantService.createParticipant(testParticipant);

        // Assert
        assertNotNull(savedParticipant);
        assertEquals(testParticipant, savedParticipant);
        verify(participantRepository, times(1)).save(testParticipant);
    }

    @Test
    void updateParticipant_WhenParticipantExists_ShouldUpdateAndReturnParticipant() {
        // Arrange
        Participant participantToUpdate = new Participant();
        participantToUpdate.setFirstName("Updated First Name");
        participantToUpdate.setLastName("Updated Last Name");
        participantToUpdate.setEmail("updated.email@example.com");

        when(participantRepository.findById(1L)).thenReturn(Optional.of(testParticipant));
        when(participantRepository.save(any(Participant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Participant updatedParticipant = participantService.updateParticipant(1L, participantToUpdate);

        // Assert
        assertNotNull(updatedParticipant);
        assertEquals("Updated First Name", updatedParticipant.getFirstName());
        assertEquals("Updated Last Name", updatedParticipant.getLastName());
        assertEquals("updated.email@example.com", updatedParticipant.getEmail());
        verify(participantRepository, times(1)).findById(1L);
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    void registerParticipantForEvent_WhenValidRequest_ShouldReturnTrue() {
        // Arrange
        when(participantRepository.findById(1L)).thenReturn(Optional.of(testParticipant));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(participantRepository.save(any(Participant.class))).thenReturn(testParticipant);

        // Act
        boolean result = participantService.registerParticipantForEvent(1L, 1L);

        // Assert
        assertTrue(result);
        verify(participantRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).findById(1L);
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    void registerParticipantForEvent_WhenParticipantNotFound_ShouldReturnFalse() {
        // Arrange
        when(participantRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        boolean result = participantService.registerParticipantForEvent(99L, 1L);

        // Assert
        assertFalse(result);
        verify(participantRepository, times(1)).findById(99L);
        verify(eventRepository, never()).findById(any());
        verify(participantRepository, never()).save(any(Participant.class));
    }

    @Test
    void registerParticipantForEvent_WhenEventNotFound_ShouldReturnFalse() {
        // Arrange
        when(participantRepository.findById(1L)).thenReturn(Optional.of(testParticipant));
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        boolean result = participantService.registerParticipantForEvent(1L, 99L);

        // Assert
        assertFalse(result);
        verify(participantRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).findById(99L);
        verify(participantRepository, never()).save(any(Participant.class));
    }
}