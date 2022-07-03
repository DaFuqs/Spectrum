package de.dafuqs.spectrum.recipe.spirit_instiller;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.memory.MemoryItem;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class SpiritInstillerRecipe implements ISpiritInstillerRecipe {
	
	protected final Identifier id;
	protected final String group;
	
	protected final IngredientStack centerIngredient;
	protected final IngredientStack bowlIngredient1;
	protected final IngredientStack bowlIngredient2;
	protected final ItemStack outputItemStack;
	
	protected final int craftingTime;
	protected final float experience;
	protected final Identifier requiredAdvancementIdentifier;
	protected final boolean noBenefitsFromYieldAndEfficiencyUpgrades;
	
	public SpiritInstillerRecipe(Identifier id, String group, IngredientStack centerIngredient, IngredientStack bowlIngredient1, IngredientStack bowlIngredient2, ItemStack outputItemStack, int craftingTime, float experience, boolean noBenefitsFromYieldAndEfficiencyUpgrades, Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.group = group;
		this.centerIngredient = centerIngredient;
		this.bowlIngredient1 = bowlIngredient1;
		this.bowlIngredient2 = bowlIngredient2;
		this.outputItemStack = outputItemStack;
		this.craftingTime = craftingTime;
		this.experience = experience;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		this.noBenefitsFromYieldAndEfficiencyUpgrades = noBenefitsFromYieldAndEfficiencyUpgrades;
		
		registerInToastManager(SpectrumRecipeTypes.SPIRIT_INSTILLING, this);
	}
	
	@Override
	public ItemStack getOutput() {
		return outputItemStack.copy();
	}
	
	@Override
	public Identifier getId() {
		return this.id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLING_SERIALIZER;
	}
	
	@Deprecated
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.centerIngredient.getIngredient());
		defaultedList.add(this.bowlIngredient1.getIngredient());
		defaultedList.add(this.bowlIngredient2.getIngredient());
		return defaultedList;
	}
	
	public List<IngredientStack> getIngredientStacks() {
		DefaultedList<IngredientStack> defaultedList = DefaultedList.of();
		defaultedList.add(this.centerIngredient);
		defaultedList.add(this.bowlIngredient1);
		defaultedList.add(this.bowlIngredient2);
		return defaultedList;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof SpiritInstillerRecipe spiritInstillerRecipe) {
			return spiritInstillerRecipe.getId().equals(this.getId());
		}
		return false;
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		ItemStack resultStack = ItemStack.EMPTY;
		if (inv instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
			Map<Upgradeable.UpgradeType, Float> upgrades = spiritInstillerBlockEntity.getUpgrades();
			World world = spiritInstillerBlockEntity.getWorld();
			BlockPos pos = spiritInstillerBlockEntity.getPos();
			
			resultStack = getOutput().copy();
			
			// Yield upgrade
			if (!areYieldAndEfficiencyUpgradesDisabled() && upgrades.get(Upgradeable.UpgradeType.YIELD) != 1.0) {
				int resultCountMod = Support.getIntFromDecimalWithChance(resultStack.getCount() * upgrades.get(Upgradeable.UpgradeType.YIELD), world.random);
				resultStack.setCount(resultCountMod);
			}
			
			if (resultStack.isOf(SpectrumBlocks.MEMORY.asItem())) {
				boolean makeUnrecognizable = spiritInstillerBlockEntity.getStack(0).isIn(SpectrumItemTags.MEMORY_BONDING_AGENTS_CONCEALABLE);
				if (makeUnrecognizable) {
					MemoryItem.makeUnrecognizable(resultStack);
				}
			}
			
			// Calculate and spawn experience
			int awardedExperience = 0;
			if (getExperience() > 0) {
				double experienceModifier = upgrades.get(Upgradeable.UpgradeType.EXPERIENCE);
				float recipeExperienceBeforeMod = getExperience();
				awardedExperience = Support.getIntFromDecimalWithChance(recipeExperienceBeforeMod * experienceModifier, world.random);
				MultiblockCrafter.spawnExperience(world, pos.up(), awardedExperience);
			}
			
			// Run Advancement trigger
			ISpiritInstillerRecipe.grantPlayerSpiritInstillingAdvancementCriterion(spiritInstillerBlockEntity.getOwnerUUID(), resultStack, awardedExperience);
		}
		
		return resultStack;
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}
	
	public float getExperience() {
		return experience;
	}
	
	public int getCraftingTime() {
		return craftingTime;
	}
	
	public Identifier getRequiredAdvancementIdentifier() {
		return requiredAdvancementIdentifier;
	}
	
	public boolean areYieldAndEfficiencyUpgradesDisabled() {
		return noBenefitsFromYieldAndEfficiencyUpgrades;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, UNLOCK_ADVANCEMENT_IDENTIFIER) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public boolean canCraftWithStacks(ItemStack instillerStack, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		return true;
	}
	
}
