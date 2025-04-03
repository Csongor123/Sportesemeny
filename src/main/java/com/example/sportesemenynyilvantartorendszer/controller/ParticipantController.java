package com.example.sportesemenynyilvantartorendszer.controller;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.service.EventService;
import com.example.sportesemenynyilvantartorendszer.service.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;
    private final EventService eventService;

    @Autowired
    public ParticipantController(ParticipantService participantService, EventService eventService) {
        this.participantService = participantService;
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        return ResponseEntity.ok(participantService.getAllParticipants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        return participantService.getParticipantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Participant> createParticipant(@Valid @RequestBody Participant participant) {
        Participant createdParticipant = participantService.createParticipant(participant);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParticipant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @Valid @RequestBody Participant participantDetails) {
        Participant updatedParticipant = participantService.updateParticipant(id, participantDetails);
        return updatedParticipant != null ?
                ResponseEntity.ok(updatedParticipant) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        boolean deleted = participantService.deleteParticipant(id);
        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Participant> getParticipantByEmail(@PathVariable String email) {
        return participantService.getParticipantByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<Participant>> getParticipantsByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(participantService.getParticipantsByLastName(lastName));
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<Set<Event>> getParticipantEvents(@PathVariable Long id) {
        return participantService.getParticipantById(id)
                .map(participant -> ResponseEntity.ok(participant.getEvents()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{participantId}/register/{eventId}")
    public ResponseEntity<Map<String, String>> registerForEvent(
            @PathVariable Long participantId,
            @PathVariable Long eventId) {

        boolean registered = participantService.registerParticipantForEvent(participantId, eventId);

        if (registered) {
            return ResponseEntity.ok(Map.of("message", "Registration successful"));
        } else if (!eventService.getEventById(eventId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Event not found"));
        } else if (!participantService.getParticipantById(participantId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Participant not found"));
        } else if (eventService.isEventFull(eventId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Event is full"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Registration deadline has passed"));
        }
    }

    @DeleteMapping("/{participantId}/cancel/{eventId}")
    public ResponseEntity<Map<String, String>> cancelRegistration(
            @PathVariable Long participantId,
            @PathVariable Long eventId) {

        boolean canceled = participantService.cancelRegistration(participantId, eventId);

        if (canceled) {
            return ResponseEntity.ok(Map.of("message", "Registration canceled successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Registration not found"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Participant>> searchParticipants(@RequestParam String keyword) {
        return ResponseEntity.ok(participantService.searchParticipants(keyword));
    }
}
