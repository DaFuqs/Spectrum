package de.dafuqs.spectrum.blocks.mob_head;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

// since SkullBlockEntity uses the fixed BlockEntityType.SKULL we have to create our own block entity :(
// but since there is no player type / redstone interaction it should be a bit more performant than the vanilla one
public class SpectrumSkullBlockEntity extends BlockEntity {
	
	public SpectrumSkullBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.SKULL, pos, state);
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
	}
	
	@Environment(EnvType.CLIENT)
	public SpectrumSkullBlockType getSkullType() {
		return SpectrumBlocks.getSkullType(world.getBlockState(this.pos).getBlock());
	}
	
}
