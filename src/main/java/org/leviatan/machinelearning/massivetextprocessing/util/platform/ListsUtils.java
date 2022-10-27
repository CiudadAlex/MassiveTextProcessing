package org.leviatan.machinelearning.massivetextprocessing.util.platform;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

/**
 * ListsUtils.
 *
 * @author alciucam
 *
 */
public final class ListsUtils {

    private ListsUtils() {
    }

    /**
     * Converts a list to an array.
     *
     * @param <T>
     *            type of object
     * @param componentClass
     *            componentClass
     * @param list
     *            list
     * @return the array
     */
    public static <T> T[] toArray(final Class<T> componentClass, final List<T> list) {

        @SuppressWarnings("unchecked")
        final T[] array = (T[]) Array.newInstance(componentClass, list.size());
        return list.toArray(array);
    }

    /**
     * Builds a list.
     *
     * @param <T>
     *            type of object
     * @param array
     *            array
     * @return the list
     */
    @SafeVarargs
    public static <T> List<T> buildList(final T... array) {

        final List<T> list = new ArrayList<T>();

        for (final T object : array) {
            list.add(object);
        }

        return list;
    }

    /**
     * Builds a list.
     *
     * @param <T>
     *            type of object
     * @param collection
     *            collection
     * @return the list
     */
    public static <T> List<T> buildList(final Collection<T> collection) {

        final List<T> list = new ArrayList<T>();

        for (final T object : collection) {
            list.add(object);
        }

        return list;
    }

    /**
     * Builds a list.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @param array
     *            array
     * @return the list
     */
    @SafeVarargs
    public static <T> List<T> addToList(final List<T> list, final T... array) {

        for (final T object : array) {
            list.add(object);
        }

        return list;
    }

    /**
     * Transforms the list.
     *
     * @param <T>
     *            Initial type
     * @param <R>
     *            Resulting type
     * @param list
     *            list
     * @param function
     *            function
     * @return the transformed list
     */
    public static <T, R> List<R> transformList(final List<T> list, final Function<T, R> function) {

        final List<R> listReturn = new ArrayList<R>();
        list.stream().map(function).forEach(listReturn::add);

        return listReturn;
    }

    /**
     * Execute for each element.
     *
     * * @param <T> object type
     *
     * @param list
     *            list
     * @param consumer
     *            consumer
     */
    public static <T> void forEach(final List<T> list, final Consumer<T> consumer) {
        list.stream().forEach(consumer);
    }

    /**
     * Transforms the list and returns only the diferent ones.
     *
     * @param <T>
     *            Initial type
     * @param <R>
     *            Resulting type
     * @param list
     *            list
     * @param function
     *            function
     * @return the transformed list with only the diferent ones
     */
    public static <T, R> List<R> transformListDistinct(final List<T> list, final Function<T, R> function) {

        final List<R> listTransformed = transformList(list, function);
        final Set<R> setTransformed = buildSet(listTransformed);

        final List<R> listReturn = buildList(setTransformed);

        return listReturn;
    }

    /**
     * Returns the first occurrence of the condition.
     *
     * @param <T>
     *            Initial type
     * @param list
     *            the list
     * @param predicate
     *            the predicate
     * @return the first item in list with the condition
     */
    public static <T> T getFirstWithCondition(final List<T> list, final Predicate<? super T> predicate) {

        final Optional<T> optional = list.stream().filter(predicate).findFirst();

        if (optional.isPresent()) {
            return optional.get();
        }

        return null;
    }

    /**
     * Filters the list by a condition.
     *
     * @param <T>
     *            object type
     * @param list
     *            list
     * @param predicate
     *            predicate
     * @return the filtered list
     */
    public static <T> List<T> filterList(final List<T> list, final Predicate<? super T> predicate) {

        final List<T> listReturn = new ArrayList<T>();
        list.stream().filter(predicate).forEach(listReturn::add);

        return listReturn;
    }

    /**
     * Returns the count of the items that match the condition.
     *
     * @param <T>
     *            object type
     * @param list
     *            list
     * @param predicate
     *            predicate
     * @return the count of the items that match the condition
     */
    public static <T> int count(final List<T> list, final Predicate<? super T> predicate) {

        final List<T> listFiltered = filterList(list, predicate);
        return listFiltered.size();
    }

