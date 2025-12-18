package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;

public record BottomlessBundleTooltipData(ItemStack variant, long amount) implements TooltipComponent {
}
