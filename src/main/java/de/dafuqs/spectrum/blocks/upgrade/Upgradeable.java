package de.dafuqs.spectrum.blocks.upgrade;

import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.block.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface Upgradeable {

	enum UpgradeType {
		SPEED(1, InkColors.MAGENTA),     // faster crafting
		EFFICIENCY(16, InkColors.BLACK), // chance to not use input resources (like gemstone powder)
		YIELD(16, InkColors.LIGHT_BLUE), // chance to increase output
		EXPERIENCE(1, InkColors.PURPLE); // increased XP output / decreased XP usage
		
		private final int effectivityDivisor; // multiplied on top of crafting speed, chance to double output, ...
		private final InkColor inkColor;
		
		UpgradeType(int effectivityDivisor, InkColor inkColor) {
			this.effectivityDivisor = effectivityDivisor;
			this.inkColor = inkColor;
		}
		
		public int getEffectivityDivisor() {
			return effectivityDivisor;
		}

		public InkColor getInkColor() {
			return inkColor;
		}
	}

	class UpgradeHolder {

		private final Map<Upgradeable.UpgradeType, Integer> upgrades;

		public UpgradeHolder() {
			this.upgrades = new HashMap<>();
			for (UpgradeType upgradeType : UpgradeType.values()) {
				this.upgrades.put(upgradeType, 0);
			}
		}

		public UpgradeHolder(Map<Upgradeable.UpgradeType, Integer> upgrades) {
			this.upgrades = upgrades;
		}

		public NbtList toNbt() {
			NbtList nbtList = new NbtList();
			if (!upgrades.isEmpty()) {
				for (Map.Entry<UpgradeType, Integer> upgrade : upgrades.entrySet()) {
					if (upgrade.getValue() > 0) {
						NbtCompound upgradeCompound = new NbtCompound();
						upgradeCompound.putString("Type", upgrade.getKey().toString());
						upgradeCompound.putFloat("Power", upgrade.getValue());
						nbtList.add(upgradeCompound);
					}
				}
			}
			return nbtList;
		}

		public static UpgradeHolder fromNbt(@NotNull NbtList nbtList) {
			Map<UpgradeType, Integer> map = new HashMap<>();
			for (UpgradeType upgradeType : UpgradeType.values()) {
				map.put(upgradeType, 0);
			}

			for (int i = 0; i < nbtList.size(); ++i) {
				NbtCompound nbtCompound = nbtList.getCompound(i);
				UpgradeType upgradeType = UpgradeType.valueOf(nbtCompound.getString("Type"));
				int upgradeMod = nbtCompound.getInt("Power");
				map.put(upgradeType, upgradeMod);
			}

			return new UpgradeHolder(map);
		}

		public int getRawValue(UpgradeType upgradeType) {
			return this.upgrades.get(upgradeType);
		}

		public float getEffectiveValue(UpgradeType upgradeType) {
			return 1 + (this.upgrades.get(upgradeType) / (float) upgradeType.getEffectivityDivisor());
		}

		public long getEffectiveCost(UpgradeType upgradeType) {
			return 1L << this.upgrades.get(upgradeType);
		}

		public long getEffectiveCostUsingEfficiency(UpgradeType upgradeType) {
			int efficiencyMod = getRawValue(Upgradeable.UpgradeType.EFFICIENCY);
			return 1L << Math.max(this.upgrades.get(upgradeType) - efficiencyMod, 0);
		}

		public Iterable<? extends Map.Entry<UpgradeType, Integer>> entrySet() {
			return this.upgrades.entrySet();
		}

	}
	
	static @NotNull UpgradeHolder calculateUpgradeMods4(World world, @NotNull BlockPos blockPos, int offsetHorizontal, int offsetUp, @Nullable UUID advancementPlayerUUID) {
		List<BlockPos> posList = new ArrayList<>();
		posList.add(blockPos.add(offsetHorizontal, offsetUp, offsetHorizontal));
		posList.add(blockPos.add(offsetHorizontal, offsetUp, -offsetHorizontal));
		posList.add(blockPos.add(-offsetHorizontal, offsetUp, offsetHorizontal));
		posList.add(blockPos.add(-offsetHorizontal, offsetUp, -offsetHorizontal));
		
		return calculateUpgrades(world, blockPos, posList, advancementPlayerUUID);
	}
	
	static @NotNull UpgradeHolder calculateUpgradeMods2(World world, BlockPos blockPos, @NotNull BlockRotation multiblockRotation, int offsetHorizontal, int offsetUp, @Nullable UUID advancementPlayerUUID) {
		return calculateUpgradeMods2(world, blockPos, multiblockRotation, offsetHorizontal, offsetHorizontal, offsetUp, advancementPlayerUUID);
	}
	
	static @NotNull UpgradeHolder calculateUpgradeMods2(World world, BlockPos blockPos, @NotNull BlockRotation multiblockRotation, int offsetSide, int offsetBack, int offsetUp, @Nullable UUID advancementPlayerUUID) {
		List<BlockPos> positions = new ArrayList<>();
		switch (multiblockRotation) {
			case NONE -> {
				positions.add(blockPos.add(-offsetSide, offsetUp, offsetBack));
				positions.add(blockPos.add(offsetSide, offsetUp, offsetBack));
			}
			case CLOCKWISE_90 -> {
				positions.add(blockPos.add(-offsetBack, offsetUp, offsetSide));
				positions.add(blockPos.add(-offsetBack, offsetUp, -offsetSide));
			}
			case CLOCKWISE_180 -> {
				positions.add(blockPos.add(-offsetSide, offsetUp, -offsetBack));
				positions.add(blockPos.add(offsetSide, offsetUp, -offsetBack));
			}
			default -> {
				positions.add(blockPos.add(offsetBack, offsetUp, -offsetSide));
				positions.add(blockPos.add(offsetBack, offsetUp, offsetSide));
			}
		}
		
		return calculateUpgrades(world, blockPos, positions, advancementPlayerUUID);
	}

	private static @NotNull UpgradeHolder calculateUpgrades(World world, BlockPos blockPos, @NotNull List<BlockPos> positions, @Nullable UUID advancementPlayerUUID) {
		// create a hash map of upgrade types and mods
		HashMap<UpgradeType, Integer> upgradeMods = new HashMap<>();
		for (UpgradeType upgradeType : UpgradeType.values()) {
			upgradeMods.put(upgradeType, 0);
		}

		int upgradeCount = 0;
		for (BlockPos offsetPos : positions) {
			Block block = world.getBlockState(offsetPos).getBlock();
			if (block instanceof UpgradeBlock upgradeBlock) {
				UpgradeType upgradeType = upgradeBlock.getUpgradeType();
				int upgradeMod = upgradeBlock.getUpgradeMod();
				upgradeMods.put(upgradeType, upgradeMods.get(upgradeType) + upgradeMod);
				upgradeCount++;
			}
		}
		
		if (advancementPlayerUUID != null && !world.isClient) {
			ServerPlayerEntity player = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(advancementPlayerUUID);
			if (player != null) {
				SpectrumAdvancementCriteria.UPGRADE_PLACING.trigger(player, (ServerWorld) world, blockPos, upgradeCount, upgradeMods);
			}
		}

		return new UpgradeHolder(upgradeMods);
	}
	
	void resetUpgrades();
	
	void calculateUpgrades();

	UpgradeHolder getUpgradeHolder();
	
}