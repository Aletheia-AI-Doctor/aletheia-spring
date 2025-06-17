package dev.aletheia.doctor.emailservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONObject;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailData {
    private Long doctorId;

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("doctorId", doctorId);
        return json;
    }

    @Override
    public String toString() {
        return toJson().toString();
    }
}
