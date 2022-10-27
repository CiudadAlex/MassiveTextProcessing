package org.leviatan.machinelearning.massivetextprocessing.engine.conceptrelationship;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.leviatan.machinelearning.massivetextprocessing.util.platform.KeyDoubleBean;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.ListsUtils;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.MapObjectToDouble;

/**
 * ConceptRelationshipResultBrowser.
 *
 * @author Alejandro
 *
 */
public final class ConceptRelationshipResultBrowser {

    private static final String CMD_EXIT = "exit";
    private static final String CMD_BROWSE_MAIN = "bm";
    private static final String CMD_BROWSE_CONCEPT = "bc";
    private static final String CMD_BROWSE_CONCEPT_RELATIVE = "br";
    private static final String CMD_HISTOGRAM_MAIN = "hm";
    private static final String CMD_HISTOGRAM_CONCEPT = "hc";

    private static final int LINES_TO_SHOW = 500;

    private final String pathDirDump;
    private MapObjectToDouble<String> mapMainConceptFreq;

    private MapObjectToDouble<String> getMapMainConceptFreq() throws Exception {

        if (this.mapMainConceptFreq == null) {
            final File file = getFile(ConceptFrequencyEngine.NAME_MAIN_FILE);
            this.mapMainConceptFreq = ConceptRelationshipUtils.loadMapFromFile(file);
        }

        return this.mapMainConceptFreq;
    }

    /**
     * Constructor for ConceptRelationshipResultBrowser.
     *
     * @param pathDirDump
     *            pathDirDump
     */
    public ConceptRelationshipResultBrowser(final String pathDirDump) {
        this.pathDirDump = pathDirDump;
    }

    /** Executes the browsing. */
    public void executeBrowsing() {

        final Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            processCommand(sc.nextLine().toLowerCase());
        }

        sc.close();
    }

    private void processCommand(final String command) {

        if (CMD_EXIT.equals(command)) {
            System.exit(0);

        } else if (CMD_BROWSE_MAIN.equals(command)) {
            browseMain();

        } else if (command.startsWith(CMD_BROWSE_CONCEPT + " ")) {
            browseConcept(command);

        } else if (command.startsWith(CMD_BROWSE_CONCEPT_RELATIVE + " ")) {
            browseConceptRelative(command);

        } else if (command.equals(CMD_HISTOGRAM_MAIN)) {
            printHistogramMain();

        } else if (command.startsWith(CMD_HISTOGRAM_CONCEPT + " ")) {
            printHistogramConcept(command);

        } else {
            printHelp();
        }
    }

    private void browseMain() {

        final File file = getFile(ConceptFrequencyEngine.NAME_MAIN_FILE);
        browseFile(file);
    }

    private void browseConcept(final String command) {

        final String concept = command.substring((CMD_BROWSE_CONCEPT + " ").length());
        final File file = getFile(concept);

        if (file.exists()) {
            browseFile(file);

        } else {
            System.out.println("No file for concept: '" + concept + "'");
        }
    }

    private File getFile(final String filename) {

        final String pathFile = this.pathDirDump + "/" + filename + "." + ConceptRelationshipEngine.EXTENSION_OF_FILES;
        final File file = new File(pathFile);

        return file;
    }

    private void browseFile(final File file) {

        String thisLine = null;
        BufferedReader br = null;

        try {
            int count = 0;
            br = new BufferedReader(new FileReader(file));

            while ((thisLine = br.readLine()) != null) {

                System.out.println(thisLine);
                count++;

                if (count >= LINES_TO_SHOW) {
                    break;
                }
            }

        } catch (final Exception e) {
            e.printStackTrace();

        } finally {

            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    // Do nothing
                }
            }
        }
    }

    private void browseConceptRelative(final String command) {

        final String concept = command.substring((CMD_BROWSE_CONCEPT + " ").length());
        final File file = getFile(concept);

        if (!file.exists()) {
            System.out.println("No file for concept: '" + concept + "'");
            return;
        }

        try {

            final MapObjectToDouble<String> mapMain = getMapMainConceptFreq();

            final MapObjectToDouble<String> mapConcept = ConceptRelationshipUtils.loadMapFromFile(file);

            for (final String subconcept : mapConcept.keySet()) {
                final double factor = 1.0 / mapMain.get(subconcept);
                mapConcept.multiplyValue(subconcept, factor);
            }

            final List<KeyDoubleBean<String>> listConceptDouble = mapConcept.getListOrdered(false);

            int count = 0;

            for (final KeyDoubleBean<String> kd : listConceptDouble) {
                System.out.println(kd.getKey() + "=" + kd.getD());

                if (count > LINES_TO_SHOW) {
                    break;
                }
                count++;
            }

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void printHistogramMain() {
        try {
            printHistogram(getMapMainConceptFreq());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void printHistogramConcept(final String command) {

        try {

            final String concept = command.substring((CMD_HISTOGRAM_CONCEPT + " ").length());
            final File file = getFile(concept);
            final MapObjectToDouble<String> mapConcept = ConceptRelationshipUtils.loadMapFromFile(file);
            printHistogram(mapConcept);

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void printHistogram(final MapObjectToDouble<String> mapConceptFreq) {

        final MapObjectToDouble<Double> mapFreqToNumberConcepts = new MapObjectToDouble<Double>();

        for (final String concept : mapConceptFreq.keySet()) {
            final Double freq = mapConceptFreq.get(concept);
            mapFreqToNumberConcepts.addToValue(freq, 1.0);
        }

        final List<KeyDoubleBean<Double>> listEntry = new ArrayList<KeyDoubleBean<Double>>();

        for (final Double freq : mapFreqToNumberConcepts.keySet()) {
            listEntry.add(new KeyDoubleBean<Double>(freq, mapFreqToNumberConcepts.get(freq)));
        }

        ListsUtils.orderListDouble(listEntry, kdb -> kdb.getKey(), false);

        for (final KeyDoubleBean<Double> kdb : listEntry) {
            System.out.println(kdb.getKey() + ": " + kdb.getD());
        }
    }

    private void printHelp() {

        System.out.println("Command summary:\n");
        System.out.println(CMD_EXIT + " >> Exits the program");
        System.out.println(CMD_BROWSE_MAIN + " >> Browses the main concept statistics");
        System.out.println(CMD_BROWSE_CONCEPT + " $concept >> Browses the concept $concept");
        System.out.println(
                CMD_BROWSE_CONCEPT_RELATIVE + " $concept >> Browses the concept $concept relative to main frequency");
        System.out.println(CMD_HISTOGRAM_MAIN + " >> Histograms the main concept statistics");
        System.out.println(CMD_HISTOGRAM_CONCEPT + " $concept >> Histograms the concept $concept");

    }
}
