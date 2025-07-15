package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import net.fabricmc.fabric.api.tag.convention.v2.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.*;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin {
	
	@Shadow
	@Final
	private Optional<HolderSet<Item>> items;
	
	@ModifyExpressionValue(method = "test(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/core/HolderSet;)Z"))
	public boolean redirectShearsPredicates(boolean original, @Local(argsOnly = true) ItemStack stack) {
		if (original)
			return true;
		
		assert items.isPresent();
		var entries = items.get();
		
		if (entries.stream().anyMatch(e -> e.value().equals(Items.SHEARS))) {
			return stack.is(Tags.Items.TOOLS_SHEAR);
		}
		
		return false;
	}
	
}