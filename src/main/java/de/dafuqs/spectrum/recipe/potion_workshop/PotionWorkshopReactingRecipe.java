package de.dafuqs.spectrum.recipe.potion_workshop;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.recipe.DescriptiveGatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PotionWorkshopReactingRecipe implements Recipe<Inventory>, DescriptiveGatedRecipe {
	
	protected static final HashMap<Item, List<PotionMod>> reagents = new HashMap<>();
	
	protected final Identifier id;
	protected final Item item;
	protected final List<PotionMod> modifiers;
	
	@Nullable
	protected final Identifier requiredAdvancementIdentifier;
	
	public PotionWorkshopReactingRecipe(Identifier id, Item item, List<PotionMod> modifiers, @Nullable Identifier requiredAdvancementIdentifier) {
		this.id = id;
		this.item = item;
		this.modifiers = modifiers;
		this.requiredAdvancementIdentifier = requiredAdvancementIdentifier;
		
		reagents.put(item, modifiers);
		
		registerInToastManager(SpectrumRecipeTypes.POTION_WORKSHOP_REACTING, this);
	}
	
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
	
	public boolean isIgnoredInRecipeBook() {
		return true;
	}
	
	@Override
	public ItemStack createIcon() {
		return SpectrumBlocks.POTION_WORKSHOP.asItem().getDefaultStack();
	}
	
	public Identifier getId() {
		return this.id;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.POTION_WORKSHOP_REACTING_SERIALIZER;
	}
	
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
	public boolean equals(Object object) {
		if (object instanceof PotionWorkshopReactingRecipe) {
			return ((PotionWorkshopReactingRecipe) object).getId().equals(this.getId());
		}
		return false;
	}
	
	@Nullable
	public Identifier getRequiredAdvancementIdentifier() {
		return this.requiredAdvancementIdentifier;
	}
	
	@Override
	public TranslatableText getSingleUnlockToastString() {
		return new TranslatableText("spectrum.toast.potion_reagent_unlocked.title");
	}
	
	@Override
	public TranslatableText getMultipleUnlockToastString() {
		return new TranslatableText("spectrum.toast.potion_reagents_unlocked.title");
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, PotionWorkshopRecipe.UNLOCK_POTION_WORKSHOP_ADVANCEMENT_IDENTIFIER) && AdvancementHelper.hasAdvancement(playerEntity, this.requiredAdvancementIdentifier);
	}
	
	public static boolean isReagent(Item item) {
		return reagents.containsKey(item);
	}
	
	public TranslatableText getDescription() {
		Identifier identifier = Registry.ITEM.getId(this.item);
		return new TranslatableText("spectrum.rei.potion_workshop_reacting." + identifier.getNamespace() + "." + identifier.getPath());
	}
	
	public Item getItem() {
		return this.item;
	}
	
	public static PotionMod modify(Item item, PotionMod potionMod, Random random) {
		if (reagents.containsKey(item)) {
			List<PotionMod> mod = reagents.get(item);
			if (mod.size() == 1) {
				return mod.get(0).modify(potionMod);
			} else {
				return mod.get(random.nextInt(mod.size())).modify(potionMod);
			}
		}
		return potionMod;
	}
	
}
