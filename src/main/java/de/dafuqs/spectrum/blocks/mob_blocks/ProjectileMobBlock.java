package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProjectileMobBlock extends MobBlock {
	
	EntityType projectileEntityType;
	
	public ProjectileMobBlock(Settings settings, EntityType projectileEntityType) {
		super(settings);
		this.projectileEntityType = projectileEntityType;
	}
	
	@Override
	public void trigger(World world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
	
	}
	
}
