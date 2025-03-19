package dev.aletheia.doctor.config;

import dev.aletheia.doctor.services.ValidationService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtils {
  private static ServiceUtils instance;

  private final ValidationService validationService;

  public ServiceUtils(ValidationService validationService) {
    this.validationService = validationService;
  }

  @PostConstruct
  public void fillInstance() {
    instance = this;
  }

  public static ValidationService getValidationService() {
    return instance.validationService;
  }
}
