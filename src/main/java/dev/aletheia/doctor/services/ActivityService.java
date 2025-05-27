package dev.aletheia.doctor.services;

import dev.aletheia.doctor.dtos.activities.ActivityLogDto;
import dev.aletheia.doctor.models.ActivityLog;
import dev.aletheia.doctor.models.Doctor;
import dev.aletheia.doctor.repositories.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService extends CRUDService<ActivityLog, ActivityLogDto> {
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private DoctorService doctorService;

    protected ActivityService() {
        super(ActivityLog.class, ActivityLogDto.class);
    }

    @Override
    public JpaRepository<ActivityLog, Long> getRepository() {
        return activityLogRepository;
    }

    public List<ActivityLogDto> getAllDTO() {
        Doctor doctor = doctorService.getCurrentDoctor();

        return activityLogRepository
                .findByDoctorIdOrderByCreatedAtDesc(doctor.getId(), Limit.of(5))
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public Page<ActivityLogDto> getAllDTO(Pageable pageable) {
        Doctor doctor = doctorService.getCurrentDoctor();

        return activityLogRepository
                .findByDoctorIdOrderByCreatedAtDesc(doctor.getId(), pageable)
                .map(this::convertToDto);
    }

    public void log(String action, String description) {
        ActivityLog log = new ActivityLog();
        log.setDoctor(doctorService.getCurrentDoctor());
        log.setAction(action);
        log.setDescription(description);
        activityLogRepository.save(log);
    }
}
