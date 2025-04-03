-- Sample events
INSERT INTO event (name, description, event_date, location, max_participants, registration_deadline)
VALUES
        ('Annual Marathon', 'A 42km marathon through the city center', '2025-06-15 08:00:00', 'City Park', 500, '2025-06-01 23:59:59'),
    ('Swimming Championship', 'Regional swimming competition for all ages', '2025-07-10 10:00:00', 'Olympic Pool', 100, '2025-06-25 23:59:59'),
            ('Basketball Tournament', '3-on-3 basketball tournament', '2025-08-05 09:00:00', 'Sports Arena', 48, '2025-07-20 23:59:59');

            -- Sample participants
INSERT INTO <first_name, last_name, email, phone, phone, phone, phone> participant (first_name, last_name, email, phone, date_of_birth)
VALUES
        ('John', 'Doe', 'john.doe@example.com', '+1234567890', '1990-05-15'),
    ('Jane', 'Smith', 'jane.smith@example.com', '+1987654321', '1992-09-20'),
            ('Michael', 'Johnson', 'michael.j@example.com', '+1122334455', '1985-12-10');

            -- Sample registrations
INSERT INTO participant_events (participant_id, event_id, registration_date, status)
VALUES
        (1, 1, '2025-05-10 14:30:00', 'CONFIRMED'),
    (1, 2, '2025-06-05 09:15:00', 'REGISTERED'),
            (2, 1, '2025-05-12 11:45:00', 'CONFIRMED'),
            (3, 3, '2025-07-01 16:20:00', 'CONFIRMED');

public void main() {
}