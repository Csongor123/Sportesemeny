package com.example.sportesemenynyilvantartorendszer.controller;

import com.example.sporteventtracker.dto.EventDTO;
import com.example.sporteventtracker.service.SportEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class SportEventController {
    private final SportEventService eventService;

    @GetMapping
    public String getAllEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "events/list";
    }

    @GetMapping("/{id}")
    public String getEventDetails(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.getEventById(id));
        return "events/details";
    }

    // További végpontok...
}
