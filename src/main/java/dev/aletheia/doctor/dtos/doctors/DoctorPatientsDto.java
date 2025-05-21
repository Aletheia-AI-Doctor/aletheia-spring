package dev.aletheia.doctor.dtos.doctors;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class DoctorPatientsDto {
    private Long total;
    private Long pending;
    private Long diagnosed;
private Long helped;
    public DoctorPatientsDto(Long total, Long pending, Long diagnosed) {
        this.total = total;
        this.pending = pending;
        this.diagnosed = diagnosed;
    }
}
