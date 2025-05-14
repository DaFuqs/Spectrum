package de.dafuqs.spectrum.recipe.enchanter;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

import java.util.*;

public class EnchanterRecipe extends GatedSpectrumRecipe<RecipeInput> {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/build_enchanting_structure");
	
	protected final List<Ingredient> inputs; // first input is the center, all others around clockwise
	protected final ItemStack output;
	
	protected final int requiredExperience;
	protected final int craftingTime;
	protected final boolean noBenefitsFromYieldAndEfficiencyUpgrades;
	// copy all modified components from the first stack in the ingredients to the output stack
	protected final boolean copyComponents;
	
	public EnchanterRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, List<Ingredient> inputs, ItemStack output, int craftingTime, int requiredExperience, boolean noBenefitsFromYieldAndEfficiencyUpgrades, boolean copyComponents) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.inputs = inputs;
		this.output = output;
		this.requiredExperience = requiredExperience;
		this.craftingTime = craftingTime;
		this.noBenefitsFromYieldAndEfficiencyUpgrades = noBenefitsFromYieldAndEfficiencyUpgrades;
		this.copyComponents = copyComponents;
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(RecipeInput inv, Level world) {
		if (inv.size() >= 10) {
			// the item on the enchanter
			if (!inputs.getFirst().test(inv.getItem(0))) {
				return false;
			}
			// is there an experience provider with enough XP?
			if (this.getRequiredExperience() > 0
					&& !(inv.getItem(1).getItem() instanceof ExperienceStorageItem)
					&& ExperienceStorageItem.getStoredExperience(inv.getItem(1)) < this.getRequiredExperience()) {
				return false;
			}
			
			// match stacks
			for (int i = 1; i < 9; i++) {
				if (!inputs.get(i).test(inv.getItem(i + 1))) {
					return false;
				}
			}
			
			return true;
		}
		return false;
	}
	
	@Override
	public ItemStack assemble(RecipeInput inv, HolderLookup.Provider drm) {
		if (this.copyComponents) {
			return inv.getItem(0).transmuteCopy(output.getItem(), output.getCount());
		}
		return output.copy();
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		return output;
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(SpectrumBlocks.ENCHANTER);
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.ENCHANTER_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ENCHANTER;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(inputs.size());
		ingredients.addAll(inputs);
		return ingredients;
	}
	
	public int getRequiredExperience() {
		return requiredExperience;
	}
	
	public int getCraftingTime() {
		return this.craftingTime;
	}
	
	public boolean areYieldAndEfficiencyUpgradesDisabled() {
		return noBenefitsFromYieldAndEfficiencyUpgrades;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "enchanter";
	}
	
	public static class Serializer implements RecipeSerializer<EnchanterRecipe> {
		
		public static final MapCodec<EnchanterRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("ingredients", List.of()).forGetter(recipe -> recipe.inputs),
				ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
				Codec.INT.optionalFieldOf("required_experience", 0).forGetter(recipe -> recipe.requiredExperience),
				Codec.INT.optionalFieldOf("time", 200).forGetter(recipe -> recipe.craftingTime),
				Codec.BOOL.optionalFieldOf("disable_yield_and_efficiency_upgrades", false).forGetter(recipe -> recipe.noBenefitsFromYieldAndEfficiencyUpgrades),
				Codec.BOOL.optionalFieldOf("copy_components", false).forGetter(recipe -> recipe.copyComponents)
		).apply(i, EnchanterRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.BOOL, recipe -> recipe.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.inputs,
				ItemStack.STREAM_CODEC, recipe -> recipe.output,
				ByteBufCodecs.VAR_INT, recipe -> recipe.requiredExperience,
				ByteBufCodecs.VAR_INT, recipe -> recipe.craftingTime,
				ByteBufCodecs.BOOL, recipe -> recipe.noBenefitsFromYieldAndEfficiencyUpgrades,
				ByteBufCodecs.BOOL, recipe -> recipe.copyComponents,
				EnchanterRecipe::new
		);
		
		@Override
		public MapCodec<EnchanterRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> streamCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
