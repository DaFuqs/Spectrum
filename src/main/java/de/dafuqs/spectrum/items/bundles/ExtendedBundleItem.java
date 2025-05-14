package de.dafuqs.spectrum.items.bundles;

import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.component.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.*;
import org.apache.commons.lang3.math.*;

import java.util.*;

public class ExtendedBundleItem extends BundleItem {
	
	// TODO: Currently, this isn't displayed properly by the tooltip component. If we make one, we can probably replace PresentTooltipComponent and BottomlessBundleTooltipComponent with it
	
	public ExtendedBundleItem(Properties settings) {
		super(settings.component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
	}
	
	@Override
	public int getBarWidth(ItemStack stack) {
		// If we're not considering max stacks, report the fullness by occupancy. Otherwise, by stacks.
		if (ignoreStacks(stack))
			return super.getBarWidth(stack);
		return Math.min(1 + Mth.mulAndTruncate(Fraction.getFraction(getStacks(stack).size(), getMaxStacks(stack)), 12), 13);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		// Tick stacks inside the bundle. Technically slot is incorrect, so it might break
		for (var bundled : getStacks(stack)) {
			bundled.inventoryTick(world, entity, slot, selected);
		}
	}
	
	public static List<ItemStack> getStacks(ItemStack stack) {
		return stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY).itemCopyStream().toList();
	}
	
	public static Fraction getMaxOccupancy(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.EXTENDED_BUNDLE, ExtendedBundleComponent.DEFAULT).maxOccupancy();
	}
	
	public static int getMaxStacks(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.EXTENDED_BUNDLE, ExtendedBundleComponent.DEFAULT).maxStacks();
	}
	
	public static boolean ignoreStacks(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.EXTENDED_BUNDLE, ExtendedBundleComponent.DEFAULT).ignoreStacks();
	}
	
	public static class ComponentBuilder extends BundleContents.Mutable {
		
		private final Fraction maxOccupancy;
		private final int maxStacks;
		
		public ComponentBuilder(BundleContents base, Fraction maxOccupancy, int maxStacks) {
			super(base);
			this.maxOccupancy = maxOccupancy;
			this.maxStacks = maxStacks;
		}
		
		@Override
		protected int findStackIndex(ItemStack stack) {
			if (stack.isStackable()) {
				var stacks = ((BundleContentsComponentBuilderAccessor) this).getItems();
				for (int i = 0; i < stacks.size(); ++i) {
					var slotStack = stacks.get(i);
					if (ItemStack.isSameItemSameComponents(slotStack, stack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
						return i;
					}
				}
			}
			return -1;
		}
		
		@Override
		public int tryInsert(ItemStack stack) {
			var total = 0;
			int added;
			while ((added = super.tryInsert(stack)) > 0)
				total += added;
			return total;
		}
		
		@Override
		protected int getMaxAmountToAdd(ItemStack stack) {
			var remainingOccupancy = maxOccupancy.subtract(weight());
			var itemOccupancy = BundleContentsComponentAccessor.invokeGetWeight(stack);
			var allowedByOccupancy = Math.max(remainingOccupancy.divideBy(itemOccupancy).intValue(), 0);
			
			var stacks = ((BundleContentsComponentBuilderAccessor) this).getItems();
			var allowedByStacks = 0;
			for (int i = 0; i < Math.min(maxStacks, stacks.size()); i++) {
				var slotStack = stacks.get(i);
				if (slotStack.isEmpty())
					allowedByStacks += stack.getMaxStackSize();
				if (ItemStack.isSameItemSameComponents(slotStack, stack))
					allowedByStacks += slotStack.getMaxStackSize() - stack.getCount();
			}
			
			return Math.min(allowedByOccupancy, allowedByStacks);
		}
		
	}
	
}
