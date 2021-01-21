package de.dafuqs.spectrum.recipe.util;

import net.minecraft.tag.Tag;

import java.util.Collections;
import java.util.List;

public class SimpleTag<T> implements Tag<T> {

	private final List<T> entries;

	public SimpleTag(List<T> entries) {
		this.entries = entries;
	}

	@Override
	public boolean contains(T entry) {
		return entries.contains(entry);
	}

	@Override
	public List<T> values() {
		return Collections.unmodifiableList(entries);
	}
}