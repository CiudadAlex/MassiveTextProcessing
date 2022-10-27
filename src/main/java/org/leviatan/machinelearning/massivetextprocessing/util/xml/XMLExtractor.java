package org.leviatan.machinelearning.massivetextprocessing.util.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.leviatan.machinelearning.massivetextprocessing.util.platform.ListsUtils;
import org.leviatan.machinelearning.massivetextprocessing.util.platform.TextUtils;

/**
 * XMLExtractor.
 *
 * @author Alejandro
 *
 */
public class XMLExtractor {

    private final XMLTagPair itemTagPair;

    private final List<XMLTagPair> listPropertiesOfItem;
    private final BufferedReader br;

    /**
     * Constructor for XMLExtractor.
     *
     * @param file
     *            file
     * @param tagItem
     *            tagItem
     * @param arrayPropertiesOfItem
     *            arrayPropertiesOfItem
     * @throws Exception
     */
    public XMLExtractor(final File file, final String tagItem, final String... arrayPropertiesOfItem) throws Exception {

        this.itemTagPair = new XMLTagPair(tagItem);

        this.listPropertiesOfItem = ListsUtils.transformList(ListsUtils.buildList(arrayPropertiesOfItem),
                prop -> new XMLTagPair(prop));

        this.br = new BufferedReader(new FileReader(file));
    }

    /**
     * Returns the next composition of item properties.
     *
     * @param separator
     *            separator
     * @return the next composition of item properties
     */
    public String nextCompositionOfItemProperties(final String separator) throws IOException {

        final StringBuilder sb = new StringBuilder();

        final String untilNextBeginning = nextTextUntilTag(this.itemTagPair.getBegin());

        if (untilNextBeginning == null) {
            return null;
        }

        final String item = nextTextUntilTag(this.itemTagPair.getEnd());

        if (item == null) {
            return null;
        }

        int count = 0;

        for (final XMLTagPair propertyTagPair : this.listPropertiesOfItem) {

            final String propertyValue = TextUtils.getTextBetweenTags(item, propertyTagPair.getBegin(),
                    propertyTagPair.getEnd());

            if (propertyValue != null) {

                if (count != 0) {
                    sb.append(separator);
                }

                sb.append(propertyValue);
                count++;
            }
        }

        return sb.toString().trim();
    }

    private String nextTextUntilTag(final String tag) throws IOException {

        final StringBuilder sb = new StringBuilder();
        final int tagLenght = tag.length();
        int chint = this.br.read();

        int indexTag = 0;

        while (chint != -1) {

            final char ch = (char) chint;
            sb.append(ch);

            if (ch == tag.charAt(indexTag)) {
                indexTag++;

                if (indexTag >= tagLenght) {
                    return sb.toString();
                }

            } else {
                indexTag = 0;
            }

            chint = this.br.read();
        }

        return null;
    }

    /**
     * Closes the reader.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        this.br.close();
    }

}
