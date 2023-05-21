package de.dafuqs.spectrum.energy.storage;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CreativeInkStorage implements InkStorage {
	
	private static final Map<InkColor, Long> STORAGE = new HashMap<>() {{
		for (InkColor inkColor : InkColor.all()) {
			put(inkColor, Long.MAX_VALUE);
		}
	}};
	
	public CreativeInkStorage() {
		super();
	}
	
	public CreativeInkStorage(long maxEnergy, InkColor color, long amount) {
		super();
	}
	
	public static CreativeInkStorage fromNbt(@NotNull NbtCompound compound) {
		return new CreativeInkStorage();
	}
	
	@Override
	public boolean accepts(InkColor color) {
		return true;
	}
	
	@Override
	public long addEnergy(InkColor color, long amount) {
		return 0;
	}
	
	@Override
	public long drainEnergy(InkColor color, long requestedAmount) {
		return requestedAmount;
	}
	
	@Override
	public boolean requestEnergy(InkColor color, long requestedAmount) {
		return true;
	}
	
	@Override
	public long getEnergy(InkColor color) {
		return Long.MAX_VALUE;
	}
	
	@Override
	public Map<InkColor, Long> getEnergy() {
		return STORAGE;
	}
	
	@Override
	public void setEnergy(Map<InkColor, Long> colors, long total) {
	}
	
	@Override
	public long getMaxPerColor() {
		return Long.MAX_VALUE;
	}
	
	@Override
	public long getMaxTotal() {
		return Long.MAX_VALUE;
	}
	
	@Override
	public long getCurrentTotal() {
		return Long.MAX_VALUE;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean isFull() {
		return true;
	}
	
	@Override
	public void fillCompletely() {
	}
	
	@Override
	public void clear() {
	}
	
	@Override
	public void addTooltip(List<Text> tooltip, boolean includeHeader) {
		if (includeHeader) {
			tooltip.add(Text.translatable("item.spectrum.creative_ink_assortment.tooltip"));
		}
	}
	
	@Override
	public long getRoom(InkColor color) {
		return Long.MAX_VALUE;
	}
	
}