package org.leviatan.machinelearning.massivetextprocessing.engine.conceptrelationship;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.leviatan.machinelearning.massivetextprocessing.engine.Engine;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.MapObjectToDouble;

/**
 * ConceptRelationshipEngine.
 *
 * @author Alejandro
 *
 */
public class ConceptFrequencyEngine implements Engine<Set<String>> {

    /** NAME_MAIN_FILE. */
    public static final String NAME_MAIN_FILE = "__MAIN__";

    /** NAME_CONSIDERED_CONCEPTS_FILE. */
    public static final String NAME_CONSIDERED_CONCEPTS_FILE = "__CONSIDERED_CONCEPTS__";

    /** EXTENSION_OF_FILES. */
    public static final String EXTENSION_OF_FILES = "properties";

    private final List<String> listStopWords;
    private final int maxTupleSize;
    private final int freqMinToConsiderConcept;
    private final String nameFile;
    private final Function<Set<String>, Boolean> filterFoundConcepts;

    private MapObjectToDouble<String> mapConceptFrequency = new MapObjectToDouble<String>();

    /**
     * Constructor for ConceptFrequencyEngine.
     *
     * @param listStopWords
     *            listStopWords
     * @param maxTupleSize
     *            maxTupleSize
     * @param freqMinToConsiderConcept
     *            freqMinToConsiderConcept
     */
    public ConceptFrequencyEngine(final List<String> listStopWords, final int maxTupleSize,
            final int freqMinToConsiderConcept) {
        this(listStopWords, maxTupleSize, freqMinToConsiderConcept, NAME_MAIN_FILE, s -> true);
    }

    /**
     * Constructor for ConceptFrequencyEngine.
     *
     * @param listStopWords
     *            listStopWords
     * @param maxTupleSize
     *            maxTupleSize
     * @param freqMinToConsiderConcept
     *            freqMinToConsiderConcept
     * @param concept
     *            concept
     */
    public ConceptFrequencyEngine(final List<String> listStopWords, final int maxTupleSize,
            final int freqMinToConsiderConcept, final String concept) {
        this(listStopWords, maxTupleSize, freqMinToConsiderConcept, concept, s -> s.contains(concept));
    }

    /**
     * Constructor for ConceptFrequencyEngine.
     *
     * @param listStopWords
     *            listStopWords
     * @param maxTupleSize
     *            maxTupleSize
     * @param freqMinToConsiderConcept
     *            freqMinToConsiderConcept
     * @param nameFile
     *            nameFile
     * @param filterFoundConcepts
     *            filterFoundConcepts
     */
    public ConceptFrequencyEngine(final List<String> listStopWords, final int maxTupleSize,
            final int freqMinToConsiderConcept, final String nameFile,
            final Function<Set<String>, Boolean> filterFoundConcepts) {

        this.listStopWords = listStopWords;
        this.maxTupleSize = maxTupleSize;
        this.freqMinToConsiderConcept = freqMinToConsiderConcept;
        this.nameFile = nameFile;
        this.filterFoundConcepts = filterFoundConcepts;
    }

    @Override
    public void addText(final String text) {

        final Set<String> foundConcepts = ConceptRelationshipUtils.getConcepts(text, this.listStopWords,
                this.maxTupleSize);

        final boolean passesFilter = this.filterFoundConcepts.apply(foundConcepts);

        if (passesFilter) {
            updateMapConceptFrequency(foundConcepts);
        }
    }

    private void updateMapConceptFrequency(final Set<String> foundConcepts) {

        for (final String concept : foundConcepts) {
            this.mapConceptFrequency.addToValue(concept, 1.0);
        }
    }

    private File getFile(final String pathDirDump, final String fileName) {
        final String path = pathDirDump + "/" + fileName + "." + EXTENSION_OF_FILES;
        return new File(path);
    }

    @Override
    public Set<String> persistResultsAndGetOutPut(final String pathDirDump) {

        ConceptRelationshipUtils.dump(pathDirDump, this.nameFile + "." + EXTENSION_OF_FILES, this.mapConceptFrequency,
                -1);
        final Set<String> setPersistedConcepts = ConceptRelationshipUtils.dump(pathDirDump,
                NAME_CONSIDERED_CONCEPTS_FILE + "." + EXTENSION_OF_FILES, this.mapConceptFrequency,
                this.freqMinToConsiderConcept);

        return setPersistedConcepts;
    }

    @Override
    public boolean isProcessingDone(final String pathDirDump) {
        final File file = getFile(pathDirDump, this.nameFile);
        return file.exists();
    }

    @Override
    public Set<String> getOutPut(final String pathDirDump) throws Exception {

        final File fileConsidered = getFile(pathDirDump, NAME_CONSIDERED_CONCEPTS_FILE);

        if (fileConsidered.exists()) {
            final MapObjectToDouble<String> mapConsidered = ConceptRelationshipUtils.loadMapFromFile(fileConsidered);
            return mapConsidered.keySet();
        }

        final File fileAll = getFile(pathDirDump, this.nameFile);
        this.mapConceptFrequency = ConceptRelationshipUtils.loadMapFromFile(fileAll);
        final Set<String> setPersistedConcepts = ConceptRelationshipUtils.dump(pathDirDump,
                NAME_CONSIDERED_CONCEPTS_FILE + "." + EXTENSION_OF_FILES, this.mapConceptFrequency,
                this.freqMinToConsiderConcept);

        return setPersistedConcepts;
    }

}
