package de.dafuqs.spectrum.recipe.spirit_instiller;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.memory.MemoryItem;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.GatedSpectrumRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpiritInstillerRecipe extends GatedSpectrumRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/build_spirit_instiller_structure");
	
	public static final int CENTER_INGREDIENT = 0;
	public static final int FIRST_INGREDIENT = 1;
	public static final int SECOND_INGREDIENT = 2;

	protected final IngredientStack centerIngredient;
	protected final IngredientStack bowlIngredient1;
	protected final IngredientStack bowlIngredient2;
	protected final ItemStack outputItemStack;
	
	protected final int craftingTime;
	protected final float experience;
	protected final boolean noBenefitsFromYieldAndEfficiencyUpgrades;
	
	public SpiritInstillerRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
	                             IngredientStack centerIngredient, IngredientStack bowlIngredient1, IngredientStack bowlIngredient2, ItemStack outputItemStack, int craftingTime, float experience, boolean noBenefitsFromYieldAndEfficiencyUpgrades) {
		
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.centerIngredient = centerIngredient;
		this.bowlIngredient1 = bowlIngredient1;
		this.bowlIngredient2 = bowlIngredient2;
		this.outputItemStack = outputItemStack;
		this.craftingTime = craftingTime;
		this.experience = experience;
		this.noBenefitsFromYieldAndEfficiencyUpgrades = noBenefitsFromYieldAndEfficiencyUpgrades;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		List<IngredientStack> ingredientStacks = getIngredientStacks();
		if (inv.size() > 2) {
			if (ingredientStacks.get(CENTER_INGREDIENT).test(inv.getStack(0))) {
				if (ingredientStacks.get(FIRST_INGREDIENT).test(inv.getStack(1))) {
					return ingredientStacks.get(SECOND_INGREDIENT).test(inv.getStack(2));
				} else if (ingredientStacks.get(FIRST_INGREDIENT).test(inv.getStack(2))) {
					return ingredientStacks.get(SECOND_INGREDIENT).test(inv.getStack(1));
				}
			}
		}
		return false;
	}
	
	@Override
	public ItemStack getOutput() {
		return outputItemStack.copy();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLING_SERIALIZER;
	}
	
	// Use getIngredientStacks() instead
	// that includes counts in stacks
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
			grantPlayerSpiritInstillingAdvancementCriterion(spiritInstillerBlockEntity.getOwnerUUID(), resultStack, awardedExperience);
		}
		
		return resultStack;
	}
	
	protected static void grantPlayerSpiritInstillingAdvancementCriterion(UUID playerUUID, ItemStack resultStack, int experience) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(playerUUID);
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.SPIRIT_INSTILLER_CRAFTING.trigger(serverPlayerEntity, resultStack, experience);
		}
	}
	
	public float getExperience() {
		return experience;
	}
	
	public int getCraftingTime() {
		return craftingTime;
	}
	
	public boolean areYieldAndEfficiencyUpgradesDisabled() {
		return noBenefitsFromYieldAndEfficiencyUpgrades;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, UNLOCK_IDENTIFIER) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLING_ID;
	}
	
	public boolean canCraftWithStacks(ItemStack instillerStack, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		return true;
	}
	
	@Override
	public  ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.SPIRIT_INSTILLER);
	}
	
	@Override
	public  RecipeType<?> getType() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLING;
	}
	
	@Override
	public  boolean fits(int width, int height) {
		return width * height >= 3;
	}
	
}
