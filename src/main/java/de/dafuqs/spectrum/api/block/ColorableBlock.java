package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.*;

import java.util.*;

/**
 * A block that can be colored by using Pigment, a Paintbrush or performing other coloring actions on them
 */
public interface ColorableBlock {

    /**
     * Color the block into the specified color
     *
     * @param color the color the block should be colored in
     * @return True if coloring was successful, false if failed (like the block was this color already)
     */
	boolean color(Level world, BlockPos pos, Optional<InkColor> color, @Nullable Entity user);
	
	Optional<InkColor> getColor(Level world, BlockPos pos);
	
	default boolean isColor(Level world, BlockPos pos, Optional<InkColor> color) {
		return getColor(world, pos) == color;
    }
	
	default boolean tryColorUsingStackInHand(ItemStack handStack, Level world, BlockPos pos, Player player) {
		InkColor itemInHandColor = null;
		if (!handStack.isEmpty()) {
			Item item = handStack.getItem();
			if (item instanceof PigmentItem pigmentItem) {
				itemInHandColor = pigmentItem.getInkColor();
			}
		}
		
		if(itemInHandColor == null) {
			return false;
		}
		
		if (color(world, pos, Optional.of(itemInHandColor), player)) {
			if (!player.isCreative()) {
				handStack.shrink(1);
			}
			return true;
		}
		return false;
    }

}
