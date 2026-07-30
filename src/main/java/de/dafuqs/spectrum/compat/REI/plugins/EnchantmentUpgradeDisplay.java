package de.dafuqs.spectrum.compat.REI.plugins;

import com.google.common.collect.*;
import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import dev.emi.emi.api.stack.*;
import me.shedaniel.rei.api.common.category.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import javax.annotation.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class EnchantmentUpgradeDisplay extends EnchanterDisplay {
	
	protected final Holder<Enchantment> enchantment;
	protected final int enchantMaxLevel;
	protected final int recipeMaxLevel;
	protected final Component transKey;
	protected final List<EnchantmentUpgradeRecipe.LevelData> levelData;
	
	int index = 0;
	
	public EnchantmentUpgradeDisplay(RecipeHolder<EnchantmentUpgradeRecipe> recipeEntry) {
		super(recipeEntry, buildIngredients(recipeEntry.value()), buildOutputs(recipeEntry.value()));
		
		var recipe = recipeEntry.value();
		enchantment = recipe.getEnchantment();
		
		enchantMaxLevel = enchantment.value().getMaxLevel();
		recipeMaxLevel = recipe.getLevelCap();
		levelData = recipe.getLevelData();
		transKey = enchantment.value().description().copy().withStyle(s -> {
			s.withItalic(true);
			s.withColor(EnchantmentUpgradeCategory.NORMAL_COLOR);
			return s;
		});
	}
	
	private static List<EntryIngredient> buildIngredients(EnchantmentUpgradeRecipe recipe) {
		Holder<Enchantment> enchant = recipe.getEnchantment();
		int levelCap = recipe.getLevelCap();
		
		List<EntryIngredient> inputs = new ArrayList<>();
		
		var knowledgeGem = new ArrayList<ItemStack>();
		var enchantedBooks = new ArrayList<ItemStack>();
		var bowlIngredients = new ArrayList<Ingredient>();
		for (int level = 1; level < levelCap; level++) {
			knowledgeGem.add(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getRequiredXPForSourceLevel(level), true));
			enchantedBooks.add(SpectrumEnchantmentHelper.getEnchantedBookStackWith(enchant, level));
			bowlIngredients.add(recipe.getLevelData().get(level-1).ingredient());
		}
		inputs.add(EntryIngredients.ofItemStacks(knowledgeGem));
		inputs.add(EntryIngredients.ofItemStacks(enchantedBooks));
		inputs.addAll(EntryIngredients.ofIngredients(bowlIngredients));
		
		return inputs;
	}
	
	private static List<EntryIngredient> buildOutputs(EnchantmentUpgradeRecipe recipe) {
		var stacks = new ArrayList<ItemStack>();
		var levelCap = recipe.getLevelCap();
		
		for (int level = 1; level < levelCap; level++) {
			stacks.add(SpectrumEnchantmentHelper.getEnchantedBookStackWith(recipe.getEnchantment(), level + 1));
		}
		
		return Collections.singletonList(EntryIngredients.ofItemStacks(stacks));
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.ENCHANTMENT_UPGRADE;
	}
	
	@Override
	public boolean isUnlocked() {
		Minecraft client = Minecraft.getInstance();
		return AdvancementHelper.hasAdvancement(client.player, EnchanterRecipe.UNLOCK_IDENTIFIER) && super.isUnlocked();
	}
}
