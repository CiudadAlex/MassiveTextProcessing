package org.leviatan.machinelearning.massivetextprocessing.util.platform;

/**
 * Function2Arg.
 *
 * @author acc
 *
 */
@FunctionalInterface
public interface Function2Arg<A1, A2, R> {

    /**
     * Function of 2 arguments.
     *
     * @param arg1
     *            arg1
     * @param arg2
     *            arg2
     * @return return
     */
    public R apply(A1 arg1, A2 arg2);
}
