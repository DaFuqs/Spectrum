package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import java.util.stream.*;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	
	@ModifyExpressionValue(method = "getAvailableEnchantmentResults",
			at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
	))
	private static Stream<Holder<Enchantment>> doNothing(Stream<Holder<Enchantment>> original, @Local(argsOnly = true) ItemStack stack) {
		if (!(stack.getItem() instanceof Preenchanted)) {
			return original;
		}
		ItemEnchantments itemEnchantments = stack.get(DataComponents.ENCHANTMENTS);
		if (itemEnchantments == null)
			return original;
		//Do not enchant an item with enchantments already present on the itemstack
		return original.filter(enchantmentHolder -> !itemEnchantments.keySet().contains(enchantmentHolder));
	}
}
