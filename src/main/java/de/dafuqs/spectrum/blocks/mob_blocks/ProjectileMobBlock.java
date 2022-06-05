package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ProjectileMobBlock extends MobBlock {
	
	protected final EntityType entityType;
	protected final SoundEvent triggerSoundEvent;
	protected final float speed;
	protected final float divergence;
	
	public ProjectileMobBlock(Settings settings, ParticleEffect particleEffect, EntityType entityType, SoundEvent triggerSoundEvent, float speed, float divergence) {
		super(settings, particleEffect);
		this.entityType = entityType;
		this.triggerSoundEvent = triggerSoundEvent;
		this.speed = speed;
		this.divergence = divergence;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText("block.spectrum.projectile_mob_block.tooltip", this.entityType.getName()));
	}
	
	@Override
	public boolean trigger(@NotNull ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		side = side.getOpposite(); // shoot out the other side of the block
		
		BlockPos outputBlockPos = blockPos.offset(side);
		if (world.getBlockState(outputBlockPos).getCollisionShape(world, outputBlockPos).isEmpty()) {
			BlockPointer pointer = new BlockPointerImpl(world, blockPos);
			Position outputLocation = getOutputLocation(pointer, side);
			
			ProjectileEntity projectileEntity = createProjectile(world, blockPos, outputLocation, side);
			projectileEntity.setVelocity(side.getOffsetX(), side.getOffsetY(), side.getOffsetZ(), this.speed, this.divergence);
			world.spawnEntity(projectileEntity);
			world.playSound(null, blockPos, this.triggerSoundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
		
		return true;
	}
	
	public abstract ProjectileEntity createProjectile(ServerWorld world, BlockPos mobBlockPos, Position projectilePos, Direction side);
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		// lets the projectiles start really close to the block without blowing itself up
		if (context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity != null && entity.getType() == this.entityType && entity.age < 2) {
				return VoxelShapes.empty();
			}
		}
		return state.getOutlineShape(world, pos);
	}
	
}
