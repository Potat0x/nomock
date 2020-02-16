package pl.potat0x.nomock.inmemoryrepository;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Optional;

final class EntityRipper {
    public static void setEntityId(Object entity, Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        getFieldAnnotatedAsId(entity)
                .ifPresentOrElse(field -> setFieldViaSetter(entity, field, id),
                        () -> {
                            throw new InMemoryCrudRepositoryException("No setter");
                        });
    }

    static Optional<Long> getEntityId(Object entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return getFieldAnnotatedAsId(entity)
                .map(field -> readFieldViaGetter(entity, field));
    }

    private static Optional<Field> getFieldAnnotatedAsId(Object entity) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(javax.persistence.Id.class)) {
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

    private static Long readFieldViaGetter(Object object, Field field) {
        try {
            Method getter = getGetter(object, field.getName());
            return (Long) getter.invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new InMemoryCrudRepositoryException(e);
        }
    }

    private static void setFieldViaSetter(Object object, Field field, Object value) {
        try {
            Method setter = getSetter(object, field.getName());
            setter.invoke(object, value);
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

    private static void printFieldInfo(Field field) {
        AnnotatedType annotatedType = field.getAnnotatedType();
        Type type = annotatedType.getType();
        int modifiers = field.getModifiers();
        System.out.println(Modifier.toString(modifiers));
        String fieldName = field.getName();
        System.out.println("EntityRipper: " + type.getTypeName() + " " + fieldName);
    }
}
