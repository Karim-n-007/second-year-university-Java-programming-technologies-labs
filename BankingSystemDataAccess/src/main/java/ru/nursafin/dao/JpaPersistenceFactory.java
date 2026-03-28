package ru.nursafin.dao;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JpaPersistenceFactory {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("BankingSystem");

    public static void shutdown() {
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

}
