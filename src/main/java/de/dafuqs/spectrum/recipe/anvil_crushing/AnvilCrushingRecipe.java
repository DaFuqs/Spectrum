package de.dafuqs.spectrum.recipe.anvil_crushing;

import de.dafuqs.spectrum.recipe.*;
import net.minecraft.block.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;

public class AnvilCrushingRecipe extends GatedSpectrumRecipe {
	
	protected final Ingredient inputIngredient;
	protected final ItemStack outputItemStack;
	protected final float crushedItemsPerPointOfDamage;
	protected final float experience;
	protected final Identifier particleEffectIdentifier;
	protected final int particleCount;
	protected final Identifier soundEvent;
	
	public AnvilCrushingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier,
	                           Ingredient inputIngredient, ItemStack outputItemStack, float crushedItemsPerPointOfDamage,
	                           float experience, Identifier particleEffectIdentifier, int particleCount, Identifier soundEventIdentifier) {
		
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.inputIngredient = inputIngredient;
		this.outputItemStack = outputItemStack;
		this.crushedItemsPerPointOfDamage = crushedItemsPerPointOfDamage;
		this.experience = experience;
		this.particleEffectIdentifier = particleEffectIdentifier;
		this.particleCount = particleCount;
		this.soundEvent = soundEventIdentifier;
		
		if (requiredAdvancementIdentifier != null) {
			registerInToastManager(getType(), this);
		}
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return this.inputIngredient.test(inv.getStack(0));
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		return null;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager drm) {
		return outputItemStack.copy();
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(Blocks.ANVIL);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.ANVIL_CRUSHING_ID;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient);
		return defaultedList;
	}

	public float getCrushedItemsPerPointOfDamage() {
		return crushedItemsPerPointOfDamage;
	}

	public SoundEvent getSoundEvent() {
		return Registries.SOUND_EVENT.get(soundEvent);
	}

	public ParticleEffect getParticleEffect() {
		return (ParticleEffect) Registries.PARTICLE_TYPE.get(particleEffectIdentifier);
	}

	public int getParticleCount() {
		return particleCount;
	}

	public float getExperience() {
		return experience;
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return null;
	}
	
}
