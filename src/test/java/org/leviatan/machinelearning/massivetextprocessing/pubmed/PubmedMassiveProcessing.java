package org.leviatan.machinelearning.massivetextprocessing.pubmed;

import java.util.List;
import java.util.Set;

import org.leviatan.machinelearning.massivetextprocessing.engine.Engine;
import org.leviatan.machinelearning.massivetextprocessing.engine.conceptrelationship.ConceptFrequencyEngine;
import org.leviatan.machinelearning.massivetextprocessing.engine.conceptrelationship.ConceptRelationshipEngine;
import org.leviatan.machinelearning.massivetextprocessing.engine.conceptrelationship.ConceptRelationshipResultBrowser;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.machinelearning.StopWords;

/**
 * PubmedMassiveProcessing.
 *
 * @author Alejandro
 *
 */
public final class PubmedMassiveProcessing {

    private static final String PATH_DATASET = "C:/Alex/Dev/data_corpus/pubmed";
    private static final String PATH_DIR_DUMP = "C:/Alex/Dev/data_corpus/pubmed_processed";

    private PubmedMassiveProcessing() {
    }

    /**
     * Main method.
     *
     * @param args
     *            args
     */
    public static void main(final String[] args) throws Exception {

        final String pathDataset = PATH_DATASET;
        final List<String> listStopWords = StopWords.getEnglish();
        final int maxTupleSize = 1;
        final int freqMin = 2000;
        final String pathDirDump = PATH_DIR_DUMP;

        final String concreteConcept = "telomerase";

        final Set<String> setInterestingConcepts = freq(pathDataset, listStopWords, maxTupleSize, freqMin, pathDirDump);

        //freqSingle(pathDataset, listStopWords, maxTupleSize, freqMin, pathDirDump, concreteConcept);

        relations(pathDataset, listStopWords, maxTupleSize, pathDirDump, setInterestingConcepts);

        // browse(pathDirDump);
    }

    protected static Set<String> freq(final String pathDataset, final List<String> listStopWords,
            final int maxTupleSize, final int freqMin, final String pathDirDump) throws Exception {

        System.out.println("Executing freq");

        final Engine<Set<String>> engine1 = new ConceptFrequencyEngine(listStopWords, maxTupleSize, freqMin);
        final Set<String> setInterestingConcepts = PubmedEngineExecutor.runEngine(pathDataset, engine1, pathDirDump,
                true);

        return setInterestingConcepts;
    }

    private static Set<String> freqSingle(final String pathDataset, final List<String> listStopWords,
                                          final int maxTupleSize, final int freqMin, final String pathDirDump, final String concreteConcept)
                    throws Exception {

        System.out.println("Executing freq single >> " + concreteConcept);

        final Engine<Set<String>> engine1 = new ConceptFrequencyEngine(listStopWords, maxTupleSize, freqMin,
                concreteConcept);
        final Set<String> setInterestingConcepts = PubmedEngineExecutor.runEngine(pathDataset, engine1, pathDirDump,
                true);

        return setInterestingConcepts;
    }

    private static void relations(final String pathDataset, final List<String> listStopWords, final int maxTupleSize,
                                  final String pathDirDump, final Set<String> setInterestingConcepts) throws Exception {

        System.out.println("Executing relations");

        final Engine<Void> engine2 = new ConceptRelationshipEngine(listStopWords, maxTupleSize, setInterestingConcepts);
        PubmedEngineExecutor.runEngine(pathDataset, engine2, pathDirDump, true);
    }

    private static void browse(final String pathDirDump) {

        System.out.println("Executing browse");

        final ConceptRelationshipResultBrowser conceptRelationshipResultBrowser = new ConceptRelationshipResultBrowser(
                pathDirDump);
        conceptRelationshipResultBrowser.executeBrowsing();
    }

}
