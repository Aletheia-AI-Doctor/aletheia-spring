package dev.aletheia.doctor.services;

import dev.aletheia.doctor.helpers.Http;
import dev.aletheia.doctor.helpers.Response;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HttpService {

    @Value("${models.api.url}")
    private String BASE_URL;

    public Response get(String url) {
        return new Http(BASE_URL + url).get();
    }

    public Response post(String url, JSONObject body) {
        return new Http(BASE_URL + url).post(body);
    }

    public Response patch(String url, JSONObject body) {
        return new Http(BASE_URL + url).patch(body);
    }

    public Response delete(String url) {
        return new Http(BASE_URL + url).delete();
    }
}
