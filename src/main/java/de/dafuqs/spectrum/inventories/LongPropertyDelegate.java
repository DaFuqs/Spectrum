package de.dafuqs.spectrum.inventories;

public interface LongPropertyDelegate {
    long get(int index);

    void set(int index, long value);

    int size();
}
