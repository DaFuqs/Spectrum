package de.dafuqs.spectrum.blocks.upgrade;

import com.google.common.collect.Maps;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface Upgradeable {
	
	enum UpgradeType {
		SPEED,      // faster crafting
		EFFICIENCY, // chance to not use input resources (like gemstone powder)
		YIELD,      // chance to increase output
		EXPERIENCE  // increases XP output
	}
	
	static NbtList toNbt(@NotNull Map<UpgradeType, Float> upgrades) {
		NbtList nbtList = new NbtList();
		if (!upgrades.isEmpty()) {
			for (Map.Entry<UpgradeType, Float> upgrade : upgrades.entrySet()) {
				NbtCompound upgradeCompound = new NbtCompound();
				upgradeCompound.putString("Type", upgrade.getKey().toString());
				upgradeCompound.putFloat("Power", upgrade.getValue());
				nbtList.add(upgradeCompound);
			}
		}
		return nbtList;
	}
	
	static Map<UpgradeType, Float> fromNbt(@NotNull NbtList nbtList) {
		Map<UpgradeType, Float> map = Maps.newLinkedHashMap();
		
		for (int i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			
			UpgradeType upgradeType = UpgradeType.valueOf(nbtCompound.getString("Type"));
			float upgradeMod = nbtCompound.getFloat("Power");
			map.put(upgradeType, upgradeMod);
		}
		
		return map;
	}
	
	static @NotNull Map<UpgradeType, Float> calculateUpgradeMods4(World world, @NotNull BlockPos blockPos, int horizontalOffset, int verticalOffset, @Nullable UUID advancementPlayerUUID) {
		List<BlockPos> posList = new ArrayList<>();
		posList.add(blockPos.add(horizontalOffset, verticalOffset, horizontalOffset));
		posList.add(blockPos.add(horizontalOffset, verticalOffset, -horizontalOffset));
		posList.add(blockPos.add(-horizontalOffset, verticalOffset, horizontalOffset));
		posList.add(blockPos.add(-horizontalOffset, verticalOffset, -horizontalOffset));
		
		return calculateUpgrades(world, blockPos, posList, advancementPlayerUUID);
	}
	
	static @NotNull Map<UpgradeType, Float> calculateUpgradeMods2(World world, BlockPos blockPos, @NotNull BlockRotation multiblockRotation, int horizontalOffset, int verticalOffset, @Nullable UUID advancementPlayerUUID) {
		List<BlockPos> positions = new ArrayList<>();
		switch (multiblockRotation) {
			case NONE -> {
				positions.add(blockPos.add(horizontalOffset, verticalOffset, -horizontalOffset));
				positions.add(blockPos.add(horizontalOffset, verticalOffset, horizontalOffset));
			}
			case CLOCKWISE_90 -> {
				positions.add(blockPos.add(-horizontalOffset, verticalOffset, horizontalOffset));
				positions.add(blockPos.add(horizontalOffset, verticalOffset, horizontalOffset));
			}
			case CLOCKWISE_180 -> {
				positions.add(blockPos.add(-horizontalOffset, verticalOffset, horizontalOffset));
				positions.add(blockPos.add(-horizontalOffset, verticalOffset, -horizontalOffset));
			}
			default -> {
				positions.add(blockPos.add(-horizontalOffset, verticalOffset, horizontalOffset));
				positions.add(blockPos.add(horizontalOffset, verticalOffset, -horizontalOffset));
			}
		}
		
		return calculateUpgrades(world, blockPos, positions, advancementPlayerUUID);
	}
	
	static @NotNull Map<UpgradeType, Float> calculateUpgradeMods2(World world, BlockPos blockPos, @NotNull BlockRotation multiblockRotation, int horizontalOffsetX, int horizontalOffsetZ, int verticalOffset, @Nullable UUID advancementPlayerUUID) {
		List<BlockPos> positions = new ArrayList<>();
		switch (multiblockRotation) {
			case NONE -> {
				positions.add(blockPos.add(-horizontalOffsetZ, verticalOffset, -horizontalOffsetX));
				positions.add(blockPos.add(-horizontalOffsetZ, verticalOffset, horizontalOffsetX));
			}
			case CLOCKWISE_90 -> {
				positions.add(blockPos.add(-horizontalOffsetX, verticalOffset, horizontalOffsetZ));
				positions.add(blockPos.add(-horizontalOffsetX, verticalOffset, -horizontalOffsetZ));
			}
			case CLOCKWISE_180 -> {
				positions.add(blockPos.add(horizontalOffsetZ, verticalOffset, horizontalOffsetX)); // works
				positions.add(blockPos.add(horizontalOffsetZ, verticalOffset, -horizontalOffsetX));
			}
			default -> {
				positions.add(blockPos.add(-horizontalOffsetX, verticalOffset, horizontalOffsetZ)); // works
				positions.add(blockPos.add(horizontalOffsetX, verticalOffset, horizontalOffsetZ));
			}
		}
		
		return calculateUpgrades(world, blockPos, positions, advancementPlayerUUID);
	}
	
	private static @NotNull Map<UpgradeType, Float> calculateUpgrades(World world, BlockPos blockPos, @NotNull List<BlockPos> positions, @Nullable UUID advancementPlayerUUID) {
		// create a hash map of upgrade types and mods
		HashMap<UpgradeType, List<Float>> upgradeMods = new HashMap<>();
		int upgradeCount = 0;
		for (BlockPos offsetPos : positions) {
			Block block = world.getBlockState(offsetPos).getBlock();
			if (block instanceof UpgradeBlock upgradeBlock) {
				UpgradeType upgradeType = upgradeBlock.getUpgradeType();
				float upgradeMod = upgradeBlock.getUpgradeMod();
				
				if (upgradeMods.containsKey(upgradeType)) {
					upgradeMods.get(upgradeType).add(upgradeMod);
				} else {
					ArrayList<Float> arrayList = new ArrayList<>();
					arrayList.add(upgradeMod);
					upgradeMods.put(upgradeType, arrayList);
				}
				
				upgradeCount++;
			}
		}
		
		// iterate through that hash map, sort the mods descending by power and apply mali, if an upgrade type is used more than once
		Map<UpgradeType, Float> upgradeMap = Maps.newLinkedHashMap();
		for (UpgradeType upgradeType : UpgradeType.values()) {
			if (upgradeMods.containsKey(upgradeType)) {
				List<Float> upgradeModList = upgradeMods.get(upgradeType);
				Collections.sort(upgradeModList);
				Collections.reverse(upgradeModList);
				
				float resultingMod = 0.0F;
				for (int i = 0; i < upgradeModList.size(); i++) {
					// highest mod counts times 1.0, second: 0.75, third: 0.5, fourth: 0.25
					resultingMod += upgradeModList.get(i) * ((4.0F - i) / 4.0F);
				}
				upgradeMap.put(upgradeType, 1.0F + resultingMod);
			} else {
				upgradeMap.put(upgradeType, 1.0F);
			}
		}
		
		if (advancementPlayerUUID != null && !world.isClient) {
			ServerPlayerEntity player = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(advancementPlayerUUID);
			if (player != null) {
				SpectrumAdvancementCriteria.UPGRADE_PLACING.trigger(player, (ServerWorld) world, blockPos, upgradeCount, upgradeMap);
			}
		}
		
		return upgradeMap;
	}
	
	void resetUpgrades();
	
	void calculateUpgrades();
	
	float getUpgradeValue(UpgradeType upgradeType);
	
}