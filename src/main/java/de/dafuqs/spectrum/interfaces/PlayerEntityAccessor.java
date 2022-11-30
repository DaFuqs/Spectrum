package de.dafuqs.spectrum.interfaces;

import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;

public interface PlayerEntityAccessor {
	
	void setSpectrumBobber(SpectrumFishingBobberEntity bobber);
	
	SpectrumFishingBobberEntity getSpectrumBobber();
	
}