package de.dafuqs.spectrum.recipe.fusion_shrine.dynamic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntity;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarItem;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldEffect;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ShootingStarHardeningRecipe extends FusionShrineRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("collect_all_shooting_star_variants");
	public static final Text DESCRIPTION = Text.translatable("spectrum.recipe.fusion_shrine.explanation.shooting_star_hardening");
	public static final RecipeSerializer<ShootingStarHardeningRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ShootingStarHardeningRecipe::new);
	
	public ShootingStarHardeningRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER, List.of(IngredientStack.of(Ingredient.fromTag(SpectrumItemTags.SHOOTING_STARS)), IngredientStack.of(Ingredient.ofItems(Items.DIAMOND))), Fluids.WATER, getHardenedShootingStar(),
				5, 100, true, true, new ArrayList<>(), FusionShrineRecipeWorldEffect.NOTHING, new ArrayList<>(), FusionShrineRecipeWorldEffect.NOTHING, DESCRIPTION);
	}
	
	private static ItemStack getHardenedShootingStar() {
		ItemStack stack = SpectrumBlocks.GLISTERING_SHOOTING_STAR.asItem().getDefaultStack();
		ShootingStarItem.setHardened(stack);
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	@Override
	public void craft(World world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		ItemStack shootingStarStack = ItemStack.EMPTY;
		ItemStack diamondStack = ItemStack.EMPTY;
		
		for (int j = 0; j < fusionShrineBlockEntity.size(); ++j) {
			ItemStack itemStack = fusionShrineBlockEntity.getStack(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof ShootingStarItem) {
					shootingStarStack = itemStack;
				} else if (itemStack.isOf(Items.DIAMOND)) {
					diamondStack = itemStack;
				}
			}
		}
		
		if (!shootingStarStack.isEmpty() && !diamondStack.isEmpty()) {
			int craftedAmount = Math.min(shootingStarStack.getCount(), diamondStack.getCount());
			
			ItemStack hardenedStack = shootingStarStack.copy();
			ShootingStarItem.setHardened(hardenedStack);
			
			shootingStarStack.decrement(craftedAmount);
			diamondStack.decrement(craftedAmount);
			
			spawnCraftingResultAndXP(world, fusionShrineBlockEntity, hardenedStack, craftedAmount, noBenefitsFromYieldUpgrades, experience); // spawn results
		}
	}
	
}
