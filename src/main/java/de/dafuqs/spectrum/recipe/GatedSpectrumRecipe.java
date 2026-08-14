package de.dafuqs.spectrum.recipe;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import org.jspecify.annotations.*;

import java.util.*;

public abstract class GatedSpectrumRecipe<C extends RecipeInput> implements GatedRecipe<C> {
	
	public final String group;
	public final Optional<ResourceLocation> requiredAdvancement;
	public final Optional<ResourceLocation> revealSecretAdvancement;
	protected final List<ItemStack> additionalResults; // these aren't actual results, but recipe managers will treat it as such, showing this recipe as a way to get them. Use for drops of the growth blocks, for example
	
	protected GatedSpectrumRecipe(String group, Optional<ResourceLocation> requiredAdvancement, Optional<ResourceLocation> revealSecretAdvancement, List<ItemStack> additionalResults) {
		this.group = group;
		this.revealSecretAdvancement = revealSecretAdvancement;
		this.requiredAdvancement = requiredAdvancement;
		this.additionalResults = additionalResults;
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}
	
	@Override
	public Optional<ResourceLocation> getRevealSecretAdvancement() {
		return this.revealSecretAdvancement;
	}
	
	/**
	 * The advancement the player has to have for the recipe be craftable
	 *
	 * @return The advancement identifier. A null value means the player is always able to craft this recipe
	 */
	@Override
	public Optional<ResourceLocation> getRequiredAdvancement() {
		return this.requiredAdvancement;
	}
	
	@Override
	public @Nullable ResourceLocation getRecipeTypeUnlockIdentifier() {
		return null;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}
	
	public List<ItemStack> getAdditionalResults() {
		return additionalResults;
	}
	
	protected static ItemStack getDefaultStackWithCount(Item item, int count) {
		ItemStack stack = item.getDefaultInstance();
		stack.setCount(count);
		return stack;
	}
	
	protected static ItemStack copyComponents(ItemStack recipeOutput, ItemStack stackToCopyComponentsFrom) {
		ItemEnchantments recipeOutputEnchantments = recipeOutput.getTagEnchantments();
		ItemEnchantments stackToCopyComponentsFromEnchantments = stackToCopyComponentsFrom.getTagEnchantments();
		
		ItemStack newOutput = stackToCopyComponentsFrom.transmuteCopy(recipeOutput.getItem(), recipeOutput.getCount());
		if(!recipeOutputEnchantments.isEmpty()) {
			EnchantmentHelper.setEnchantments(newOutput, recipeOutputEnchantments);
		}
		for (Object2IntMap.Entry<Holder<Enchantment>> entry : stackToCopyComponentsFromEnchantments.entrySet()) {
			SpectrumEnchantmentHelper.addOrUpgradeEnchantment(newOutput, entry.getKey(), entry.getIntValue(), false, false);
		}
		
		return newOutput;
	}
	
}
