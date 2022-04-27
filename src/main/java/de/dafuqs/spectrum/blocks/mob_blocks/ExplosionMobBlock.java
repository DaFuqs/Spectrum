package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class ExplosionMobBlock extends MobBlock {
	
	protected float power;
	protected boolean createFire;
	protected Explosion.DestructionType destructionType;
	
	public ExplosionMobBlock(Settings settings, float power, boolean createFire, Explosion.DestructionType destructionType) {
		super(settings);
		this.power = power;
		this.createFire = createFire;
		this.destructionType = destructionType;
	}
	
	@Override
	public void trigger(World world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
	
	}
	
}
