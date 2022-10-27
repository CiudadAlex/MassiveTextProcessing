package org.leviatan.machinelearning.massivetextprocessing.util.platform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * TextUtils.
 *
 * @author alciucam
 *
 */
public final class TextUtils {

    private static final String CHARACTER_NUMERIC = "0123456789";
    private static final String CHARACTER_UPPER_ALPHABETIC = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÇ";

    private static final Map<String, String> MAP_LOW_TEXT_VOWELS_REPLACEMENTS = buildMapLowTextVowelsReplacements();

    private TextUtils() {
    }

    /**
     * Makes upercase que first letter.
     *
     * @param txt
     *            txt
     * @return a string with upercase que first letter
     */
    public static String upercaseFirstLetter(final String txt) {

        if (txt.isEmpty()) {
            return txt;
        }

        if (txt.length() == 1) {
            return txt.toUpperCase();
        }

        return txt.substring(0, 1).toUpperCase() + txt.substring(1);
    }

    /**
     * Returns if the String is blank.
     *
     * @param txt
     *            txt
     * @return if the String is blank
     */
    public static boolean isBlank(final String txt) {

        if (txt == null) {
            return true;
        }

        if ("".equals(txt.trim())) {
            return true;
        }

        return false;
    }

    /**
     * Retuns the string or null if the string is blank.
     *
     * @param txt
     *            txt
     * @return the string or null if the string is blank
     */
    public static String getStringNullIfBlank(final String txt) {

        if (isBlank(txt)) {
            return null;

        } else {
            return txt;
        }
    }

    /**
     * Trunks the text.
     *
     * @param txt
     *            txt
     * @param maxLength
     *            maxLength
     * @return trunked text
     */
    public static String trunk(final String txt, final int maxLength) {

        if (txt == null) {
            return txt;
        }

        if (txt.length() > maxLength) {
            return txt.substring(0, maxLength);
        }

        return txt;
    }

    /**
     * Transforms to a HEX String.
     *
     * @param arrayBytes
     *            arrayBytes
     * @return HEX String
     */
    public static String toHex(final byte[] arrayBytes) {

        final StringBuilder sb = new StringBuilder();

        int count = 1;

        for (final byte b : arrayBytes) {

            sb.append(String.format("%02X ", b));

            if (count % 50 == 0) {
                sb.append("\n");
            }

            count++;
        }

        return sb.toString();
    }

    /**
     * Concatenates the list of strings using the separator to separate them.
     *
     * @param listString
     *            listString
     * @param separator
     *            separator
     *
     * @return concatenation
     */
    public static String concatList(final List<String> listString, final String separator) {
        return concatList(listString, separator, u -> u);
    }

    /**
     * Concatenates the list of objects into a string using the separator to
     * separate them.
     *
     * @param <T>
     *            type of object
     * @param listObject
     *            listObject
     * @param separator
     *            separator
     * @param getString
     *            getString
     * @return concatenation
     */
    public static <T> String concatList(final List<T> listObject, final String separator,
            final Function<T, String> getString) {

        final StringBuilder sb = new StringBuilder();

        boolean firstIteration = true;

        for (final T obj : listObject) {

            if (!firstIteration) {
                sb.append(separator);
            }

            final String term = getString.apply(obj);
            sb.append(term);

            firstIteration = false;
        }

        return sb.toString();
    }

    /**
     * Returns the text between the tags.
     *
     * @param text
     *            text
     * @param beginTag
     *            beginTag
     * @param endTag
     *            endTag
     * @return the text between the tags
     */
    public static String getTextBetweenTags(final String text, final String beginTag, final String endTag) {

        final int beginTagIndex = text.indexOf(beginTag);
        final int postBeginTagIndex = beginTagIndex + beginTag.length();

        final int endTagIndex = text.indexOf(endTag, postBeginTagIndex);

        if (beginTagIndex == -1 || endTagIndex == -1) {
            return null;
        }

        return text.substring(postBeginTagIndex, endTagIndex);
    }

