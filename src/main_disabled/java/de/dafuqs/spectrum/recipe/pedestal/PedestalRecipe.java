package de.dafuqs.spectrum.recipe.pedestal;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class PedestalRecipe extends GatedStackSpectrumRecipe<PedestalRecipeInput> {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("place_pedestal");
	
	public static final int[] CRAFTING_GRID_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
	
	protected final PedestalRecipeTier tier;
	protected final List<IngredientStack> inputs;
	protected final Map<GemstoneColor, Integer> powderInputs;
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
	
	public PedestalRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier,
						  PedestalRecipeTier tier, List<IngredientStack> inputs, Map<GemstoneColor, Integer> powderInputs, ItemStack output,
						  float experience, int craftingTime, boolean skipRecipeRemainders, boolean noBenefitsFromYieldUpgrades) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.tier = tier;
		this.inputs = inputs;
		this.powderInputs = powderInputs;
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
	public static @Nullable PedestalVariant getUpgradedPedestalVariantForOutput(ItemStack outputItemStack) {
		if (outputItemStack.getItem() instanceof PedestalBlockItem pedestalBlockItem) {
			return pedestalBlockItem.getVariant();
		}
		return null;
	}
	
	@Override
	public boolean matches(PedestalRecipeInput inv, Level world) {
		int topazPowderAmount = this.powderInputs.getOrDefault(BuiltinGemstoneColor.CYAN, 0);
		int amethystPowderAmount = this.powderInputs.getOrDefault(BuiltinGemstoneColor.MAGENTA, 0);
		int citrinePowderAmount = this.powderInputs.getOrDefault(BuiltinGemstoneColor.YELLOW, 0);
		int onyxPowderAmount = this.powderInputs.getOrDefault(BuiltinGemstoneColor.BLACK, 0);
		int moonstonePowderAmount = this.powderInputs.getOrDefault(BuiltinGemstoneColor.WHITE, 0);
		
		return ((topazPowderAmount == 0 || isStackAtLeast(inv.getItem(9), SpectrumItems.TOPAZ_POWDER, topazPowderAmount))
				&& (amethystPowderAmount == 0 || isStackAtLeast(inv.getItem(10), SpectrumItems.AMETHYST_POWDER, amethystPowderAmount))
				&& (citrinePowderAmount == 0 || isStackAtLeast(inv.getItem(11), SpectrumItems.CITRINE_POWDER, citrinePowderAmount))
				&& (onyxPowderAmount == 0 || isStackAtLeast(inv.getItem(12), SpectrumItems.ONYX_POWDER, onyxPowderAmount))
				&& (moonstonePowderAmount == 0 || isStackAtLeast(inv.getItem(13), SpectrumItems.MOONSTONE_POWDER, moonstonePowderAmount)));
	}
	
	private boolean isStackAtLeast(ItemStack sourceItemStack, Item item, int amount) {
		return sourceItemStack.is(item) && sourceItemStack.getCount() >= amount;
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		return inputs;
	}
	
	@Override
	public ItemStack assemble(PedestalRecipeInput inventory, HolderLookup.Provider registryManager) {
		return this.output.copy();
	}
	
	public PedestalRecipeTier getTier() {
		return this.tier;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		return this.output;
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST);
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.PEDESTAL;
	}
	
	public int getGemstonePowderAmount(GemstoneColor gemstoneColor) {
		return powderInputs.getOrDefault(gemstoneColor, 0);
	}
	
	public int getCraftingTime() {
		return craftingTime;
	}
	
	public float getExperience() {
		return this.experience;
	}
	
	public Map<GemstoneColor, Integer> getPowderInputs() {
		return this.powderInputs;
	}
	
	/**
	 * Returns a sound event matching for this recipe.
	 * Dependent on the amount of gemstone dust used in it
	 *
	 * @return The sound effect to play when this recipe is finished
	 */
	public SoundEvent getSoundEvent(RandomSource random) {
		List<SoundEvent> choices = new ArrayList<>();
		
		for (int i = 0; i < this.powderInputs.getOrDefault(BuiltinGemstoneColor.MAGENTA, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_AMETHYST);
		}
		for (int i = 0; i < this.powderInputs.getOrDefault(BuiltinGemstoneColor.YELLOW, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_CITRINE);
		}
		for (int i = 0; i < this.powderInputs.getOrDefault(BuiltinGemstoneColor.CYAN, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_TOPAZ);
		}
		for (int i = 0; i < this.powderInputs.getOrDefault(BuiltinGemstoneColor.BLACK, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_ONYX);
		}
		for (int i = 0; i < this.powderInputs.getOrDefault(BuiltinGemstoneColor.WHITE, 0); i++) {
			choices.add(SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_MOONSTONE);
		}
		
		if (choices.isEmpty()) {
			return SpectrumSoundEvents.PEDESTAL_CRAFTING_FINISHED_GENERIC;
		} else {
			return choices.get(random.nextInt(choices.size()));
		}
	}
	
	public void consumeIngredients(PedestalBlockEntity pedestal) {
		// consume the required gemstone powders
		Level world = pedestal.getLevel();
		if (world == null) return;
		for (GemstoneColor gemstoneColor : BuiltinGemstoneColor.values()) {
			double efficiencyModifier = pedestal.getUpgradeHolder().getEffectiveValue(Upgradeable.UpgradeType.EFFICIENCY);
			int gemstonePowderAmount = this.getGemstonePowderAmount(gemstoneColor);
			int gemstonePowderAmountAfterMod = Support.getIntFromDecimalWithChance(gemstonePowderAmount / efficiencyModifier, world.random);
			pedestal.getItem(PedestalBlockEntity.getSlotForGemstonePowder(gemstoneColor)).shrink(gemstonePowderAmountAfterMod);
		}
	}
	
	protected void decrementGridSlot(PedestalBlockEntity pedestal, int slot, int count, ItemStack invStack) {
		ItemStack remainder = this.skipRecipeRemainders() ? ItemStack.EMPTY : invStack.getRecipeRemainder();
		remainder.setCount(count);
		if (pedestal.getLevel() == null) return;
		if (remainder.isEmpty()) {
			invStack.shrink(count);
		} else {
			if (pedestal.getItem(slot).getCount() == count) {
				pedestal.setItem(slot, remainder);
			} else {
				pedestal.getItem(slot).shrink(count);
				
				ItemEntity itemEntity = new ItemEntity(pedestal.getLevel(), pedestal.getBlockPos().getX() + 0.5, pedestal.getBlockPos().getY() + 1, pedestal.getBlockPos().getZ() + 0.5, remainder);
				itemEntity.push(0, 0.05, 0);
				pedestal.getLevel().addFreshEntity(itemEntity);
			}
		}
	}
	
	public boolean canCraft(PedestalBlockEntity pedestalBlockEntity) {
		Player playerEntity = pedestalBlockEntity.getOwnerIfOnline();
		if (playerEntity == null || !canPlayerCraft(playerEntity)) {
			return false;
		}
		
		@Nullable PedestalVariant newPedestalVariant = getUpgradedPedestalVariantForOutput(this.output);
		if (newPedestalVariant == null) {
			return pedestalBlockEntity.getHighestAvailableRecipeTier().ordinal() >= this.tier.ordinal();
		} else {
			// pedestal upgrade recipes do not require the structure,
			// but you can only craft it using the previous variant
			int currentTier = PedestalBlockEntity.getVariant(pedestalBlockEntity).getRecipeTier().ordinal();
			int thisTier = newPedestalVariant.getRecipeTier().ordinal();
			return thisTier == currentTier + 1;
		}
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public boolean canPlayerCraft(Player playerEntity) {
		return this.tier.hasUnlocked(playerEntity) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier.orElse(null));
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "pedestal";
	}
	
	public boolean areYieldUpgradesDisabled() {
		return noBenefitsFromYieldUpgrades;
	}
	
	public boolean skipRecipeRemainders() {
		return this.skipRecipeRemainders;
	}
	
	public int getWidth() {
		return 3;
	}
	
	public int getHeight() {
		return 3;
	}
	
	public boolean isShapeless() {
		return true;
	}
	
	public int getGridSlotId(int index) {
		int width = getWidth();
		int x = index % width;
		int y = (index - x) / width;
		return 3 * y + x;
	}
	
}
