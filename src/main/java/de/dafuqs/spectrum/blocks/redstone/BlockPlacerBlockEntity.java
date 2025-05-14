package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class BlockPlacerBlockEntity extends DispenserBlockEntity {
	
	public BlockPlacerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BLOCK_PLACER, pos, state);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.block_placer");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return Spectrum3x3ContainerScreenHandler.createTier1(syncId, playerInventory, this);
	}
	
}
