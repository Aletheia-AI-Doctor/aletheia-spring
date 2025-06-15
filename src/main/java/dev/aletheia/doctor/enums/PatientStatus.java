package dev.aletheia.doctor.enums;

public enum PatientStatus {
    PENDING,
    DIAGNOSED;

    public static PatientStatus fromString(String value) {
        return value.equalsIgnoreCase("Diagnosed")
                ? DIAGNOSED
                : PENDING;
    }
}
