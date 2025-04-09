package de.dafuqs.spectrum.recipe.potion_workshop;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.blocks.potion_workshop.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionWorkshopReactingRecipe extends GatedSpectrumRecipe<RecipeInput> implements DescriptiveGatedRecipe<RecipeInput> {
	
	protected static final HashMap<Item, List<PotionMod>> reagents = new HashMap<>();
	
	protected final Item item;
	protected final List<PotionMod> modifiers;
	
	public PotionWorkshopReactingRecipe(String group, boolean secret, Optional<Identifier> requiredAdvancementIdentifier, Item item, List<PotionMod> modifiers) {
		super(group, secret, requiredAdvancementIdentifier);
		this.item = item;
		this.modifiers = modifiers;
		
		reagents.put(item, modifiers);
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(@NotNull RecipeInput inv, World world) {
		for (int i : PotionWorkshopBlockEntity.REAGENT_SLOTS) {
			ItemStack itemStack = inv.getStackInSlot(i);
			if (!itemStack.isEmpty() && itemStack.getItem() == item) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ItemStack craft(RecipeInput inventory, RegistryWrapper.WrapperLookup registryManager) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return false;
	}
	
	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup registryManager) {
		return item.getDefaultStack();
	}
	
	@Override
	public ItemStack createIcon() {
		return SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultStack();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.POTION_WORKSHOP_REACTING_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_REACTING;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(Ingredient.ofItems(this.item));
		return defaultedList;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return PotionWorkshopRecipe.UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "potion_workshop_reacting";
	}
	
	@Override
	public Text getDescription() {
		Identifier identifier = Registries.ITEM.getId(this.item);
		return Text.translatable("spectrum.rei.potion_workshop_reacting." + identifier.getNamespace() + "." + identifier.getPath());
	}
	
	@Override
	public Item getItem() {
		return this.item;
	}
	
	public static boolean isReagent(Item item) {
		return reagents.containsKey(item);
	}
	
	public static PotionMod.Builder combine(PotionMod.Builder builder, ItemStack reagentStack, Random random) {
		Item reagent = reagentStack.getItem();
		List<PotionMod> reagentMods = reagents.getOrDefault(reagent, null);
		if (reagentMods != null)
			builder.combine(reagentMods.get(random.nextInt(reagentMods.size())));
		return builder;
	}
	
	public static class Serializer implements RecipeSerializer<PotionWorkshopReactingRecipe> {
		
		public static final MapCodec<PotionWorkshopReactingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(c -> c.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(c -> c.secret),
				Identifier.CODEC.optionalFieldOf("required_advancement").forGetter(c -> c.requiredAdvancementIdentifier),
				Registries.ITEM.getCodec().fieldOf("item").forGetter(c -> c.item),
				CodecHelper.singleOrList(PotionMod.CODEC).fieldOf("modifiers").forGetter(c -> c.modifiers)
		).apply(i, PotionWorkshopReactingRecipe::new));
		
		public static final PacketCodec<RegistryByteBuf, PotionWorkshopReactingRecipe> PACKET_CODEC = PacketCodec.tuple(
				PacketCodecs.STRING, c -> c.group,
				PacketCodecs.BOOL, c -> c.secret,
				PacketCodecs.optional(Identifier.PACKET_CODEC), c -> c.requiredAdvancementIdentifier,
				PacketCodecs.registryValue(RegistryKeys.ITEM), c -> c.item,
				PotionMod.PACKET_CODEC.collect(PacketCodecs.toList()), c -> c.modifiers,
				PotionWorkshopReactingRecipe::new
		);
		
		@Override
		public MapCodec<PotionWorkshopReactingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public PacketCodec<RegistryByteBuf, PotionWorkshopReactingRecipe> packetCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
