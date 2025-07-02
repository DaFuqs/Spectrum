package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.items.bundles.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BundleItem.class)
public class BundleItemMixin {
	
	@ModifyVariable(method = "overrideStackedOnOther", at = @At(value = "STORE"))
	private BundleContents.Mutable spectrum$onStackClicked$replaceBuilder(BundleContents.Mutable builder, ItemStack stack) {
		return getBuilder(stack);
	}
	
	@ModifyVariable(method = "overrideOtherStackedOnMe", at = @At(value = "STORE"))
	private BundleContents.Mutable spectrum$onClicked$replaceBuilder(BundleContents.Mutable builder, ItemStack stack) {
		return getBuilder(stack);
	}
	
	@Unique
	private static BundleContents.Mutable getBuilder(ItemStack stack) {
		var component = stack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
		if (stack.getItem() instanceof ExtendedBundleItem)
			return new ExtendedBundleItem.ComponentBuilder(component, ExtendedBundleItem.getMaxOccupancy(stack), ExtendedBundleItem.getMaxStacks(stack));
		return new BundleContents.Mutable(component);
	}
}
