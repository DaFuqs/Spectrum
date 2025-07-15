package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.blocks.present.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

/**
 * Doing a lil trolling
 */
public interface PresentUnpackBehavior {
	
	/**
	 * Invoked when an item is unwrapped from a present
	 *
	 * @return the resulting stack after unpacking. Can be the original stack, ItemStack.AIR, or a new stack altogether
	 */
	ItemStack onPresentUnpack(ItemStack stack, PresentBlockEntity presentBlockEntity, ServerLevel world, BlockPos pos, RandomSource random);
	
}
