package de.dafuqs.spectrum.api.entity;

import de.dafuqs.spectrum.entity.entity.*;

public interface PlayerEntityAccessor {
	
	void setSpectrumBobber(SpectrumFishingBobberEntity bobber);
	
	SpectrumFishingBobberEntity getSpectrumBobber();
	
}