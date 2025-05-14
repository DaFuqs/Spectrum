package de.dafuqs.spectrum.blocks;

import net.minecraft.world.inventory.*;

public class CraftingDelegate implements ContainerData {
	
	public int craftingTime;
	public int craftingTimeTotal;
	
	@Override
	public int get(int index) {
		return switch (index) {
			case 0 -> craftingTime;
			case 1 -> craftingTimeTotal;
			default -> 0;
		};
	}
	
	@Override
	public void set(int index, int value) {
		switch (index) {
			case 0 -> craftingTime = value;
			case 1 -> craftingTimeTotal = value;
		}
	}
	
	@Override
	public int getCount() {
		return 2;
	}
	
}
