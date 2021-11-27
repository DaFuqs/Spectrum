package de.dafuqs.spectrum.compat.REI.info;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import net.minecraft.screen.ScreenHandler;

public class PedestalGridMenuInfo<PedestalScreenHandler, PedestalCraftingRecipeDisplay> implements SimpleGridMenuInfo {

	private final PedestalCraftingRecipeDisplay display;
	
	public PedestalGridMenuInfo(PedestalCraftingRecipeDisplay display) {
		this.display = display;
	}
	
	@Override
	public int getCraftingResultSlotIndex(ScreenHandler menu) {
		return -1;
	}
	
	@Override
	public int getCraftingWidth(ScreenHandler menu) {
		return 3;
	}
	
	@Override
	public int getCraftingHeight(ScreenHandler menu) {
		return 3;
	}
	
	@Override
	public PedestalCraftingRecipeDisplay getDisplay() {
		return display;
	}
	
}
