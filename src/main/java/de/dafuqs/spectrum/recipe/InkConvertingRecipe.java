package de.dafuqs.spectrum.recipe;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

import java.util.*;

public class InkConvertingRecipe extends GatedSpectrumRecipe<RecipeInput> {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/place_color_picker");
	protected static final List<Item> INPUT_ITEMS = new ArrayList<>();
	
	protected final Ingredient inputIngredient;
	protected final InkColor color;
	protected final long amount;
	
	public InkConvertingRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, Ingredient inputIngredient, InkColor color, long amount) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.inputIngredient = inputIngredient;
		this.color = color;
		this.amount = amount;
		
		for (ItemStack itemStack : inputIngredient.getItems()) {
			Item item = itemStack.getItem();
			if (!INPUT_ITEMS.contains(item)) {
				INPUT_ITEMS.add(item);
			}
		}
		
		registerInToastManager(getType(), this);
	}
	
	public static boolean isInput(Item item) {
		return INPUT_ITEMS.contains(item);
	}
	
	@Override
	public boolean matches(RecipeInput inv, Level world) {
		return this.inputIngredient.test(inv.getItem(0));
	}
	
	@Override
	public ItemStack assemble(RecipeInput inv, HolderLookup.Provider drm) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(SpectrumBlocks.COLOR_PICKER);
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.INK_CONVERTING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.INK_CONVERTING;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.of(), this.inputIngredient);
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "ink_converting";
	}
	
	public InkColor getInkColor() {
		return this.color;
	}
	
	public long getInkAmount() {
		return this.amount;
	}
	
	public static class Serializer implements RecipeSerializer<InkConvertingRecipe> {
		
		public static final MapCodec<InkConvertingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.inputIngredient),
				InkColor.CODEC.fieldOf("ink_color").forGetter(recipe -> recipe.color),
				Codec.LONG.fieldOf("amount").forGetter(recipe -> recipe.amount)
		).apply(i, InkConvertingRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, InkConvertingRecipe> PACKET_CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.BOOL, recipe -> recipe.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
				Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.inputIngredient,
				InkColor.PACKET_CODEC, recipe -> recipe.color,
				ByteBufCodecs.VAR_LONG, recipe -> recipe.amount,
				InkConvertingRecipe::new
		);
		
		@Override
		public MapCodec<InkConvertingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, InkConvertingRecipe> streamCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
