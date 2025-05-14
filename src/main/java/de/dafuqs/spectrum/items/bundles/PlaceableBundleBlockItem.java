package de.dafuqs.spectrum.items.bundles;

import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.inventory.tooltip.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class PlaceableBundleBlockItem extends BlockItem {
	
	private final ExtendedBundleComponent component;
	
	public PlaceableBundleBlockItem(ExtendedBundleComponent component, Block block, Properties settings) {
		super(block, settings);
		this.component = component;
	}
	
	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		stack.set(SpectrumDataComponentTypes.EXTENDED_BUNDLE, component);
		return stack;
	}
	
	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
		return SpectrumItems.EXTENDED_BUNDLE_ITEM.overrideStackedOnOther(stack, slot, clickType, player);
	}
	
	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
		return SpectrumItems.EXTENDED_BUNDLE_ITEM.overrideOtherStackedOnMe(stack, otherStack, slot, clickType, player, cursorStackReference);
	}
	
	@Override
	public boolean isBarVisible(ItemStack stack) {
		return SpectrumItems.EXTENDED_BUNDLE_ITEM.isBarVisible(stack);
	}
	
	@Override
	public int getBarWidth(ItemStack stack) {
		return SpectrumItems.EXTENDED_BUNDLE_ITEM.getBarWidth(stack);
	}
	
	@Override
	public int getBarColor(ItemStack stack) {
		return SpectrumItems.EXTENDED_BUNDLE_ITEM.getBarColor(stack);
	}
	
	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return SpectrumItems.EXTENDED_BUNDLE_ITEM.getTooltipImage(stack);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		SpectrumItems.EXTENDED_BUNDLE_ITEM.appendHoverText(stack, context, tooltip, type);
	}
	
	@Override
	public void onDestroyed(ItemEntity entity) {
		SpectrumItems.EXTENDED_BUNDLE_ITEM.onDestroyed(entity);
		super.onDestroyed(entity);
	}
	
}
