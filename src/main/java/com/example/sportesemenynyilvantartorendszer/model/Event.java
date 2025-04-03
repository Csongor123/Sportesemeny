package com.example.sportesemenynyilvantartorendszer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name is required")
    private String name;

    private String description;

    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDate;

    @NotBlank(message = "Location is required")
    private String location;

    @Min(value = 1, message = "Maximum participants must be at least 1")
    private int maxParticipants;

    @NotNull(message = "Registration deadline is required")
    @Future(message = "Registration deadline must be in the future")
    private LocalDateTime registrationDeadline;

    @ManyToMany(mappedBy = "events")
    private Set<Participant> participants = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean addParticipant(Participant participant) {
        if (this.participants.size() < this.maxParticipants) {
            return this.participants.add(participant);
        }
        return false;
    }

    public boolean removeParticipant(Participant participant) {
        return this.participants.remove(participant);
    }

    public int getAvailableSpots() {
        return this.maxParticipants - this.participants.size();
    }

    public boolean isRegistrationOpen() {
        return LocalDateTime.now().isBefore(this.registrationDeadline);
    }
}