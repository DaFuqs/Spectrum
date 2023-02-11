package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BuildingStaffItem extends Item {

	public BuildingStaffItem(Settings settings) {
		super(settings);
	}

	public static boolean canProcess(BlockState blockState, BlockView blockView, BlockPos blockPos, PlayerEntity player) {
		if (player == null || blockView.getBlockEntity(blockPos) != null || blockState.isIn(SpectrumBlockTags.BUILDING_STAFFS_BLACKLISTED)) {
			return false;
		}

		float hardness = blockState.getHardness(blockView, blockPos);
		return hardness >= 0 && hardness < 20;
	}

}
