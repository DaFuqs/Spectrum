package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.helpers.ColorHelper;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

/**
 * A block that can be colored by using dye, pigment, a paintbrush or other coloring actions on them
 */
public interface ColorableBlock {

    /**
     * Color the block into the specified color
     *
     * @param color the color the block should be colored in
     * @return True if coloring was successful, false if failed (like the block was this color already)
     */
    boolean color(World world, BlockPos pos, DyeColor color);

    DyeColor getColor(BlockState state);

    default boolean isColor(BlockState state, DyeColor color) {
        return getColor(state) == color;
    }

    default boolean tryColorUsingStackInHand(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getStackInHand(hand);
        Optional<DyeColor> itemInHandColor = ColorHelper.getDyeColorOfItemStack(handStack);
        if (itemInHandColor.isPresent()) {
            if (color(world, pos, itemInHandColor.get()) && !player.isCreative()) {
                handStack.decrement(1);
                return true;
            }
        }
        return false;
    }

}
