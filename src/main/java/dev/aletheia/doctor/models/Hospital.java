package dev.aletheia.doctor.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import java.util.List;

@Setter
@Getter
@Entity(name = "hospitals")
@DynamicUpdate
public class Hospital extends BaseModel {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "hr_email", nullable = false, unique = true)
    // @Unique(table = "doctors", value = "email", message = "Email must be unique")
    @Email(message = "Email should be valid")
    private String hr_email;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctor> doctors;


    @Override
    public String toString() {
        return "Hospital{" +
                "id='" + getId() + '\'' +
                " name='" + name + '\'' +
                ", hr_email='" + hr_email + '\'' +
                '}';
    }
}
