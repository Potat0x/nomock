package pl.potat0x.nomock.inmemoryrepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

final class EntityRipper {
    public static void setEntityId(Object entity, Long id) {
        getFieldAnnotatedAsId(entity)
                .ifPresentOrElse(field -> setFieldValue(entity, field, id),
                        () -> {
                            throw new InMemoryCrudRepositoryException("@Id field not found");
                        });
    }

    static <ID> Optional<ID> getEntityId(Object entity) {
        return getFieldAnnotatedAsId(entity)
                .map(field -> readFieldValue(entity, field));
    }

    private static Optional<Field> getFieldAnnotatedAsId(Object entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(javax.persistence.Id.class)) {
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

    private static <ID> ID readFieldValue(Object object, Field field) {
        try {
            if (field.canAccess(object)) {
                return (ID) field.get(object);
            } else {
                return (ID) getGetter(object, field.getName()).invoke(object);
            }
        } catch (Exception e) {
            throw new InMemoryCrudRepositoryException(e);
        }
    }

    private static void setFieldValue(Object object, Field field, Object value) {
        try {
            if (field.canAccess(object)) {
                field.set(object, value);
            } else {
                getSetter(object, field.getName()).invoke(object, value);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new InMemoryCrudRepositoryException(e);
        }
    }

    private static Method getSetter(Object object, String fieldName) throws NoSuchMethodException {
        return object.getClass().getDeclaredMethod("set" + capitalizeFirstLetter(fieldName), Long.class);
    }

    private static Method getGetter(Object object, String fieldName) throws NoSuchMethodException {
        return object.getClass().getDeclaredMethod("get" + capitalizeFirstLetter(fieldName));
    }

    private static String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
