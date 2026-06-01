package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;

import java.util.*;

public class InkStorageScreenHandler extends BaseInkScreenHandler {
	
	// clientside
	public InkStorageScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		super(SpectrumMenuTypes.INK_STORAGE, syncId, playerInventory, ScreenOpeningData.STREAM_CODEC.decode(buf), 2);
	}
	
	// serverside
	public InkStorageScreenHandler(int syncId, Inventory playerInventory, TintingStationBlockEntity blockEntity, Optional<Holder<InkColor>> selectedColor) {
		super(SpectrumMenuTypes.INK_STORAGE, syncId, playerInventory, blockEntity, selectedColor, 2);
	}
	
	@Override
	public void addBlockEntitySlots() {
		this.addSlot(new InkStorageSlot(blockEntity, 1, 133, 33));
	}
	
}
