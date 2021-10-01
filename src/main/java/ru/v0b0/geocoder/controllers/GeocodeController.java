package ru.v0b0.geocoder.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.v0b0.geocoder.entities.Point;
import ru.v0b0.geocoder.services.GeocodeService;

@Controller
public class GeocodeController {

    private final GeocodeService service;

    public GeocodeController(GeocodeService service) {
        this.service = service;
    }

    @ModelAttribute
    public Model loadAttributes(Model model) {
        model.addAttribute("point", new Point());
        return model;
    }

    @GetMapping("/")
    public String welcome() {
        return "geocode";
    }

    @PostMapping("/")
    public String geocode(Point point, Model model) {
        model.addAttribute("answer", service.convert(point));
        return "geocode";
    }
}
