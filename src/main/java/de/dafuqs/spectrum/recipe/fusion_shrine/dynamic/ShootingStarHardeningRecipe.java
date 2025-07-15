package de.dafuqs.spectrum.recipe.fusion_shrine.dynamic;


import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.fusion_shrine.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public class ShootingStarHardeningRecipe extends FusionShrineRecipe {
	
	public static final ResourceLocation UNLOCK_IDENTIFIER = SpectrumCommon.locate("collect_all_shooting_star_variants");
	public static final Component DESCRIPTION = Component.translatable("spectrum.recipe.fusion_shrine.explanation.shooting_star_hardening");
	
	public ShootingStarHardeningRecipe() {
		super("", false, Optional.of(UNLOCK_IDENTIFIER), List.of(IngredientStack.ofTag(SpectrumItemTags.SHOOTING_STARS), IngredientStack.ofItems(Items.DIAMOND)), FluidIngredient.of(Fluids.WATER), getHardenedShootingStar(),
				5, 100, true, true, true, new ArrayList<>(), FusionShrineRecipeWorldEffect.NOTHING, new ArrayList<>(), FusionShrineRecipeWorldEffect.NOTHING, DESCRIPTION);
	}
	
	private static ItemStack getHardenedShootingStar() {
		ItemStack stack = SpectrumBlocks.GLISTERING_SHOOTING_STAR.asItem().getDefaultInstance();
		ShootingStarItem.setHardened(stack);
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.SHOOTING_STAR_HARDENING;
	}
	
	@Override
	public void craft(Level world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		ItemStack shootingStarStack = ItemStack.EMPTY;
		ItemStack diamondStack = ItemStack.EMPTY;
		
		for (int j = 0; j < fusionShrineBlockEntity.getContainerSize(); ++j) {
			ItemStack itemStack = fusionShrineBlockEntity.getItem(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof ShootingStarItem) {
					shootingStarStack = itemStack;
				} else if (itemStack.is(Items.DIAMOND)) {
					diamondStack = itemStack;
				}
			}
		}
		
		if (!shootingStarStack.isEmpty() && !diamondStack.isEmpty()) {
			int craftedAmount = Math.min(shootingStarStack.getCount(), diamondStack.getCount());
			
			ItemStack hardenedStack = shootingStarStack.copy();
			ShootingStarItem.setHardened(hardenedStack);
			
			shootingStarStack.shrink(craftedAmount);
			diamondStack.shrink(craftedAmount);
			
			spawnCraftingResultAndXP(world, fusionShrineBlockEntity, hardenedStack, craftedAmount); // spawn results
		}
	}
	
}
