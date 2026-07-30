package de.dafuqs.spectrum.recipe.enchanter;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.pastel_network.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;

import java.util.*;

public class EnchantmentUpgradeRecipe extends GatedStackSpectrumRecipe<RecipeInput> {
	
	public record LevelData(Ingredient ingredient, int countPerBowl, int experience) {
		
		public static final Codec<LevelData> CODEC = RecordCodecBuilder.create(i -> i.group(
				Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(LevelData::ingredient),
				ExtraCodecs.POSITIVE_INT.fieldOf("ingredient_count_per_bowl").forGetter(LevelData::countPerBowl),
				ExtraCodecs.POSITIVE_INT.fieldOf("experience").forGetter(LevelData::experience)
		).apply(i, LevelData::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, LevelData> STREAM_CODEC = PacketCodecHelper.tuple(
				Ingredient.CONTENTS_STREAM_CODEC, o -> o.ingredient,
				ByteBufCodecs.INT, o -> o.countPerBowl,
				ByteBufCodecs.INT, o -> o.experience,
				LevelData::new
		);
		
	}
	
	protected final Either<Holder<Enchantment>, ResourceKey<Enchantment>> either;
	protected final List<LevelData> levelData;
	protected final NonNullList<IngredientStack> inputs;
	protected final ItemStack output;
	
	public EnchantmentUpgradeRecipe(
			String group,
			boolean secret,
			Optional<ResourceLocation> requiredAdvancementIdentifier,
			Either<Holder<Enchantment>, ResourceKey<Enchantment>> enchantmentEntry,
			List<LevelData> levelData
	) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.either = enchantmentEntry;
		this.levelData = levelData;
		
		if (enchantmentEntry.left().isPresent()) {
			NonNullList<IngredientStack> inputs = NonNullList.withSize(1 + levelData.size(), IngredientStack.EMPTY);
			ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
			stack.enchant(enchantmentEntry.left().get(), 1);
			inputs.set(0, IngredientStack.of(Ingredient.of(stack)));
			for(int i = 0; i < levelData.size(); ++i) {
				LevelData ld = levelData.get(i);
				inputs.set(i + 1, IngredientStack.of(ld.ingredient(), ld.countPerBowl()));
			}
			this.inputs = inputs;
			
			ItemStack outputStack = new ItemStack(Items.ENCHANTED_BOOK);
			outputStack.enchant(enchantmentEntry.left().get(), 2);
			this.output = outputStack;
		} else {
			this.inputs = NonNullList.create();
			this.output = ItemStack.EMPTY;
		}
	}
	
	@Override
	public boolean matches(RecipeInput inv, Level world) {
		if (inv.size() > 9) {
			ItemStack centerStack = inv.getItem(0);
			if (either.left().isEmpty())
				throw new UnsupportedOperationException("Attempted to match a datagen enchantment upgrade");
			
			var enchantment = either.left().get();
			
			//Check if the book matches
			if (!inputs.getFirst().test(centerStack)) {
				return false;
			}
			
			var enchantments = centerStack.get(DataComponents.STORED_ENCHANTMENTS);
			if (enchantments == null) {
				return false;
			}
			
			var bookLevel = enchantments.getLevel(enchantment);
			
			if (!enchantments.keySet().contains(enchantment) || bookLevel >= getLevelCap()) {
				return false;
			}
			
			// Check XP requirements
			var availableXp = ExperienceStorageItem.getStoredExperience(inv.getItem(1));
			LevelData levelData = getForSourceLevel(bookLevel);
			var requiredXp = levelData.experience();
			
			if (availableXp < requiredXp)
				return false;
			
			// Finally, check the ingredients
			int bulkInput = 0;
			for (int i = 1; i < 9; i++) {
				ItemStack currentStack = inv.getItem(i + 1);
				
				if (!currentStack.isEmpty()) {
					ItemStack slotStack = inv.getItem(i + 1);
					if (levelData.ingredient().test(slotStack)) {
						bulkInput += slotStack.getCount();
					} else {
						return false;
					}
				}
			}
			
			return bulkInput >= levelData.countPerBowl() * 8;
		}
		return false;
	}
	
	public List<LevelData> getLevelData() {
		return this.levelData;
	}
	
	public LevelData getForSourceLevel(int sourceLevel) {
		return this.levelData.get(sourceLevel - 1);
	}
	
	public int getRequiredXPForSourceLevel(int sourceLevel) {
		return this.levelData.get(sourceLevel - 1).experience();
	}
	
	public int getRequiredItemCountForSourceLevel(int sourceLevel) {
		return this.levelData.get(sourceLevel - 1).countPerBowl() * 8;
	}
	
	public int getLevelCap() {
		return this.levelData.size() + 1;
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
	public ItemStack getToastSymbol() {
		return new ItemStack(SpectrumBlocks.ENCHANTER);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.ENCHANTMENT_UPGRADE_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ENCHANTMENT_UPGRADE;
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return EnchanterRecipe.UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "enchantment_upgrade";
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		return this.inputs;
	}
	
	public Holder<Enchantment> getEnchantment() {
		if (either.left().isEmpty()) {
			throw new UnsupportedOperationException("Attempted to match a datagen enchantment upgrade");
		}
		return either.left().get();
	}
	
	public boolean isInNormalRange(int level) {
		if (either.left().isEmpty()) {
			throw new UnsupportedOperationException("Attempted to match a datagen enchantment upgrade");
		}
		return level < this.either.left().get().value().getMaxLevel();
	}
	
	public static class Serializer implements RecipeSerializer<EnchantmentUpgradeRecipe> {
		
		public static final MapCodec<EnchantmentUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				Codec.either(Enchantment.CODEC, ResourceKey.codec(Registries.ENCHANTMENT)).fieldOf("enchantment").forGetter(c -> c.either),
				LevelData.CODEC.listOf().fieldOf("levels").forGetter(recipe -> recipe.levelData)
		).apply(i, EnchantmentUpgradeRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentUpgradeRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.BOOL, recipe -> recipe.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
				ByteBufCodecs.either(Enchantment.STREAM_CODEC, ResourceKey.streamCodec(Registries.ENCHANTMENT)), c -> c.either,
				LevelData.STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity)), recipe -> recipe.levelData,
				EnchantmentUpgradeRecipe::new
		);
		
		@Override
		public MapCodec<EnchantmentUpgradeRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, EnchantmentUpgradeRecipe> streamCodec() {
			return PACKET_CODEC;
		}
	}
}
