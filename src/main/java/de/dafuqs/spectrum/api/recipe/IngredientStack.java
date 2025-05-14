package de.dafuqs.spectrum.api.recipe;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.fabricmc.fabric.api.recipe.v1.ingredient.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class IngredientStack implements CustomIngredient {
	
	private final Ingredient ingredient;
	private final DataComponentPredicate componentPredicate;
	private final DataComponentPatch previewComponents;
	private final int count;
	
	// These are from the codec, to handle encoding
	private Item item = null;
	private TagKey<Item> tag = null;
	
	public static final IngredientStack EMPTY = new IngredientStack(Ingredient.EMPTY, DataComponentPredicate.EMPTY, DataComponentPatch.EMPTY, 0);
	
	public IngredientStack(Ingredient ingredient, DataComponentPredicate componentPredicate, DataComponentPatch previewComponents, int count) {
		this.ingredient = ingredient;
		this.componentPredicate = componentPredicate;
		this.previewComponents = previewComponents;
		this.count = count;
	}
	
	private IngredientStack(Ingredient ingredient) {
		this(ingredient, DataComponentPredicate.EMPTY, DataComponentPatch.EMPTY, 1);
	}
	
	public int getCount() {
		return count;
	}
	
	public Ingredient getIngredient() {
		return ingredient;
	}
	
	public static IngredientStack of(Ingredient ingredient) {
		return new IngredientStack(ingredient);
	}
	
	public static IngredientStack ofItems(Item item) {
		return new IngredientStack(Ingredient.of(item));
	}
	
	public static IngredientStack ofItems(Item item, int count) {
		IngredientStack ingredientStack = new IngredientStack(Ingredient.of(item), DataComponentPredicate.EMPTY, DataComponentPatch.EMPTY, count);
		ingredientStack.item = item;
		return ingredientStack;
	}
	
	public static IngredientStack ofTag(TagKey<Item> tag) {
		return new IngredientStack(Ingredient.of(tag));
	}
	
	public static IngredientStack ofTag(TagKey<Item> tag, int count) {
		IngredientStack ingredientStack = new IngredientStack(Ingredient.of(tag), DataComponentPredicate.EMPTY, DataComponentPatch.EMPTY, count);
		ingredientStack.tag = tag;
		return ingredientStack;
	}
	
	@Override
	public boolean test(ItemStack itemStack) {
		return this.ingredient.test(itemStack)
				&& this.count <= itemStack.getCount()
				&& this.componentPredicate.test(itemStack.getComponents());
	}
	
	@Nullable
	private List<ItemStack> matchingStacks;
	
	@Override
	public List<ItemStack> getMatchingStacks() {
		if (this.matchingStacks == null) {
			ItemStack[] matchingStacks = this.ingredient.getItems();
			List<ItemStack> stacks = new ArrayList<>(matchingStacks.length);
			for (ItemStack is : matchingStacks) {
				ItemStack stack = new ItemStack(is.getItem(), count);
				stack.applyComponentsAndValidate(previewComponents);
				stacks.add(stack);
			}
			this.matchingStacks = stacks;
		}
		return this.matchingStacks;
	}
	
	@Override
	public boolean requiresTesting() {
		return true;
	}
	
	public boolean isEmpty() {
		return this == EMPTY || this.ingredient.isEmpty();
	}
	
	@Override
	public CustomIngredientSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}
	
	public static class Serializer implements CustomIngredientSerializer<IngredientStack> {
		
		public static Serializer INSTANCE = new Serializer();
		
		public static final MapCodec<IngredientStack> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				MapCodec.assumeMapUnsafe(Ingredient.CODEC_NONEMPTY).forGetter(IngredientStack::getIngredient),
				DataComponentPredicate.CODEC.optionalFieldOf("components", DataComponentPredicate.EMPTY).forGetter(o -> o.componentPredicate),
				DataComponentPatch.CODEC.optionalFieldOf("preview_components", DataComponentPatch.EMPTY).forGetter(o -> o.previewComponents),
				Codec.INT.optionalFieldOf("count", 1).forGetter(o -> o.count)
		).apply(i, IngredientStack::new));
		
		public static final Codec<IngredientStack> CODEC = Codec.withAlternative(
				MAP_CODEC.codec(),
				Codec.xor(BuiltInRegistries.ITEM.byNameCodec(), TagKey.hashedCodec(Registries.ITEM)).xmap(
						either -> either.map(IngredientStack::ofItems, IngredientStack::ofTag),
						ingredientStack -> ingredientStack.item != null ? Either.left(ingredientStack.item) : Either.right(ingredientStack.tag)
				)
		);
		
		public static final StreamCodec<RegistryFriendlyByteBuf, IngredientStack> PACKET_CODEC = StreamCodec.composite(
				Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient,
				DataComponentPredicate.STREAM_CODEC, o -> o.componentPredicate,
				DataComponentPatch.STREAM_CODEC, o -> o.previewComponents,
				ByteBufCodecs.VAR_INT, o -> o.count,
				IngredientStack::new
		);
		
		@Override
		public ResourceLocation getIdentifier() {
			return ResourceLocation.parse("spectrum:ingredient_stack");
		}
		
		@Override
		public MapCodec<IngredientStack> getCodec(boolean allowEmpty) {
			return MAP_CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, IngredientStack> getPacketCodec() {
			return PACKET_CODEC;
		}
	}
}
