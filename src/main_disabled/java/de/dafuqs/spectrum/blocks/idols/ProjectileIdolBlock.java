package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class ProjectileIdolBlock extends IdolBlock {
	
	protected final EntityType<?> entityType;
	protected final SoundEvent triggerSoundEvent;
	protected final float speed;
	protected final float divergence;
	
	public ProjectileIdolBlock(Properties settings, ParticleOptions particleEffect, EntityType<?> entityType, SoundEvent triggerSoundEvent, float speed, float divergence) {
		super(settings, particleEffect);
		this.entityType = entityType;
		this.triggerSoundEvent = triggerSoundEvent;
		this.speed = speed;
		this.divergence = divergence;
	}

	@Override
	public MapCodec<? extends ProjectileIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.projectile_idol.tooltip", this.entityType.getDescription()));
	}
	
	@Override
	public boolean trigger(@NotNull ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		side = side.getOpposite(); // shoot out the other side of the block
		
		BlockPos outputBlockPos = blockPos.relative(side);
		if (world.getBlockState(outputBlockPos).getCollisionShape(world, outputBlockPos).isEmpty()) {
			Vec3 outputLocation = getOutputLocation(blockPos, side);
			Projectile projectileEntity = createProjectile(world, blockPos, outputLocation, side);
			projectileEntity.shoot(side.getStepX(), side.getStepY(), side.getStepZ(), this.speed, this.divergence);
			world.addFreshEntity(projectileEntity);
			world.playSound(null, blockPos, this.triggerSoundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
		
		return true;
	}
	
	public abstract Projectile createProjectile(ServerLevel world, BlockPos mobBlockPos, Position projectilePos, Direction side);
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		// lets the projectiles start really close to the block without blowing itself up
		if (context instanceof EntityCollisionContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity != null && entity.getType() == this.entityType && entity.tickCount < 2) {
				return Shapes.empty();
			}
		}
		return state.getShape(world, pos);
	}
	
}
