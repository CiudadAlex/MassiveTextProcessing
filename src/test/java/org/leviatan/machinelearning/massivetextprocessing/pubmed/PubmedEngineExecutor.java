package org.leviatan.machinelearning.massivetextprocessing.pubmed;

import java.io.File;

import org.leviatan.machinelearning.massivetextprocessing.engine.Engine;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.TimeProgressLogger;
import org.leviatan.machinelearning.massivetextprocessing.util.xml.XMLExtractor;

/**
 * PubmedEngineExecutor.
 *
 * @author Alejandro
 *
 */
public final class PubmedEngineExecutor {

    private PubmedEngineExecutor() {
    }

    /**
     * Runs an engine over Pubmed.
     *
     * @param <T>
     *            type of output
     * @param pathDataset
     *            pathDataset
     * @param engine
     *            engine
     * @param pathDirDump
     *            pathDirDump
     * @param trace
     *            trace
     * @return the engine output
     * @throws Exception
     */
    public static <T> T runEngine(final String pathDataset, final Engine<T> engine, final String pathDirDump,
            final boolean trace) throws Exception {

        if (engine.isProcessingDone(pathDirDump)) {
            // Shortcut
            return engine.getOutPut(pathDirDump);
        }

        final File dirDataset = new File(pathDataset);

        final File[] arrayFiles = dirDataset.listFiles();

        final TimeProgressLogger progress = new TimeProgressLogger(arrayFiles.length);

        int count = 0;

        for (final File file : arrayFiles) {

            final XMLExtractor xmlExtractor = new XMLExtractor(file, "PubmedArticle", "ArticleTitle", "AbstractText");

            while (true) {

                final String articleText = xmlExtractor.nextCompositionOfItemProperties(".\n\n");

                if (articleText == null) {
                    break;
                }

                engine.addText(articleText);

                if (trace && count % 100 == 0) {
                    System.out.println("Articles Processed: " + count);
                }

                count++;
            }

            xmlExtractor.close();
            progress.stepFinishedAndPrintProgress();
        }

        System.out.println("Persisting results...");
        return engine.persistResultsAndGetOutPut(pathDirDump);
    }
}
