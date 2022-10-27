package org.leviatan.machinelearning.massivetextprocessing.util.platform.crawl;

/**
 * LowerLetterRepetitionCompactor.
 *
 * @author acc
 *
 */
public final class LowerLetterRepetitionCompactor {

    private static final String[] SINGLE_LETTERS = { "a", "b", "d", "f", "g", "h", "i", "j", "k", "m", "ñ", "p", "q",
            "u", "v", "w", "x", "y" };

    private static final String[] DOUBLE_LETTERS = { "c", "e", "l", "n", "o", "r", "s", "t", "z" };

    private LowerLetterRepetitionCompactor() {
    }

    /**
     * Compacts the text.
     *
     * @param text
     *            text
     * @return compactedText
     */
    public static String compact(final String text) {

        String compactedText = text;

        for (final String letterSingle : SINGLE_LETTERS) {
            compactedText = compactSingleLetter(compactedText, letterSingle);
        }

        for (final String letterDouble : DOUBLE_LETTERS) {
            compactedText = compactDoubleLetter(compactedText, letterDouble);
        }

        return compactedText;
    }

    private static String compactSingleLetter(final String text, final String letterSingle) {

        String compactedText = text;

        final String letterSingleX2 = letterSingle + letterSingle;

        boolean containsRepetition = compactedText.contains(letterSingleX2);

        while (containsRepetition) {

            compactedText = compactedText.replace(letterSingleX2, letterSingle);
            containsRepetition = compactedText.contains(letterSingleX2);
        }

        return compactedText;
    }

    private static String compactDoubleLetter(final String text, final String letterDouble) {

        String compactedText = text;

        final String letterDoubleX3 = letterDouble + letterDouble + letterDouble;
        final String letterDoubleX4 = letterDoubleX3 + letterDouble;

        boolean containsRepetitionX4 = compactedText.contains(letterDoubleX4);

        while (containsRepetitionX4) {

            compactedText = compactedText.replace(letterDoubleX4, letterDoubleX3);
            containsRepetitionX4 = compactedText.contains(letterDoubleX4);
        }

        compactedText = compactedText.replace(letterDoubleX3, letterDouble);

        return compactedText;
    }
}
