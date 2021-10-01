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
        String request = service.convert(point);
        assertEquals("53.902284 27.561831", request);
    }

    @Test
    void test2(){
        Point point = new Point("Астана");
        String request = service.convert(point);
        assertEquals("51.128207 71.43042", request);
    }

    @Test
    void test3(){
        Point point = new Point("Минск ленина 12");
        String request = service.convert(point);
        assertEquals("53.900167 27.559055", request);
    }

    @Test
    void test4(){
        Point point = new Point("");
        String request = service.convert(point);
        assertEquals("Try again", request);
    }

    @Test
    void test5(){
        Point point = new Point("53.902284 27.561831");
        String request = service.convert(point);
        assertEquals("Беларусь, Минск, проспект Независимости", request);
    }

    @Test
    void test6(){
        Point point = new Point("55.753605 37.621094");
        String request = service.convert(point);
        assertEquals("Россия, Москва, Красная площадь", request);
    }

    @Test
    void test7() {
        Point point = new Point("Vbycr");
        String request = service.convert(point);
        assertEquals("53.902284 27.561831",request);
    }
}
