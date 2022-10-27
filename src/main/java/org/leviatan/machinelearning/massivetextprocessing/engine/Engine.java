package org.leviatan.machinelearning.massivetextprocessing.engine;

/**
 * Engine.
 *
 * @author Alejandro
 *
 */
public interface Engine<T> {

    /**
     * Adds a text.
     *
     * @param text
     *            text
     */
    public void addText(final String text);

    /**
     * Persists the results.
     *
     * @param pathDirDump
     *            pathDirDump
     * @return output
     */
    public T persistResultsAndGetOutPut(final String pathDirDump);

    /**
     * Returns if the processing is done.
     *
     * @param pathDirDump
     *            pathDirDump
     * @return output
     */
    public boolean isProcessingDone(final String pathDirDump);

    /**
     * Gets the results.
     *
     * @param pathDirDump
     *            pathDirDump
     * @return output
     */
    public T getOutPut(final String pathDirDump) throws Exception;
}
