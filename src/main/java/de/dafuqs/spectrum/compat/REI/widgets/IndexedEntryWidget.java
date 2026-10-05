package de.dafuqs.spectrum.compat.REI.widgets;

import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.impl.client.gui.widget.*;

import java.util.function.*;

public class IndexedEntryWidget extends EntryWidget {
	
	private final Supplier<Integer> indexer;
	
	public IndexedEntryWidget(Point point, Supplier<Integer> indexer) {
		super(point);
		this.indexer = indexer;
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
		return getCyclingEntries().get().get(indexer.get());
	}
}
