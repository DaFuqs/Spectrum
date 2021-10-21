package de.dafuqs.spectrum.events;

import net.minecraft.util.DyeColor;
import net.minecraft.world.event.GameEvent;

public class RedstoneTransferGameEvent extends GameEvent {

	private DyeColor dyeColor;
	private int power;

	public RedstoneTransferGameEvent(String id, int range, DyeColor dyeColor, int power) {
		super(id, range);
		this.dyeColor = dyeColor;
		this.power = power;
	}

	public DyeColor getDyeColor() {
		return dyeColor;
	}

	public int getPower() {
		return power;
	}

}
