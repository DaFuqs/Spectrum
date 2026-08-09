package de.dafuqs.spectrum.compat.REI.widgets;

import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.impl.client.gui.widget.*;

import java.util.*;
import java.util.function.*;

public class OffsetEntryWidget extends EntryWidget {
	
	List<EntryIngredient> inputs;
	private final Supplier<Integer> indexer;
	int prevIndex = -1;
	
	public OffsetEntryWidget(Point point, List<EntryIngredient> inputs, Supplier<Integer> indexer) {
		super(point);
		this.inputs = inputs;
		this.indexer = indexer;
		entries(inputs.getFirst());
	}
	
	@Override
	public Slot clearEntries() {
		return this;
	}
	
	@Override
	public EntryWidget entry(EntryStack<?> stack) {
		return this;
	}
	
	@Override
	public EntryStack<?> getCurrentEntry() {
		int i = indexer.get();
		if(i != prevIndex) {
			entries(inputs.get(i));
		}
		return getCyclingEntries().get().get(i);
	}
}
