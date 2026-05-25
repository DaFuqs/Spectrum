package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.ink.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;

import javax.annotation.*;
import java.util.*;

public abstract class InkStorageWithColorSelectionScreenHandler extends InkStorageScreenHandler implements InkColorSelectedPacketReceiver {
	
	public record ScreenOpeningData(BlockPos pos, Optional<Holder<InkColor>> inkColor) {
		public static final StreamCodec<RegistryFriendlyByteBuf, ScreenOpeningData> STREAM_CODEC = StreamCodec.composite(
				BlockPos.STREAM_CODEC, ScreenOpeningData::pos,
				ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(SpectrumRegistryKeys.INK_COLOR)), ScreenOpeningData::inkColor,
				ScreenOpeningData::new
		);
	}
	
	// clientside
	protected InkStorageWithColorSelectionScreenHandler(@Nullable MenuType<?> menuType, int syncId, Inventory playerInventory, ScreenOpeningData screenOpeningData) {
		this(menuType, syncId, playerInventory, (BaseInkTransferBlockEntity<?>) playerInventory.player.level().getBlockEntity(screenOpeningData.pos()), screenOpeningData.inkColor());
	}
	
	// serverside
	protected InkStorageWithColorSelectionScreenHandler(@Nullable MenuType<?> menuType, int syncId, Inventory playerInventory, BaseInkTransferBlockEntity<?> blockEntity, Optional<Holder<InkColor>> selectedColor) {
		super(menuType, syncId, playerInventory, blockEntity, 2);
		this.blockEntity.setSelectedColor(selectedColor);
	}
	
	@Override
	public void onInkColorSelectedPacket(Optional<Holder<InkColor>> inkColor) {
		this.blockEntity.setSelectedColor(inkColor);
	}
	
}
