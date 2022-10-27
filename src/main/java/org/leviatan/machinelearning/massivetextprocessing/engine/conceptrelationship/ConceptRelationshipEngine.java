package org.leviatan.machinelearning.massivetextprocessing.engine.conceptrelationship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.leviatan.machinelearning.massivetextprocessing.engine.Engine;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.ListsUtils;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.MapObjectToDouble;

/**
 * ConceptRelationshipEngine.
 *
 * @author Alejandro
 *
 */
public class ConceptRelationshipEngine implements Engine<Void> {

    /** EXTENSION_OF_FILES. */
    public static final String EXTENSION_OF_FILES = "properties";

    private final List<String> listStopWords;
    private final int maxTupleSize;
    private final Set<String> listConsideredConcepts;

    private final Map<String, MapObjectToDouble<String>> mapConceptCoocurrence = new HashMap<String, MapObjectToDouble<String>>();

    private final Map<String, String> mapConceptSecondOptimization = new HashMap<String, String>();

    /**
     * Constructor for ConceptRelationshipEngine.
     *
     * @param listStopWords
     *            listStopWords
     * @param maxTupleSize
     *            maxTupleSize
     * @param listConsideredConcepts
     *            listConsideredConcepts
     */
    public ConceptRelationshipEngine(final List<String> listStopWords, final int maxTupleSize,
            final Set<String> listConsideredConcepts) {
        this.listStopWords = listStopWords;
        this.maxTupleSize = maxTupleSize;
        this.listConsideredConcepts = listConsideredConcepts;

        updateMapConceptSecondOptimization();
    }

    private void updateMapConceptSecondOptimization() {

        for (final String concept : this.listConsideredConcepts) {
            this.mapConceptSecondOptimization.put(concept, concept);
        }
    }

    @Override
    public void addText(final String text) {

        final Set<String> foundConcepts = ConceptRelationshipUtils.getConcepts(text, this.listStopWords,
                this.maxTupleSize);

        final List<String> foundAndConsideredConcepts = ListsUtils.filterList(ListsUtils.buildList(foundConcepts),
                c -> this.listConsideredConcepts.contains(c));

        updateMapConceptCoocurrence(foundAndConsideredConcepts);
    }

    private void updateMapConceptCoocurrence(final List<String> foundAndConsideredConcepts) {

        for (final String conceptMain : foundAndConsideredConcepts) {

            for (final String conceptSecond : foundAndConsideredConcepts) {

                if (!conceptMain.equals(conceptSecond)) {

                    MapObjectToDouble<String> submap = this.mapConceptCoocurrence.get(conceptMain);

                    if (submap == null) {
                        submap = new MapObjectToDouble<String>();
                        this.mapConceptCoocurrence.put(conceptMain, submap);
                    }

                    final String conceptSecondUnique = this.mapConceptSecondOptimization.get(conceptSecond);
                    submap.addToValue(conceptSecondUnique, 1.0);
                }
            }
        }
    }

    @Override
    public Void persistResultsAndGetOutPut(final String pathDirDump) {

        final Set<String> setConcepts = this.mapConceptCoocurrence.keySet();

        for (final String concept : setConcepts) {
            ConceptRelationshipUtils.dump(pathDirDump, concept + "." + EXTENSION_OF_FILES,
                    this.mapConceptCoocurrence.get(concept), -1);
        }

        return null;
    }

    @Override
    public boolean isProcessingDone(final String pathDirDump) {
        return false;
    }

    @Override
    public Void getOutPut(final String pathDirDump) throws Exception {
        return null;
    }

}
