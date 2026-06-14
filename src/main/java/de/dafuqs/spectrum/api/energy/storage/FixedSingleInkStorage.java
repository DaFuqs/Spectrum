package de.dafuqs.spectrum.api.energy.storage;

import de.dafuqs.spectrum.api.energy.color.*;

public class FixedSingleInkStorage extends SingleInkStorage {
	
	public FixedSingleInkStorage(long maxEnergy, InkColor color) {
		super(maxEnergy);
		this.storedColor = color;
	}
	
	public FixedSingleInkStorage(long maxEnergy, InkColor color, long amount) {
		super(maxEnergy, color, amount);
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return this.storedColor == color;
	}

}