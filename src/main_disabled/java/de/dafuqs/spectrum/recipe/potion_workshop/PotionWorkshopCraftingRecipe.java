package de.dafuqs.spectrum.recipe.potion_workshop;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.potion_workshop.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionWorkshopCraftingRecipe extends PotionWorkshopRecipe {
	
	protected final IngredientStack baseIngredient;
	protected final boolean consumeBaseIngredient;
	protected final int requiredExperience;
	protected final ItemStack output;
	
	public PotionWorkshopCraftingRecipe(
			String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, int craftingTime, int color,
			IngredientStack ingredient1, IngredientStack ingredient2, IngredientStack ingredient3,
			IngredientStack baseIngredient, boolean consumeBaseIngredient, int requiredExperience, ItemStack output
	) {
		super(group, secret, requiredAdvancementIdentifier, craftingTime, color, ingredient1, ingredient2, ingredient3);
		this.output = output;
		this.baseIngredient = baseIngredient;
		this.requiredExperience = requiredExperience;
		this.consumeBaseIngredient = consumeBaseIngredient;
		
		registerInToastManager(getType(), this);
	}
	
	public IngredientStack getBaseIngredient() {
		return baseIngredient;
	}
	
	public boolean consumesBaseIngredient() {
		return consumeBaseIngredient;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.POTION_WORKSHOP_CRAFTING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING;
	}
	
	@Override
	public boolean usesReagents() {
		return false;
	}
	
	@Override
	public int getRequiredExperience() {
		return this.requiredExperience;
	}
	
	@Override
	public ItemStack assemble(RecipeInput inventory, HolderLookup.Provider drm) {
		return output.copy();
	}
	
	@Override
	public boolean isValidBaseIngredient(ItemStack itemStack) {
		return baseIngredient.test(itemStack);
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		NonNullList<IngredientStack> defaultedList = NonNullList.create();
		defaultedList.add(IngredientStack.ofItems(SpectrumItems.MERMAIDS_GEM));
		defaultedList.add(this.baseIngredient);
		addIngredientStacks(defaultedList);
		return defaultedList;
	}
	
	@Override
	public boolean matches(@NotNull RecipeInput inv, Level world) {
		if (enoughExperienceSupplied(inv)) {
			return super.matches(inv, world);
		}
		return false;
	}
	
	// we just test for a single ExperienceStorageItem here instead
	// of iterating over every item. The specification mentions that
	// Only one is supported and just a single ExperienceStorageItem
	// should be used per recipe, tough
	private boolean enoughExperienceSupplied(RecipeInput inv) {
		if (this.requiredExperience > 0) {
			for (int i : new int[]{PotionWorkshopBlockEntity.BASE_INPUT_SLOT_ID, PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT,
					PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT + 1, PotionWorkshopBlockEntity.FIRST_INGREDIENT_SLOT + 2}) {
				
				if ((inv.getItem(i).getItem() instanceof ExperienceStorageItem)) {
					return ExperienceStorageItem.getStoredExperience(inv.getItem(i)) >= requiredExperience;
				}
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		return output;
	}
	
	@Override
	public int getMinOutputCount(ItemStack itemStack) {
		return 1;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "potion_workshop_crafting";
	}
	
	public static class Serializer implements RecipeSerializer<PotionWorkshopCraftingRecipe> {
		
		public static final MapCodec<PotionWorkshopCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(c -> c.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(c -> c.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(c -> c.requiredAdvancementIdentifier),
				Codec.INT.optionalFieldOf("time", 200).forGetter(c -> c.craftingTime),
				Codec.INT.optionalFieldOf("color", 0xc03058).forGetter(c -> c.color),
				IngredientStack.Serializer.CODEC.fieldOf("ingredient1").forGetter(c -> c.ingredient1),
				IngredientStack.Serializer.CODEC.optionalFieldOf("ingredient2", IngredientStack.EMPTY).forGetter(c -> c.ingredient2),
				IngredientStack.Serializer.CODEC.optionalFieldOf("ingredient3", IngredientStack.EMPTY).forGetter(c -> c.ingredient3),
				IngredientStack.Serializer.CODEC.fieldOf("base_ingredient").forGetter(c -> c.baseIngredient),
				Codec.BOOL.optionalFieldOf("use_up_base_ingredient", true).forGetter(c -> c.consumeBaseIngredient),
				Codec.INT.optionalFieldOf("required_experience", 0).forGetter(c -> c.requiredExperience),
				ItemStack.CODEC.fieldOf("result").forGetter(c -> c.output)
		).apply(i, PotionWorkshopCraftingRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, PotionWorkshopCraftingRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, c -> c.group,
				ByteBufCodecs.BOOL, c -> c.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), c -> c.requiredAdvancementIdentifier,
				ByteBufCodecs.VAR_INT, c -> c.craftingTime,
				ByteBufCodecs.VAR_INT, c -> c.color,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.ingredient1,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.ingredient2,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.ingredient3,
				IngredientStack.Serializer.PACKET_CODEC, c -> c.baseIngredient,
				ByteBufCodecs.BOOL, c -> c.consumeBaseIngredient,
				ByteBufCodecs.VAR_INT, c -> c.requiredExperience,
				ItemStack.STREAM_CODEC, c -> c.output,
				PotionWorkshopCraftingRecipe::new
		);
		
		@Override
		public MapCodec<PotionWorkshopCraftingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, PotionWorkshopCraftingRecipe> streamCodec() {
			return PACKET_CODEC;
		}
	}
	
}
