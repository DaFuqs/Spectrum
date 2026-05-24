package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;

import java.util.*;

public class TintingStationScreenHandler extends InkTransferScreenHandler {
	
	// clientside
	public TintingStationScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		super(SpectrumScreenHandlerTypes.TINTING_STATION, syncId, playerInventory, ScreenOpeningData.PACKET_CODEC.decode(buf));
	}
	
	// serverside
	public TintingStationScreenHandler(int syncId, Inventory playerInventory, BaseInkTransferBlockEntity<?> blockEntity, Optional<Holder<InkColor>> selectedColor) {
		super(SpectrumScreenHandlerTypes.TINTING_STATION, syncId, playerInventory, blockEntity, selectedColor);
	}
	
	@Override
	public void addBlockEntitySlots() {
		this.addSlot(new Slot(blockEntity, 0, 26, 33));
		this.addSlot(new InkStorageSlot(blockEntity, 1, 133, 33));
	}
	
}
