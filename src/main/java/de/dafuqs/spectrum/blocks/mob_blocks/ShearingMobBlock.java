package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShearingMobBlock extends MobBlock {
	
	protected int range;
	
	public ShearingMobBlock(Settings settings, ParticleEffect particleEffect, int range) {
		super(settings, particleEffect);
		this.range = range;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText("block.spectrum.shearing_mob_block.tooltip"));
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
