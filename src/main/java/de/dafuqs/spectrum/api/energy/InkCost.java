package de.dafuqs.spectrum.api.energy;

import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.nbt.*;

import java.util.*;

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
		nbt.putString("InkColor", color.getID().toString());
		nbt.putLong("InkCost", cost);
	}
	
	public static InkCost fromNbt(NbtCompound nbt) {
		Optional<InkColor> inkColor = InkColor.ofIdString(nbt.getString("InkColor"));
		long inkCost = nbt.getLong("InkCost");
		return new InkCost(inkColor.orElse(InkColors.CYAN), inkCost);
	}
	
}