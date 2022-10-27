package org.leviatan.machinelearning.massivetextprocessing.util.platform.crawl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MatcheableDocument.
 *
 * @author acc
 *
 */
public class MatcheableDocument {

    private final String normSourceContent;
    private final List<String> wordsNormSourceContent;
    private final Map<String, String> mapWordsNormSourceContentAndCompact;

    /**
     * Constructor for MatcheableDocument.
     *
     * @param rawSourceContent
     *            rawSourceContent
     * @param tryCompact
     *            tryCompact
     */
    public MatcheableDocument(final String rawSourceContent, final boolean tryCompact) {

        this.normSourceContent = TextMatcher.normText(rawSourceContent);
        this.wordsNormSourceContent = TextMatcher.splitInWords(this.normSourceContent);

        if (tryCompact) {

            this.mapWordsNormSourceContentAndCompact = new HashMap<String, String>();

            for (final String wordNorm : this.wordsNormSourceContent) {

                final String compactWordInContent = LowerLetterRepetitionCompactor.compact(wordNorm);
                this.mapWordsNormSourceContentAndCompact.put(wordNorm, compactWordInContent);
            }

        } else {
            this.mapWordsNormSourceContentAndCompact = null;
        }

    }

    public String getNormSourceContent() {
        return this.normSourceContent;
    }

    public List<String> getWordsNormSourceContent() {
        return this.wordsNormSourceContent;
    }

    public Map<String, String> getMapWordsNormSourceContentAndCompact() {
        return this.mapWordsNormSourceContentAndCompact;
    }

}
