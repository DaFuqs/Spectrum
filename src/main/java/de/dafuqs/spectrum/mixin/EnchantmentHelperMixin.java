package de.dafuqs.spectrum.mixin;

import com.google.common.collect.*;
import com.llamalad7.mixinextras.injector.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	
	@Inject(method = "getPossibleEntries(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
	private static void spectrum$getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
		if (stack.getItem() instanceof ExtendedEnchantable) {
			List<EnchantmentLevelEntry> list = Lists.newArrayList();
			Iterator<Enchantment> enchantments = Registries.ENCHANTMENT.iterator();
			
			while (true) {
				Enchantment enchantment;
				do {
					do {
						do {
							if (!enchantments.hasNext()) {
								cir.setReturnValue(list);
								return;
							}
							
							enchantment = enchantments.next();
						} while (enchantment.isTreasure() && !treasureAllowed);
					} while (!enchantment.isAvailableForRandomSelection());
				} while (!enchantment.isAcceptableItem(stack)); // this line is the only change, away from "enchantment.type.isAcceptableItem(item)"
				for (int level = enchantment.getMaxLevel(); level > enchantment.getMinLevel() - 1; --level) {
					if (power >= enchantment.getMinPower(level) && power <= enchantment.getMaxPower(level)) {
						list.add(new EnchantmentLevelEntry(enchantment, level));
						break;
					}
				}
			}
		}
	}
	
	@ModifyReturnValue(method = "getPossibleEntries(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;", at = @At("RETURN"))
	private static List<EnchantmentLevelEntry> spectrum$disallowConflictingAndDuplicateEnchants(List<EnchantmentLevelEntry> original, int power, ItemStack stack, boolean treasureAllowed) {
		for (Enchantment entry : EnchantmentHelper.get(stack).keySet()) {
			original.removeIf(levelEntry -> levelEntry.enchantment == entry || !levelEntry.enchantment.canCombine(entry));
		}
		return original;
	}
	
}
