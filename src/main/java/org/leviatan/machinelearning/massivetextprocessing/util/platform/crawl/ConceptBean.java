package org.leviatan.machinelearning.massivetextprocessing.util.platform.crawl;

import java.util.List;

/**
 * ConceptBean.
 *
 * @author acc
 *
 */
public class ConceptBean<ID> {

    private final ID id;
    private final String name;
    private final List<String> listMisspeltsAndSynonyms;

    private final List<String> listDisambiguationIncluded;
    private final List<String> listDisambiguationExcluded;

    /**
     * Constructor for ConceptBean.
     *
     * @param id
     *            id
     * @param name
     *            name
     * @param listMisspeltsAndSynonyms
     *            listMisspeltsAndSynonyms
     * @param listDisambiguationIncluded
     *            listDisambiguationIncluded
     * @param listDisambiguationExcluded
     *            listDisambiguationExcluded
     */
    public ConceptBean(final ID id, final String name, final List<String> listMisspeltsAndSynonyms,
            final List<String> listDisambiguationIncluded, final List<String> listDisambiguationExcluded) {

        this.id = id;
        this.name = name;
        this.listMisspeltsAndSynonyms = listMisspeltsAndSynonyms;
        this.listDisambiguationIncluded = listDisambiguationIncluded;
        this.listDisambiguationExcluded = listDisambiguationExcluded;
    }

    public ID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getListMisspeltsAndSynonyms() {
        return this.listMisspeltsAndSynonyms;
    }

    public List<String> getListDisambiguationIncluded() {
        return this.listDisambiguationIncluded;
    }

    public List<String> getListDisambiguationExcluded() {
        return this.listDisambiguationExcluded;
    }

}
