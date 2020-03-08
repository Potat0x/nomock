package pl.potat0x.nomock.inmemoryrepository.reflection;

import pl.potat0x.nomock.inmemoryrepository.InMemoryRepositoryException;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.Callable;

public final class EntityRipper<T, ID> {

    public void setEntityId(T entity, ID id) {
        getFieldAnnotatedAsId(entity)
                .ifPresentOrElse(field -> setFieldValue(entity, field, id),
                        () -> {
                            throw new InMemoryRepositoryException("@Id field not found");
                        });
    }

    public Optional<ID> getEntityId(T entity) {
        return getFieldAnnotatedAsId(entity)
                .map(field -> (ID) getFieldValue(entity, field));
    }

    static Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            return getFieldValue(object, field);
        } catch (Exception e) {
            throw new InMemoryRepositoryException(e);
        }
    }

    static boolean checkIfFieldIsString(Object object, String fieldName) {
        try {
            return object.getClass().getDeclaredField(fieldName).getType().equals(String.class);
        } catch (Exception e) {
            throw new InMemoryRepositoryException(e);
        }
    }

    private Optional<Field> getFieldAnnotatedAsId(Object entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(javax.persistence.Id.class)) {
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

    private static Object getFieldValue(Object object, Field field) {
        try {
            return performActionOnSecuredFieldAndGetResult(object, field, () -> field.get(object));
        } catch (Exception e) {
            throw new InMemoryRepositoryException(e);
        }
    }

    private static void setFieldValue(Object object, Field field, Object value) {
        try {
            performActionOnSecuredFieldAndGetResult(object, field, () -> {
                field.set(object, value);
                return field.get(object);
            });
        } catch (Exception e) {
            throw new InMemoryRepositoryException(e);
        }
    }

    private static Object performActionOnSecuredFieldAndGetResult(Object object, Field field, Callable<?> action) throws Exception {
        final boolean fieldAccessibility = field.canAccess(object);
        field.setAccessible(true);
        Object fieldValue = action.call();
        field.setAccessible(fieldAccessibility);
        return fieldValue;
    }
}
