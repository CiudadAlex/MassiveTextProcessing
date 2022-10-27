package org.leviatan.machinelearning.massivetextprocessing.util.platform.crawl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.leviatan.machinelearning.massivetextprocessing.util.platform.ListsUtils;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.TextUtils;

/**
 * Matcher.
 *
 * @author acc
 *
 */
public final class TextMatcher {

    private static final String WILDCARD = "*";
    private static final String AND_SEPARATOR = "[+]";
    private static final String SPACE = " ";
    private static final String CHARACTERS_NOT_ALLOWED_IN_WORD_REGEX = "[^A-Za-z0-9ñÑçÇ\\-]";

    private TextMatcher() {
    }

    /**
     * Returns the list of conceptId that matches in the text.
     *
     * @param <C>
     *            concept object
     * @param <ID>
     *            id of the concept object
     * @param rawSourceContent
     *            rawSourceContent
     * @param listConcepts
     *            listConcepts
     * @param conceptBeanExtractor
     *            conceptBeanExtractor
     * @param tryCompact
     *            tryCompact
     * @return the list of conceptId that matches in the text
     */
    public static <C, ID> List<ID> matchConceptsOneByOneReturnConceptIdMatched(final String rawSourceContent,
            final List<? extends C> listConcepts, final Function<C, ConceptBean<ID>> conceptBeanExtractor,
            final boolean tryCompact) {

        final MatcheableDocument matcheableDocument = new MatcheableDocument(rawSourceContent, tryCompact);

        return getListOfConceptIdMatches(matcheableDocument, listConcepts, conceptBeanExtractor, tryCompact);
    }

    /**
     * Returns the list of conceptId that matches in the text.
     *
     * @param <C>
     *            concept object
     * @param <ID>
     *            id of the concept object
     * @param matcheableDocument
     *            matcheableDocument
     * @param listConcepts
     *            listConcepts
     * @param conceptBeanExtractor
     *            conceptBeanExtractor
     * @param tryCompact
     *            tryCompact
     * @return the list of conceptId that matches in the text
     */
    public static <C, ID> List<ID> getListOfConceptIdMatches(final MatcheableDocument matcheableDocument,
            final List<? extends C> listConcepts, final Function<C, ConceptBean<ID>> conceptBeanExtractor,
            final boolean tryCompact) {

        final List<ID> listMatch = new ArrayList<ID>();

        for (final C concept : listConcepts) {

            final ConceptBean<ID> conceptBean = conceptBeanExtractor.apply(concept);
            final ID conceptId = conceptBean.getId();
            final boolean matches = matches(matcheableDocument, concept, conceptBeanExtractor, tryCompact);

            if (matches) {
                listMatch.add(conceptId);
            }
        }

        return listMatch;
    }

    /**
     * Returns if the concept that matches in the text.
     *
     * @param <C>
     *            concept object
     * @param <ID>
     *            id of the concept object
     * @param matcheableDocument
     *            matcheableDocument
     * @param concept
     *            concept
     * @param conceptBeanExtractor
     *            conceptBeanExtractor
     * @param tryCompact
     *            tryCompact
     * @return Returns if the concept that matches in the text
     */
    public static <C, ID> boolean matches(final MatcheableDocument matcheableDocument, final C concept,
            final Function<C, ConceptBean<ID>> conceptBeanExtractor, final boolean tryCompact) {

        final ConceptBean<ID> conceptBean = conceptBeanExtractor.apply(concept);

        final boolean matchesConceptOrSynonyms = matchesConceptOrSynonyms(matcheableDocument, conceptBean, tryCompact);

        if (!matchesConceptOrSynonyms) {
            return false;
        }

        // There is provisional match. Let's take disambiguation into account

        final List<String> listDisambiguationIncluded = conceptBean.getListDisambiguationIncluded();
        final List<String> listDisambiguationExcluded = conceptBean.getListDisambiguationExcluded();

        return matchesDisambiguation(matcheableDocument, listDisambiguationIncluded, listDisambiguationExcluded,
                tryCompact);
    }

    private static boolean matchesDisambiguation(final MatcheableDocument matcheableDocument,
            final List<String> listDisambiguationIncluded, final List<String> listDisambiguationExcluded,
            final boolean tryCompact) {

        final boolean matchDisambiguationIncluded = matchesDisambiguation(matcheableDocument,
                listDisambiguationIncluded, true, tryCompact);
        final boolean matchDisambiguationExcluded = matchesDisambiguation(matcheableDocument,
                listDisambiguationExcluded, false, tryCompact);

        return matchDisambiguationIncluded && matchDisambiguationExcluded;
    }

    private static boolean matchesDisambiguation(final MatcheableDocument matcheableDocument,
            final List<String> listDisambiguation, final boolean included, final boolean tryCompact) {

        if (ListsUtils.isEmpty(listDisambiguation)) {
            return true;
        }

        for (final String conceptDisambiguation : listDisambiguation) {

            final boolean match = matches(matcheableDocument, conceptDisambiguation, tryCompact);

            if (match) {
                return included;
            }
        }

        return !included;
    }

