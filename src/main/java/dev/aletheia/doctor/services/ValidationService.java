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
        return isUnique(table, column, value, null, null);
    }

    @Transactional
    public boolean isUnique(String table, String column, String value, String ignoreColumn, String ignoreValue) {
        boolean ignore = ignoreColumn != null && ignoreValue != null;

        StringBuilder builder = new StringBuilder()
                .append("SELECT COUNT(*) FROM ")
                .append(table)
                .append(" WHERE ")
                .append(column)
                .append(" = :value");
        if(ignore) {
            builder
                    .append(" AND ")
                    .append(ignoreColumn)
                    .append(" != :ignoreValue");
        }

        try(EntityManager entityManager = this.entityManager.getEntityManagerFactory().createEntityManager()) {
            Query query = entityManager.createNativeQuery(builder.toString());
            query.setParameter("value", value);
            if (ignore) {
                query.setParameter("ignoreValue", ignoreValue);
            }

            int count = ((Number) query.getSingleResult()).intValue();
            return count == 0;
        }
    }
}