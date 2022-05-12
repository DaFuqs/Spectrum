package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class AmethystChestBlockEntity extends TreasureChestBlockEntity {
	
	public AmethystChestBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.AMETHYST_CHEST, pos, state);
	}
	
	@Override
	protected Text getContainerName() {
		return new TranslatableText("block.spectrum.amethyst_treasure_chest");
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
}
