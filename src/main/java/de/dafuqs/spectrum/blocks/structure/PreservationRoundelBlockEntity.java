package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.item_roundel.ItemRoundelBlockEntity;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class PreservationRoundelBlockEntity extends ItemRoundelBlockEntity {
	
	public PreservationRoundelBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PRESERVATION_ROUNDEL, pos, state);
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
	}
	
}