    /**
     * Returns the text between the tags.
     *
     * @param text
     *            text
     * @param beginTag
     *            beginTag
     * @param endTag
     *            endTag
     * @return the text between the tags
     */
    public static List<String> getListTextBetweenTags(final String text, final String beginTag, final String endTag) {

        final List<String> list = new ArrayList<String>();

        String restOfText = text;
        String item = getTextBetweenTags(restOfText, beginTag, endTag);

        while (item != null) {

            list.add(item);

            final int index = restOfText.indexOf(endTag) + endTag.length();
            restOfText = restOfText.substring(index);
            item = getTextBetweenTags(restOfText, beginTag, endTag);
        }

        return list;
    }

    /**
     * Convert InputStream to String.
     *
     * @param is
     *            InputStream
     * @return String
     * @throws IOException
     */
    public static String toString(final InputStream is) throws IOException {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while (is.available() > 0) {
            baos.write(is.read());
        }

        return new String(baos.toByteArray());
    }

    /**
     * Trunks the String starting from the delimiterToBegin.
     *
     * @param delimiterToBegin
     *            delimiterToBegin
     * @param txt
     *            txt
     * @param includeDelimeter
     *            includeDelimeter
     * @return the trunke String
     */
    public static String trunkInitialPart(final String delimiterToBegin, final String txt,
            final boolean includeDelimeter) {

        if (delimiterToBegin == null) {
            return txt;
        }

        final int index = txt.indexOf(delimiterToBegin);

        if (index == -1) {
            return txt;
        }

        final int extraIndexSum = includeDelimeter ? 0 : delimiterToBegin.length();

        return txt.substring(index + extraIndexSum);
    }

