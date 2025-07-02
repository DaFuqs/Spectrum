package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ShearingIdolBlock extends IdolBlock {
	
	protected final int range;
	
	public ShearingIdolBlock(Properties settings, ParticleOptions particleEffect, int range) {
		super(settings, particleEffect);
		this.range = range;
	}

	@Override
	public MapCodec<? extends ShearingIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.shearing_idol.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		int boxSize = range + range;
		
		List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(Vec3.atCenterOf(blockPos), boxSize, boxSize, boxSize));
		for (LivingEntity currentEntity : entities) {
			if (currentEntity instanceof Shearable shearable && shearable.readyForShearing()) {
				shearable.shear(SoundSource.BLOCKS);
			}
		}
		return true;
	}
	
}
