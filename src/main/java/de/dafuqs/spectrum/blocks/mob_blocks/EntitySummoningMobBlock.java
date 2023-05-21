package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class EntitySummoningMobBlock extends MobBlock {
	
	protected final EntityType entityType;
	
	public EntitySummoningMobBlock(Settings settings, ParticleEffect particleEffect, EntityType entityType) {
		super(settings, particleEffect);
		this.entityType = entityType;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.entity_summoning_mob_block.tooltip", entityType.getName()));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		// alignPosition: center the mob in the center of the blockPos
		Entity summonedEntity = entityType.create(world);
		if (summonedEntity != null) {
			summonedEntity.refreshPositionAndAngles(blockPos.up(), 0.0F, 0.0F);
			if (summonedEntity instanceof MobEntity mobEntity) {
				mobEntity.initialize(world, world.getLocalDifficulty(blockPos), SpawnReason.MOB_SUMMONED, null, null);
			}
			afterSummon(world, summonedEntity);
			world.spawnEntityAndPassengers(summonedEntity);
		}
		return true;
	}
	
	public abstract void afterSummon(ServerWorld world, Entity entity);
	
}
