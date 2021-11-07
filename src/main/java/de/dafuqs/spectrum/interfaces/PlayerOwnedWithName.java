package de.dafuqs.spectrum.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

public interface PlayerOwnedWithName extends PlayerOwned {
	
	public abstract String getOwnerName();
	
}
