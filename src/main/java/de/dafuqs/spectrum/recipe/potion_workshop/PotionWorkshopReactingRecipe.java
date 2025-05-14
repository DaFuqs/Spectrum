package de.dafuqs.spectrum.recipe.potion_workshop;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionWorkshopReactingRecipe extends GatedSpectrumRecipe<RecipeInput> implements DescriptiveGatedRecipe<RecipeInput> {
	
	protected static final HashMap<Item, List<PotionMod>> reagents = new HashMap<>();
	
	protected final Item item;
	protected final List<PotionMod> modifiers;
	
	public PotionWorkshopReactingRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier, Item item, List<PotionMod> modifiers) {
		super(group, secret, requiredAdvancementIdentifier);
		this.item = item;
		this.modifiers = modifiers;
		
		reagents.put(item, modifiers);
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(@NotNull RecipeInput inv, Level world) {
		return false;
	}
	
	@Override
	public ItemStack assemble(RecipeInput inventory, HolderLookup.Provider registryManager) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registryManager) {
		return item.getDefaultInstance();
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultInstance();
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
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> defaultedList = NonNullList.create();
		defaultedList.add(Ingredient.of(this.item));
		return defaultedList;
	}
	
	@Override
	public ResourceLocation getRecipeTypeUnlockIdentifier() {
		return PotionWorkshopRecipe.UNLOCK_IDENTIFIER;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "potion_workshop_reacting";
	}
	
	@Override
	public Component getDescription() {
		ResourceLocation identifier = BuiltInRegistries.ITEM.getKey(this.item);
		return Component.translatable("spectrum.rei.potion_workshop_reacting." + identifier.getNamespace() + "." + identifier.getPath());
	}
	
	@Override
	public Item getItem() {
		return this.item;
	}
	
	public static boolean isReagent(Item item) {
		return reagents.containsKey(item);
	}
	
	public static PotionMod.Builder combine(PotionMod.Builder builder, ItemStack reagentStack, RandomSource random) {
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
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(c -> c.requiredAdvancementIdentifier),
				BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(c -> c.item),
				CodecHelper.singleOrList(PotionMod.CODEC).fieldOf("modifiers").forGetter(c -> c.modifiers)
		).apply(i, PotionWorkshopReactingRecipe::new));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, PotionWorkshopReactingRecipe> PACKET_CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8, c -> c.group,
				ByteBufCodecs.BOOL, c -> c.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), c -> c.requiredAdvancementIdentifier,
				ByteBufCodecs.registry(Registries.ITEM), c -> c.item,
				PotionMod.PACKET_CODEC.apply(ByteBufCodecs.list()), c -> c.modifiers,
				PotionWorkshopReactingRecipe::new
		);
		
		@Override
		public MapCodec<PotionWorkshopReactingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, PotionWorkshopReactingRecipe> streamCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
