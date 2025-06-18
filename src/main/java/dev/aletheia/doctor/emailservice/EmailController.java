package dev.aletheia.doctor.emailservice;

import dev.aletheia.doctor.enums.DoctorStates;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.services.DigitalSignService;
import dev.aletheia.doctor.services.DoctorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class EmailController {

    private final DigitalSignService digitalSignService;
    private final DoctorService doctorService;
    private final EmailQueueManager emailQueueManager;

    @Value("${spring.application.url}")
    private String appUrl;

    @Value("${spring.application.frontend_url}")
    private String frontendUrl;

    public EmailController(DigitalSignService digitalSignService, DoctorService doctorService, EmailQueueManager emailQueueManager) {
        this.digitalSignService = digitalSignService;
        this.doctorService = doctorService;
        this.emailQueueManager = emailQueueManager;
    }

    @GetMapping("/appeal/{id}")
    public ResponseEntity<Object> appeal(@PathVariable Long id, @RequestParam(name = "token") String token) {
        boolean verify = digitalSignService.verifySignature(appUrl + "/api/reject-email/" + id, token);

        if (!verify) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Invalid token",
                    "success", false
            ));
        }

        Doctor doctor = doctorService.findOrFail(id);

        emailQueueManager.queueConfirmationRequest(doctor.getId());

        doctor.setStatus(DoctorStates.PENDING);

        doctorService.save(doctor);

        return ResponseEntity.ok(Map.of(
                "message", "Appeal successful! Please check your email for confirmation instructions.",
                "success", true
        ));
    }


    @GetMapping("/api/confirm-email/{id}")
    public ResponseEntity<Void> confirmEmail(@PathVariable Long id, HttpServletResponse response, @RequestParam(name = "token") String token) throws IOException {
        String frontendConfirmUrl = frontendUrl + "/confirm-email/" + id;
        boolean verify = digitalSignService.verifySignature(appUrl + "/api/confirm-email/" + id, token);

        Doctor doctor = doctorService.find(id);
        if (doctor == null || !verify) {
            response.sendRedirect(frontendConfirmUrl + "?status=error");
            return null;
        }

        if (doctor.getStatus() == DoctorStates.CONFIRMED) {
            response.sendRedirect(frontendConfirmUrl + "?status=already-confirmed");
            return null;
        }

        doctorService.confirmDoctor(id);

        if (doctor.getHospital() != null) {
            emailQueueManager.queueConfirmationDoctor(doctor.getId());
        }

        response.sendRedirect(frontendConfirmUrl + "?status=success");
        return null;
    }



    @GetMapping("/api/reject-email/{id}")
    public ResponseEntity<Void> rejectEmail(@PathVariable Long id, HttpServletResponse response, @RequestParam(name = "token") String token) throws IOException {
        String frontendConfirmUrl = frontendUrl + "/confirm-email/" + id;
        boolean verify = digitalSignService.verifySignature(appUrl + "/api/reject-email/" + id, token);

        Doctor doctor = doctorService.find(id);
        if (doctor == null || !verify) {
            response.sendRedirect(frontendConfirmUrl + "?status=error");
            return null;
        }

        doctorService.rejectDoctor(id);

        if (doctor.getHospital() != null) {
            emailQueueManager.queueRejectionDoctor(doctor.getId());
        }

        response.sendRedirect(frontendConfirmUrl + "?status=success");
        return null;
    }

}
