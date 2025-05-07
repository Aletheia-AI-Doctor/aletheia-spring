package dev.aletheia.doctor.enums;

import org.springframework.security.access.method.P;

public enum PatientStatus {
    PENDING,
    ACTIVE,
    DISCHARGED;
    public static PatientStatus fromString(String value){
        switch (value.toUpperCase()) {
            case "ACTIVE":
            return ACTIVE;    
            case "DISCHARGED":
                return DISCHARGED;
            default:
                return PENDING;
        }
    }
}
