package com.example.sportesemenynyilvantartorendszer.dto;

public record ParticipantDTO(
        Long id,
        String name,
        String email,
        int age,
        String skillLevel,
        Long eventId
) {}
