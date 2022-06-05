package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

// since SkullBlockEntity uses the fixed BlockEntityType.SKULL we have to create our own block entity :(
// but since there is no player type / redstone interaction it should be a bit more performant than the vanilla one
public class SpectrumSkullBlockEntity extends BlockEntity {
	
	public SpectrumSkullBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.SKULL, pos, state);
	}
	
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
	}
	
	@Environment(EnvType.CLIENT)
	public SpectrumSkullBlock.SpectrumSkullBlockType getSkullType() {
		return SpectrumBlocks.getSkullType(world.getBlockState(this.pos).getBlock());
	}
	
}
