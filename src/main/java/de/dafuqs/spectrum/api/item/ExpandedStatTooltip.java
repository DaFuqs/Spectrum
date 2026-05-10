package de.dafuqs.spectrum.api.item;

import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jspecify.annotations.Nullable;

import java.util.function.*;

public interface ExpandedStatTooltip {
	
	void expandTooltip(ItemStack stack, @Nullable Player player, Consumer<Component> tooltip, Item.TooltipContext context);
}
