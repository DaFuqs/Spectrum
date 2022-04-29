package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StatusEffectMobBlock extends MobBlock {
	
	protected StatusEffect statusEffect;
	protected int amplifier;
	protected int duration;
	
	public StatusEffectMobBlock(Settings settings, StatusEffect statusEffect, int amplifier, int duration) {
		super(settings);
		this.statusEffect = statusEffect;
		this.amplifier = amplifier;
		this.duration = duration;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.potion_effect_mob_block.tooltip", this.statusEffect.getName()));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if(entity instanceof LivingEntity livingEntity && !livingEntity.hasStatusEffect(statusEffect)) {
			livingEntity.addStatusEffect(new StatusEffectInstance(statusEffect, duration, amplifier, true, true));
			return true;
		}
		return false;
	}
	
	
}
