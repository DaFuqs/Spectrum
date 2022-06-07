package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class RandomTeleportingMobBlock extends MobBlock {
	
	protected final int horizontalRange;
	protected final int verticalRange;
	
	public RandomTeleportingMobBlock(Settings settings, ParticleEffect particleEffect, int horizontalRange, int verticalRange) {
		super(settings, particleEffect);
		this.horizontalRange = horizontalRange;
		this.verticalRange = verticalRange;
	}
	
	public static boolean teleportTo(ServerWorld world, Entity entity, int x, int y, int z) {
		return teleportTo(world, entity, new BlockPos(x, y, z));
	}
	
	public static boolean teleportTo(ServerWorld world, Entity entity, BlockPos blockPos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		// if in solid: move up
		while (mutable.getY() < world.getTopY() && world.getBlockState(mutable).getMaterial().blocksMovement()) {
			mutable.move(Direction.UP);
		}
		// if in air: move down
		while (mutable.getY() > world.getBottomY() && !world.getBlockState(mutable).getMaterial().blocksMovement()) {
			mutable.move(Direction.DOWN);
		}
		
		BlockState blockState = world.getBlockState(mutable);
		if (blockState.getMaterial().blocksMovement()) {
			double boundingBoxY = entity.getBoundingBox().getYLength(); // bouncy
			if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.teleport((ServerWorld) serverPlayerEntity.world, mutable.getX() + 0.5, mutable.getY() + boundingBoxY, mutable.getZ() + 0.5, serverPlayerEntity.getYaw(), serverPlayerEntity.getPitch());
				world.sendEntityStatus(serverPlayerEntity, (byte) 46); // particles
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
		tooltip.add(new TranslatableText("block.spectrum.random_teleporting_mob_block.tooltip", horizontalRange));
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
