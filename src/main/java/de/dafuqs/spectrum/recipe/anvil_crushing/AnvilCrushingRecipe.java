package de.dafuqs.spectrum.recipe.anvil_crushing;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class AnvilCrushingRecipe extends GatedSpectrumRecipe<SingleRecipeInput> {
	
	protected final Ingredient ingredient;
	protected final ItemStack result;
	protected final float crushedItemsPerPointOfDamage;
	protected final float experience;
	protected final Optional<ResourceLocation> particleEffectIdentifier;
	protected final int particleCount;
	protected final ResourceLocation soundEvent;
	
	public AnvilCrushingRecipe(String group, boolean secret, Optional<ResourceLocation> requiredAdvancementIdentifier,
							   Ingredient ingredient, ItemStack result, float crushedItemsPerPointOfDamage,
							   float experience, Optional<ResourceLocation> particleEffectIdentifier, int particleCount, ResourceLocation soundEventIdentifier) {
		
		super(group, secret, requiredAdvancementIdentifier);
		
		this.ingredient = ingredient;
		this.result = result;
		this.crushedItemsPerPointOfDamage = crushedItemsPerPointOfDamage;
		this.experience = experience;
		this.particleEffectIdentifier = particleEffectIdentifier;
		this.particleCount = particleCount;
		this.soundEvent = soundEventIdentifier;
		
		if (requiredAdvancementIdentifier.isPresent()) {
			registerInToastManager(getType(), this);
		}
	}
	
	@Override
	public boolean matches(SingleRecipeInput input, Level world) {
		return ingredient.test(input.item());
	}
	
	@Override
	public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registryLookup) {
		return result.copy();
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider registriesLookup) {
		return result;
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(Blocks.ANVIL);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.ANVIL_CRUSHING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return "anvil_crushing";
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> defaultedList = NonNullList.create();
		defaultedList.add(this.ingredient);
		return defaultedList;
	}
	
	public float getCrushedItemsPerPointOfDamage() {
		return crushedItemsPerPointOfDamage;
	}
	
	public SoundEvent getSoundEvent() {
		return BuiltInRegistries.SOUND_EVENT.get(soundEvent);
	}
	
	public ParticleOptions getParticleEffect() {
		return (ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(particleEffectIdentifier.orElse(null));
	}
	
	public int getParticleCount() {
		return particleCount;
	}
	
	public float getExperience() {
		return experience;
	}
	
	public static class Serializer implements RecipeSerializer<AnvilCrushingRecipe> {
		
		private static final MapCodec<AnvilCrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				Codec.BOOL.optionalFieldOf("secret", false).forGetter(recipe -> recipe.secret),
				ResourceLocation.CODEC.optionalFieldOf("required_advancement").forGetter(recipe -> recipe.requiredAdvancementIdentifier),
				Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
				Codec.FLOAT.fieldOf("crushedItemsPerPointOfDamage").forGetter(recipe -> recipe.crushedItemsPerPointOfDamage),
				Codec.FLOAT.fieldOf("experience").forGetter(recipe -> recipe.experience),
				ResourceLocation.CODEC.optionalFieldOf("particleEffectIdentifier").forGetter(recipe -> recipe.particleEffectIdentifier),
				Codec.INT.optionalFieldOf("particleCount", 1).forGetter(recipe -> recipe.particleCount),
				ResourceLocation.CODEC.fieldOf("soundEventIdentifier").forGetter(recipe -> recipe.soundEvent)
		).apply(instance, AnvilCrushingRecipe::new));
		
		private static final StreamCodec<RegistryFriendlyByteBuf, AnvilCrushingRecipe> PACKET_CODEC = PacketCodecHelper.tuple(
				ByteBufCodecs.STRING_UTF8, c -> c.group,
				ByteBufCodecs.BOOL, c -> c.secret,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), c -> c.requiredAdvancementIdentifier,
				Ingredient.CONTENTS_STREAM_CODEC, c -> c.ingredient,
				ItemStack.STREAM_CODEC, c -> c.result,
				ByteBufCodecs.FLOAT, c -> c.crushedItemsPerPointOfDamage,
				ByteBufCodecs.FLOAT, c -> c.experience,
				ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), c -> c.particleEffectIdentifier,
				ByteBufCodecs.VAR_INT, c -> c.particleCount,
				ResourceLocation.STREAM_CODEC, c -> c.soundEvent,
				AnvilCrushingRecipe::new
		);
		
		@Override
		public MapCodec<AnvilCrushingRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, AnvilCrushingRecipe> streamCodec() {
			return PACKET_CODEC;
		}
		
	}
	
}