    /**
     * Returns true if the text ends with any of the given sufixes.
     *
     * @param txt
     *            txt
     * @param arrayPossilbleSufixes
     *            arrayPossilbleSufixes
     * @return if the text ends with any of the given sufixes
     */
    public static boolean endsWithList(final String txt, final String... arrayPossilbleSufixes) {

        for (final String suffix : arrayPossilbleSufixes) {

            if (txt.endsWith(suffix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if the text starts with any of the given prefixes.
     *
     * @param txt
     *            txt
     * @param arrayPossilblePrefixes
     *            arrayPossilblePrefixes
     * @return if the text starts with any of the given prefixes
     */
    public static boolean startsWithList(final String txt, final String... arrayPossilblePrefixes) {

        for (final String prefix : arrayPossilblePrefixes) {

            if (txt.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the baseURL.
     *
     * @param url
     *            url
     * @return the baseURL
     */
    public static String getBaseURL(final String url) {

        final String doubleSlash = "//";
        final int indexOfDoubleSlash = url.indexOf(doubleSlash);

        if (indexOfDoubleSlash == -1) {
            return null;
        }

        final int indexOfPostDoubleSlash = indexOfDoubleSlash + doubleSlash.length();

        final String singleSlash = "/";
        final int indexOfSlash = url.indexOf(singleSlash, indexOfPostDoubleSlash);

        if (indexOfSlash == -1) {
            // If no single slash the url is a base url
            return url;
        }

        return url.substring(0, indexOfSlash);
    }

    /**
     * Returns the Extended baseURL.
     *
     * @param url
     *            url
     * @return the Extended baseURL
     */
    public static String getExtendedBaseURL(final String url) {

        final String singleSlash = "/";
        final String doubleSlash = "//";
        final int indexOfDoubleSlash = url.indexOf(doubleSlash);

        if (indexOfDoubleSlash == -1) {
            return url;
        }

        final String urlFromDoubleSlash = url.substring(indexOfDoubleSlash + doubleSlash.length());

        if (!urlFromDoubleSlash.contains(singleSlash)) {
            return url;
        }

        final int indexOfLastSlash = url.lastIndexOf(singleSlash);

        if (indexOfLastSlash == -1) {
            // If no single slash the url is a base url
            return url;
        }

        return url.substring(0, indexOfLastSlash);
    }

    /**
     * Returns the host.
     *
     * @param url
     *            url
     * @return the host
     */
    public static String getURLHost(final String url) {

        final String doubleSlash = "//";
        final int indexOfDoubleSlash = url.indexOf(doubleSlash);

        if (indexOfDoubleSlash == -1) {
            return null;
        }

        final int indexOfPostDoubleSlash = indexOfDoubleSlash + doubleSlash.length();

        final String singleSlash = "/";
        final int indexOfSlash = url.indexOf(singleSlash, indexOfPostDoubleSlash);

        if (indexOfSlash == -1) {
            return null;
        }

        return url.substring(indexOfPostDoubleSlash, indexOfSlash);
    }

    /**
     * Slugs the text.
     *
     * @param text
     *            text
     * @return slugged text
     */
    public static String slugText(final String text) {

        if (text == null) {
            return null;
        }

        return text.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }

    /**
     * UpperSlugs the text.
     *
     * @param text
     *            text
     * @return upperslugged text
     */
    public static String upperSlugText(final String text) {

        if (text == null) {
            return null;
        }

        return text.toUpperCase().replaceAll("[^A-Z0-9]+", "_");
    }

    /**
     * Returns a trimmed String.
     *
     * @param text
     *            text
     * @return a trimmed String
     */
    public static String safeTrim(final String text) {

        if (text == null) {
            return null;
        }

        return text.trim();
    }

    /**
     * Returns if the text contains any of the given strings.
     *
     * @param text
     *            text
     * @param arrayPossiblyContained
     *            arrayPossiblyContained
     * @return if the text contains any of the given strings
     */
    public static boolean containsAny(final String text, final String... arrayPossiblyContained) {

        if (text == null) {
            return false;
        }

        for (final String possiblyContained : arrayPossiblyContained) {
            if (text.contains(possiblyContained)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Divides the String by the delimeter.
     *
     * @param text
     *            text
     * @param delimeter
     *            delimeter
     * @return the diviced String
     */
    public static List<String> divideString(final String text, final String delimeter) {

        final List<String> listString = new ArrayList<String>();

        String restOfText = text;
        int indexOfSeparator = restOfText.indexOf(delimeter);

        while (indexOfSeparator != -1) {

            final String token = restOfText.substring(0, indexOfSeparator);
            listString.add(token);

            restOfText = restOfText.substring(indexOfSeparator + delimeter.length(), restOfText.length());
            indexOfSeparator = restOfText.indexOf(delimeter);
        }

        listString.add(restOfText);

        return listString;
    }

    /**
     * Returns the final part of the text from the last delimeter.
     *
     * @param text
     *            text
     * @param delimeter
     *            delimeter
     * @return the final part of the text from the last delimeter
     */
    public static String getFinalPartFromLastDelimeter(final String text, final String delimeter) {

        final int index = text.lastIndexOf(delimeter);

        if (index >= 0) {
            return text.substring(index + delimeter.length());
        }

        return text;
    }

    /**
     * Finishes the text with ellipsis points without breaking a word.
     *
     * @param text
     *            text
     * @return the text with ellipsis points without breaking a word
     */
    public static String finishWithEllipsisPoints(final String text) {

        final int lastIndexSpace = text.lastIndexOf(" ");
        return text.substring(0, lastIndexSpace) + "...";
    }

    /**
     * Returns if the character is alphabetic.
     *
     * @param character
     *            character
     * @return if the character is alphabetic
     */
    public static boolean isCharacterAlphabetic(final String character) {
        return CHARACTER_UPPER_ALPHABETIC.contains(character.toUpperCase());
    }

    /**
     * Returns if the character is numeric.
     *
     * @param character
     *            character
     * @return if the character is numeric
     */
    public static boolean isCharacterNumeric(final String character) {
        return CHARACTER_NUMERIC.contains(character);
    }

    /**
     * Returns if the character is alphanumeric.
     *
     * @param character
     *            character
     * @return if the character is alphanumeric
     */
    public static boolean isCharacterAlphaNumeric(final String character) {
        return isCharacterAlphabetic(character) || isCharacterNumeric(character);
    }

    /**
     * Splits the text in phrases.
     *
     * @param txt
     *            txt
     * @return list of phrases
     */
    public static List<String> splitInPhrases(final String txt) {

        final String[] arrayPhrases = txt.split("[.][^\\d]");

        final List<String> listPhrases = ListsUtils.buildList(arrayPhrases);

        return ListsUtils.filterList(listPhrases, s -> !isBlank(s));
    }

    /**
     * Replaces if null.
     *
     * @param txt
     *            txt
     * @param replacementIfNull
     *            replacementIfNull
     * @return replaced if null
     */
    public static String replaceIfNull(final String txt, final String replacementIfNull) {
        return txt != null ? txt : replacementIfNull;
    }

    /**
     * Replaces if null.
     *
     * @param txt
     *            txt
     * @return replaced if null
     */
    public static String replaceIfNull(final String txt) {
        return replaceIfNull(txt, "-");
    }

    /**
     * To String.
     *
     * @param number
     *            number
     * @param replacementIfNull
     *            replacementIfNull
     * @return String representing the number
     */
    public static String toString(final Number number, final String replacementIfNull) {
        return number != null ? number.toString() : replacementIfNull;
    }

    /**
     * Replaces all occurrences of keys of the given map in the given string
     * with the associated value in that map.
     *
     * This method is semantically the same as calling
     * {@link String#replace(CharSequence, CharSequence)} for each of the
     * entries in the map, but may be significantly faster for many replacements
     * performed on a short string, since
     * {@link String#replace(CharSequence, CharSequence)} uses regular
     * expressions internally and results in many String object allocations when
     * applied iteratively.
     *
     * The order in which replacements are applied depends on the order of the
     * map's entry set.
     *
     * @param string
     *            string
     * @param replacements
     *            replacements
     * @return replaced string
     */
    public static String replaceFromMap(final String string, final Map<String, String> replacements) {

        final StringBuilder sb = new StringBuilder(string);

        for (final Entry<String, String> entry : replacements.entrySet()) {

            final String key = entry.getKey();
            final String value = entry.getValue();

            int start = sb.indexOf(key, 0);

            while (start > -1) {
                final int end = start + key.length();
                final int nextSearchStart = start + value.length();
                sb.replace(start, end, value);
                start = sb.indexOf(key, nextSearchStart);
            }
        }

        return sb.toString();
    }

    private static Map<String, String> buildMapLowTextVowelsReplacements() {

        final Map<String, String> mapReplacements = new HashMap<String, String>();

        mapReplacements.put("�", "a");
        mapReplacements.put("�", "e");
        mapReplacements.put("�", "i");
        mapReplacements.put("�", "o");
        mapReplacements.put("�", "u");

        mapReplacements.put("�", "a");
        mapReplacements.put("�", "e");
        mapReplacements.put("�", "i");
        mapReplacements.put("�", "o");
        mapReplacements.put("�", "u");

        mapReplacements.put("�", "a");
        mapReplacements.put("�", "e");
        mapReplacements.put("�", "i");
        mapReplacements.put("�", "o");
        mapReplacements.put("�", "u");

        mapReplacements.put("�", "a");
        mapReplacements.put("�", "e");
        mapReplacements.put("�", "i");
        mapReplacements.put("�", "o");
        mapReplacements.put("�", "u");

        return mapReplacements;
    }

    /**
     * Lowercases the string and replaces modified vowels by normal vowels and
     * trims.
     *
     * @param txt
     *            txt
     * @return processed text
     */
    public static String toLowerCaseAndNormVowelsAndTrim(final String txt) {

        if (txt == null) {
            return "";
        }

        String normText = txt.trim().toLowerCase();
        normText = replaceFromMap(normText, MAP_LOW_TEXT_VOWELS_REPLACEMENTS);

        return normText;
    }
}
