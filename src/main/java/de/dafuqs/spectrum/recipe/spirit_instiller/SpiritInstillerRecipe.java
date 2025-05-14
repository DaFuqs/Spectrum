package de.dafuqs.spectrum.recipe.spirit_instiller;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

import java.util.*;

public class SpiritInstillerRecipe extends GatedStackSpectrumRecipe<InstanceRecipeInput<SpiritInstillerBlockEntity>> {
	
	public static final int CENTER_INGREDIENT = 0;
	public static final int FIRST_INGREDIENT = 1;
	public static final int SECOND_INGREDIENT = 2;
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/build_spirit_instiller_structure");
	
	protected final IngredientStack centerIngredient;
	protected final IngredientStack bowlIngredient1;
	protected final IngredientStack bowlIngredient2;
	protected final ItemStack output;
	
	protected final int craftingTime;
	protected final float experience;
	protected final boolean noBenefitsFromYieldAndEfficiencyUpgrades;
	
	public SpiritInstillerRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier,
								 IngredientStack centerIngredient, IngredientStack bowlIngredient1, IngredientStack bowlIngredient2, ItemStack output, int craftingTime, float experience, boolean noBenefitsFromYieldAndEfficiencyUpgrades) {
		
		super(group, secret, requiredAdvancementIdentifier);
		
		this.centerIngredient = centerIngredient;
		this.bowlIngredient1 = bowlIngredient1;
		this.bowlIngredient2 = bowlIngredient2;
		this.output = output;
		this.craftingTime = craftingTime;
		this.experience = experience;
		this.noBenefitsFromYieldAndEfficiencyUpgrades = noBenefitsFromYieldAndEfficiencyUpgrades;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(InstanceRecipeInput recipeInput, Level world) {
		List<IngredientStack> ingredientStacks = getIngredientStacks();
		if (recipeInput.size() > 2) {
			if (ingredientStacks.get(CENTER_INGREDIENT).test(recipeInput.getItem(CENTER_INGREDIENT))) {
				if (ingredientStacks.get(FIRST_INGREDIENT).test(recipeInput.getItem(FIRST_INGREDIENT))) {
					return ingredientStacks.get(SECOND_INGREDIENT).test(recipeInput.getItem(SECOND_INGREDIENT));
				} else if (ingredientStacks.get(FIRST_INGREDIENT).test(recipeInput.getItem(SECOND_INGREDIENT))) {
					return ingredientStacks.get(SECOND_INGREDIENT).test(recipeInput.getItem(FIRST_INGREDIENT));
				}
			}
		}
		return false;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		return output;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SPIRIT_INSTILLING_SERIALIZER;
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		NonNullList<IngredientStack> defaultedList = NonNullList.create();
		defaultedList.add(this.centerIngredient);
		defaultedList.add(this.bowlIngredient1);
		defaultedList.add(this.bowlIngredient2);
		return defaultedList;
	}
	
	@Override
	public ItemStack assemble(InstanceRecipeInput<SpiritInstillerBlockEntity> recipeInput, HolderLookup.Provider drm) {
		ItemStack resultStack = ItemStack.EMPTY;
		SpiritInstillerBlockEntity spiritInstillerBlockEntity = recipeInput.getInstance();
		Upgradeable.UpgradeHolder upgradeHolder = spiritInstillerBlockEntity.getUpgradeHolder();
		Level world = spiritInstillerBlockEntity.getLevel();
		if (world == null) return ItemStack.EMPTY;
		BlockPos pos = spiritInstillerBlockEntity.getBlockPos();
		
		resultStack = getResultItem(drm).copy();
		
		// Yield upgrade
		if (!areYieldAndEfficiencyUpgradesDisabled() && upgradeHolder.getEffectiveValue(Upgradeable.UpgradeType.YIELD) != 1.0) {
			int resultCountMod = Support.getIntFromDecimalWithChance(resultStack.getCount() * upgradeHolder.getEffectiveValue(Upgradeable.UpgradeType.YIELD), world.random);
			resultStack.setCount(resultCountMod);
		}
		
		if (resultStack.is(SpectrumBlocks.MEMORY.asItem())) {
			boolean makeUnrecognizable = spiritInstillerBlockEntity.getItem(0).is(SpectrumItemTags.MEMORY_BONDING_AGENTS_CONCEALABLE);
			if (makeUnrecognizable) {
				MemoryItem.makeUnrecognizable(resultStack);
			}
		}
		
		spawnXPAndGrantAdvancements(resultStack, spiritInstillerBlockEntity, upgradeHolder, world, pos);
		
		return resultStack;
	}
	
	// Calculate and spawn experience
	protected void spawnXPAndGrantAdvancements(ItemStack resultStack, SpiritInstillerBlockEntity spiritInstillerBlockEntity, Upgradeable.UpgradeHolder upgradeHolder, Level world, BlockPos pos) {
		int awardedExperience = 0;
		if (getExperience() > 0) {
			double experienceModifier = upgradeHolder.getEffectiveValue(Upgradeable.UpgradeType.EXPERIENCE);
			float recipeExperienceBeforeMod = getExperience();
			awardedExperience = Support.getIntFromDecimalWithChance(recipeExperienceBeforeMod * experienceModifier, world.random);
			MultiblockCrafter.spawnExperience(world, pos.above(), awardedExperience);
		}
		
		// Run Advancement trigger
		grantPlayerSpiritInstillingAdvancementCriterion(spiritInstillerBlockEntity.getOwnerUUID(), resultStack, awardedExperience);
	}
	
	protected static void grantPlayerSpiritInstillingAdvancementCriterion(UUID playerUUID, ItemStack resultStack, int experience) {
		ServerPlayer serverPlayerEntity = (ServerPlayer) PlayerOwned.getPlayerEntityIfOnline(playerUUID);
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
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "spirit_instiller";
	}
	
	public boolean canCraftWithStacks(RecipeInput inventory) {
		return true;
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(SpectrumBlocks.SPIRIT_INSTILLER);
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.SPIRIT_INSTILLING;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 3;
	}
	
	public static class Serializer implements RecipeSerializer<SpiritInstillerRecipe> {
		
		public static final MapCodec<SpiritInstillerRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				IngredientStack.Serializer.CODEC.fieldOf("center_ingredient").forGetter(recipe -> recipe.centerIngredient),
				IngredientStack.Serializer.CODEC.fieldOf("ingredient1").forGetter(recipe -> recipe.bowlIngredient1),
				IngredientStack.Serializer.CODEC.fieldOf("ingredient2").forGetter(recipe -> recipe.bowlIngredient2),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
				Codec.INT.optionalFieldOf("time", 200).forGetter(recipe -> recipe.craftingTime),
				Codec.FLOAT.optionalFieldOf("experience", 1.0f).forGetter(recipe -> recipe.experience),
				Codec.BOOL.optionalFieldOf("disable_yield_and_efficiency_upgrades", false).forGetter(recipe -> recipe.noBenefitsFromYieldAndEfficiencyUpgrades)
		).apply(i, SpiritInstillerRecipe::new));
		
		private static final StreamCodec<RegistryFriendlyByteBuf, SpiritInstillerRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, c -> c.group,
				ByteBufCodecs.BOOL, c -> c.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), c -> c.requiredAdvancementIdentifier,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.centerIngredient,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.bowlIngredient1,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.bowlIngredient2,
				ItemStack.STREAM_CODEC, c -> c.output,
				ByteBufCodecs.VAR_INT, recipe -> recipe.craftingTime,
				ByteBufCodecs.FLOAT, recipe -> recipe.experience,
				ByteBufCodecs.BOOL, recipe -> recipe.noBenefitsFromYieldAndEfficiencyUpgrades,
				SpiritInstillerRecipe::new
		);
		
		@Override
		public MapCodec<SpiritInstillerRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, SpiritInstillerRecipe> streamCodec() {
			return PACKET_CODEC;
		}
	}
	
}
