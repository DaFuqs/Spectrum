package de.dafuqs.spectrum.api.entity;

import de.dafuqs.spectrum.entity.entity.*;

public interface PlayerEntityAccessor {
	
	void spectrum$setSpectrumBobber(SpectrumFishingBobberEntity bobber);
	
	SpectrumFishingBobberEntity spectrum$getSpectrumBobber();
	
	void spectrum$setSleepTimer(int ticks);
}