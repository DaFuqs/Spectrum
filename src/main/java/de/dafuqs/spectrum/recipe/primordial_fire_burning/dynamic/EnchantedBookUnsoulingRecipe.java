package de.dafuqs.spectrum.recipe.primordial_fire_burning.dynamic;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import net.minecraft.enchantment.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class EnchantedBookUnsoulingRecipe extends PrimordialFireBurningRecipe {
	
	public static final RecipeSerializer<EnchantedBookUnsoulingRecipe> SERIALIZER = new EmptyRecipeSerializer<>(EnchantedBookUnsoulingRecipe::new);
	
	public EnchantedBookUnsoulingRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER,
				Ingredient.ofStacks(SpectrumEnchantmentHelper.addOrExchangeEnchantment(Items.ENCHANTED_BOOK.getDefaultStack(), Enchantments.SOUL_SPEED, 1, false, false)),
				SpectrumEnchantmentHelper.addOrExchangeEnchantment(Items.ENCHANTED_BOOK.getDefaultStack(), Enchantments.SWIFT_SNEAK, 1, false, false));
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		ItemStack stack = inv.getStack(0);
		return EnchantmentHelper.get(stack).containsKey(Enchantments.SOUL_SPEED);
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		ItemStack stack = inv.getStack(0);
		
		int level = EnchantmentHelper.get(stack).getOrDefault(Enchantments.SOUL_SPEED, 0);
		if(level > 0) {
			stack = SpectrumEnchantmentHelper.removeEnchantments(stack, Enchantments.SOUL_SPEED).getLeft();
			stack = SpectrumEnchantmentHelper.addOrExchangeEnchantment(stack, Enchantments.SWIFT_SNEAK, level, false, false);
		}
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
