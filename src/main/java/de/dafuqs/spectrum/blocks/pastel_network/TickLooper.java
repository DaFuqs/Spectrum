package de.dafuqs.spectrum.blocks.pastel_network;

import net.minecraft.nbt.NbtCompound;

public final class TickLooper {
	
	private int currentTick;
	private int maxTick;
	
	public TickLooper(int maxTick) {
		this.maxTick = maxTick;
	}
	
	public void tick() {
		currentTick++;
	}
	
	public boolean reachedCap() {
		return currentTick >= maxTick;
	}
	
	public boolean checkCap() {
		boolean cap = this.reachedCap();
		if (cap) {
			this.reset();
		}
		return cap;
	}
	
	public void reset() {
		currentTick = 0;
	}
	
	public int getTick() {
		return currentTick;
	}
	
	public int getMaxTick() {
		return maxTick;
	}
	
	public float getProgress() {
		return (float) currentTick / (float) maxTick;
	}
	
	@Override
	public String toString() {
		return "TickLooper (" + currentTick + "/" + maxTick + ")";
	}
	
	public void readNbt(NbtCompound nbt) {
		maxTick = nbt.getInt("max");
		currentTick = nbt.getInt("current");
	}
	
	public void writeNbt(NbtCompound nbt) {
		nbt.putInt("max", maxTick);
		nbt.putInt("current", currentTick);
	}
	
}