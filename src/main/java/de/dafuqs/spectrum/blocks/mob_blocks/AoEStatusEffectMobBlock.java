package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class AoEStatusEffectMobBlock extends MobBlock {
	
	protected final int range;
	protected final StatusEffect statusEffect;
	protected final int amplifier;
	protected final int duration;
	
	public AoEStatusEffectMobBlock(Settings settings, ParticleEffect particleEffect, StatusEffect statusEffect, int amplifier, int duration, int range) {
		super(settings, particleEffect);
		this.statusEffect = statusEffect;
		this.amplifier = amplifier;
		this.duration = duration;
		this.range = range;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.echolocating_mob_block.tooltip", range));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = range + range;
		List<LivingEntity> livingEntities = world.getNonSpectatingEntities(LivingEntity.class, Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize));
		for (LivingEntity livingEntity : livingEntities) {
			livingEntity.addStatusEffect(new StatusEffectInstance(statusEffect, duration, amplifier));
		}
		return true;
	}
	
}
