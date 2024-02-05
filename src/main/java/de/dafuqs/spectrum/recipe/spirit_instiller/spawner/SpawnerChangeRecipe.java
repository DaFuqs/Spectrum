package de.dafuqs.spectrum.recipe.spirit_instiller.spawner;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.id.incubus_core.recipe.matchbook.*;
import net.minecraft.block.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public abstract class SpawnerChangeRecipe extends SpiritInstillerRecipe {
	
	public SpawnerChangeRecipe(Identifier identifier, IngredientStack ingredient) {
		super(identifier, "spawner_manipulation", false, SpectrumCommon.locate("milestones/unlock_spawner_manipulation"),
				IngredientStack.of(Ingredient.ofItems(Items.SPAWNER)), ingredient, IngredientStack.of(Ingredient.ofItems(SpectrumItems.VEGETAL), Matchbook.empty(), null, 4),
				Items.SPAWNER.getDefaultStack(), 200, 0, true);
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		ItemStack resultStack = ItemStack.EMPTY;
		
		if (inv instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
			BlockEntity leftBowlBlockEntity = spiritInstillerBlockEntity.getWorld().getBlockEntity(SpiritInstillerBlockEntity.getItemBowlPos(spiritInstillerBlockEntity, false));
			BlockEntity rightBowlBlockEntity = spiritInstillerBlockEntity.getWorld().getBlockEntity(SpiritInstillerBlockEntity.getItemBowlPos(spiritInstillerBlockEntity, true));
			if (leftBowlBlockEntity instanceof ItemBowlBlockEntity leftBowl && rightBowlBlockEntity instanceof ItemBowlBlockEntity rightBowl) {
				World world = spiritInstillerBlockEntity.getWorld();
				BlockPos pos = spiritInstillerBlockEntity.getPos();
				
				ItemStack firstBowlStack = leftBowl.getStack(0);
				ItemStack secondBowlStack = rightBowl.getStack(0);
				ItemStack spawnerStack = spiritInstillerBlockEntity.getStack(0);
				
				NbtCompound spawnerNbt = spawnerStack.getOrCreateNbt();
				NbtCompound blockEntityTag;
				if (spawnerNbt.contains("BlockEntityTag")) {
					blockEntityTag = spawnerNbt.getCompound("BlockEntityTag").copy();
				} else {
					blockEntityTag = new NbtCompound();
				}
				
				blockEntityTag = getSpawnerResultNbt(blockEntityTag, firstBowlStack, secondBowlStack);
				
				resultStack = spawnerStack.copy();
				resultStack.setCount(1);
				resultStack.setSubNbt("BlockEntityTag", blockEntityTag);
				
				spawnXPAndGrantAdvancements(resultStack, spiritInstillerBlockEntity, spiritInstillerBlockEntity.getUpgradeHolder(), world, pos);
			}
		}
		
		return resultStack;
	}
	
	@Override
	public boolean canCraftWithStacks(Inventory inventory) {
		NbtCompound blockEntityTag = inventory.getStack(0).getSubNbt("BlockEntityTag");
		if (blockEntityTag == null) {
			return true;
		}
		return canCraftWithBlockEntityTag(blockEntityTag, inventory.getStack(1), inventory.getStack(2));
	}
	
	// Overwrite these
	@Override
	public abstract RecipeSerializer<?> getSerializer();
	
	public abstract boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack);
	
	public abstract NbtCompound getSpawnerResultNbt(NbtCompound spawnerBlockEntityNbt, ItemStack secondBowlStack, ItemStack centerStack);
	
	public abstract Text getOutputLoreText();
	
}
