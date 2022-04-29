package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
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
	
	public RandomTeleportingMobBlock(Settings settings, int horizontalRange, int verticalRange) {
		super(settings);
		this.horizontalRange = horizontalRange;
		this.verticalRange = verticalRange;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.random_teleporting_mob_block.tooltip", horizontalRange));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity != null) {
			Random random = world.getRandom();
			double x = blockPos.getX() + (random.nextDouble() - 0.5D) * this.horizontalRange;
			double y = blockPos.getY() + (double)(random.nextInt(this.verticalRange) - (this.verticalRange / 2));
			double z = blockPos.getZ() + (random.nextDouble() - 0.5D) * this.horizontalRange;
			teleportTo(world, entity, x, y, z);
			return true;
		}
		return false;
	}
	
	public static boolean teleportTo(ServerWorld world, Entity entity, double x, double y, double z) {
		BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
		
		while(mutable.getY() > world.getBottomY() && !world.getBlockState(mutable).getMaterial().blocksMovement()) {
			mutable.move(Direction.DOWN);
		}
		
		BlockState blockState = world.getBlockState(mutable);
		boolean blocksMovement = blockState.getMaterial().blocksMovement();
		boolean isWater = blockState.getFluidState().isIn(FluidTags.WATER);
		if (blocksMovement && !isWater) {
			if(entity instanceof LivingEntity livingEntity) {
				boolean success = livingEntity.teleport(x, y, z, true);
				world.playSound(null, entity.prevX, entity.prevY, entity.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
				entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
				return success;
			} else {
				entity.teleport(x, y, z);
				world.playSound(null, entity.prevX, entity.prevY, entity.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
				entity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
				return true;
			}
		} else {
			return false;
		}
	}
	
}
