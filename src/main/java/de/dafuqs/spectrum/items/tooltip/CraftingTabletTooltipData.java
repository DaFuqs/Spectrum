package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.network.chat.*;
import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class CraftingTabletTooltipData implements TooltipComponent {
	
	private final ItemStack itemStack;
	private final Component description;
	
	public CraftingTabletTooltipData(Recipe<?> recipe, Level world) {
		this.itemStack = recipe.getResultItem(world.registryAccess());
		this.description = Component.translatable("item.spectrum.crafting_tablet.tooltip.recipe", this.itemStack.getCount(), this.itemStack.getHoverName());
	}
	
	public ItemStack getItemStack() {
		return this.itemStack;
	}
	
	public Component getDescription() {
		return this.description;
	}
	
}
