package de.dafuqs.spectrum.recipe.pedestal;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.enums.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import oshi.util.tuples.*;

import java.util.*;

public class PedestalCraftingRecipe extends GatedSpectrumRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	
	protected final int width;
	protected final int height;
	
	protected final PedestalRecipeTier tier;
	protected final DefaultedList<IngredientStack> craftingInputs;
	protected final Map<BuiltinGemstoneColor, Integer> gemstonePowderInputs;
	protected final ItemStack output;
	protected final float experience;
	protected final int craftingTime;
	protected final boolean skipRecipeRemainders; // true means the recipe does not return remainders, like empty buckets from water buckets
	// since there are a few recipes that are basically compacting recipes
	// they could be crafted ingots>block and block>ingots back
	// In that case:
	// - the player should not get XP
	// - Yield upgrades disabled (item multiplication)
	protected final boolean noBenefitsFromYieldUpgrades;
	
	public PedestalCraftingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
								  PedestalRecipeTier tier, int width, int height, DefaultedList<IngredientStack> craftingInputs, Map<BuiltinGemstoneColor, Integer> gemstonePowderInputs, ItemStack output,
								  float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.tier = tier;
		this.width = width;
		this.height = height;
		this.craftingInputs = craftingInputs;
		this.gemstonePowderInputs = gemstonePowderInputs;
		this.output = output;
		this.experience = experience;
		this.craftingTime = craftingTime;
		this.skipRecipeRemainders = skipRecipeRemainders;
		this.noBenefitsFromYieldUpgrades = noBenefitsFromYieldUpgrades;
		
		registerInToastManager(getType(), this);
	}
	
	/**
	 * When a recipe is set to output a pedestal block item
	 * it is treated as an upgrade recipe. Meaning the item does not
	 * get crafted, but the current pedestal replaced with the new one.
	 */
	public static PedestalVariant getUpgradedPedestalVariantForOutput(ItemStack outputItemStack) {
		if (outputItemStack.getItem() instanceof PedestalBlockItem) {
			return ((PedestalBlockItem) outputItemStack.getItem()).getVariant();
		} else {
			return null;
		}
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		if (!matchesGrid(inv)) {
			return false;
		}
		
		int topazPowderAmount = this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.CYAN, 0);
		int amethystPowderAmount = this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.MAGENTA, 0);
		int citrinePowderAmount = this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.YELLOW, 0);
		int onyxPowderAmount = this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.BLACK, 0);
		int moonstonePowderAmount = this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.WHITE, 0);
		
		return ((topazPowderAmount == 0 || isStackAtLeast(inv.getStack(9), SpectrumItems.TOPAZ_POWDER, topazPowderAmount))
				&& (amethystPowderAmount == 0 || isStackAtLeast(inv.getStack(10), SpectrumItems.AMETHYST_POWDER, amethystPowderAmount))
				&& (citrinePowderAmount == 0 || isStackAtLeast(inv.getStack(11), SpectrumItems.CITRINE_POWDER, citrinePowderAmount))
				&& (onyxPowderAmount == 0 || isStackAtLeast(inv.getStack(12), SpectrumItems.ONYX_POWDER, onyxPowderAmount))
				&& (moonstonePowderAmount == 0 || isStackAtLeast(inv.getStack(13), SpectrumItems.MOONSTONE_POWDER, moonstonePowderAmount)));
	}
	
	public boolean matchesGrid(Inventory inv) {
		for (int i = 0; i <= 3 - this.width; ++i) {
			for (int j = 0; j <= 3 - this.height; ++j) {
				if (this.matchesPattern(inv, i, j, true)) {
					return true;
				}
				
				if (this.matchesPattern(inv, i, j, false)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean matchesPattern(Inventory inv, int offsetX, int offsetY, boolean flipped) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int k = i - offsetX;
				int l = j - offsetY;
				IngredientStack ingredient = IngredientStack.EMPTY;
				if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
					if (flipped) {
						ingredient = this.craftingInputs.get(this.width - k - 1 + l * this.width);
					} else {
						ingredient = this.craftingInputs.get(k + l * this.width);
					}
				}
				
				if (!ingredient.test(inv.getStack(i + j * 3))) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean isStackAtLeast(ItemStack sourceItemStack, Item item, int amount) {
		return sourceItemStack.getItem().equals(item) && sourceItemStack.getCount() >= amount;
	}
	
	@Deprecated
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		var defList = DefaultedList.<Ingredient>of();
		craftingInputs.stream().map(IngredientStack::getIngredient).forEach(defList::add);
		return defList;
	}
	
	public DefaultedList<IngredientStack> getIngredientStacks() {
		return craftingInputs;
	}
	
	// Triplet<XOffset, YOffset, Flipped>
	public Triplet<Integer, Integer, Boolean> getRecipeOrientation(Inventory inv) {
		for (int i = 0; i <= 3 - this.width; ++i) {
			for (int j = 0; j <= 3 - this.height; ++j) {
				if (this.matchesPattern(inv, i, j, true)) {
					return new Triplet<>(i, j, true);
				}
				if (this.matchesPattern(inv, i, j, false)) {
					return new Triplet<>(i, j, false);
				}
			}
		}
		return null;
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		return this.output.copy();
	}
	
	public ItemStack craftAndDecrement(Inventory inv, DynamicRegistryManager drm) {
		if (inv instanceof PedestalBlockEntity pedestal) {
			Triplet<Integer, Integer, Boolean> orientation = getRecipeOrientation(inv);
			if (orientation == null) {
				return ItemStack.EMPTY;
			}
			decrementIngredientStacks(pedestal, orientation);
			
			ItemStack recipeOutput = craft(inv, drm);
			PlayerEntity player = pedestal.getOwnerIfOnline();
			if (player != null) {
				recipeOutput.onCraft(pedestal.getWorld(), player, recipeOutput.getCount());
			}
			return recipeOutput;
		}
		return ItemStack.EMPTY;
	}
	
	protected void decrementIngredientStacks(PedestalBlockEntity pedestal, Triplet<Integer, Integer, Boolean> orientation) {
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				int ingredientStackId = orientation.getC() ? ((this.width - 1) - x) + this.width * y : x + this.width * y;
				int invStackId = (x + orientation.getA()) + 3 * (y + orientation.getB());
				
				IngredientStack ingredientStackAtPos = this.craftingInputs.get(ingredientStackId);
				ItemStack invStack = pedestal.getStack(invStackId);
				if (!ingredientStackAtPos.test(invStack)) {
					SpectrumCommon.logError("Looks like DaFuqs fucked up Spectrums Pedestal recipe matching. Go open up a report with the recipe that was crafted and an image of the pedestals contents, please! :)");
				}
				
				if (!invStack.isEmpty()) {
					Item recipeReminderItem = this.skipRecipeRemainders() ? null : invStack.getItem().getRecipeRemainder();
					if (recipeReminderItem == null) {
						invStack.decrement(ingredientStackAtPos.getCount());
					} else {
						if (pedestal.getStack(invStackId).getCount() == ingredientStackAtPos.getCount()) {
							ItemStack remainderStack = recipeReminderItem.getDefaultStack();
							remainderStack.setCount(ingredientStackAtPos.getCount());
							pedestal.setStack(invStackId, remainderStack);
						} else {
							pedestal.getStack(invStackId).decrement(ingredientStackAtPos.getCount());
							
							ItemStack remainderStack = recipeReminderItem.getDefaultStack();
							ItemEntity itemEntity = new ItemEntity(pedestal.getWorld(), pedestal.getPos().getX() + 0.5, pedestal.getPos().getY() + 1, pedestal.getPos().getZ() + 0.5, remainderStack);
							itemEntity.addVelocity(0, 0.05, 0);
							pedestal.getWorld().spawnEntity(itemEntity);
						}
					}
				}
				
			}
		}
		
		// -X for all the pigment inputs
		for (BuiltinGemstoneColor gemstoneColor : BuiltinGemstoneColor.values()) {
			double efficiencyModifier = pedestal.getUpgradeHolder().getEffectiveValue(Upgradeable.UpgradeType.EFFICIENCY);
			int gemstonePowderAmount = this.getGemstonePowderAmount(gemstoneColor);
			int gemstonePowderAmountAfterMod = Support.getIntFromDecimalWithChance(gemstonePowderAmount / efficiencyModifier, pedestal.getWorld().random);
			pedestal.getStack(PedestalBlockEntity.getSlotForGemstonePowder(gemstoneColor)).decrement(gemstonePowderAmountAfterMod);
		}
	}
	
	public PedestalRecipeTier getTier() {
		return this.tier;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager drm) {
		return this.output;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.PEDESTAL_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.PEDESTAL;
	}
	
	public int getGemstonePowderAmount(GemstoneColor gemstoneColor) {
		return gemstonePowderInputs.getOrDefault(gemstoneColor, 0);
	}
	
	public int getCraftingTime() {
		return craftingTime;
	}
	
	public float getExperience() {
		return this.experience;
	}
	
	public Map<BuiltinGemstoneColor, Integer> getGemstonePowderInputs() {
		return this.gemstonePowderInputs;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Returns a sound event matching for this recipe.
	 * Dependent on the amount of gemstone dust used in it
	 *
	 * @return The sound effect to play when this recipe is finished
	 */
	public SoundEvent getSoundEvent(Random random) {
		List<SoundEvent> choices = new ArrayList<>();
		
		for (int i = 0; i < this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.MAGENTA, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_AMETHYST);
		}
		for (int i = 0; i < this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.YELLOW, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_CITRINE);
		}
		for (int i = 0; i < this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.CYAN, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_TOPAZ);
		}
		for (int i = 0; i < this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.BLACK, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_ONYX);
		}
		for (int i = 0; i < this.gemstonePowderInputs.getOrDefault(BuiltinGemstoneColor.WHITE, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_MOONSTONE);
		}
		
		if (choices.size() == 0) {
			return SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_GENERIC;
		} else {
			return choices.get(random.nextInt(choices.size()));
		}
	}
	
	public boolean canCraft(PedestalBlockEntity pedestalBlockEntity) {
		PlayerEntity playerEntity = pedestalBlockEntity.getOwnerIfOnline();
		if (playerEntity == null || !canPlayerCraft(playerEntity)) {
			return false;
		}
		
		@Nullable PedestalVariant newPedestalVariant = getUpgradedPedestalVariantForOutput(this.output);
		if (newPedestalVariant == null) {
			return pedestalBlockEntity.getHighestAvailableRecipeTierWithStructure().ordinal() >= this.tier.ordinal();
		} else {
			// pedestal upgrade recipes do not require the structure,
			// but you can only craft it using the previous variant
			int currentTier = PedestalBlockEntity.getVariant(pedestalBlockEntity).getRecipeTier().ordinal();
			int thisTier = newPedestalVariant.getRecipeTier().ordinal();
			return thisTier == currentTier + 1;
		}
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return PedestalRecipeTier.hasUnlockedRequiredTier(playerEntity, this.tier) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.PEDESTAL_RECIPE_ID;
	}
	
	public boolean areYieldUpgradesDisabled() {
		return noBenefitsFromYieldUpgrades;
	}
	
	public boolean skipRecipeRemainders() {
		return this.skipRecipeRemainders;
	}
	
}
