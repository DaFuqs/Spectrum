package de.dafuqs.spectrum.recipe.pedestal;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.recipe.*;
import it.unimi.dsi.fastutil.chars.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;
import java.util.function.*;

public class RawShapedPedestalRecipe {
	public static final MapCodec<RawShapedPedestalRecipe> CODEC = RawShapedPedestalRecipe.Data.CODEC.flatXmap(
			RawShapedPedestalRecipe::fromData,
			(recipe) -> recipe.data.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe")));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, RawShapedPedestalRecipe> PACKET_CODEC = StreamCodec.ofMember(RawShapedPedestalRecipe::writeToBuf, RawShapedPedestalRecipe::readFromBuf);
	
	private final int width;
	private final int height;
	private final NonNullList<IngredientStack> ingredients;
	private final Optional<Data> data;
	private final int ingredientCount;
	private final boolean symmetrical;
	
	public RawShapedPedestalRecipe(int width, int height, NonNullList<IngredientStack> ingredients, Optional<Data> data) {
		this.width = width;
		this.height = height;
		this.ingredients = ingredients;
		this.data = data;
		int i = 0;
		
		for (IngredientStack ingredient : ingredients) {
			if (!ingredient.isEmpty()) {
				++i;
			}
		}
		
		this.ingredientCount = i;
		this.symmetrical = Util.isSymmetrical(width, height, ingredients);
	}
	
	public static RawShapedPedestalRecipe create(Map<Character, IngredientStack> key, String... pattern) {
		return create(key, List.of(pattern));
	}
	
	public static RawShapedPedestalRecipe create(Map<Character, IngredientStack> key, List<String> pattern) {
		Data data = new Data(key, pattern);
		return fromData(data).getOrThrow();
	}
	
	private static DataResult<RawShapedPedestalRecipe> fromData(Data data) {
		String[] strings = removePadding(data.pattern);
		int i = strings[0].length();
		int j = strings.length;
		NonNullList<IngredientStack> defaultedList = NonNullList.withSize(i * j, IngredientStack.EMPTY);
		CharSet charSet = new CharArraySet(data.key.keySet());
		
		for (int k = 0; k < strings.length; ++k) {
			String string = strings[k];
			
			for (int l = 0; l < string.length(); ++l) {
				char c = string.charAt(l);
				IngredientStack ingredient = c == ' ' ? IngredientStack.EMPTY : data.key.get(c);
				if (ingredient == null) {
					return DataResult.error(() -> "Pattern references symbol '" + c + "' but it's not defined in the key");
				}
				
				charSet.remove(c);
				defaultedList.set(l + i * k, ingredient);
			}
		}
		
		if (!charSet.isEmpty()) {
			return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charSet);
		} else {
			return DataResult.success(new RawShapedPedestalRecipe(i, j, defaultedList, Optional.of(data)));
		}
	}
	
	static String[] removePadding(List<String> pattern) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;
		
		for (int m = 0; m < pattern.size(); ++m) {
			String string = pattern.get(m);
			i = Math.min(i, findFirstSymbol(string));
			int n = findLastSymbol(string);
			j = Math.max(j, n);
			if (n < 0) {
				if (k == m) {
					++k;
				}
				
				++l;
			} else {
				l = 0;
			}
		}
		
		if (pattern.size() == l) {
			return new String[0];
		} else {
			String[] strings = new String[pattern.size() - l - k];
			
			for (int o = 0; o < strings.length; ++o) {
				strings[o] = (pattern.get(o + k)).substring(i, j + 1);
			}
			
			return strings;
		}
	}
	
	private static int findFirstSymbol(String line) {
		int i;
		for (i = 0; i < line.length() && line.charAt(i) == ' '; ++i) ;
		return i;
	}
	
	private static int findLastSymbol(String line) {
		int i;
		for (i = line.length() - 1; i >= 0 && line.charAt(i) == ' '; --i) ;
		return i;
	}
	
	public boolean matches(CraftingInput input) {
		if (input.ingredientCount() != this.ingredientCount)
			return false;
		
		if (input.width() == this.width && input.height() == this.height) {
			if (!this.symmetrical && this.matches(input, true)) {
				return true;
			}
			
			return this.matches(input, false);
		}
		
		return false;
	}
	
	public boolean matches(CraftingInput input, boolean mirrored) {
		for (int i = 0; i < this.height; ++i) {
			for (int j = 0; j < this.width; ++j) {
				IngredientStack ingredient;
				if (mirrored) {
					ingredient = this.ingredients.get(this.width - j - 1 + i * this.width);
				} else {
					ingredient = this.ingredients.get(j + i * this.width);
				}
				
				ItemStack itemStack = input.getItem(j, i);
				if (!ingredient.test(itemStack)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void writeToBuf(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(this.width);
		buf.writeVarInt(this.height);
		
		for (IngredientStack ingredient : this.ingredients) {
			IngredientStack.Serializer.PACKET_CODEC.encode(buf, ingredient);
		}
		
	}
	
	private static RawShapedPedestalRecipe readFromBuf(RegistryFriendlyByteBuf buf) {
		int i = buf.readVarInt();
		int j = buf.readVarInt();
		NonNullList<IngredientStack> defaultedList = NonNullList.withSize(i * j, IngredientStack.EMPTY);
		defaultedList.replaceAll((ingredient) -> IngredientStack.Serializer.PACKET_CODEC.decode(buf));
		return new RawShapedPedestalRecipe(i, j, defaultedList, Optional.empty());
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public NonNullList<IngredientStack> getIngredients() {
		return this.ingredients;
	}
	
	public record Data(Map<Character, IngredientStack> key, List<String> pattern) {
		
		private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap((pattern) -> {
			if (pattern.isEmpty())
				return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
			int i = pattern.getFirst().length();
			for (String string : pattern)
				if (i != string.length())
					return DataResult.error(() -> "Invalid pattern: each row must be the same width");
			return DataResult.success(pattern);
		}, Function.identity());
		
		private static final Codec<Character> KEY_ENTRY_CODEC = Codec.STRING.comapFlatMap(keyEntry -> {
			if (keyEntry.length() != 1)
				return DataResult.error(() -> "Invalid key entry: '" + keyEntry + "' is an invalid symbol (must be 1 character only).");
			return " ".equals(keyEntry) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(keyEntry.charAt(0));
		}, String::valueOf);
		
		public static final MapCodec<Data> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				ExtraCodecs.strictUnboundedMap(KEY_ENTRY_CODEC, IngredientStack.Serializer.CODEC).fieldOf("key").forGetter((data) -> data.key),
				PATTERN_CODEC.fieldOf("pattern").forGetter((data) -> data.pattern)
		).apply(i, Data::new));
		
	}
	
}
