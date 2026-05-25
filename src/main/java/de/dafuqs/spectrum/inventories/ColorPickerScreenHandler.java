package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.player.*;

import java.util.*;

public class ColorPickerScreenHandler extends InkStorageScreenHandler {
	
	// clientside
	public ColorPickerScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		super(SpectrumMenuTypes.COLOR_PICKER, syncId, playerInventory, ScreenOpeningData.STREAM_CODEC.decode(buf), 2);
	}
	
	// serverside
	public ColorPickerScreenHandler(int syncId, Inventory playerInventory, ColorPickerBlockEntity blockEntity, Optional<Holder<InkColor>> selectedColor) {
		super(SpectrumMenuTypes.COLOR_PICKER, syncId, playerInventory, blockEntity, selectedColor, 2);
	}
	
	@Override
	public void addBlockEntitySlots() {
		this.addSlot(new ColorPickerInputSlot(blockEntity, 0, 26, 33));
		this.addSlot(new InkStorageSlot(blockEntity, 1, 133, 33));
	}
	
}
