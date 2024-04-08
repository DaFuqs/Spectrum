package de.dafuqs.spectrum.helpers;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;

/**
 * Zoink. Fuck you mojang.
 */
public class WeightedPool<E extends Weighted> {
    private final int totalWeight;
    private final ImmutableList<E> entries;

    public WeightedPool(List<? extends E> entries) {
        this.entries = ImmutableList.copyOf(entries);
        this.totalWeight = Weighting.getWeightSum(entries);
    }

    public static <E extends Weighted> WeightedPool<E> empty() {
        return new WeightedPool<>(ImmutableList.of());
    }

    @SafeVarargs
    public static <E extends Weighted> WeightedPool<E> of(E... entries) {
        return new WeightedPool<>(ImmutableList.copyOf(entries));
    }

    public static <E extends Weighted> WeightedPool<E> of(List<E> entries) {
        return new WeightedPool<>(entries);
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public Optional<E> getOrEmpty(Random random) {
        if (this.totalWeight == 0) {
            return Optional.empty();
        } else {
            int i = random.nextInt(this.totalWeight);
            return Weighting.getAt(this.entries, i);
        }
    }

    public List<E> getEntries() {
        return this.entries;
    }

    public static <E extends Weighted> Codec<WeightedPool<E>> createCodec(Codec<E> entryCodec) {
        return entryCodec.listOf().xmap(WeightedPool::of, WeightedPool::getEntries);
    }
}

