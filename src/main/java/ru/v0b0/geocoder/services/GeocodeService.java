package ru.v0b0.geocoder.services;

import ru.v0b0.geocoder.entities.Point;
import ru.v0b0.geocoder.data.PointsData;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

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

    public GeocodeService(PointsData data) {
        this.data = data;
    }

    public Point sendRequest(Point point) {
        Point gettingPoint = new Point();
        try {
            if (data.getData().containsKey(point.getRequest())) {
                gettingPoint = data.getData().get(point.getRequest());
            } else {
                gettingPoint = getPointFromRequest(point.getRequest());
                gettingPoint.setRequest(point.getRequest());
                data.addPoint(point.getRequest(), gettingPoint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gettingPoint;
    }

    private Point getPointFromRequest(String request) throws IOException {
        request = request.replaceAll(" ", "%20");
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(URL + request);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        BufferedReader reader = new BufferedReader(new InputStreamReader
                (httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            return parseJSNOToPoint(readResponse(reader));
        } else {
            return new Point("", "");
        }
    }

    private Point parseJSNOToPoint(JSONObject jsonObject) {
        Point point = new Point();
        JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONObject("GeoObjectCollection")
                .getJSONArray("featureMember");
        if (!jsonArray.isEmpty()) {
            JSONObject geoObject = jsonArray.getJSONObject(0).getJSONObject("GeoObject");
            point.setAddress(geoObject.getJSONObject("metaDataProperty")
                    .getJSONObject("GeocoderMetaData").getString("text"));
            point.setCoordinates(geoObject.getJSONObject("Point").getString("pos"));
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
