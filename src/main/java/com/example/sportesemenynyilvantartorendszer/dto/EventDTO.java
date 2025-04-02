package com.example.sportesemenynyilvantartorendszer.dto;

import java.time.LocalDateTime;

public record EventDTO(
        Long id,
        String name,
        LocalDateTime dateTime,
        String location,
        String sportType,
        int maxParticipants,
        int currentParticipants
) {}