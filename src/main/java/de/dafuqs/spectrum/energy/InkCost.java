package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.color.*;
import net.minecraft.nbt.*;

public class InkCost {
	
	private final InkColor color;
	private final long cost;
	
	public InkCost(InkColor color, long cost) {
		this.color = color;
		this.cost = cost;
	}
	
	public InkColor getColor() {
		return color;
	}
	
	public long getCost() {
		return cost;
	}
	
	public void writeNbt(NbtCompound nbt) {
		nbt.putString("InkColor", color.toString());
		nbt.putLong("InkCost", cost);
	}
	
	public static InkCost fromNbt(NbtCompound nbt) {
		InkColor inkColor = InkColor.of(nbt.getString("InkColor"));
		long inkCost = nbt.getLong("InkCost");
		return new InkCost(inkColor, inkCost);
	}
	
}