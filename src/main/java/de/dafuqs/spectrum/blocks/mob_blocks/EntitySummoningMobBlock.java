package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class EntitySummoningMobBlock extends MobBlock {
	
	protected final EntityType entityType;
	
	public EntitySummoningMobBlock(Settings settings, EntityType entityType) {
		super(settings);
		this.entityType = entityType;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.entity_summoning_mob_block.tooltip", entityType.getName()));
	}
	
	@Override
	public void trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		// alignPosition: center the mob in the center of the blockPos
		Entity summonedEntity = entityType.create(world);
		if (summonedEntity != null) {
			summonedEntity.refreshPositionAndAngles(blockPos.up(), 0.0F, 0.0F);
			if(summonedEntity instanceof MobEntity mobEntity) {
				mobEntity.initialize(world, world.getLocalDifficulty(blockPos), SpawnReason.MOB_SUMMONED, null, null);
			}
			world.spawnEntityAndPassengers(summonedEntity);
		}
	}
	
}
