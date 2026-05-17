package de.dafuqs.spectrum.api.entity;

import de.dafuqs.spectrum.entity.entity.*;
import org.jspecify.annotations.*;

public interface PlayerEntityAccessor {
	
	void setSpectrumBobber(@Nullable SpectrumFishingBobberEntity bobber);
	
	@Nullable SpectrumFishingBobberEntity getSpectrumBobber();

	void setSleepTimer(int ticks);
}