    /**
     * Returns if the object is in the list.
     *
     * @param <T>
     *            tipe of object
     * @param obj
     *            obj
     * @param list
     *            list
     * @return if the object is in the list
     */
    public static <T> boolean isInList(final T obj, final List<T> list) {

        for (final T item : list) {

            if (item.equals(obj)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Splits the list in portions.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @param perOnageFirstPart
     *            perOnageFirstPart
     * @return the partition
     */
    public static <T> List<List<T>> splitListInPortions(final List<T> list, final double perOnageFirstPart) {

        final int partitionSize = (int) (list.size() * perOnageFirstPart);
        final List<List<T>> partition = Lists.partition(list, partitionSize);
        return partition;
    }

    /**
     * Checks if the list is empty.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @return if the list is empty
     */
    public static <T> boolean isEmpty(final List<T> list) {

        return list == null || list.isEmpty();
    }

    /**
     * Builds a set with a collection.
     *
     * @param <T>
     *            type of object
     * @param collection
     *            collection
     * @return a set
     */
    public static <T> Set<T> buildSet(final Collection<T> collection) {

        final Set<T> set = new HashSet<T>();
        set.addAll(collection);

        return set;
    }

    /**
     * Returns the object with max property.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @param propertyExtractor
     *            propertyExtractor
     * @return the object with max property
     */
    public static <T> T getObjectWithMaxProperty(final List<T> list,
            final Function<? super T, Double> propertyExtractor) {

        T objectMax = null;
        Double maxPropertyValue = Double.NEGATIVE_INFINITY;

        for (final T obj : list) {

            final Double propertyValue = propertyExtractor.apply(obj);

            if (propertyValue != null && propertyValue > maxPropertyValue) {

                maxPropertyValue = propertyValue;
                objectMax = obj;
            }
        }

        return objectMax;
    }

    /**
     * Returns the object with min property.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @param propertyExtractor
     *            propertyExtractor
     * @return the object with min property
     */
    public static <T> T getObjectWithMinProperty(final List<T> list,
            final Function<? super T, Double> propertyExtractor) {

        T objectMin = null;
        Double minPropertyValue = Double.POSITIVE_INFINITY;

        for (final T obj : list) {

            final Double propertyValue = propertyExtractor.apply(obj);

            if (propertyValue != null && propertyValue < minPropertyValue) {

                minPropertyValue = propertyValue;
                objectMin = obj;
            }
        }

        return objectMin;
    }

    /**
     * Orders the list.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @param orderCriteriaExtractor
     *            orderCriteriaExtractor
     * @param asc
     *            asc
     */
    public static <T> void orderListDate(final List<T> list, final Function<T, Date> orderCriteriaExtractor,
            final boolean asc) {

        final Function<T, Long> orderCriteriaExtractorLong = t -> {

            final Date date = orderCriteriaExtractor.apply(t);

            if (date != null) {
                return date.getTime();
            } else {
                return 0L;
            }
        };

        orderListLong(list, orderCriteriaExtractorLong, asc);
    }

    /**
     * Orders the list.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @param orderCriteriaExtractor
     *            orderCriteriaExtractor
     * @param asc
     *            asc
     */
    public static <T> void orderListLong(final List<T> list, final Function<T, Long> orderCriteriaExtractor,
            final boolean asc) {

        Collections.sort(list, new Comparator<T>() {

            @Override
            public int compare(final T o1, final T o2) {

                Long num1 = orderCriteriaExtractor.apply(o1);
                Long num2 = orderCriteriaExtractor.apply(o2);

                num1 = num1 != null ? num1 : 0;
                num2 = num2 != null ? num2 : 0;

                int comparison = num1.compareTo(num2);

                if (!asc) {
                    comparison = -comparison;
                }

                return comparison;
            }
        });
    }

    /**
     * Orders the list.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @param orderCriteriaExtractor
     *            orderCriteriaExtractor
     * @param asc
     *            asc
     */
    public static <T> void orderListDouble(final List<T> list, final Function<T, Double> orderCriteriaExtractor,
            final boolean asc) {

        Collections.sort(list, new Comparator<T>() {

            @Override
            public int compare(final T o1, final T o2) {

                Double num1 = orderCriteriaExtractor.apply(o1);
                Double num2 = orderCriteriaExtractor.apply(o2);

                num1 = num1 != null ? num1 : 0;
                num2 = num2 != null ? num2 : 0;

                int comparison = num1.compareTo(num2);

                if (!asc) {
                    comparison = -comparison;
                }

                return comparison;
            }
        });
    }

    /**
     * Returns if the object is in the list.
     *
     * @param <T>
     *            type of object
     * @param obj
     *            obj
     * @param list
     *            list
     * @param equalsFunction
     *            equalsFunction
     * @return if the object is in the list
     */
    public static <T> boolean isObjectInList(final T obj, final List<T> list,
            final Function2Arg<T, T, Boolean> equalsFunction) {

        for (final T item : list) {

            final boolean equals = equalsFunction.apply(obj, item);

            if (equals) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retuns the objects in list 1 not present in list 2.
     *
     * @param <T>
     *            type of object
     * @param list1
     *            list1
     * @param list2
     *            list2
     * @param equalsFunction
     *            equalsFunction
     * @return the objects in list 1 not present in list 2
     */
    public static <T> List<T> getObjectsInList1NotPresentInList2(final List<T> list1, final List<T> list2,
            final Function2Arg<T, T, Boolean> equalsFunction) {

        final List<T> listReturn = new ArrayList<T>();

        for (final T obj1 : list1) {

            final boolean isInList2 = isObjectInList(obj1, list2, equalsFunction);

            if (!isInList2) {
                listReturn.add(obj1);
            }
        }

        return listReturn;
    }

    /**
     * Trunks the list.
     *
     * @param <T>
     *            type of object
     * @param list
     *            list
     * @param maxNumElements
     *            maxNumElements
     * @return the trunked list
     */
    public static <T> List<T> trunkList(final List<T> list, final int maxNumElements) {

        final List<T> listTrunk;

        if (list.size() > maxNumElements) {
            listTrunk = list.subList(0, maxNumElements);
        } else {
            listTrunk = list;
        }

        return listTrunk;
    }

}
