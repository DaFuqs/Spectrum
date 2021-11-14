package de.dafuqs.spectrum.blocks.upgrade;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	
	@Contract(pure = true)
	default double getMultiplier(@NotNull Map<UpgradeType, Double> upgrades, UpgradeType upgradeType) {
		if(upgrades.containsKey(upgradeType)) {
			return getMultiplier(upgrades.get(upgradeType));
		} else {
			return 1.0;
		}
	}
	
	@Contract(pure = true)
	private double getMultiplier(double upgradeMod) {
		// linear progression may be better understandable for players
		// exponential increase could make it more interesting
		// players could use 4 yield mods (making very cheap to craft low-level stuff),
		// but crafting higher level stuff would take ages in return => feeling of power, player has to choose, or switch constantly
		return Math.pow(2, upgradeMod);
		
		// Or logarithmic? That way specialization would be punished a bit
		//return Math.log(2 + upgradeMod,2);
		// Maybe a combination of both?
	}
	
	
	public static Pair<Integer, Map<UpgradeType, Double>> getUpgrades(World world, BlockPos blockPos, int horizontalOffset, int verticalOffset) {
		Map<UpgradeType, Double> map = Maps.newLinkedHashMap();
		
		List<BlockPos> offsetPosList = new ArrayList<>();
		offsetPosList.add(blockPos.add(horizontalOffset, verticalOffset, horizontalOffset));
		offsetPosList.add(blockPos.add(horizontalOffset, verticalOffset, -horizontalOffset));
		offsetPosList.add(blockPos.add(-horizontalOffset, verticalOffset, horizontalOffset));
		offsetPosList.add(blockPos.add(-horizontalOffset, verticalOffset, -horizontalOffset));
		
		int upgradeCount = 0;
		for(BlockPos offsetPos : offsetPosList) {
			Block block = world.getBlockState(offsetPos).getBlock();
			if(block instanceof UpgradeBlock upgradeBlock) {
				UpgradeType upgradeType = upgradeBlock.getUpgradeType();
				double upgradeMod = upgradeBlock.getUpgradeMod();
				
				if(map.containsKey(upgradeType)) {
					map.put(upgradeType, map.get(upgradeType) + upgradeMod);
				} else {
					map.put(upgradeType, upgradeMod);
				}
				
				upgradeCount++;
			}
		}
		
		return new Pair<>(upgradeCount, map);
	}
	
}
