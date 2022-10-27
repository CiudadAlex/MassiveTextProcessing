package org.leviatan.machinelearning.massivetextprocessing.util.platform;

/**
 * KeyDoubleBean.
 *
 * @author acc
 *
 */
public class KeyDoubleBean<K> {

    private final K key;
    private final Double d;

    /**
     * Constructor for KeyDoubleBean.
     *
     * @param key
     *            key
     * @param d
     *            d
     */
    public KeyDoubleBean(final K key, final Double d) {
        this.key = key;
        this.d = d;
    }

    public K getKey() {
        return this.key;
    }

    public Double getD() {
        return this.d;
    }

}
