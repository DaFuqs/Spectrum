package de.dafuqs.spectrum.recipe.enchanter;

import com.google.gson.*;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.component.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;

import java.util.*;

public class EnchantmentUpgradeRecipe extends GatedSpectrumRecipe<RecipeInput> {
	
	protected final Either<RegistryEntry<Enchantment>, RegistryKey<Enchantment>> either;
	protected final int levelCap;
	protected final Ingredient bulkItem;
	protected final RecipeScaling.ScalingData itemScaling, XPScaling;
	
	protected final DefaultedList<Ingredient> inputs;
	protected final ItemStack output;
	
	public EnchantmentUpgradeRecipe(
			String group,
			boolean secret,
			Optional<Identifier> requiredAdvancementIdentifier,
			Either<RegistryEntry<Enchantment>, RegistryKey<Enchantment>> enchantmentEntry,
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
		
		DefaultedList<Ingredient> inputs = DefaultedList.ofSize(2, Ingredient.EMPTY);
		
		if (enchantmentEntry.left().isPresent()) {
			var enchantment = enchantmentEntry.left().get();
			var baseMax = enchantment.value().getMaxLevel();
			if (levelCap < baseMax)
				throw new JsonParseException("Level Cap cannot be lower than the Enchantment's base level (levelCap " + levelCap + "< enchantment's" + baseMax + ")");
			
			ItemStack ingredientStack = new ItemStack(Items.ENCHANTED_BOOK);
			ingredientStack.addEnchantment(enchantment, levelCap - 1);
			inputs.set(0, Ingredient.ofStacks(ingredientStack));
			inputs.set(1, bulkItem);
			this.inputs = inputs;
			
			ItemStack outputStack = new ItemStack(Items.ENCHANTED_BOOK);
			outputStack.addEnchantment(enchantmentEntry.left().get(), levelCap);
			this.output = outputStack;
		}
		else {
			this.inputs = DefaultedList.of();
			this.output = ItemStack.EMPTY;
		}
	}
	
	
	@Override
	public boolean matches(RecipeInput inv, World world) {
		if (inv.getSize() > 9) {
			ItemStack centerStack = inv.getStackInSlot(0);
			if (either.left().isEmpty())
				throw new UnsupportedOperationException("Attempted to match a datagen enchantment upgrade");
			
			var enchantment = either.left().get();
			
			//Check if the book matches
			if (!inputs.getFirst().test(centerStack)) {
				return false;
			}
			
			var enchantments = centerStack.get(DataComponentTypes.STORED_ENCHANTMENTS);
			if (enchantments == null) {
				return false;
			}
			
			var bookLevel = enchantments.getLevel(enchantment);
			
			if (!enchantments.getEnchantments().contains(enchantment) || bookLevel >= levelCap) {
				return false;
			}
			
			// Check XP requirements
			var availableXp = ExperienceStorageItem.getStoredExperience(inv.getStackInSlot(1));
			var requiredXp = XPScaling.apply(bookLevel);
			
			if (availableXp < requiredXp)
				return false;
			
			// Finally, check the ingredients
			int bulkInput = 0;
			for (int i = 1; i < 9; i++) {
				ItemStack currentStack = inv.getStackInSlot(i + 1);
				
				if (!currentStack.isEmpty()) {
					ItemStack slotStack = inv.getStackInSlot(i + 1);
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
	public ItemStack craft(RecipeInput inv, RegistryWrapper.WrapperLookup drm) {
		return output.copy();
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup registryManager) {
		return output;
	}
	
	@Override
	public ItemStack createIcon() {
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
	public Identifier getRecipeTypeUnlockIdentifier() {
		return EnchanterRecipe.UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "enchantment_upgrade";
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return inputs;
	}
	
	// Janky Hack
	public Item getBulkItem() {
		var match = bulkItem.getMatchingStacks();
		if (match != null && match.length > 0)
			return bulkItem.getMatchingStacks()[0].getItem();
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
	
	public RegistryEntry<Enchantment> getEnchantment() {
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
				Identifier.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				Codec.either(Enchantment.ENTRY_CODEC, RegistryKey.createCodec(RegistryKeys.ENCHANTMENT)).fieldOf("enchantment").forGetter(c -> c.either),
				Codec.INT.fieldOf("level_cap").forGetter(recipe -> recipe.levelCap),
				Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("bulk_item").forGetter(recipe -> recipe.bulkItem),
				RecipeScaling.CODEC.fieldOf("xp_scaling").forGetter(recipe -> recipe.XPScaling),
				RecipeScaling.CODEC.fieldOf("item_scaling").forGetter(recipe -> recipe.itemScaling)
		).apply(i, EnchantmentUpgradeRecipe::new));
		
		public static final PacketCodec<RegistryByteBuf, EnchantmentUpgradeRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				PacketCodecs.STRING, recipe -> recipe.group,
				PacketCodecs.BOOL, recipe -> recipe.secret,
				PacketCodecs.optional(Identifier.PACKET_CODEC), recipe -> recipe.requiredAdvancementIdentifier,
				PacketCodecs.either(Enchantment.ENTRY_PACKET_CODEC, RegistryKey.createPacketCodec(RegistryKeys.ENCHANTMENT)), c -> c.either,
				PacketCodecs.VAR_INT, recipe -> recipe.levelCap,
				Ingredient.PACKET_CODEC, recipe -> recipe.bulkItem,
				RecipeScaling.PACKET_CODEC, recipe -> recipe.XPScaling,
				RecipeScaling.PACKET_CODEC, recipe -> recipe.itemScaling,
				EnchantmentUpgradeRecipe::new
		);
		
		@Override
		public MapCodec<EnchantmentUpgradeRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public PacketCodec<RegistryByteBuf, EnchantmentUpgradeRecipe> packetCodec() {
			return PACKET_CODEC;
		}
	}
}
