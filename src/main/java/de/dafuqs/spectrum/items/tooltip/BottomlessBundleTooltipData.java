package de.dafuqs.spectrum.items.tooltip;

import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.world.inventory.tooltip.*;

public record BottomlessBundleTooltipData(ItemVariant variant, long amount) implements TooltipComponent {
}
