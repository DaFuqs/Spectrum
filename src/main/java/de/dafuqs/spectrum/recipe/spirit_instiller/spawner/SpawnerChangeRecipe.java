package de.dafuqs.spectrum.recipe.spirit_instiller.spawner;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlockEntity;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public abstract class SpawnerChangeRecipe extends SpiritInstillerRecipe {
	
	public SpawnerChangeRecipe(Identifier identifier, IngredientStack ingredient) {
		super(identifier, "spawner_manipulation", false, SpectrumCommon.locate("milestones/unlock_spawner_manipulation"), IngredientStack.of(Ingredient.fromTag(SpectrumItemTags.SPAWNERS)), ingredient, IngredientStack.of(Ingredient.ofItems(SpectrumItems.VEGETAL), 4), SpectrumItems.SPAWNER.getDefaultStack(), 0, 200, true);
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
				
				ItemStack firstBowlStack = leftBowl.getInventory().getStack(0);
				ItemStack secondBowlStack = rightBowl.getInventory().getStack(0);
				
				NbtCompound spawnerNbt = spiritInstillerBlockEntity.getStack(0).getOrCreateNbt();
				NbtCompound blockEntityTag;
				if (spawnerNbt.contains("BlockEntityTag")) {
					blockEntityTag = spawnerNbt.getCompound("BlockEntityTag");
				} else {
					blockEntityTag = new NbtCompound();
				}
				
				blockEntityTag = getSpawnerResultNbt(blockEntityTag, firstBowlStack, secondBowlStack);
				
				resultStack = SpectrumItems.SPAWNER.getDefaultStack();
				NbtCompound outputNbt = resultStack.getOrCreateNbt();
				outputNbt.put("BlockEntityTag", blockEntityTag);
				resultStack.setNbt(outputNbt);
				if (!resultStack.isEmpty()) {
					// put the stack back into the instiller
					// so the player can craft > 1x automatically
					
					// Calculate and spawn experience
					int awardedExperience = 0;
					if (getExperience() > 0) {
						Map<Upgradeable.UpgradeType, Float> upgrades = spiritInstillerBlockEntity.getUpgrades();
						double experienceModifier = upgrades.get(Upgradeable.UpgradeType.EXPERIENCE);
						float recipeExperienceBeforeMod = getExperience();
						awardedExperience = Support.getIntFromDecimalWithChance(recipeExperienceBeforeMod * experienceModifier, world.random);
						MultiblockCrafter.spawnExperience(world, pos.up(), awardedExperience);
					}
					
					// Run Advancement trigger
					grantPlayerSpiritInstillingAdvancementCriterion(spiritInstillerBlockEntity.getOwnerUUID(), resultStack, awardedExperience);
				}
			}
		}
		
		return resultStack;
	}
	
	@Override
	public boolean canCraftWithStacks(ItemStack instillerStack, ItemStack leftBowlStack, ItemStack rightBowlStack) {
		NbtCompound blockEntityTag = instillerStack.getSubNbt("BlockEntityTag");
		if (blockEntityTag == null) {
			return true;
		}
		return canCraftWithBlockEntityTag(instillerStack.getSubNbt("BlockEntityTag"), leftBowlStack, rightBowlStack);
	}
	
	// Overwrite these
	@Override public abstract RecipeSerializer<?> getSerializer();
	public abstract boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack);
	public abstract NbtCompound getSpawnerResultNbt(NbtCompound spawnerBlockEntityNbt, ItemStack secondBowlStack, ItemStack centerStack);
	public abstract TranslatableText getOutputLoreText();
	
}
