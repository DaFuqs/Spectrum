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
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AoEStatusEffectIdolBlock extends IdolBlock {
	
	protected final int range;
	protected final Holder<MobEffect> statusEffect;
	protected final int amplifier;
	protected final int duration;
	
	public AoEStatusEffectIdolBlock(Properties settings, ParticleOptions particleEffect, Holder<MobEffect> statusEffect, int amplifier, int duration, int range) {
		super(settings, particleEffect);
		this.statusEffect = statusEffect;
		this.amplifier = amplifier;
		this.duration = duration;
		this.range = range;
	}

	@Override
	public MapCodec<? extends AoEStatusEffectIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.echolocating_idol.tooltip", range));
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = range + range;
		List<LivingEntity> livingEntities = world.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(Vec3.atCenterOf(blockPos), boxSize, boxSize, boxSize));
		for (LivingEntity livingEntity : livingEntities) {
			livingEntity.addEffect(new MobEffectInstance(statusEffect, duration, amplifier));
		}
		return true;
	}
	
}
