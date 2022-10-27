package org.leviatan.machinelearning.massivetextprocessing.util.platform.crawl;

/**
 * WildcardPosition.
 *
 * @author acc
 *
 */
public enum WildcardPosition {

    /** BEGIN. */
    BEGIN,

    /** END. */
    END,

    /** BOTH. */
    BOTH,

    /** NONE. */
    NONE;

    /**
     * Returns the WildcardPosition.
     *
     * @param txt
     *            txt
     * @param wildcard
     *            wildcard
     * @return WildcardPosition
     */
    public static WildcardPosition getWildcardPosition(final String txt, final String wildcard) {

        if (txt.startsWith(wildcard) && txt.endsWith(wildcard)) {
            return BOTH;
        } else if (txt.startsWith(wildcard)) {
            return BEGIN;
        } else if (txt.endsWith(wildcard)) {
            return END;
        } else {
            return NONE;
        }
    }
}
