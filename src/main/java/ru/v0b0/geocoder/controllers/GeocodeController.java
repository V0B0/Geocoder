package ru.v0b0.geocoder.controllers;

import ru.v0b0.geocoder.services.GeocodeService;
import ru.v0b0.geocoder.entities.Point;
import ru.v0b0.geocoder.data.PointsData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GeocodeController {

    private final GeocodeService service;
    private final PointsData cache;

    public GeocodeController(GeocodeService service, PointsData cache) {
        this.service = service;
        this.cache = cache;
    }

    @ModelAttribute
    public Model loadAttributes(Model model) {
        model.addAttribute("point", new Point());
        model.addAttribute("values", cache.getData().values());
        return model;
    }

    @GetMapping("/")
    public String welcome() {
        return "geocode";
    }

    @PostMapping("/")
    public String geocode(Point point) {
        service.sendRequest(point);
        return "geocode";
    }
}
