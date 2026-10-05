package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.blocks.ink.gen.*;
import de.dafuqs.spectrum.inventories.slots.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.world.entity.player.*;

import java.util.*;

public class ColorPickerScreenHandler extends BaseInkScreenHandler {
	
	// clientside
	public ColorPickerScreenHandler(int syncId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
		super(SpectrumMenuTypes.COLOR_PICKER, syncId, playerInventory, ScreenOpeningData.STREAM_CODEC.decode(buf), 2);
	}
	
	// serverside
	public ColorPickerScreenHandler(int syncId, Inventory playerInventory, BaseInkBlockEntity<?> blockEntity, Optional<Holder<InkColor>> selectedColor) {
		super(SpectrumMenuTypes.COLOR_PICKER, syncId, playerInventory, blockEntity, selectedColor, 2);
	}
	
	@Override
	public void addBlockEntitySlots() {
		this.addSlot(new ColorPickerInputSlot(blockEntity, ColorPickerBlockEntity.INPUT_SLOT_ID, 26, 33));
		this.addSlot(new InkStorageSlot(blockEntity, ColorPickerBlockEntity.INK_SLOT_ID, 133, 33));
	}
	
}
