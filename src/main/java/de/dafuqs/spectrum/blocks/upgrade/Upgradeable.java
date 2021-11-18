package de.dafuqs.spectrum.blocks.upgrade;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface Upgradeable {
	
	enum UpgradeType {
		SPEED,      // faster crafting
		EFFICIENCY, // chance to not use input resources (like gemstone powder)
		YIELD,      // chance to increase output
		EXPERIENCE; // increases XP output
		
	}
	
	static NbtList toNbt(@NotNull Map<UpgradeType, Double> upgrades) {
		NbtList nbtList = new NbtList();
		if(!upgrades.isEmpty()) {
			for (Map.Entry<UpgradeType, Double> upgrade : upgrades.entrySet()) {
				NbtCompound upgradeCompound = new NbtCompound();
				upgradeCompound.putString("Type", upgrade.getKey().toString());
				upgradeCompound.putDouble("Power", upgrade.getValue());
				nbtList.add(upgradeCompound);
			}
		}
		return nbtList;
	}
	
	static Map<UpgradeType, Double> fromNbt(@NotNull NbtList nbtList) {
		Map<UpgradeType, Double> map = Maps.newLinkedHashMap();

		for(int i = 0; i < nbtList.size(); ++i) {
			NbtCompound nbtCompound = nbtList.getCompound(i);
			
			UpgradeType upgradeType = UpgradeType.valueOf(nbtCompound.getString("Type"));
			double upgradeMod = nbtCompound.getDouble("Power");
			map.put(upgradeType, upgradeMod);
		}
		
		return map;
	}
	
	static Pair<Integer, Map<UpgradeType, Double>> checkUpgradeMods(World world, BlockPos blockPos, int horizontalOffset, int verticalOffset) {
		List<BlockPos> offsetPosList = new ArrayList<>();
		offsetPosList.add(blockPos.add(horizontalOffset, verticalOffset, horizontalOffset));
		offsetPosList.add(blockPos.add(horizontalOffset, verticalOffset, -horizontalOffset));
		offsetPosList.add(blockPos.add(-horizontalOffset, verticalOffset, horizontalOffset));
		offsetPosList.add(blockPos.add(-horizontalOffset, verticalOffset, -horizontalOffset));
		
		// create a hash map of upgrade types and mods
		HashMap<UpgradeType, List<Double>> upgradeMods = new HashMap<>();
		int upgradeCount = 0;
		for(BlockPos offsetPos : offsetPosList) {
			Block block = world.getBlockState(offsetPos).getBlock();
			if(block instanceof UpgradeBlock upgradeBlock) {
				UpgradeType upgradeType = upgradeBlock.getUpgradeType();
				double upgradeMod = upgradeBlock.getUpgradeMod();
				
				if(upgradeMods.containsKey(upgradeType)) {
					upgradeMods.get(upgradeType).add(upgradeMod);
				} else {
					ArrayList<Double> arrayList = new ArrayList<>();
					arrayList.add(upgradeMod);
					upgradeMods.put(upgradeType, arrayList);
				}
				
				upgradeCount++;
			}
		}
		
		// iterate through that hash map, sort the mods descending by power and apply mali, if an upgrade type is used more than once
		Map<UpgradeType, Double> upgradeMap = Maps.newLinkedHashMap();
		for(UpgradeType upgradeType : UpgradeType.values()) {
			if(upgradeMods.containsKey(upgradeType)) {
				List<Double> upgradeModList = upgradeMods.get(upgradeType);
				Collections.sort(upgradeModList);
				Collections.reverse(upgradeModList);
				
				double resultingMod = 0.0;
				for (int i = 0; i < upgradeModList.size(); i++) {
					// highest mod counts times 1.0, second: 0.75, third: 0.5, fourth: 0.25
					resultingMod += upgradeModList.get(i) * ((4.0-i)/4.0);
				}
				upgradeMap.put(upgradeType, 1.0 + resultingMod);
			} else {
				upgradeMap.put(upgradeType, 1.0);
			}
		}
		
		return new Pair<>(upgradeCount, upgradeMap);
	}
	
}
