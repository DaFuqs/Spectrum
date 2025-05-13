package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import net.fabricmc.fabric.api.tag.convention.v2.*;
import net.minecraft.item.*;
import net.minecraft.predicate.item.*;
import net.minecraft.registry.entry.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.*;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin {
	
	@Shadow
	@Final
	private Optional<RegistryEntryList<Item>> items;

	// FUCK THIS MIXIN
	// GO TO HELL - XOXO AZZYYPAARAS
	@ModifyExpressionValue(method = "test(Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/entry/RegistryEntryList;)Z"))
	public boolean redirectShearsPredicates(boolean original, @Local(argsOnly = true) ItemStack stack) {
		if (original)
			return true;
		
		assert items.isPresent();
		var entries = items.get();
		
		if (entries.stream().anyMatch(e -> e.value().equals(Items.SHEARS))) {
			return stack.isIn(ConventionalItemTags.SHEAR_TOOLS);
		}
		
		return false;
	}
	
}