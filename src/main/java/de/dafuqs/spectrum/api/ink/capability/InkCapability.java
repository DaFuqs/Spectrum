package de.dafuqs.spectrum.api.ink.capability;

import de.dafuqs.spectrum.api.ink.storage.*;

public interface InkCapability {
	InkStorage getStorage();
	
	void markDirty();
}
