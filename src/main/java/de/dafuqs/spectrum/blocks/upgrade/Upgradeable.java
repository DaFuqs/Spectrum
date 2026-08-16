package de.dafuqs.spectrum.blocks.upgrade;

import com.google.common.collect.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.progression.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import org.jspecify.annotations.*;

import java.util.*;

public interface Upgradeable {
	
	// Positions to check on place / destroy to upgrade those blocks upgrade counts
	Set<Vec3i> POSSIBLE_UPGRADE_POS_OFFSETS = Sets.newTreeSet();
	
	enum UpgradeType {
		SPEED(1),     // faster crafting
		EFFICIENCY(16), // chance to not use input resources (like gemstone powder)
		YIELD(16), // chance to increase output
		EXPERIENCE(1); // increased XP output / decreased XP usage
		
		private final int effectivityDivisor; // multiplied on top of crafting speed, chance to double output, ...
		
		UpgradeType(int effectivityDivisor) {
			this.effectivityDivisor = effectivityDivisor;
		}
		
		public int getEffectivityDivisor() {
			return effectivityDivisor;
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
		
		public ListTag toNbt() {
			ListTag nbtList = new ListTag();
			if (!upgrades.isEmpty()) {
				for (Map.Entry<UpgradeType, Integer> upgrade : upgrades.entrySet()) {
					if (upgrade.getValue() > 0) {
						CompoundTag upgradeCompound = new CompoundTag();
						upgradeCompound.putString("Variant", upgrade.getKey().toString());
						upgradeCompound.putFloat("Power", upgrade.getValue());
						nbtList.add(upgradeCompound);
					}
				}
			}
			return nbtList;
		}
		
		public static UpgradeHolder fromNbt(ListTag nbtList) {
			Map<UpgradeType, Integer> map = new HashMap<>();
			for (UpgradeType upgradeType : UpgradeType.values()) {
				map.put(upgradeType, 0);
			}

			for (int i = 0; i < nbtList.size(); ++i) {
				CompoundTag nbtCompound = nbtList.getCompound(i);
				UpgradeType upgradeType = UpgradeType.valueOf(nbtCompound.getString("Variant"));
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
		
		public long getEffectiveCostUsingEfficiency(long amount) {
			int efficiencyMod = getRawValue(Upgradeable.UpgradeType.EFFICIENCY);
			return Math.max(1, amount >> efficiencyMod);
		}

		public Iterable<? extends Map.Entry<UpgradeType, Integer>> entrySet() {
			return this.upgrades.entrySet();
		}

	}
	
	static UpgradeHolder calculateUpgradeMods4(Level world, BlockPos blockPos, int offsetHorizontal, int offsetUp, @Nullable UUID advancementPlayerUUID) {
		List<BlockPos> posList = new ArrayList<>();
		posList.add(blockPos.offset(offsetHorizontal, offsetUp, offsetHorizontal));
		posList.add(blockPos.offset(offsetHorizontal, offsetUp, -offsetHorizontal));
		posList.add(blockPos.offset(-offsetHorizontal, offsetUp, offsetHorizontal));
		posList.add(blockPos.offset(-offsetHorizontal, offsetUp, -offsetHorizontal));
		
		return calculateUpgrades(world, blockPos, posList, advancementPlayerUUID);
	}
	
	static UpgradeHolder calculateUpgradeMods2(Level world, BlockPos blockPos, Rotation multiblockRotation, int offsetHorizontal, int offsetUp, @Nullable UUID advancementPlayerUUID) {
		return calculateUpgradeMods2(world, blockPos, multiblockRotation, offsetHorizontal, offsetHorizontal, offsetUp, advancementPlayerUUID);
	}
	
	static UpgradeHolder calculateUpgradeMods2(Level world, BlockPos blockPos, Rotation multiblockRotation, int offsetSide, int offsetBack, int offsetUp, @Nullable UUID advancementPlayerUUID) {
		List<BlockPos> positions = new ArrayList<>();
		switch (multiblockRotation) {
			case NONE -> {
				positions.add(blockPos.offset(-offsetSide, offsetUp, offsetBack));
				positions.add(blockPos.offset(offsetSide, offsetUp, offsetBack));
			}
			case CLOCKWISE_90 -> {
				positions.add(blockPos.offset(-offsetBack, offsetUp, offsetSide));
				positions.add(blockPos.offset(-offsetBack, offsetUp, -offsetSide));
			}
			case CLOCKWISE_180 -> {
				positions.add(blockPos.offset(-offsetSide, offsetUp, -offsetBack));
				positions.add(blockPos.offset(offsetSide, offsetUp, -offsetBack));
			}
			default -> {
				positions.add(blockPos.offset(offsetBack, offsetUp, -offsetSide));
				positions.add(blockPos.offset(offsetBack, offsetUp, offsetSide));
			}
		}
		
		return calculateUpgrades(world, blockPos, positions, advancementPlayerUUID);
	}
	
	private static UpgradeHolder calculateUpgrades(Level world, BlockPos blockPos, List<BlockPos> positions, @Nullable UUID advancementPlayerUUID) {
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
		
		if (advancementPlayerUUID != null && !world.isClientSide()) {
			ServerPlayer player = (ServerPlayer) PlayerOwned.getPlayerIfOnline(world, advancementPlayerUUID);
			if (player != null) {
				SpectrumAdvancementCriteria.UPGRADE_PLACING.trigger(player, (ServerLevel) world, blockPos, upgradeCount, upgradeMods);
			}
		}

		return new UpgradeHolder(upgradeMods);
	}
	
	void calculateUpgrades(Level level);

	UpgradeHolder getUpgradeHolder();
	
	List<Vec3i> getUpgradePosOffsets();
	
	static void registerUpgradePosOffsets(List<Vec3i> offsets) {
		POSSIBLE_UPGRADE_POS_OFFSETS.addAll(offsets);
	}
	
}