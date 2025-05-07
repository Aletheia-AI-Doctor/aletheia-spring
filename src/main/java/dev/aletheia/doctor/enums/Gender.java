package dev.aletheia.doctor.enums;

public enum Gender {
    MALE,
    FEMALE;
    public static Gender fromString(String value){
        switch (value.toUpperCase()) {
            case "FEMALE":
            return FEMALE;    
            case "MALE":
                return MALE;
            default:
                return null;
        }
    }
}
