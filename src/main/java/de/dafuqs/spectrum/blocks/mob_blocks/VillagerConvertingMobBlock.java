package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class VillagerConvertingMobBlock extends MobBlock {
	
	public VillagerConvertingMobBlock(Settings settings, ParticleEffect particleEffect) {
		super(settings, particleEffect);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.villager_converting_mob_block.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity instanceof VillagerEntity villagerEntity) {
			ZombieVillagerEntity zombieVillagerEntity = villagerEntity.convertTo(EntityType.ZOMBIE_VILLAGER, false);
			zombieVillagerEntity.initialize(world, world.getLocalDifficulty(zombieVillagerEntity.getBlockPos()), SpawnReason.CONVERSION, new ZombieEntity.ZombieData(false, true), null);
			zombieVillagerEntity.setVillagerData(villagerEntity.getVillagerData());
			zombieVillagerEntity.setGossipData(villagerEntity.getGossip().serialize(NbtOps.INSTANCE));
			zombieVillagerEntity.setOfferData(villagerEntity.getOffers().toNbt());
			zombieVillagerEntity.setXp(villagerEntity.getExperience());
			
			zombieVillagerEntity.playAmbientSound();
			return true;
		}
		return false;
	}
	
}
