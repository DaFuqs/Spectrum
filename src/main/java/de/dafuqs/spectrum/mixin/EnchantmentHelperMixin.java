package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.api.item.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

import java.util.*;
import java.util.stream.*;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	
	@ModifyExpressionValue(method = "getAvailableEnchantmentResults",
			at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
	))
	private static Stream<Holder<Enchantment>> doNothing(Stream<Holder<Enchantment>> original, @Local(argsOnly = true) ItemStack stack) {
		if (stack.getItem() instanceof Preenchanted preenchanted) {
			Map<ResourceKey<Enchantment>, Integer> defaultEnchantments = preenchanted.getDefaultEnchantments();
			return original.filter(enchantmentHolder -> !defaultEnchantments.containsKey(enchantmentHolder.getKey()));
		}
		return original;
	}
}
