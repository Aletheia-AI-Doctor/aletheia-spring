package dev.aletheia.doctor.autoload;

import dev.aletheia.doctor.enums.DoctorSpeciality;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DoctorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DoctorLoader implements CommandLineRunner {

    private final DoctorService doctorService;

    DoctorLoader(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(doctorService.getRepository().count() > 0) {
            System.out.println("Doctors already loaded");
            return;
        }

        Doctor doctor = new Doctor();
        doctor.setName("Dr. John Doe");
        doctor.setSpeciality(DoctorSpeciality.CARDIOLOGIST);
        doctor.setPassword("password");
        doctor.setUsername("johndoe");
        doctor.setEmail("johndoe@aletheia.dev");
        doctor.setBio("Experienced cardiologist with over 10 years in the field. Passionate about heart health and patient care.");

        doctorService.save(doctor);
    }
}
