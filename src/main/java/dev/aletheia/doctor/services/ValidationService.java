package dev.aletheia.doctor.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean isUnique(String table, String column, String value) {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE " + column + " = :value";

        try(EntityManager entityManager = this.entityManager.getEntityManagerFactory().createEntityManager()) {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("value", value);

            int count = ((Number) query.getSingleResult()).intValue();
            return count == 0;
        }
    }
}