package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.screen.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;

public class BlockPlacerBlockEntity extends DispenserBlockEntity {
	
	public BlockPlacerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BLOCK_PLACER, pos, state);
	}
	
	@Override
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.block_placer");
	}
	
	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return Spectrum3x3ContainerScreenHandler.createTier1(syncId, playerInventory, this);
	}
	
}
