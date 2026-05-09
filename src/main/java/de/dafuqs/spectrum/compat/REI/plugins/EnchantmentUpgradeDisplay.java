package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
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

public class EnchantmentUpgradeDisplay extends EnchanterDisplay {
	
	protected final Holder<Enchantment> enchantment;
	
	final int enchantMaxLevel;
	final int recipeMaxLevel;
	
	final Component transKey;
	final RecipeScaling.ScalingData itemScaling;
	final RecipeScaling.ScalingData xpScaling;
	
	int index = 0;
	
	public EnchantmentUpgradeDisplay(RecipeHolder<EnchantmentUpgradeRecipe> recipeEntry) {
		super(recipeEntry, buildIngredients(recipeEntry.value()), buildOutputs(recipeEntry.value()));
		
		var recipe = recipeEntry.value();
		enchantment = recipe.getEnchantment();
		
		enchantMaxLevel = enchantment.value().getMaxLevel();
		recipeMaxLevel = recipe.getLevelCap();
		
		itemScaling = recipe.getItemScaling();
		xpScaling = recipe.getXPScaling();
		transKey = enchantment.value().description().copy().withStyle(s -> {
			s.withItalic(true);
			s.withColor(EnchantmentUpgradeCategory.NORMAL_COLOR);
			return s;
		});
	}
	
	private static List<EntryIngredient> buildIngredients(EnchantmentUpgradeRecipe recipe) {
		List<EntryIngredient> inputs = new ArrayList<>();
		
		var enchant = recipe.getEnchantment();
		var levelCap = recipe.getLevelCap();
		
		var knowledgeGem = new ArrayList<ItemStack>();
		var enchantedBooks = new ArrayList<ItemStack>();
		
		for (int i = 0; i < 8; i++) {
			inputs.add(EntryIngredients.of(recipe.getBulkItem(), 1));
		}
		
		for (int level = 1; level < levelCap; level++) {
			knowledgeGem.add(KnowledgeGemItem.getKnowledgeDropStackWithXP(recipe.getXPScaling().apply(level), true));
			enchantedBooks.add(getEnchantedBookStackWith(enchant, level));
		}
		
		inputs.add(EntryIngredients.ofItemStacks(knowledgeGem));
		inputs.add(EntryIngredients.ofItemStacks(enchantedBooks));
		return inputs;
	}
	
	private static List<EntryIngredient> buildOutputs(EnchantmentUpgradeRecipe recipe) {
		var stacks = new ArrayList<ItemStack>();
		var levelCap = recipe.getLevelCap();
		
		for (int level = 1; level < levelCap; level++) {
			stacks.add(getEnchantedBookStackWith(recipe.getEnchantment(), level + 1));
		}
		
		return Collections.singletonList(EntryIngredients.ofItemStacks(stacks));
	}
	
	private static ItemStack getEnchantedBookStackWith(Holder<Enchantment> enchant, int level) {
		var enchStack = new ItemStack(Items.ENCHANTED_BOOK);
		var builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		builder.set(enchant, level);
		enchStack.set(DataComponents.STORED_ENCHANTMENTS, builder.toImmutable());
		return enchStack;
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
