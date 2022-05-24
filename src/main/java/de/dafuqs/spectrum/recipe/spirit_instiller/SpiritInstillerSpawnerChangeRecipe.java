package de.dafuqs.spectrum.recipe.spirit_instiller;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.MultiblockCrafter;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity;
import de.dafuqs.spectrum.blocks.memory.MemoryItem;
import de.dafuqs.spectrum.blocks.mob_head.SpectrumSkullBlockItem;
import de.dafuqs.spectrum.blocks.spirit_instiller.SpiritInstillerBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.SpectrumMobSpawnerItem;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.TippedArrowRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpiritInstillerSpawnerChangeRecipe implements ISpiritInstillerRecipe {
	
	public static final RecipeSerializer<SpiritInstillerSpawnerChangeRecipe> SERIALIZER = new SpecialRecipeSerializer<>(SpiritInstillerSpawnerChangeRecipe::new);
	public static final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_spawner_manipulation");
	public final Identifier identifier;
	
	public SpiritInstillerSpawnerChangeRecipe(Identifier identifier) {
		super();
		this.identifier = identifier;
	}
	
	@Override
	public ItemStack getOutput() {
		return SpectrumItems.SPAWNER.getDefaultStack();
	}
	
	@Override
	public ItemStack craft(Inventory inv) {
		if(inv instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
			Map<Upgradeable.UpgradeType, Double> upgrades = spiritInstillerBlockEntity.getUpgrades();
			World world = spiritInstillerBlockEntity.getWorld();
			BlockPos pos = spiritInstillerBlockEntity.getPos();
			
			ItemStack firstBowlStack = inv.getStack(0);
			ItemStack secondBowlStack = inv.getStack(1);
			
			ItemStack inputSpawnerStack;
			ItemStack mobHeadStack;
			if(firstBowlStack.isIn(SpectrumItemTags.SPAWNERS)) {
				if(!secondBowlStack.isIn(SpectrumItemTags.MOB_HEADS)) {
					return ItemStack.EMPTY;
				}
				inputSpawnerStack = firstBowlStack;
				mobHeadStack = secondBowlStack;
			} else {
				if(!firstBowlStack.isIn(SpectrumItemTags.MOB_HEADS)) {
					return ItemStack.EMPTY;
				}
				inputSpawnerStack = secondBowlStack;
				mobHeadStack = firstBowlStack;
			}
			
			Optional<EntityType> entityType = SpectrumSkullBlockItem.getEntityTypeOfSkullStack(mobHeadStack);
			if(entityType.isEmpty()) {
				return ItemStack.EMPTY;
			}
			
			Identifier entityTypeIdentifier = Registry.ENTITY_TYPE.getId(entityType.get());
			
			// Example spawner tag:
			/* {
				MaxNearbyEntities: 6s,
				RequiredPlayerRange: 16s,
				SpawnCount: 4s,
				SpawnData: {entity: {id: "minecraft:zombie"}},
				MaxSpawnDelay: 800s,
				SpawnRange: 4s,
				Delay: 83s,
				MinSpawnDelay: 200s,
				SpawnPotentials: []
			   }
			 */
			NbtCompound spawnerNbt = inputSpawnerStack.getOrCreateNbt();
			NbtCompound idCompound = new NbtCompound();
			idCompound.putString("id", entityTypeIdentifier.toString());
			NbtCompound entityCompound = new NbtCompound();
			entityCompound.put("entity", idCompound);
			spawnerNbt.put("SpawnData", entityCompound);
			
			if(spawnerNbt.contains("SpawnPotentials")) {
				spawnerNbt.remove("SpawnPotentials");
			}
			
			ItemStack resultStack = SpectrumItems.SPAWNER.getDefaultStack();
			resultStack.setNbt(spawnerNbt);
			
			// spawn the result stack in world
			// TODO: it just vanishes?
			EnchanterBlockEntity.spawnItemStackAsEntitySplitViaMaxCount(world, pos, resultStack, resultStack.getCount());
			
			// Calculate and spawn experience
			double experienceModifier = upgrades.get(Upgradeable.UpgradeType.EXPERIENCE);
			float recipeExperienceBeforeMod = getExperience();
			int awardedExperience = Support.getIntFromDecimalWithChance(recipeExperienceBeforeMod * experienceModifier, world.random);
			MultiblockCrafter.spawnExperience(world, pos.up(), awardedExperience);
			
			// Run Advancement trigger
			ISpiritInstillerRecipe.grantPlayerSpiritInstillingAdvancementCriterion(world, spiritInstillerBlockEntity.getOwnerUUID(), resultStack, awardedExperience);
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public Identifier getId() {
		return this.identifier;
	}
	
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(Ingredient.fromTag(SpectrumItemTags.SPAWNERS));
		defaultedList.add(Ingredient.fromTag(SpectrumItemTags.MOB_HEADS));
		defaultedList.add(Ingredient.ofItems(SpectrumItems.VEGETAL));
		return defaultedList;
	}
	
	@Override
	public List<IngredientStack> getIngredientStacks() {
		DefaultedList<IngredientStack> defaultedList = DefaultedList.of();
		defaultedList.add(IngredientStack.of(Ingredient.fromTag(SpectrumItemTags.SPAWNERS)));
		defaultedList.add(IngredientStack.of(Ingredient.fromTag(SpectrumItemTags.MOB_HEADS)));
		defaultedList.add(IngredientStack.of(Ingredient.ofItems(SpectrumItems.VEGETAL), 4));
		return defaultedList;
	}
	
	@Override
	public float getExperience() {
		return 0;
	}
	
	@Override
	public int getCraftingTime() {
		return 400;
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
		return Support.hasAdvancement(playerEntity, SpiritInstillerRecipe.UNLOCK_SPIRIT_INSTILLER_ADVANCEMENT_IDENTIFIER) && Support.hasAdvancement(playerEntity, UNLOCK_IDENTIFIER);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
