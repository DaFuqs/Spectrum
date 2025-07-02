package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LineTeleportingIdolBlock extends IdolBlock {
	
	protected final int range;
	
	public LineTeleportingIdolBlock(Properties settings, ParticleOptions particleEffect, int range) {
		super(settings, particleEffect);
		this.range = range;
	}

	@Override
	public MapCodec<? extends LineTeleportingIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	public static Direction getLookDirection(@NotNull Entity entity, boolean mirrorVertical, boolean mirrorHorizontal) {
		double pitch = entity.getXRot();
		if (pitch < -60) {
			return mirrorVertical ? Direction.UP : Direction.DOWN;
		} else if (pitch > 60) {
			return mirrorVertical ? Direction.DOWN : Direction.UP;
		} else {
			return mirrorHorizontal ? entity.getMotionDirection().getOpposite() : entity.getMotionDirection();
		}
	}
	
	public static Optional<BlockPos> searchForBlock(Level world, BlockPos pos, BlockState searchedState, Direction direction, int range) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		for (int i = 1; i < range; i++) {
			BlockPos currPos = mutable.relative(direction, i);
			if (world.getBlockState(currPos) == searchedState) {
				return Optional.of(currPos);
			}
		}
		return Optional.empty();
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.line_teleporting_idol.tooltip", range));
		tooltip.add(Component.translatable("block.spectrum.line_teleporting_idol.tooltip2", range));
	}
	
	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
		if (!world.isClientSide && !hasCooldown(state)) {
			if (trigger((ServerLevel) world, pos, state, entity, getLookDirection(entity, true, false).getOpposite())) { // we want the movement direction here, instead of only "top"
				playTriggerParticles((ServerLevel) world, pos);
				playTriggerSound(world, pos);
				triggerCooldown(world, pos);
			}
		}
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity != null) {
			Optional<BlockPos> foundBlockPos = searchForBlock(world, blockPos, state, side.getOpposite(), this.range);
			if (foundBlockPos.isPresent()) {
				BlockPos targetPos = foundBlockPos.get();
				triggerCooldown(world, targetPos);
				RandomTeleportingIdolBlock.teleportTo(world, entity, targetPos);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getCooldownTicks() {
		return 10;
	}
	
}
