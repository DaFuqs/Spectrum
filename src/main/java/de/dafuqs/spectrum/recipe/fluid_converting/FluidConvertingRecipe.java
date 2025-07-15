package de.dafuqs.spectrum.recipe.fluid_converting;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.recipe.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class FluidConvertingRecipe extends GatedSpectrumRecipe<RecipeInput> {
	
	protected final Ingredient input;
	protected final ItemStack output;
	
	public FluidConvertingRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, @NotNull Ingredient input, ItemStack output) {
		super(group, secret, requiredAdvancementIdentifier);
		this.input = input;
		this.output = output;
	}
	
	@Override
	public boolean matches(@NotNull RecipeInput inv, Level world) {
		return this.input.test(inv.getItem(0));
	}
	
	@Override
	public ItemStack assemble(RecipeInput inv, HolderLookup.Provider drm) {
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
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> defaultedList = NonNullList.create();
		defaultedList.add(this.input);
		return defaultedList;
	}
	
	public static class Serializer<T extends FluidConvertingRecipe> implements RecipeSerializer<T> {
		
		private final MapCodec<T> codec;
		private final StreamCodec<RegistryFriendlyByteBuf, T> packetCodec;
		
		public Serializer(Function5<String, Boolean, Optional<ResourceLocation>, Ingredient, ItemStack, T> factory) {
			codec = RecordCodecBuilder.mapCodec(i -> i.group(
					Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
					Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
					ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
					Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.input),
					ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.output)
			).apply(i, factory));
			
			packetCodec = StreamCodec.composite(
					ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
					ByteBufCodecs.BOOL, recipe -> recipe.secret,
					ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
					Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.input,
					ItemStack.STREAM_CODEC, recipe -> recipe.output,
					factory
			);
		}
		
		@Override
		public MapCodec<T> codec() {
			return codec;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
			return packetCodec;
		}
	}
	
}
