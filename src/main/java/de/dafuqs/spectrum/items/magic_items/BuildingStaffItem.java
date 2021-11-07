package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BuildingStaffItem extends Item {
	
	public BuildingStaffItem(Settings settings) {
		super(settings);
	}

	public static boolean isBlacklisted(Block block) {
		return SpectrumBlockTags.BUILDING_STAFFS_BLACKLISTED.contains(block) || block.getHardness() < 0;
	}
	
}
