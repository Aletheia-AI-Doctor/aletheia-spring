package dev.aletheia.doctor.autoload;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DoctorLoader implements CommandLineRunner {

    DoctorLoader() {
        System.out.println("DoctorLoader constructor");
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
