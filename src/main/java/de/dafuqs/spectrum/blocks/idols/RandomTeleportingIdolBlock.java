package de.dafuqs.spectrum.blocks.idols;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class RandomTeleportingIdolBlock extends IdolBlock {
	
	protected final int horizontalRange;
	protected final int verticalRange;
	
	public RandomTeleportingIdolBlock(Settings settings, ParticleEffect particleEffect, int horizontalRange, int verticalRange) {
		super(settings, particleEffect);
		this.horizontalRange = horizontalRange;
		this.verticalRange = verticalRange;
	}
	
	public static void teleportTo(ServerWorld world, Entity entity, int x, int y, int z) {
		teleportTo(world, entity, new BlockPos(x, y, z));
	}
	
	public static boolean teleportTo(ServerWorld world, Entity entity, BlockPos blockPos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		// if in solid: move up
		while (mutable.getY() < world.getTopY() && world.getBlockState(mutable).blocksMovement()) {
			mutable.move(Direction.UP);
		}
		// if in air: move down
		while (mutable.getY() > world.getBottomY() && !world.getBlockState(mutable).blocksMovement()) {
			mutable.move(Direction.DOWN);
		}
		
		BlockState blockState = world.getBlockState(mutable);
		if (blockState.blocksMovement()) {
			double boundingBoxY = entity.getBoundingBox().getYLength(); // bouncy
			if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.teleport((ServerWorld) serverPlayerEntity.getWorld(), mutable.getX() + 0.5, mutable.getY() + boundingBoxY, mutable.getZ() + 0.5, serverPlayerEntity.getYaw(), serverPlayerEntity.getPitch());
				world.sendEntityStatus(serverPlayerEntity, EntityStatuses.ADD_BREEDING_PARTICLES);
				return true;
			} else if (entity instanceof LivingEntity livingEntity) {
				boolean success = livingEntity.teleport(mutable.getX() + 0.5, mutable.getY() + boundingBoxY, mutable.getZ() + 0.5, true);
				if (success) {
					world.playSound(null, entity.prevX, entity.prevY, entity.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
					entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
				}
				return success;
			} else {
				entity.teleport(mutable.getX() + 0.5, mutable.getY() + boundingBoxY, mutable.getZ() + 0.5);
				world.playSound(null, entity.prevX, entity.prevY, entity.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
				entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
				return true;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.random_teleporting_idol.tooltip", horizontalRange));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity != null) {
			Random random = world.getRandom();
			int x = (int) (blockPos.getX() + (random.nextDouble() - 0.5D) * (this.horizontalRange + this.horizontalRange));
			int y = blockPos.getY() + (random.nextInt(this.verticalRange + this.verticalRange) - (this.verticalRange));
			int z = (int) (blockPos.getZ() + (random.nextDouble() - 0.5D) * (this.horizontalRange + this.horizontalRange));
			teleportTo(world, entity, x, y, z);
			return true;
		}
		return false;
	}
	
}
