package pl.potat0x.nomock.inmemoryrepository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

final class EntityRipper<T, ID> {

    private final Class<?> idType;

    EntityRipper(Class<?> entityIdType) {
        this.idType = entityIdType;
    }

    void setEntityId(T entity, ID id) {
        getFieldAnnotatedAsId(entity)
                .ifPresentOrElse(field -> setFieldValue(entity, field, id, idType),
                        () -> {
                            throw new InMemoryCrudRepositoryException("@Id field not found");
                        });
    }

    Optional<ID> getEntityId(T entity) {
        return getFieldAnnotatedAsId(entity)
                .map(field -> (ID) getFieldValue(entity, field));
    }

    static Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            return getFieldValue(object, field);
        } catch (Exception e) {
            throw new InMemoryCrudRepositoryException(e);
        }
    }

    private static Object getFieldValue(Object object, Field field) {
        try {
            if (field.canAccess(object)) {
                return field.get(object);
            } else {
                return getter(object, field.getName()).invoke(object);
            }
        } catch (Exception e) {
            throw new InMemoryCrudRepositoryException(e);
        }
    }

    static boolean checkIfFieldIsString(Object object, String fieldName) {
        try {
            return object.getClass().getDeclaredField(fieldName).getType().equals(String.class);
        } catch (Exception e) {
            throw new InMemoryCrudRepositoryException(e);
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

    private static void setFieldValue(Object object, Field field, Object value, Class<?> parameterType) {
        try {
            if (field.canAccess(object)) {
                field.set(object, value);
            } else {
                setter(object, field.getName(), parameterType).invoke(object, value);
            }
        } catch (Exception e) {
            throw new InMemoryCrudRepositoryException(e);
        }
    }

    private static Method setter(Object object, String fieldName, Class<?> parameterType) throws NoSuchMethodException {
        return object.getClass().getDeclaredMethod("set" + capitalizeFirstLetter(fieldName), parameterType);
    }

    private static Method getter(Object object, String fieldName) throws NoSuchMethodException {
        return object.getClass().getDeclaredMethod("get" + capitalizeFirstLetter(fieldName));
    }

    private static String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
