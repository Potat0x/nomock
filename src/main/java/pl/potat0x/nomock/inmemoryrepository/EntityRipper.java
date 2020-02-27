package pl.potat0x.nomock.inmemoryrepository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

final class EntityRipper<T, ID> {

    private final Class<?> idType;

    public EntityRipper(Class<?> entityIdType) {
        this.idType = entityIdType;
    }

    void setEntityId(T entity, ID id) {
        getFieldAnnotatedAsId(entity)
                .ifPresentOrElse(field -> setFieldValue(entity, field, id),
                        () -> {
                            throw new InMemoryCrudRepositoryException("@Id field not found");
                        });
    }

    Optional<ID> getEntityId(T entity) {
        return getFieldAnnotatedAsId(entity)
                .map(field -> readFieldValue(entity, field));
    }

    private Optional<Field> getFieldAnnotatedAsId(T entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(javax.persistence.Id.class)) {
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

    private ID readFieldValue(T object, Field field) {
        try {
            if (field.canAccess(object)) {
                return (ID) field.get(object);
            } else {
                return (ID) getter(object, field.getName()).invoke(object);
            }
        } catch (Exception e) {
            throw new InMemoryCrudRepositoryException(e);
        }
    }

    private void setFieldValue(T object, Field field, ID value) {
        try {
            if (field.canAccess(object)) {
                field.set(object, value);
            } else {
                setter(object, field.getName()).invoke(object, value);
            }
        } catch (Exception e) {
            throw new InMemoryCrudRepositoryException(e);
        }
    }

    private Method setter(T object, String fieldName) throws NoSuchMethodException {
        return object.getClass().getDeclaredMethod("set" + capitalizeFirstLetter(fieldName), idType);
    }

    private Method getter(T object, String fieldName) throws NoSuchMethodException {
        return object.getClass().getDeclaredMethod("get" + capitalizeFirstLetter(fieldName));
    }

    private String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
