package ru.v0b0.geocoder.services;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import ru.v0b0.geocoder.data.PointsData;
import ru.v0b0.geocoder.entities.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodeService {

    private static final String baseURL = "https://geocode-maps.yandex.ru/1.x/";
    private static final String yandexApiKey = "apikey=48a66f25-0223-40d6-9fdb-8535992c5371";
    private static final String format = "format=json";
    private static final String geocode = "geocode=";
    private static final String URL = baseURL + '?' + yandexApiKey + '&' + format + '&' + geocode;

    private final PointsData data;

    private boolean isCoordinate;
    private boolean hasErrors = false;

    public GeocodeService(PointsData data) {
        this.data = data;
    }

    public String convert(Point point) {
        Point gettingPoint;
        isCoordinate = isCoordinates(point.getRequest());
        if (data.getData().containsKey(point.getRequest())) {
            gettingPoint = data.getData().get(point.getRequest());
            hasErrors = false;
        } else {
            gettingPoint = getPointFromRequest(point.getRequest());
            gettingPoint.setRequest(point.getRequest());
            data.addPoint(point.getRequest(), gettingPoint);
        }
        if (hasErrors) {
            return "Try again";
        } else {
            return "Found = "+(isCoordinate ? "place '"+gettingPoint.getAddress()+'\'' :
                    "coordinates '"+gettingPoint.getCoordinates()+'\'');
        }
    }

    public boolean isCoordinates(String request) {
        boolean answer = false;
        request = request.replaceAll("[,]", "").trim();
        if (request.split(" ").length == 2) {
            answer = NumberUtils.isCreatable(request.split(" ")[0]) &&
                    NumberUtils.isCreatable(request.split(" ")[1]);
        }
        return answer;
    }

    private Point getPointFromRequest(String request) {
        request = request.replaceAll("[ ]", "%20");
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet;
            if (isCoordinate) {
                httpGet = new HttpGet(URL + request + "&sco=latlong");
            } else {
                httpGet = new HttpGet(URL + request);
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader
                    (httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                hasErrors = false;
                return parseJSNOToPoint(readResponse(reader));
            } else {
                throw new IOException("StatusCode != 200");
            }
        } catch (IOException e) {
            hasErrors = true;
            return new Point("", "");
        }
    }

    private Point parseJSNOToPoint(JSONObject jsonObject) throws IOException {
        Point point = new Point();
        JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONObject("GeoObjectCollection")
                .getJSONArray("featureMember");
        if (!jsonArray.isEmpty()) {
            JSONObject geoObject = jsonArray.getJSONObject(0).getJSONObject("GeoObject");
            point.setAddress(geoObject.getJSONObject("metaDataProperty")
                    .getJSONObject("GeocoderMetaData").getString("text"));
            String coordinates = geoObject.getJSONObject("Point").getString("pos");
            coordinates = coordinates.split(" ")[1] + ' ' + coordinates.split(" ")[0];
            point.setCoordinates(coordinates);
        } else {
            throw new IOException();
        }
        return point;
    }

    private JSONObject readResponse(BufferedReader reader) throws IOException, JSONException {
        final StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return new JSONObject(sb.toString());
    }
}
