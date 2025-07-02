package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StatusEffectIdolBlock extends IdolBlock {
	
	protected final Holder<MobEffect> statusEffect;
	protected final int amplifier;
	protected final int duration;
	
	public StatusEffectIdolBlock(Properties settings, ParticleOptions particleEffect, Holder<MobEffect> statusEffect, int amplifier, int duration) {
		super(settings, particleEffect);
		this.statusEffect = statusEffect;
		this.amplifier = amplifier;
		this.duration = duration;
	}

	@Override
	public MapCodec<? extends StatusEffectIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.potion_effect_idol.tooltip", this.statusEffect.value().getDisplayName()));
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity instanceof LivingEntity livingEntity) {
			livingEntity.addEffect(new MobEffectInstance(statusEffect, duration, amplifier, true, true));
			
			// if entity is burning: put out fire
			if (statusEffect == MobEffects.FIRE_RESISTANCE && livingEntity.isOnFire()) {
				livingEntity.setRemainingFireTicks(0);
			}
			
			return true;
		}
		return false;
	}
	
}
