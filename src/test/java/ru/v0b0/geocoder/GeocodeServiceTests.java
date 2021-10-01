package ru.v0b0.geocoder;

import ru.v0b0.geocoder.entities.Point;
import ru.v0b0.geocoder.services.GeocodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GeocodeServiceTests {

    @Autowired
    private GeocodeService service;

    @Test
    void test1(){
        Point point = new Point("Беларусь, Минск");
        Point request = service.sendRequest(point);

        assertEquals("Беларусь, Минск", request.getAddress());
        assertEquals("27.561831 53.902284", request.getCoordinates());
    }

    @Test
    void test2(){
        Point point = new Point("Астана");
        Point request = service.sendRequest(point);

        assertEquals("Казахстан, Нур-Султан (Астана)", request.getAddress());
        assertEquals("71.43042 51.128207", request.getCoordinates());
    }

    @Test
    void test3(){
        Point point = new Point("Минск ленина 12");
        Point request = service.sendRequest(point);

        assertEquals("Беларусь, Минск, улица Ленина, 12", request.getAddress());
        assertEquals("27.559055 53.900167", request.getCoordinates());
    }

    @Test
    void test4(){
        Point point = new Point("");
        Point request = service.sendRequest(point);

        assertEquals("", request.getAddress());
        assertEquals("", request.getCoordinates());
    }

    @Test
    void test5(){
        Point point = new Point("27.561831 53.902284");
        Point request = service.sendRequest(point);

        assertEquals("Беларусь, Минск, проспект Независимости", request.getAddress());
        assertEquals("27.636158 53.930226", request.getCoordinates());
    }

    @Test
    void test6(){
        Point point = new Point("37.621094 55.753605");
        Point request = service.sendRequest(point);

        assertEquals("Россия, Москва, Красная площадь", request.getAddress());
        assertEquals("37.621094 55.753605", request.getCoordinates());
    }

    @Test
    void test7() {
        Point point = new Point("Vbycr");
        Point request = service.sendRequest(point);

        assertEquals("Беларусь, Минск", request.getAddress());
        assertEquals("27.561831 53.902284",request.getCoordinates());
    }
}
