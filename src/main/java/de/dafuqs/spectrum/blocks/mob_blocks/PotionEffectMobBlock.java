package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionEffectMobBlock extends MobBlock {
	
	protected StatusEffect statusEffect;
	protected int amplifier;
	protected int duration;
	
	public PotionEffectMobBlock(Settings settings, StatusEffect statusEffect, int amplifier, int duration) {
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
	public void trigger(World world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
	
	}
	
	
}
