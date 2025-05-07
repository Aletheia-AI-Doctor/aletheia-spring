package dev.aletheia.doctor.enums;

import org.springframework.security.access.method.P;

public enum PatientStatus {
    PENDING,
    DIAGNOSED;
    public static PatientStatus fromString(String value){
        switch (value.toUpperCase()) {  
            case "DIAGNOSED":
                return DIAGNOSED;
            default:
                return PENDING;
        }
    }
}
