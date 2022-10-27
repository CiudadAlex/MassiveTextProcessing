package org.leviatan.machinelearning.massivetextprocessing.util.xml;

/**
 * XMLTagPair.
 *
 * @author Alejandro
 *
 */
public class XMLTagPair {

    private final String begin;
    private final String end;

    /**
     * Constructor for XMLTagPair.
     *
     * @param tag
     *            tag
     */
    public XMLTagPair(final String tag) {

        this.begin = "<" + tag + ">";
        this.end = "</" + tag + ">";
    }

    public String getBegin() {
        return this.begin;
    }

    public String getEnd() {
        return this.end;
    }

}
