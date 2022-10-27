package org.leviatan.machinelearning.massivetextprocessing.util.platform;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * SequenceUtils.
 *
 * @author acc
 *
 */
public final class SequenceUtils {

    private SequenceUtils() {
    }

    /**
     * Subsamples the sequence.
     *
     * @param <T>
     *            type of object
     * @param sequence
     *            sequence
     * @param window
     *            window
     * @return the Subsamples
     */
    public static <T> List<List<T>> subsample(final List<T> sequence, final int window) {

        final List<List<T>> distributeSubsample = new ArrayList<List<T>>();

        for (int i = 0; i < window; i++) {
            distributeSubsample.add(new ArrayList<T>());
        }

        for (final T item : sequence) {

            for (int i = 0; i < window; i++) {

                final List<T> list = distributeSubsample.get(i);
                final boolean wasEmpty = list.isEmpty();

                distributeSubsample.get(i).add(item);

                if (wasEmpty) {
                    break;
                }
            }
        }

        final List<List<T>> listSubsample = new ArrayList<List<T>>();

        for (int i = 0; i < window; i++) {

            final List<T> list = distributeSubsample.get(i);
            final List<List<T>> partition = Lists.partition(list, window);

            for (final List<T> tuple : partition) {

                if (tuple.size() == window) {
                    listSubsample.add(tuple);
                }
            }
        }

        return listSubsample;
    }
}
