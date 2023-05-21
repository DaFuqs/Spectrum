package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ShearingMobBlock extends MobBlock {
	
	protected final int range;
	
	public ShearingMobBlock(Settings settings, ParticleEffect particleEffect, int range) {
		super(settings, particleEffect);
		this.range = range;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.shearing_mob_block.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = range + range;
		
		List<LivingEntity> entities = world.getNonSpectatingEntities(LivingEntity.class, Box.of(Vec3d.ofCenter(blockPos), boxSize, boxSize, boxSize));
		for (LivingEntity currentEntity : entities) {
			if (currentEntity instanceof Shearable shearable && shearable.isShearable()) {
				shearable.sheared(SoundCategory.BLOCKS);
			}
		}
		return true;
	}
	
}
