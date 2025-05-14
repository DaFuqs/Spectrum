package de.dafuqs.spectrum.recipe.pedestal.dynamic;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class StarCandyRecipe extends ShapedPedestalRecipe {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/food/star_candy");
	public static final float PURPLE_STAR_CANDY_CHANCE = 0.01F;
	
	public StarCandyRecipe() {
		super("", false, Optional.of(UNLOCK_IDENTIFIER), PedestalRecipeTier.BASIC, new RawShapedPedestalRecipe(3, 3, generateInputs(), Optional.empty()), Map.of(BuiltinGemstoneColor.YELLOW, 1), SpectrumItems.STAR_CANDY.getDefaultInstance(), 1.0F, 20, false, false);
	}
	
	@Override
	public ItemStack assemble(PedestalRecipeInput input, HolderLookup.Provider wrapperLookup) {
		@Nullable Player player = input.getPlayer();
		if (player != null && player.getRandom().nextFloat() < PURPLE_STAR_CANDY_CHANCE + player.getAttributeValue(Attributes.LUCK)) {
			return SpectrumItems.ENCHANTED_STAR_CANDY.getDefaultInstance();
		}
		return this.output.copy();
	}
	
	private static NonNullList<IngredientStack> generateInputs() {
		return NonNullList.of(IngredientStack.EMPTY,
				IngredientStack.ofItems(Items.SUGAR),
				IngredientStack.ofItems(Items.SUGAR),
				IngredientStack.ofItems(Items.SUGAR),
				IngredientStack.ofItems(SpectrumItems.STARDUST),
				IngredientStack.ofItems(SpectrumItems.STARDUST),
				IngredientStack.ofItems(SpectrumItems.STARDUST),
				IngredientStack.ofItems(SpectrumItems.AMARANTH_GRAINS),
				IngredientStack.ofItems(SpectrumItems.AMARANTH_GRAINS),
				IngredientStack.ofItems(SpectrumItems.AMARANTH_GRAINS)
		);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.PEDESTAL_STAR_CANDY;
	}
	
	
}
