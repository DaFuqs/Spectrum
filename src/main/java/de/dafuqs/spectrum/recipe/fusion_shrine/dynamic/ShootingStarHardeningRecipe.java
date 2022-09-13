package de.dafuqs.spectrum.recipe.fusion_shrine.dynamic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntity;
import de.dafuqs.spectrum.blocks.shooting_star.ShootingStarItem;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipeWorldEffect;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ShootingStarHardeningRecipe extends FusionShrineRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("collect_all_shooting_star_variants");
	public static final Text DESCRIPTION = new TranslatableText("spectrum.recipe.fusion_shrine.explanation.shooting_star_hardening");
	public static final RecipeSerializer<ShootingStarHardeningRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ShootingStarHardeningRecipe::new);
	
	public ShootingStarHardeningRecipe(Identifier identifier) {
		super(identifier, "", List.of(IngredientStack.of(Ingredient.ofItems(SpectrumBlocks.GLISTERING_SHOOTING_STAR.asItem())), IngredientStack.of(Ingredient.ofItems(Items.DIAMOND))), Fluids.WATER, getHardenedShootingStar(),
				5, 100, true, UNLOCK_IDENTIFIER, new ArrayList<>(), FusionShrineRecipeWorldEffect.NOTHING, new ArrayList<>(), FusionShrineRecipeWorldEffect.NOTHING, DESCRIPTION);
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
	public boolean matches(Inventory inv, World world) {
		boolean shootingStarFound = false;
		boolean diamondFound = false;
		
		for (int j = 0; j < inv.size(); ++j) {
			ItemStack itemStack = inv.getStack(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof ShootingStarItem) {
					if (shootingStarFound) {
						return false;
					}
					shootingStarFound = true;
				} else if (itemStack.isOf(Items.DIAMOND)) {
					if (diamondFound) {
						return false;
					}
					diamondFound = true;
				}
			}
		}
		
		return shootingStarFound && diamondFound;
	}
	
	@Override
	public void craft(World world, FusionShrineBlockEntity fusionShrineBlockEntity) {
		ItemStack shootingStarStack = ItemStack.EMPTY;
		ItemStack diamondStack = ItemStack.EMPTY;
		
		for (int j = 0; j < fusionShrineBlockEntity.getInventory().size(); ++j) {
			ItemStack itemStack = fusionShrineBlockEntity.getInventory().getStack(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof ShootingStarItem && itemStack.getCount() == 1) {
					shootingStarStack = itemStack;
				} else if (itemStack.isOf(Items.DIAMOND)) {
					diamondStack = itemStack;
				}
			}
		}
		
		if(!shootingStarStack.isEmpty() && !diamondStack.isEmpty()) {
			int craftedAmount = Math.min(shootingStarStack.getCount(), diamondStack.getCount());
			
			ItemStack hardenedStack = shootingStarStack.copy();
			ShootingStarItem.setHardened(hardenedStack);
			hardenedStack.setCount(craftedAmount);
			
			shootingStarStack.decrement(craftedAmount);
			diamondStack.decrement(craftedAmount);
			
			InventoryHelper.smartAddToInventory(hardenedStack, fusionShrineBlockEntity.getInventory(), null);
			fusionShrineBlockEntity.setFluid(Fluids.EMPTY); // empty the shrine
			FusionShrineBlockEntity.spawnCraftingResultAndXP(world, fusionShrineBlockEntity, this, craftedAmount); // spawn results
		}
	}
	
}
