package de.dafuqs.spectrum.recipe.ink_converting;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.*;

import java.util.*;

public class InkConvertingRecipe extends GatedSpectrumRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/place_color_picker");
	protected static final List<Item> INPUT_ITEMS = new ArrayList<>();
	
	protected final Ingredient inputIngredient;
	protected final InkColor color;
	protected final long amount;
	
	public InkConvertingRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier, Ingredient inputIngredient, InkColor color, long amount) {
		super(id, group, secret, requiredAdvancementIdentifier);
		
		this.inputIngredient = inputIngredient;
		this.color = color;
		this.amount = amount;
		
		for (ItemStack itemStack : inputIngredient.getMatchingStacks()) {
			Item item = itemStack.getItem();
			if (!INPUT_ITEMS.contains(item)) {
				INPUT_ITEMS.add(item);
			}
		}
		
		registerInToastManager(getType(), this);
	}
	
	public static boolean isInput(Item item) {
		return INPUT_ITEMS.contains(item);
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return this.inputIngredient.test(inv.getStack(0));
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput(DynamicRegistryManager registryManager) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(SpectrumBlocks.COLOR_PICKER);
	}
	
	@Override
	public Identifier getRecipeTypeUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeTypes.INK_CONVERTING_RECIPE_SERIALIZER;
	}
	
	@Override
	public RecipeType<?> getType() {
		return SpectrumRecipeTypes.INK_CONVERTING;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.inputIngredient);
		return defaultedList;
	}
	
	@Override
	public String getRecipeTypeShortID() {
		return SpectrumRecipeTypes.INK_CONVERTING_ID;
	}
	
	public InkColor getInkColor() {
		return this.color;
	}
	
	public long getInkAmount() {
		return this.amount;
	}
	
}
