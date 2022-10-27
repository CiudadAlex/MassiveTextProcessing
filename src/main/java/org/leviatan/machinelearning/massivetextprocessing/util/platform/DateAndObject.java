package org.leviatan.machinelearning.massivetextprocessing.util.platform;

import java.util.Date;

/**
 * DateAndObject.
 *
 * @author acc
 *
 */
public class DateAndObject<T> {

    private final Date date;
    private final T object;

    /**
     * Constructor for DateAndObject.
     *
     * @param date
     *            date
     * @param object
     *            object
     */
    public DateAndObject(final Date date, final T object) {
        this.date = date;
        this.object = object;
    }

    public Date getDate() {
        return this.date;
    }

    public T getObject() {
        return this.object;
    }

}
