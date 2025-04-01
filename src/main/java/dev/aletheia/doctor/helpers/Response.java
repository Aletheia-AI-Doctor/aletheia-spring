package dev.aletheia.doctor.helpers;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class Response {
    private String body;
    private String headers;
    private HttpStatus statusCode;

    public boolean isSuccessful() {
        return statusCode.is2xxSuccessful() && body != null;
    }

    public JSONObject getJsonBody() {
        JSONParser parser = new JSONParser(1);

        try {
            return (JSONObject) parser.parse(getBody());
        } catch (ParseException ignored) {}

        return null;
    }
}
