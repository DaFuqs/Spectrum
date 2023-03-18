package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PotionWorkshopReactingRecipe extends GatedSpectrumRecipe implements DescriptiveGatedRecipe {
	
	protected static final HashMap<Item, List<PotionMod>> reagents = new HashMap<>();
	
	protected final Item item;
	protected final List<PotionMod> modifiers;
	
	public PotionWorkshopReactingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Item item, List<PotionMod> modifiers) {
		super(id, group, secret, requiredAdvancementIdentifier);
		this.item = item;
		this.modifiers = modifiers;
		
		reagents.put(item, modifiers);
		
		registerInToastManager(getType(), this);
	}
	
	@Override
	public boolean matches(@NotNull Inventory inv, World world) {
		return false;
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return false;
	}
	
	@Override
	public ItemStack getOutput() {
		return item.getDefaultStack();
	}
	
	@Override
	public ItemStack createIcon() {
		return SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultStack();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_REACTING_SERIALIZER;
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
		return SpectrumRecipeTypes.POTION_WORKSHOP_REACTING_ID;
	}
	
	@Override
	public Text getDescription() {
		Identifier identifier = Registry.ITEM.getId(this.item);
		return Text.translatable("spectrum.rei.potion_workshop_reacting." + identifier.getNamespace() + "." + identifier.getPath());
	}
	
	@Override
	public Item getItem() {
		return this.item;
	}
	
	public static boolean isReagent(Item item) {
		return reagents.containsKey(item);
	}
	
	public static PotionMod combine(PotionMod potionMod, ItemStack reagentStack, Random random) {
		Item reagent = reagentStack.getItem();
		List<PotionMod> reagentMods = reagents.getOrDefault(reagent, null);
		if (reagentMods != null) {
			potionMod.modifyFrom(reagentMods.get(random.nextInt(reagentMods.size())));
		}
		return potionMod;
	}
	
}
