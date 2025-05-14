package dev.aletheia.doctor.dtos.hospitals;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HospitalDto {
    private long id;
    private String name;
    private String hr_email;
}
