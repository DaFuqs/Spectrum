package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;

public class BuildingStaffItem extends Item {
	
	public BuildingStaffItem(Settings settings) {
		super(settings);
	}
	
	public static boolean isBlacklisted(BlockState blockState) {
		return blockState.isIn(SpectrumBlockTags.BUILDING_STAFFS_BLACKLISTED) || blockState.getBlock().getHardness() < 0;
	}
	
}
