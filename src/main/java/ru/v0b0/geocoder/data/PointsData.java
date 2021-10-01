package ru.v0b0.geocoder.data;

import ru.v0b0.geocoder.entities.Point;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PointsData {

    private final Map<String, Point> data = new HashMap<>();

    public void addPoint(String request, Point afterPoint) {
        if (afterPoint.getAddress() != null && !afterPoint.getAddress().equals("") &&
                afterPoint.getCoordinates() != null && !afterPoint.getCoordinates().equals(""))
            data.put(request, afterPoint);
    }

    public Map<String, Point> getData() {
        return data;
    }
}