    private static <ID> boolean matchesConceptOrSynonyms(final MatcheableDocument matcheableDocument,
            final ConceptBean<ID> conceptBean, final boolean tryCompact) {

        if (matches(matcheableDocument, conceptBean.getName(), tryCompact)) {
            return true;
        }

        for (final String mispell : conceptBean.getListMisspeltsAndSynonyms()) {

            if (matches(matcheableDocument, mispell, tryCompact)) {
                return true;
            }
        }

        return false;
    }

    private static boolean matches(final MatcheableDocument matcheableDocument, final String rawConcept,
            final boolean tryCompact) {

        final String normConcept = normText(rawConcept);

        if (normConcept.contains(AND_SEPARATOR)) {

            // AND functionality. Search for all the "[+]" separated words.
            // Matches if all are found
            final List<String> listSubConcepts = TextUtils.divideString(normConcept, AND_SEPARATOR);
            return areAllSubConceptsInText(matcheableDocument, listSubConcepts, tryCompact);

        } else if (normConcept.contains(SPACE)) {

            if (matcheableDocument.getNormSourceContent().contains(normConcept)) {
                return true;
            }

        } else {
            return matchesSingleWord(matcheableDocument, normConcept, tryCompact);
        }

        return false;
    }

    private static boolean areAllSubConceptsInText(final MatcheableDocument matcheableDocument,
            final List<String> listSubConcepts, final boolean tryCompact) {

        for (final String subConcepts : listSubConcepts) {

            final boolean matchesSubconcept = matches(matcheableDocument, subConcepts, tryCompact);

            if (!matchesSubconcept) {
                return false;
            }
        }

        return true;
    }

    private static boolean matchesSingleWord(final MatcheableDocument matcheableDocument, final String normConcept,
            final boolean tryCompact) {

        final List<String> wordsNormSourceContent = matcheableDocument.getWordsNormSourceContent();

        final WildcardPosition wildcardPosition = WildcardPosition.getWildcardPosition(normConcept, WILDCARD);
        final String cleanNormConcept = normConcept.replace(WILDCARD, "");

        for (final String wordInContent : wordsNormSourceContent) {

            final boolean matchesWord = matchesWordOrCompactVersionOfWord(matcheableDocument, wordInContent,
                    cleanNormConcept, wildcardPosition, tryCompact);

            if (matchesWord) {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesWordOrCompactVersionOfWord(final MatcheableDocument matcheableDocument,
            final String wordInContent, final String cleanNormConcept, final WildcardPosition wildcardPosition,
            final boolean tryCompact) {

        final boolean matchesWord = matchesWord(wordInContent, cleanNormConcept, wildcardPosition);

        if (matchesWord) {
            return true;
        }

        final Map<String, String> mapWordsNormSourceContentAndCompact = matcheableDocument
                .getMapWordsNormSourceContentAndCompact();

        if (tryCompact && mapWordsNormSourceContentAndCompact != null) {
            final String compactWordInContent = mapWordsNormSourceContentAndCompact.get(wordInContent);
            return matchesWord(compactWordInContent, cleanNormConcept, wildcardPosition);
        }

        return false;
    }

    private static boolean matchesWord(final String wordInContent, final String cleanNormConcept,
            final WildcardPosition wildcardPosition) {

        final boolean equals = WildcardPosition.NONE.equals(wildcardPosition) && wordInContent.equals(cleanNormConcept);
        final boolean ends = WildcardPosition.BEGIN.equals(wildcardPosition)
                && wordInContent.endsWith(cleanNormConcept);
        final boolean begins = WildcardPosition.END.equals(wildcardPosition)
                && wordInContent.startsWith(cleanNormConcept);
        final boolean contains = WildcardPosition.BOTH.equals(wildcardPosition)
                && wordInContent.contains(cleanNormConcept);

        return equals || ends || begins || contains;
    }

    /**
     * Splits the text in words.
     *
     * @param txt
     *            txt
     * @return the splited text
     */
    public static List<String> splitInWords(final String txt) {

        final String[] arrayStr = txt.split("[.,:<> \\t\\n\\x0B\\f\\r\\/]");

        final List<String> listWords = new ArrayList<String>();

        for (final String word : arrayStr) {

            final String wordClean = word.replaceAll(CHARACTERS_NOT_ALLOWED_IN_WORD_REGEX, "");

            if (!TextUtils.isBlank(wordClean)) {
                listWords.add(wordClean);
            }
        }

        return listWords;
    }

    /**
     * Normalizes the text so that it can be compared.
     *
     * @param txt
     *            txt
     * @return normalized text
     */
    public static String normText(final String txt) {

        if (txt == null) {
            return "";
        }

        final String normText = TextUtils.toLowerCaseAndNormVowelsAndTrim(txt);

        return normText;
    }

}
