package org.leviatan.machinelearning.massivetextprocessing.engine.conceptrelationship;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.leviatan.machinelearning.massivetextprocessing.util.platform.KeyDoubleBean;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.ListsUtils;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.MapObjectToDouble;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.SequenceUtils;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.TextUtils;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.crawl.TextMatcher;

/**
 * ConceptRelationshipUtils.
 *
 * @author Alejandro
 *
 */
public final class ConceptRelationshipUtils {

    private ConceptRelationshipUtils() {
    }

    /**
     * Returns the found concepts.
     *
     * @param text
     *            text
     * @param listStopWords
     *            listStopWords
     * @param maxTupleSize
     *            maxTupleSize
     * @return the found concepts
     */
    public static Set<String> getConcepts(final String text, final List<String> listStopWords, final int maxTupleSize) {

        final Set<String> foundConcepts = new HashSet<String>();

        final List<String> listPhrases = TextUtils.splitInPhrases(text);

        for (final String phrase : listPhrases) {
            addPhrase(phrase, foundConcepts, listStopWords, maxTupleSize);
        }

        return foundConcepts;
    }

    private static void addPhrase(final String phrase, final Set<String> foundConcepts,
            final List<String> listStopWords, final int maxTupleSize) {

        final String normPhrase = TextMatcher.normText(phrase);
        final List<String> wordsNormPhrase = TextMatcher.splitInWords(normPhrase);
        final List<String> wordsNormPhraseClean = cleanListOfWords(wordsNormPhrase, listStopWords);

        for (final String concept : wordsNormPhraseClean) {
            foundConcepts.add(concept);
        }

        for (int tupleSize = 2; tupleSize <= maxTupleSize; tupleSize++) {
            processTuplesToOutput(wordsNormPhraseClean, tupleSize, foundConcepts);
        }
    }

    private static List<String> cleanListOfWords(final List<String> wordsNormPhrase, final List<String> listStopWords) {

        final List<String> wordsNormPhraseClean = ListsUtils.filterList(wordsNormPhrase,
                w -> !listStopWords.contains(w) && !isNumber(w));

        return wordsNormPhraseClean;
    }

    private static boolean isNumber(final String txt) {

        try {
            Double.parseDouble(txt);
            return true;

        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static void processTuplesToOutput(final List<String> wordsNormPhraseClean, final int tupleSize,
            final Set<String> foundConcepts) {

        final List<List<String>> listSubsample = SequenceUtils.subsample(wordsNormPhraseClean, tupleSize);

        for (final List<String> tuple : listSubsample) {

            final String concept = TextUtils.concatList(tuple, " ");
            foundConcepts.add(concept);
        }
    }

    /**
     * Dumps the map.
     *
     * @param pathDirDump
     *            pathDirDump
     * @param filename
     *            filename
     * @param mapConceptFreq
     *            mapConceptFreq
     * @param minFreqAllowed
     *            minFreqAllowed
     * @return setPersistedConcepts
     */
    public static Set<String> dump(final String pathDirDump, final String filename,
            final MapObjectToDouble<String> mapConceptFreq, final int minFreqAllowed) {

        final Set<String> setPersistedConcepts = new HashSet<String>();
        final List<KeyDoubleBean<String>> listKeyDoubleBean = mapConceptFreq.getListOrdered(false);

        BufferedWriter writer = null;

        try {
            final File file = new File(pathDirDump + "/" + filename);
            writer = new BufferedWriter(new FileWriter(file));

            for (final KeyDoubleBean<String> kd : listKeyDoubleBean) {

                final String concept = kd.getKey();
                final long freq = kd.getD().longValue();

                if (freq < minFreqAllowed) {
                    break;
                }

                setPersistedConcepts.add(concept);

                writer.write(concept.replace(" ", "_"));
                writer.write("=");
                writer.write("" + freq);
                writer.write("\r\n");
            }

        } catch (final Exception e) {
            e.printStackTrace();

        } finally {

            try {

                if (writer != null) {
                    writer.close();
                }

            } catch (final Exception e) {
                // Nothing to do
            }
        }

        return setPersistedConcepts;
    }

    /**
     * Loads map from file.
     *
     * @param file
     *            file
     * @return MapObjectToDouble
     * @throws Exception
     */
    public static MapObjectToDouble<String> loadMapFromFile(final File file) throws Exception {

        final MapObjectToDouble<String> mapConceptFrequency = new MapObjectToDouble<String>();

        final Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        for (final Entry<Object, Object> entry : properties.entrySet()) {

            final String concept = entry.getKey().toString().replace("_", " ");

            final Double freq = Double.parseDouble(entry.getValue().toString());
            mapConceptFrequency.addToValue(concept, freq);
        }

        return mapConceptFrequency;
    }
}
