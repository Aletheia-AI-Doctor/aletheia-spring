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
@RequestMapping("/api")
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
    public ResponseEntity<Object> appeal(@PathVariable Long id, @RequestParam(name = "token") String token, HttpServletResponse response) throws IOException {
        boolean verify = digitalSignService.verifySignature(appUrl + "/api/appeal/" + id, token);

        String frontendResponseUrl = frontendUrl + "/email-response";

        if (!verify) {
            response.sendRedirect(frontendResponseUrl + "?status=error");
            return null;
        }

        Doctor doctor = doctorService.findOrFail(id);

        if (doctor.getStatus() == DoctorStates.CONFIRMED) {
            response.sendRedirect(frontendResponseUrl + "?status=alreadyConfirmed");
            return null;
        }

        emailQueueManager.queueConfirmationRequest(doctor.getId());

        doctor.setStatus(DoctorStates.PENDING);

        doctorService.save(doctor);

        response.sendRedirect(frontendResponseUrl + "?status=appeal");
        return null;
    }


    @GetMapping("/confirm-email/{id}")
    public ResponseEntity<Void> confirmEmail(@PathVariable Long id, HttpServletResponse response, @RequestParam(name = "token") String token) throws IOException {
        String frontendResponseUrl = frontendUrl + "/email-response";
        boolean verify = digitalSignService.verifySignature(appUrl + "/api/confirm-email/" + id, token);

        Doctor doctor = doctorService.find(id);
        if (doctor == null || !verify) {
            response.sendRedirect(frontendResponseUrl + "?status=error");
            return null;
        }

        doctorService.confirmDoctor(id);

        if (doctor.getHospital() != null) {
            emailQueueManager.queueConfirmationDoctor(doctor.getId());
        }

        response.sendRedirect(frontendResponseUrl + "?status=confirmed");
        return null;
    }



    @GetMapping("/reject-email/{id}")
    public ResponseEntity<Void> rejectEmail(@PathVariable Long id, HttpServletResponse response, @RequestParam(name = "token") String token) throws IOException {
        String frontendResponseUrl = frontendUrl + "/email-response";
        boolean verify = digitalSignService.verifySignature(appUrl + "/api/reject-email/" + id, token);

        Doctor doctor = doctorService.find(id);
        if (doctor == null || !verify) {
            response.sendRedirect(frontendResponseUrl + "?status=error");
            return null;
        }

        doctorService.rejectDoctor(id);

        if (doctor.getHospital() != null) {
            emailQueueManager.queueRejectionDoctor(doctor.getId());
        }

        response.sendRedirect(frontendResponseUrl + "?status=rejected");
        return null;
    }

}
