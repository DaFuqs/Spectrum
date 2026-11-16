package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.sink.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;

import java.util.*;

public class TintingStationScreenHandler extends BaseInkScreenHandler {
	
	// clientside
	public TintingStationScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		super(SpectrumMenuTypes.TINTING_STATION, syncId, playerInventory, ScreenOpeningData.STREAM_CODEC.decode(buf), 2);
	}
	
	// serverside
	public TintingStationScreenHandler(int syncId, Inventory playerInventory, TintingStationBlockEntity blockEntity, Optional<Holder<InkColor>> selectedColor) {
		super(SpectrumMenuTypes.TINTING_STATION, syncId, playerInventory, blockEntity, selectedColor, 2);
	}
	
	@Override
	public void addBlockEntitySlots() {
		this.addSlot(new Slot(blockEntity, TintingStationBlockEntity.INPUT_SLOT_ID, 40, 33));
		this.addSlot(new InkStorageSlot(blockEntity, TintingStationBlockEntity.OUTPUT_SLOT_ID, 102, 33));
	}
	
}
