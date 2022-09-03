package de.dafuqs.spectrum.recipe.spirit_instiller.spawner;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlockEntity;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.spirit_instiller.ISpiritInstillerRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public abstract class SpawnerChangeRecipe implements ISpiritInstillerRecipe {
	
	public static final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_spawner_manipulation");
	public final Identifier identifier;
	
	public SpawnerChangeRecipe(Identifier identifier) {
		super();
		this.identifier = identifier;
		registerInToastManager(SpectrumRecipeTypes.SPIRIT_INSTILLING, this);
	}
	
	@Override
	public ItemStack getOutput() {
		return SpectrumItems.SPAWNER.getDefaultStack();
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
					ISpiritInstillerRecipe.grantPlayerSpiritInstillingAdvancementCriterion(spiritInstillerBlockEntity.getOwnerUUID(), resultStack, awardedExperience);
				}
			}
		}
		
		return resultStack;
	}
	
	@Override
	public Identifier getId() {
		return this.identifier;
	}
	
	@Override
	public String getGroup() {
		return "spawner_manipulation";
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() { // 0: instiller stack; 1-2: item bowl stacks
		DefaultedList<IngredientStack> defaultedList = DefaultedList.of();
		defaultedList.add(IngredientStack.of(Ingredient.fromTag(SpectrumItemTags.SPAWNERS)));
		defaultedList.add(getIngredientStack());
		defaultedList.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.VEGETAL), 4));
		return defaultedList;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		for (IngredientStack ingredientStack : getIngredientStacks()) {
			defaultedList.add(ingredientStack.getIngredient());
		}
		return defaultedList;
	}
	
	@Override
	public float getExperience() {
		return 0;
	}
	
	@Override
	public int getCraftingTime() {
		return 200;
	}
	
	@Override
	public Identifier getRequiredAdvancementIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public boolean areYieldAndEfficiencyUpgradesDisabled() {
		return true;
	}
	
	@Override
	public boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, SpiritInstillerRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER) && AdvancementHelper.hasAdvancement(playerEntity, UNLOCK_IDENTIFIER);
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
	
	public abstract IngredientStack getIngredientStack();
	
	public abstract boolean canCraftWithBlockEntityTag(NbtCompound spawnerBlockEntityNbt, ItemStack leftBowlStack, ItemStack rightBowlStack);
	
	public abstract NbtCompound getSpawnerResultNbt(NbtCompound spawnerBlockEntityNbt, ItemStack secondBowlStack, ItemStack centerStack);
	
	@Override
	public abstract RecipeSerializer<?> getSerializer();
	
	public abstract TranslatableText getOutputLoreText();
	
}
