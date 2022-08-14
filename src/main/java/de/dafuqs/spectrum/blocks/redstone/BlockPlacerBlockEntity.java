package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.inventories.Spectrum3x3ContainerScreenHandler;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class BlockPlacerBlockEntity extends DispenserBlockEntity {
	
	public BlockPlacerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BLOCK_PLACER, pos, state);
	}
	
	protected Text getContainerName() {
		return Text.translatable("block.spectrum.block_placer");
	}
	
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return Spectrum3x3ContainerScreenHandler.createTier1(syncId, playerInventory, this);
	}
	
}
