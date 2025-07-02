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

public class FallDamageNegatingIdolBlock extends IdolBlock {
	
	public FallDamageNegatingIdolBlock(Properties settings, ParticleOptions particleEffect) {
		super(settings, particleEffect);
	}

	@Override
	public MapCodec<? extends FallDamageNegatingIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity != null && entity.getDeltaMovement().y() < -0.01) {
			entity.setDeltaMovement(0, 0.5, 0); // makes it feel bouncy
			entity.hurtMarked = true;
			entity.hasImpulse = true;
			entity.fallDistance = 0;
			return true;
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.fall_damage_negating_idol.tooltip"));
		tooltip.add(Component.translatable("block.spectrum.fall_damage_negating_idol.tooltip2"));
	}
	
	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!hasCooldown(state) && fallDistance > 3F) {
			entity.causeFallDamage(fallDistance, 0.0F, world.damageSources().fall());
			if (!world.isClientSide) {
				playTriggerParticles((ServerLevel) world, pos);
				playTriggerSound(world, pos);
				triggerCooldown(world, pos);
			}
		}
	}
	
}
