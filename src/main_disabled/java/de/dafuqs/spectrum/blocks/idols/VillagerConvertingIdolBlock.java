package de.dafuqs.spectrum.blocks.idols;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class VillagerConvertingIdolBlock extends IdolBlock {
	
	public VillagerConvertingIdolBlock(Properties settings, ParticleOptions particleEffect) {
		super(settings, particleEffect);
	}

	@Override
	public MapCodec<? extends VillagerConvertingIdolBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.villager_converting_idol.tooltip"));
	}
	
	@Override
	public boolean trigger(ServerLevel world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		if (entity instanceof Villager villagerEntity) {
			ZombieVillager zombieVillagerEntity = villagerEntity.convertTo(EntityType.ZOMBIE_VILLAGER, false);
			zombieVillagerEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(zombieVillagerEntity.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true));
			zombieVillagerEntity.setVillagerData(villagerEntity.getVillagerData());
			zombieVillagerEntity.setGossips(villagerEntity.getGossips().store(NbtOps.INSTANCE));
			zombieVillagerEntity.setTradeOffers(villagerEntity.getOffers());
			zombieVillagerEntity.setVillagerXp(villagerEntity.getVillagerXp());
			
			zombieVillagerEntity.playAmbientSound();
			return true;
		}
		return false;
	}
	
}
