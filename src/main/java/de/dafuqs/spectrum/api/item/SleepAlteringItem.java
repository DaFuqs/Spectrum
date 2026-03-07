package de.dafuqs.spectrum.api.item;

import net.minecraft.world.entity.player.*;

// TODO: migrate to component
public interface SleepAlteringItem {
	
	void applyPenalties(Player player);
}
