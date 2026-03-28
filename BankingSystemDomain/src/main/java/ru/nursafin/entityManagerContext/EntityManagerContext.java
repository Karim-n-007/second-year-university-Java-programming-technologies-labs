package ru.nursafin.entityManagerContext;

import jakarta.persistence.EntityManager;
import lombok.Getter;

public class EntityManagerContext {
    @Getter
    private static EntityManager current;

    public static void bind(EntityManager entityManager) {
        current = entityManager;
    }

    public static boolean hasCurrent() {
        return current != null;
    }

    public static void unbind() {
        current = null;
    }
}
