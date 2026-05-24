package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.energy.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.player.*;

import java.util.*;

public class ColorPickerScreenHandler extends InkTransferScreenHandler {
	
	// clientside
	public ColorPickerScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		super(SpectrumScreenHandlerTypes.COLOR_PICKER, syncId, playerInventory, ScreenOpeningData.PACKET_CODEC.decode(buf));
	}
	
	// serverside
	public ColorPickerScreenHandler(int syncId, Inventory playerInventory, BaseInkTransferBlockEntity<?> blockEntity, Optional<Holder<InkColor>> selectedColor) {
		super(SpectrumScreenHandlerTypes.COLOR_PICKER, syncId, playerInventory, blockEntity, selectedColor);
	}
	
	@Override
	public void addBlockEntitySlots() {
		this.addSlot(new ColorPickerInputSlot(blockEntity, 0, 26, 33));
		this.addSlot(new InkStorageSlot(blockEntity, 1, 133, 33));
	}
	
}
