package de.dafuqs.spectrum.api.recipe;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.registries.*;
import io.netty.buffer.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.crafting.*;
import org.jspecify.annotations.*;

import java.util.*;
import java.util.stream.*;

public class IngredientStack implements ICustomIngredient {
	
	public static final MapCodec<IngredientStack> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			MapCodec.assumeMapUnsafe(Ingredient.CODEC_NONEMPTY).forGetter(IngredientStack::getIngredient),
			DataComponentPredicate.CODEC.optionalFieldOf("components", DataComponentPredicate.EMPTY).forGetter(o -> o.componentPredicate),
			ItemSubPredicate.CODEC.optionalFieldOf("predicates", Map.of()).forGetter(o -> o.subPredicates),
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
	
	public static final StreamCodec<ByteBuf, Map<ItemSubPredicate.Type<?>, ItemSubPredicate>> NOOP = new StreamCodec<>() {
		public Map<ItemSubPredicate.Type<?>, ItemSubPredicate> decode(ByteBuf buf) {
			return Map.of();
		}
		
		public void encode(ByteBuf buf, Map<ItemSubPredicate.Type<?>, ItemSubPredicate> predicate) {
		
		}
	};
	
	public static final StreamCodec<RegistryFriendlyByteBuf, IngredientStack> STREAM_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient,
			DataComponentPredicate.STREAM_CODEC, o -> o.componentPredicate,
			NOOP, o -> o.subPredicates,
			DataComponentPatch.STREAM_CODEC, o -> o.previewComponents,
			ByteBufCodecs.VAR_INT, o -> o.count,
			IngredientStack::new
	);
	
	private final Ingredient ingredient;
	private final DataComponentPredicate componentPredicate;
	private final Map<ItemSubPredicate.Type<?>, ItemSubPredicate> subPredicates;
	private final DataComponentPatch previewComponents;
	private final int count;
	
	// These are from the codec, to handle encoding
	private @Nullable Item item = null;
	private @Nullable TagKey<Item> tag = null;
	
	public static final IngredientStack EMPTY = new IngredientStack(Ingredient.EMPTY, DataComponentPredicate.EMPTY, Map.of(), DataComponentPatch.EMPTY, 0);
	
	public IngredientStack(Ingredient ingredient, DataComponentPredicate componentPredicate, Map<ItemSubPredicate.Type<?>, ItemSubPredicate> subPredicates, DataComponentPatch previewComponents, int count) {
		this.ingredient = ingredient;
		this.componentPredicate = componentPredicate;
		this.subPredicates = subPredicates;
		this.previewComponents = previewComponents;
		this.count = count;
	}
	
	private IngredientStack(Ingredient ingredient) {
		this(ingredient, DataComponentPredicate.EMPTY, Map.of(), DataComponentPatch.EMPTY, 1);
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
	
	public static IngredientStack of(ItemStack stack) {
		return new IngredientStack(Ingredient.of(stack));
	}
	
	public static IngredientStack of(Ingredient ingredient, int count) {
		return new IngredientStack(ingredient, DataComponentPredicate.EMPTY, Map.of(), DataComponentPatch.EMPTY, count);
	}
	
	public static IngredientStack ofItems(Item item) {
		return new IngredientStack(Ingredient.of(item));
	}
	
	public static IngredientStack ofItems(Item item, int count) {
		IngredientStack ingredientStack = new IngredientStack(Ingredient.of(item), DataComponentPredicate.EMPTY, Map.of(), DataComponentPatch.EMPTY, count);
		ingredientStack.item = item;
		return ingredientStack;
	}
	
	public static IngredientStack ofTag(TagKey<Item> tag) {
		return new IngredientStack(Ingredient.of(tag));
	}
	
	public static IngredientStack ofTag(TagKey<Item> tag, int count) {
		IngredientStack ingredientStack = new IngredientStack(Ingredient.of(tag), DataComponentPredicate.EMPTY, Map.of(), DataComponentPatch.EMPTY, count);
		ingredientStack.tag = tag;
		return ingredientStack;
	}
	
	@Override
	public boolean test(ItemStack stack) {
		if(!this.ingredient.test(stack)) {
			return false;
		}
		if(stack.getCount() < this.count) {
			return false;
		}
		if(!this.componentPredicate.test(stack.getComponents())) {
			return false;
		}
		
		for (ItemSubPredicate itemsubpredicate : this.subPredicates.values()) {
			if (!itemsubpredicate.matches(stack)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public Stream<ItemStack> getItems() {
		return Arrays.stream(this.ingredient.getItems()).map(stack -> {
			ItemStack itemStack = new ItemStack(stack.getItem(), count);
			itemStack.applyComponentsAndValidate(previewComponents);
			return itemStack;
		});
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
	
	@Override
	public IngredientType<?> getType() {
		return SpectrumIngredientTypes.INGREDIENT_STACK;
	}
	
	public boolean isEmpty() {
		return this == EMPTY || this.ingredient.isEmpty();
	}
	
}
