package de.dafuqs.spectrum.compat.REI.info;

import de.dafuqs.spectrum.compat.REI.PedestalCraftingRecipeDisplay;
import de.dafuqs.spectrum.inventories.PedestalScreenHandler;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;

public class PedestalGridMenuInfo<T extends PedestalScreenHandler, D extends PedestalCraftingRecipeDisplay> implements SimpleGridMenuInfo<T, D> {

	private final D display;
	
	public PedestalGridMenuInfo(Display display) {
		this.display = (D) display;
	}
	
	@Override
	public int getCraftingResultSlotIndex(T menu) {
		return -1;
	}
	
	@Override
	public int getCraftingWidth(T menu) {
		return 3;
	}
	
	@Override
	public int getCraftingHeight(T menu) {
		return 3;
	}
	
	@Override
	public D getDisplay() {
		return display;
	}
	

}
