package pl.potat0x.nomock.inmemoryrepository;

import org.springframework.data.domain.Sort;

import java.util.List;

import static pl.potat0x.nomock.inmemoryrepository.EntityRipper.checkIfFieldIsString;
import static pl.potat0x.nomock.inmemoryrepository.EntityRipper.getFieldValue;

final class ReflectiveComparator {

    static int compareObjectsByMultipleFields(Object object1, Object object2, List<Sort.Order> orders) {
        int cmp = 0;
        for (Sort.Order order : orders) {
            Object obj1Field = getFieldValue(object1, order.getProperty());
            Object obj2Field = getFieldValue(object2, order.getProperty());

            if (obj1Field == null && obj2Field == null) {
                cmp = 0;
                continue;
            } else if (obj1Field == null || obj2Field == null) {
                return compareFieldsIfOneIsNull(order, obj1Field, obj2Field);
            }

            cmp = compareNonNullFields(order, obj1Field, obj2Field, checkIfFieldIsString(object2, order.getProperty()));

            if (cmp != 0) {
                return cmp;
            }
        }
        return cmp;
    }

    private static Integer compareFieldsIfOneIsNull(Sort.Order order, Object obj1Field, Object obj2Field) {
        assertThatExactlyOneObjectIsNull(obj1Field, obj2Field);

        final int nullOrdering = getNullOrdering(order);

        int cmp = 1;
        if (obj1Field == null) {
            cmp = order.isAscending() ? -1 : 1;
        }

        if (obj2Field == null) {
            cmp = order.isAscending() ? 1 : -1;
        }
        return cmp * nullOrdering;
    }

    private static int compareNonNullFields(Sort.Order order, Object obj1Field, Object obj2Field, boolean comparedObjectsAreStringType) {
        assertNotNull(obj1Field, obj2Field);

        int cmp;
        if (comparedObjectsAreStringType && order.isIgnoreCase()) {
            if (order.isAscending()) {
                cmp = ((String) obj1Field).compareToIgnoreCase((String) obj2Field);
            } else {
                cmp = ((String) obj2Field).compareToIgnoreCase((String) obj1Field);
            }
        } else {
            if (order.isAscending()) {
                cmp = ((Comparable) obj1Field).compareTo(obj2Field);
            } else {
                cmp = ((Comparable) obj2Field).compareTo(obj1Field);
            }
        }
        return cmp;
    }

    private static int getNullOrdering(Sort.Order order) {
        if (order.isAscending()) {
            return order.getNullHandling() == Sort.NullHandling.NULLS_LAST ? -1 : 1;
        } else {
            return order.getNullHandling() == Sort.NullHandling.NULLS_FIRST ? -1 : 1;
        }
    }

    private static void assertNotNull(Object object1, Object object2) {
        if (object1 == null || object2 == null) {
            throw new ReflectiveComparatorException("Both objects should be not null");
        }
    }

    private static void assertThatExactlyOneObjectIsNull(Object object1, Object object2) {
        if ((object1 == null && object2 == null) || (object1 != null && object2 != null)) {
            throw new ReflectiveComparatorException("Exactly one object should be null");
        }
    }

    private static class ReflectiveComparatorException extends RuntimeException {
        public ReflectiveComparatorException(String message) {
            super(message);
        }
    }
}
