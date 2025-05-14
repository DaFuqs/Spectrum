package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;

import java.util.*;

public record PresentTooltipData(List<ItemStack> itemStacks) implements TooltipComponent {
}
