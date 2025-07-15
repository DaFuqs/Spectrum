package de.dafuqs.spectrum.helpers;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import net.minecraft.util.*;
import net.minecraft.util.random.*;

import java.util.*;

/**
 * Zoink. Fuck you mojang.
 */
public class WeightedPool<E extends WeightedEntry> {
	private final int totalWeight;
	private final ImmutableList<E> entries;
	
	public WeightedPool(List<? extends E> entries) {
		this.entries = ImmutableList.copyOf(entries);
		this.totalWeight = WeightedRandom.getTotalWeight(entries);
	}
	
	public static <E extends WeightedEntry> WeightedPool<E> empty() {
		return new WeightedPool<>(ImmutableList.of());
	}
	
	@SafeVarargs
	public static <E extends WeightedEntry> WeightedPool<E> of(E... entries) {
		return new WeightedPool<>(ImmutableList.copyOf(entries));
	}
	
	public static <E extends WeightedEntry> WeightedPool<E> of(List<E> entries) {
		return new WeightedPool<>(entries);
	}
	
	public boolean isEmpty() {
		return this.entries.isEmpty();
	}
	
	public Optional<E> getOrEmpty(RandomSource random) {
		if (this.totalWeight == 0) {
			return Optional.empty();
		} else {
			int i = random.nextInt(this.totalWeight);
			return WeightedRandom.getWeightedItem(this.entries, i);
		}
	}
	
	public List<E> getEntries() {
		return this.entries;
	}
	
	public static <E extends WeightedEntry> Codec<WeightedPool<E>> createCodec(Codec<E> entryCodec) {
		return entryCodec.listOf().xmap(WeightedPool::of, WeightedPool::getEntries);
	}
}

