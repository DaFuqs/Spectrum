package de.dafuqs.spectrum.recipe.enchanter;

import com.google.gson.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;

import java.util.*;

public class EnchantmentUpgradeRecipe extends GatedSpectrumRecipe<RecipeInput> {
	
	protected final Either<Holder<Enchantment>, ResourceKey<Enchantment>> either;
	protected final int levelCap;
	protected final Ingredient bulkItem;
	protected final RecipeScaling.ScalingData itemScaling, XPScaling;
	
	protected final NonNullList<Ingredient> inputs;
	protected final ItemStack output;
	
	public EnchantmentUpgradeRecipe(
			String group,
			boolean secret,
			Optional<ResourceLocation> requiredAdvancementIdentifier,
			Either<Holder<Enchantment>, ResourceKey<Enchantment>> enchantmentEntry,
			int levelCap,
			Ingredient bulkItem,
			RecipeScaling.ScalingData XPScaling,
			RecipeScaling.ScalingData itemScaling
	) {
		super(group, secret, requiredAdvancementIdentifier);
		
		this.either = enchantmentEntry;
		this.levelCap = levelCap;
		this.bulkItem = bulkItem;
		this.itemScaling = itemScaling;
		this.XPScaling = XPScaling;
		
		NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);
		
		if (enchantmentEntry.left().isPresent()) {
			var enchantment = enchantmentEntry.left().get();
			var baseMax = enchantment.value().getMaxLevel();
			if (levelCap < baseMax)
				throw new JsonParseException("Level Cap cannot be lower than the Enchantment's base level (levelCap " + levelCap + "< enchantment's" + baseMax + ")");
			
			ItemStack ingredientStack = new ItemStack(Items.ENCHANTED_BOOK);
			ingredientStack.enchant(enchantment, levelCap - 1);
			inputs.set(0, Ingredient.of(ingredientStack));
			inputs.set(1, bulkItem);
			this.inputs = inputs;
			
			ItemStack outputStack = new ItemStack(Items.ENCHANTED_BOOK);
			outputStack.enchant(enchantmentEntry.left().get(), levelCap);
			this.output = outputStack;
		}
		else {
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
			
			if (!enchantments.keySet().contains(enchantment) || bookLevel >= levelCap) {
				return false;
			}
			
			// Check XP requirements
			var availableXp = ExperienceStorageItem.getStoredExperience(inv.getItem(1));
			var requiredXp = XPScaling.apply(bookLevel);
			
			if (availableXp < requiredXp)
				return false;
			
			// Finally, check the ingredients
			int bulkInput = 0;
			for (int i = 1; i < 9; i++) {
				ItemStack currentStack = inv.getItem(i + 1);
				
				if (!currentStack.isEmpty()) {
					ItemStack slotStack = inv.getItem(i + 1);
					if (bulkItem.test(slotStack)) {
						bulkInput += slotStack.getCount();
					} else {
						return false;
					}
				}
			}
			
			return bulkInput >= itemScaling.apply(bookLevel);
		}
		return false;
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
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}
	
	// Janky Hack
	public Item getBulkItem() {
		var match = bulkItem.getItems();
		if (match != null && match.length > 0)
			return bulkItem.getItems()[0].getItem();
		return Items.AIR;
	}
	
	public int getBaseXPCost() {
		return XPScaling.apply(1);
	}
	
	public int getBaseItemCost() {
		return itemScaling.apply(1);
	}
	
	public RecipeScaling.ScalingData getXPScaling() {
		return XPScaling;
	}
	
	public RecipeScaling.ScalingData getItemScaling() {
		return itemScaling;
	}
	
	public Holder<Enchantment> getEnchantment() {
		if (either.left().isEmpty()) {
			throw new UnsupportedOperationException("Attempted to match a datagen enchantment upgrade");
		}
		return either.left().get();
	}
	
	public int getLevelCap() {
		return levelCap;
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
				Codec.INT.fieldOf("level_cap").forGetter(recipe -> recipe.levelCap),
				Ingredient.CODEC_NONEMPTY.fieldOf("bulk_item").forGetter(recipe -> recipe.bulkItem),
				RecipeScaling.CODEC.fieldOf("xp_scaling").forGetter(recipe -> recipe.XPScaling),
				RecipeScaling.CODEC.fieldOf("item_scaling").forGetter(recipe -> recipe.itemScaling)
		).apply(i, EnchantmentUpgradeRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, EnchantmentUpgradeRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, recipe -> recipe.group,
				ByteBufCodecs.BOOL, recipe -> recipe.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
				ByteBufCodecs.either(Enchantment.STREAM_CODEC, ResourceKey.streamCodec(Registries.ENCHANTMENT)), c -> c.either,
				ByteBufCodecs.VAR_INT, recipe -> recipe.levelCap,
				Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.bulkItem,
				RecipeScaling.PACKET_CODEC, recipe -> recipe.XPScaling,
				RecipeScaling.PACKET_CODEC, recipe -> recipe.itemScaling,
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
