package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class EntitySummoningIdolBlock extends IdolBlock {
	
	protected final EntityType<?> entityType;
	
	public EntitySummoningIdolBlock(Properties settings, ParticleOptions particleEffect, EntityType<?> entityType) {
		super(settings, particleEffect);
		this.entityType = entityType;
	}

	@Override
	public MapCodec<? extends EntitySummoningIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.entity_summoning_idol.tooltip", entityType.getDescription()));
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		// alignPosition: center the mob in the center of the blockPos
		Entity summonedEntity = entityType.create(world);
		if (summonedEntity != null) {
			summonedEntity.moveTo(blockPos.above(), 0.0F, 0.0F);
			if (summonedEntity instanceof Mob mobEntity) {
				mobEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null);
			}
			afterSummon(world, summonedEntity);
			world.addFreshEntityWithPassengers(summonedEntity);
		}
		return true;
	}
	
	public abstract void afterSummon(ServerLevel world, Entity entity);
	
}
