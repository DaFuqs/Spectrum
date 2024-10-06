package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.blocks.present.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;

/**
 * Doing a lil trolling
 */
public interface PresentUnpackBehavior {
    
    /**
     * Invoked when an item is unwrapped from a present
     *
     * @return the resulting stack after unpacking. Can be the original stack, ItemStack.AIR, or a new stack altogether
     */
    ItemStack onPresentUnpack(ItemStack stack, PresentBlockEntity presentBlockEntity, ServerWorld world, BlockPos pos, Random random);
    
}
