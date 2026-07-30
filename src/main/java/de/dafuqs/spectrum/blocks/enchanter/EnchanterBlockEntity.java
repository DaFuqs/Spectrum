package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.item_bowl.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.*;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.*;

public class EnchanterBlockEntity extends InWorldInteractionBlockEntity implements MultiblockCrafter, WorldlyContainer {
	
	public static final String ITEM_TRANS = "container.spectrum.rei.enchantment_upgrade.required_item_count";
	public static final String LEVEL_TRANS = "container.spectrum.rei.enchantment_upgrade.level";
	public static final String OVERCHANTING_TOOLTIP = "container.spectrum.rei.enchantment_upgrade.tooltip";
	public static final String CYCLING = "container.spectrum.rei.enchantment_upgrade.button";
	
	public static final List<Vec3i> ITEM_BOWL_OFFSETS = new ArrayList<>() {{
		add(new Vec3i(5, 0, -3));
		add(new Vec3i(5, 0, 3));
		add(new Vec3i(3, 0, 5));
		add(new Vec3i(-3, 0, 5));
		add(new Vec3i(-5, 0, 3));
		add(new Vec3i(-5, 0, -3));
		add(new Vec3i(-3, 0, -5));
		add(new Vec3i(3, 0, -5));
	}};
	
	public static final int REQUIRED_TICKS_FOR_EACH_EXPERIENCE_POINT = 4;
	public static final int INVENTORY_SIZE = 2; // 0: any itemstack, 1: Knowledge Gem
	
	protected UUID ownerUUID;
	protected boolean canOwnerApplyConflictingEnchantments;
	protected boolean canOwnerOverenchant;
	
	// since the item bowls around the enchanter hold some items themselves
	// they get cached here for faster recipe lookup
	// virtualInventoryRecipeOrientation is the order the items are ordered for the recipe to match (rotations from 0-3)
	protected EnchanterInventory virtualInventory;
	protected int virtualInventoryRecipeOrientation;
	protected boolean virtualInventoryRecipeMirrored;
	
	protected boolean inventoryChanged;
	private UpgradeHolder upgrades;
	
	private @Nullable RecipeHolder<?> currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	private int currentItemProcessingTime;
	
	@Nullable
	private Direction itemFacing; // for rendering the item on the enchanter only
	
	public EnchanterBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.ENCHANTER.get(), pos, state, INVENTORY_SIZE);
		this.virtualInventory = new EnchanterInventory();
		this.currentItemProcessingTime = -1;
	}
	
	@Override
	public List<Vec3i> getUpgradePosOffsets() {
		return EnchanterBlock.UPGRADE_BLOCK_OFFSETS;
	}
	
	@SuppressWarnings("unused")
	public static void clientTick(Level world, BlockPos blockPos, BlockState blockState, EnchanterBlockEntity enchanterBlockEntity) {
		if (enchanterBlockEntity.currentRecipe != null) {
			ItemStack experienceStack = enchanterBlockEntity.getItem(1);
			if (!experienceStack.isEmpty() && experienceStack.getItem() instanceof ExperienceStorageItem) {
				int experience = ExperienceStorageItem.getStoredExperience(experienceStack);
				int amount = ExperienceHelper.getExperienceOrbSizeForExperience(experience);
				
				if (world.getRandom().nextInt(10) < amount) {
					float randomX = 0.2F + world.getRandom().nextFloat() * 0.6F;
					float randomZ = 0.2F + world.getRandom().nextFloat() * 0.6F;
					float randomY = -0.1F + world.getRandom().nextFloat() * 0.4F;
					world.addParticle(ColoredCraftingParticleEffect.LIME, blockPos.getX() + randomX, blockPos.getY() + 2.5 + randomY, blockPos.getZ() + randomZ, 0.0D, -0.1D, 0.0D);
				}
			}
		} else if (enchanterBlockEntity.currentItemProcessingTime > -1) {
			float randomX = 0.2F + world.getRandom().nextFloat() * 0.6F;
			float randomZ = 0.2F + world.getRandom().nextFloat() * 0.6F;
			float randomY = -0.2F + world.getRandom().nextFloat() * 0.4F;
			world.addParticle(ColoredCraftingParticleEffect.LIME, blockPos.getX() + randomX, blockPos.getY() + 2.5 + randomY, blockPos.getZ() + randomZ, 0.0D, -0.1D, 0.0D);
			
			if (world.getGameTime() % 12 == 0) {
				world.playSound(null, enchanterBlockEntity.worldPosition, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, (float) (0.8D * SpectrumConfig.CONFIG.BlockSoundVolume.get()), 0.8F + world.getRandom().nextFloat() * 0.4F);
				enchanterBlockEntity.doItemBowlOrbs(world);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static void serverTick(Level world, BlockPos blockPos, BlockState blockState, EnchanterBlockEntity enchanterBlockEntity) {
		if (enchanterBlockEntity.upgrades == null) {
			enchanterBlockEntity.calculateUpgrades();
		}
		
		if (enchanterBlockEntity.inventoryChanged) {
			calculateCurrentRecipe(world, enchanterBlockEntity);
			
			// if no default recipe found => check in-code recipe for enchanting the center item with enchanted books
			if (enchanterBlockEntity.currentRecipe == null) {
				if (isValidCenterEnchantingSetup(enchanterBlockEntity)) {
					int requiredExperience = getRequiredExperienceToEnchantCenterItem(enchanterBlockEntity);
					if (requiredExperience > 0) {
						enchanterBlockEntity.currentItemProcessingTime = requiredExperience * REQUIRED_TICKS_FOR_EACH_EXPERIENCE_POINT;
					} else {
						enchanterBlockEntity.currentItemProcessingTime = -1;
					}
				} else {
					enchanterBlockEntity.currentItemProcessingTime = -1;
				}
				enchanterBlockEntity.updateInClientWorld();
			}
			
			enchanterBlockEntity.inventoryChanged = false;
		}
		
		boolean craftingSuccess = false;
		
		if (enchanterBlockEntity.currentRecipe != null || enchanterBlockEntity.currentItemProcessingTime > 1) {
			if (enchanterBlockEntity.craftingTime % 60 == 1) {
				if (!checkRecipeRequirements(world, blockPos, enchanterBlockEntity)) {
					enchanterBlockEntity.craftingTime = 0;
					PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerLevel) enchanterBlockEntity.getLevel(), enchanterBlockEntity.worldPosition);
					return;
				}
			}
			if (enchanterBlockEntity.craftingTime == 1) {
				PlayBlockBoundSoundInstancePayload.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.ENCHANTER_WORKING, (ServerLevel) enchanterBlockEntity.getLevel(), enchanterBlockEntity.worldPosition, Integer.MAX_VALUE);
			}
			
			var recipe = enchanterBlockEntity.currentRecipe == null ? null : enchanterBlockEntity.currentRecipe.value();
			if (recipe instanceof EnchanterRecipe enchanterRecipe) {
				enchanterBlockEntity.craftingTime++;
				
				// looks cooler this way
				if (enchanterBlockEntity.craftingTime == enchanterBlockEntity.craftingTimeTotal - 20) {
					enchanterBlockEntity.doItemBowlOrbs(world);
				} else if (enchanterBlockEntity.craftingTime == enchanterBlockEntity.craftingTimeTotal) {
					playCraftingFinishedEffects(enchanterBlockEntity);
					craftEnchanterRecipe(world, enchanterBlockEntity, enchanterRecipe);
					craftingSuccess = true;
				}
				enchanterBlockEntity.setChanged();
			} else if (recipe instanceof EnchantmentUpgradeRecipe enchantmentUpgradeRecipe) {
				enchanterBlockEntity.currentItemProcessingTime++;
				if (enchanterBlockEntity.currentItemProcessingTime == REQUIRED_TICKS_FOR_EACH_EXPERIENCE_POINT) {
					enchanterBlockEntity.currentItemProcessingTime = 0;
					
					int consumedItems = tickEnchantmentUpgradeRecipe(world, enchanterBlockEntity, enchanterBlockEntity.craftingTimeTotal - enchanterBlockEntity.craftingTime);
					if (consumedItems == 0) {
						enchanterBlockEntity.inventoryChanged();
					} else {
						enchanterBlockEntity.craftingTime += consumedItems;
						if (enchanterBlockEntity.craftingTime >= enchanterBlockEntity.craftingTimeTotal) {
							playCraftingFinishedEffects(enchanterBlockEntity);
							enchanterBlockEntity.craftEnchantmentUpgradeRecipe(world, enchantmentUpgradeRecipe);
							PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerLevel) enchanterBlockEntity.getLevel(), enchanterBlockEntity.worldPosition);
							
							craftingSuccess = true;
						}
					}
				}
				enchanterBlockEntity.setChanged();
			} else if (enchanterBlockEntity.currentItemProcessingTime > -1) {
				int speedTicks = Support.getIntFromDecimalWithChance(enchanterBlockEntity.upgrades.getEffectiveValue(UpgradeType.SPEED), world.getRandom());
				enchanterBlockEntity.craftingTime += speedTicks;
				if (world.getGameTime() % REQUIRED_TICKS_FOR_EACH_EXPERIENCE_POINT == 0) {
					// in-code recipe for item + books => enchanted item
					boolean drained = enchanterBlockEntity.drainExperience(speedTicks);
					if (!drained) {
						enchanterBlockEntity.currentItemProcessingTime = -1;
						enchanterBlockEntity.updateInClientWorld();
						PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerLevel) enchanterBlockEntity.getLevel(), enchanterBlockEntity.worldPosition);
						
					}
				}
				if (enchanterBlockEntity.currentItemProcessingTime > 0 && enchanterBlockEntity.craftingTime >= enchanterBlockEntity.currentItemProcessingTime) {
					playCraftingFinishedEffects(enchanterBlockEntity);
					enchantCenterItem(enchanterBlockEntity);
					
					enchanterBlockEntity.currentItemProcessingTime = -1;
					enchanterBlockEntity.craftingTime = 0;
					enchanterBlockEntity.updateInClientWorld();
					PlayBlockBoundSoundInstancePayload.sendCancelBlockBoundSoundInstance((ServerLevel) enchanterBlockEntity.getLevel(), enchanterBlockEntity.worldPosition);
					
					craftingSuccess = true;
				}
				enchanterBlockEntity.setChanged();
			}
			
			if (craftingSuccess) {
				enchanterBlockEntity.currentItemProcessingTime = -1;
				enchanterBlockEntity.craftingTime = 0;
				enchanterBlockEntity.inventoryChanged();
			}
		}
	}
	
	/**
	 * For an enchanting setup to be valid there has to be an enchantable stack in the center, an ExperienceStorageItem
	 * and Enchanted Books in the Item Bowls
	 *
	 * @param enchanterBlockEntity The Enchanter to check
	 * @return True if the enchanters inventory matches an enchanting setup
	 */
	public static boolean isValidCenterEnchantingSetup(EnchanterBlockEntity enchanterBlockEntity) {
		ItemStack centerStack = enchanterBlockEntity.virtualInventory.getItem(0);
		boolean isEnchantableBookInCenter = SpectrumEnchantmentHelper.isEnchantableBook(centerStack);
		
		var centerIsEnchantable = (isEnchantableBookInCenter || centerStack.getItem().isEnchantable(centerStack));
		var hasExpStorage = enchanterBlockEntity.virtualInventory.getItem(1).getItem() instanceof ExperienceStorageItem;
		
		if (!centerStack.isEmpty() && centerIsEnchantable && hasExpStorage) {
			// gilded books can copy enchantments from any source item
			boolean centerStackIsGildedBook = centerStack.is(SpectrumItems.GILDED_BOOK);
			boolean enchantedBookWithAdditionalEnchantmentsFound = false;
			
			var existingEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(centerStack).entrySet();
			for (int i = 0; i < 8; i++) {
				ItemStack virtualSlotStack = enchanterBlockEntity.virtualInventory.getItem(2 + i);
				
				// empty slots do not count
				if (!virtualSlotStack.isEmpty()) {
					if (centerStackIsGildedBook || virtualSlotStack.getItem() instanceof EnchantedBookItem) {
						for (var entry : EnchantmentHelper.getEnchantmentsForCrafting(virtualSlotStack).entrySet()) {
							var enchantment = entry.getKey();
							var isAcceptable = isEnchantableBookInCenter || centerStack.supportsEnchantment(enchantment);
							var isRedundant = existingEnchantments.stream().anyMatch(existing -> existing.getKey() == enchantment && existing.getIntValue() >= entry.getIntValue());
							if (isAcceptable && !isRedundant) {
								if (enchanterBlockEntity.canOwnerApplyConflictingEnchantments) {
									enchantedBookWithAdditionalEnchantmentsFound = true;
									break;
								} else if (SpectrumEnchantmentHelper.canCombineAny(centerStack, virtualSlotStack)) {
									enchantedBookWithAdditionalEnchantmentsFound = true;
									break;
								}
							}
						}
					} else {
						return false;
					}
				}
			}
			
			return enchantedBookWithAdditionalEnchantmentsFound;
		}
		
		return false;
	}
	
	public static void playCraftingFinishedEffects(EnchanterBlockEntity enchanterBlockEntity) {
		if (enchanterBlockEntity.level == null) {
			return;
		}
		enchanterBlockEntity.level.playSound(null, enchanterBlockEntity.worldPosition, SpectrumSoundEvents.ENCHANTER_FINISH, SoundSource.BLOCKS, 1.0F, 1.0F);
		
		PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) enchanterBlockEntity.getLevel(),
				new Vec3(enchanterBlockEntity.worldPosition.getX() + 0.5D, enchanterBlockEntity.worldPosition.getY() + 0.5, enchanterBlockEntity.worldPosition.getZ() + 0.5D),
				ColoredSparkleRisingParticleEffect.LIME, 75, new Vec3(0.5D, 0.5D, 0.5D),
				new Vec3(0.1D, -0.1D, 0.1D));
	}
	
	private static boolean checkRecipeRequirements(Level world, BlockPos blockPos, EnchanterBlockEntity enchanter) {
		Player lastInteractedPlayer = enchanter.getOwnerIfOnline(world);
		
		if (lastInteractedPlayer == null) {
			return false;
		}
		if (enchanter.currentRecipe == null) {
			// Hard-coded center enchanting recipe has no serialized recipe, so currentRecipe == null
			// does not mean the enchanter has no valid recipe.
			return isValidCenterEnchantingSetup(enchanter);
		}
		
		var recipe = enchanter.currentRecipe.value();
		
		boolean playerCanCraft = true;
		if (recipe instanceof EnchanterRecipe enchanterRecipe) {
			playerCanCraft = enchanterRecipe.canPlayerCraft(lastInteractedPlayer);
		} else if (recipe instanceof EnchantmentUpgradeRecipe upgrade) {
			var enchLevel = getLevel(enchanter, upgrade);
			
			playerCanCraft = upgrade.canPlayerCraft(lastInteractedPlayer)
					&& (enchanter.canOwnerOverenchant || upgrade.isInNormalRange(enchLevel));
		}
		boolean structureComplete = EnchanterBlock.verifyStructure(world, blockPos, null);
		
		if (!playerCanCraft || !structureComplete) {
			if (!structureComplete) {
				world.playSound(null, enchanter.getBlockPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundSource.BLOCKS, 0.9F + world.getRandom().nextFloat() * 0.2F, 0.9F + world.getRandom().nextFloat() * 0.2F);
				world.playSound(null, enchanter.getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.9F + world.getRandom().nextFloat() * 0.2F, 0.5F + world.getRandom().nextFloat() * 0.2F);
				EnchanterBlock.scatterContents(world, blockPos);
			}
			return false;
		}
		return true;
	}
	
	private static int getLevel(EnchanterBlockEntity enchanter, EnchantmentUpgradeRecipe upgrade) {
		return enchanter.getItem(0).get(DataComponents.STORED_ENCHANTMENTS).getLevel(upgrade.getEnchantment());
	}
	
	public static void enchantCenterItem(EnchanterBlockEntity enchanterBlockEntity) {
		ItemStack centerStack = enchanterBlockEntity.getItem(0);
		ItemStack centerStackCopy = centerStack.copy();
		var highestEnchantments = getHighestEnchantmentsInItemBowls(enchanterBlockEntity);
		
		for (var entry : highestEnchantments.entrySet()) {
			centerStackCopy = SpectrumEnchantmentHelper.addOrUpgradeEnchantment(centerStackCopy, entry.getKey(), entry.getIntValue(), false, enchanterBlockEntity.canOwnerApplyConflictingEnchantments).getB();
		}
		
		int spentExperience = enchanterBlockEntity.currentItemProcessingTime / EnchanterBlockEntity.REQUIRED_TICKS_FOR_EACH_EXPERIENCE_POINT;
		if (centerStack.getCount() > 1) {
			centerStackCopy.setCount(1);
			MultiblockCrafter.spawnOutputAsItemEntity(enchanterBlockEntity.getLevel(), enchanterBlockEntity.worldPosition, centerStackCopy);
			centerStack.shrink(1);
		} else {
			enchanterBlockEntity.setItem(0, centerStackCopy);
		}
		
		// vanilla
		grantPlayerEnchantingAdvancementCriterion(enchanterBlockEntity.getLevel(), enchanterBlockEntity.ownerUUID, centerStackCopy, spentExperience);
		
		// enchanter enchanting criterion
		ServerPlayer serverPlayerEntity = (ServerPlayer) enchanterBlockEntity.getOwnerIfOnline(enchanterBlockEntity.getLevel());
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.ENCHANTER_ENCHANTING.trigger(serverPlayerEntity, centerStackCopy, spentExperience);
		}
	}
	
	public static ItemEnchantments getHighestEnchantmentsInItemBowls(EnchanterBlockEntity enchanterBlockEntity) {
		return SpectrumEnchantmentHelper.collectHighestEnchantments(
				enchanterBlockEntity.virtualInventory.getItems().subList(2, 10));
	}
	
	public static int getRequiredExperienceToEnchantCenterItem(EnchanterBlockEntity enchanterBlockEntity) {
		boolean valid = false;
		ItemStack centerStack = enchanterBlockEntity.getItem(0);
		if (!centerStack.isEmpty() && (centerStack.getItem().isEnchantable(centerStack) || SpectrumEnchantmentHelper.isEnchantableBook(centerStack))) {
			ItemStack centerStackCopy = centerStack.copy();
			var highestEnchantments = getHighestEnchantmentsInItemBowls(enchanterBlockEntity);
			int requiredExperience = 0;
			for (var entry : highestEnchantments.entrySet()) {
				var enchantment = entry.getKey();
				int level = entry.getIntValue();
				int currentRequired = getRequiredExperienceToEnchantWithEnchantment(centerStackCopy, enchantment, level, enchanterBlockEntity.canOwnerApplyConflictingEnchantments);
				if (currentRequired > 0) {
					centerStackCopy = SpectrumEnchantmentHelper.addOrUpgradeEnchantment(centerStackCopy, enchantment, level, false, enchanterBlockEntity.canOwnerApplyConflictingEnchantments).getB();
					requiredExperience += currentRequired;
					valid = true;
				} else {
					requiredExperience += 50; // conflicting enchantments (like more enchantments in a book where not all can be applied amount extra
				}
			}
			if (valid) { // and applicable enchantment found
				return requiredExperience;
			} else {
				return -1; // all enchantments already applied
			}
		}
		return -1;
	}
	
	/**
	 * Returns the experience required to enchant the given stack with the enchantment at that level
	 * Returns -1 if the enchantment is not valid for that stack or the item can not be enchanted
	 *
	 * @param stack       The item stack to enchant
	 * @param enchantment The enchantment
	 * @param level       The enchantments level
	 * @return The required experience to enchant. -1 if the enchantment is not applicable
	 */
	public static int getRequiredExperienceToEnchantWithEnchantment(ItemStack stack, Holder<Enchantment> enchantment, int level, boolean allowEnchantmentConflicts) {
		if (!stack.supportsEnchantment(enchantment) && !SpectrumEnchantmentHelper.isEnchantableBook(stack)) {
			return -1;
		}
		
		int existingLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
		if (existingLevel >= level) {
			return -1;
		}
		
		boolean conflicts = !EnchantmentHelper.isEnchantmentCompatible(stack.getEnchantments().keySet(), enchantment);
		if (conflicts && !allowEnchantmentConflicts) {
			return -1;
		}
		
		int requiredExperience = getEnchantingPrice(stack, enchantment, level);
		if (conflicts) {
			requiredExperience *= 4;
		}
		return requiredExperience;
	}
	
	public static Integer getEnchantingPrice(ItemStack stack, Holder<Enchantment> enchantment, int level) {
		int enchantability = Math.max(1, stack.getItem().getEnchantmentValue()); // items like Elytras have an enchantability of 0, but can get unbreaking
		if (stack.supportsEnchantment(enchantment) || SpectrumEnchantmentHelper.isEnchantableBook(stack) || stack.is(Items.ENCHANTED_BOOK)) {
			return getRequiredExperienceForEnchantment(enchantability, enchantment, level);
		}
		return -1;
	}
	
	public static int getRequiredExperienceForEnchantment(int enchantability, Holder<Enchantment> entry, int level) {
		if (enchantability > 0) {
			var enchantment = entry.value();
			
			// Interpolated version of COMMON -> 10, UNCOMMON -> 25, RARE -> 50, VERY_RARE -> 80
			var rarityMults = new float[]{0, 10, 12.5F, 12.67F, 12.5F, 12, 11.33F, 10.71F, 10};
			var anvilCost = enchantment.getAnvilCost();
			var rarityCost = rarityMults[Math.min(anvilCost, rarityMults.length - 1)] * anvilCost;
			
			float levelCost = level * Math.min(1, (float) level / enchantment.getMaxLevel()); // the higher the level, the pricier. But not as bad for enchantments with high max levels
			float specialMulti = entry.is(EnchantmentTags.TREASURE) ? 2.0F : entry.is(EnchantmentTags.CURSE) ? 1.5F : 1.0F;
			float selectionAvailabilityMod = (entry.is(EnchantmentTags.IN_ENCHANTING_TABLE) ? 0.5F : 0.75F) + (entry.is(EnchantmentTags.TRADEABLE) ? 0.5F : 0.75F);
			float enchantabilityMod = 16.0F / (2 + enchantability);
			return (int) Math.floor(rarityCost * levelCost * specialMulti * selectionAvailabilityMod * enchantabilityMod);
		}
		return -1;
	}
	
	public static int getExperienceWithMod(int experience, double mod, RandomSource random) {
		double modNormalized = 1.0 / (1.0 + Math.log10(mod));
		return Support.getIntFromDecimalWithChance(experience * modNormalized, random);
	}
	
	public static void craftEnchanterRecipe(Level world, EnchanterBlockEntity enchanterBlockEntity, EnchanterRecipe enchanterRecipe) {
		enchanterBlockEntity.drainExperience(enchanterRecipe.getRequiredExperience());
		
		// if there is room: place the output on the table
		// otherwise: pop it off
		ItemStack resultStack = enchanterRecipe.assemble(enchanterBlockEntity.virtualInventory.createInput(), world.registryAccess());
		ItemStack existingCenterStack = enchanterBlockEntity.getItem(0);
		
		// decrement stacks in item bowls
		double efficiencyModifier = 1.0 / enchanterBlockEntity.upgrades.getEffectiveValue(UpgradeType.EFFICIENCY);
		for (int i = 0; i < 8; i++) {
			int resultAmountAfterEfficiencyMod = 1;
			if (!enchanterRecipe.areYieldAndEfficiencyUpgradesDisabled() && efficiencyModifier != 1.0) {
				resultAmountAfterEfficiencyMod = Support.getIntFromDecimalWithChance(efficiencyModifier, world.getRandom());
			}
			
			if (resultAmountAfterEfficiencyMod > 0) {
				// since this recipe uses 1 item in each slot we can just iterate them all and decrement with 1
				BlockPos itemBowlPos = enchanterBlockEntity.worldPosition.offset(getItemBowlPositionOffset(i, enchanterBlockEntity.virtualInventoryRecipeOrientation, enchanterBlockEntity.virtualInventoryRecipeMirrored));
				BlockEntity blockEntity = world.getBlockEntity(itemBowlPos);
				if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
					itemBowlBlockEntity.decrementBowlStack(new Vec3(enchanterBlockEntity.worldPosition.getX(), enchanterBlockEntity.worldPosition.getY() + 1, enchanterBlockEntity.worldPosition.getX() + 0.5), resultAmountAfterEfficiencyMod, false);
					itemBowlBlockEntity.updateInClientWorld();
				}
			}
		}
		
		if (!enchanterRecipe.areYieldAndEfficiencyUpgradesDisabled() && enchanterBlockEntity.upgrades.getEffectiveValue(UpgradeType.YIELD) != 1.0) {
			int resultCountMod = Support.getIntFromDecimalWithChance(resultStack.getCount() * enchanterBlockEntity.upgrades.getEffectiveValue(UpgradeType.YIELD), world.getRandom());
			resultStack.setCount(resultCountMod);
		}
		
		boolean decrementCenterStack = enchanterRecipe.copyComponents() || Support.getIntFromDecimalWithChance(efficiencyModifier, world.getRandom()) == 1;
		if(decrementCenterStack) {
			existingCenterStack.shrink(1);
		}
		if(existingCenterStack.isEmpty()) {
			enchanterBlockEntity.setItem(0, resultStack);
		} else {
			MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, enchanterBlockEntity.worldPosition, resultStack, resultStack.getCount(), MultiblockCrafter.RECIPE_STACK_VELOCITY);
		}
		
		// vanilla
		grantPlayerEnchantingAdvancementCriterion(enchanterBlockEntity.getLevel(), enchanterBlockEntity.ownerUUID, resultStack, enchanterRecipe.getRequiredExperience());
		
		// enchanter crafting criterion
		ServerPlayer serverPlayerEntity = (ServerPlayer) enchanterBlockEntity.getOwnerIfOnline(world);
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.ENCHANTER_CRAFTING.trigger(serverPlayerEntity, resultStack, enchanterRecipe.getRequiredExperience());
		}
	}
	
	public static int tickEnchantmentUpgradeRecipe(Level world, EnchanterBlockEntity enchanterBlockEntity, int itemsToConsumeLeft) {
		int itemCountToConsume = Math.min(itemsToConsumeLeft, Support.getIntFromDecimalWithChance(enchanterBlockEntity.upgrades.getEffectiveValue(UpgradeType.SPEED), world.getRandom()));
		
		int consumedAmount = 0;
		int bowlsChecked = 0;
		int randomBowlPosition = world.getRandom().nextInt(8);
		
		int itemCountToConsumeAfterMod = itemCountToConsume;
		if (enchanterBlockEntity.upgrades.getEffectiveValue(UpgradeType.EFFICIENCY) != 1.0) {
			itemCountToConsumeAfterMod = Support.getIntFromDecimalWithChance(itemCountToConsume / enchanterBlockEntity.upgrades.getEffectiveValue(UpgradeType.EFFICIENCY), world.getRandom());
		}
		
		// cycle at least once for fancy particles
		while ((consumedAmount < itemCountToConsumeAfterMod && bowlsChecked < 8) || (itemCountToConsumeAfterMod == 0 & consumedAmount == 0)) {
			Vec3i bowlOffset = getItemBowlPositionOffset(randomBowlPosition + bowlsChecked, enchanterBlockEntity.virtualInventoryRecipeOrientation, enchanterBlockEntity.virtualInventoryRecipeMirrored);
			
			BlockEntity blockEntity = world.getBlockEntity(enchanterBlockEntity.worldPosition.offset(bowlOffset));
			if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				if (itemCountToConsumeAfterMod == 0) {
					itemBowlBlockEntity.spawnOrbParticles(new Vec3(enchanterBlockEntity.worldPosition.getX() + 0.5, enchanterBlockEntity.worldPosition.getY() + 1.0, enchanterBlockEntity.worldPosition.getZ() + 0.5));
					consumedAmount += itemCountToConsume;
				} else {
					int decrementedAmount = itemBowlBlockEntity.decrementBowlStack(new Vec3(enchanterBlockEntity.worldPosition.getX() + 0.5, enchanterBlockEntity.worldPosition.getY() + 1.0, enchanterBlockEntity.worldPosition.getZ() + 0.5), itemCountToConsumeAfterMod, true);
					consumedAmount += decrementedAmount;
				}
			}
			bowlsChecked++;
		}
		
		return consumedAmount;
	}
	
	public void craftEnchantmentUpgradeRecipe(Level world, EnchantmentUpgradeRecipe upgrade) {
		ItemStack resultStack = getItem(0);
		
		var curLevel = resultStack.get(DataComponents.STORED_ENCHANTMENTS).getLevel(upgrade.getEnchantment());
		var targetLevel = Math.min(curLevel + 1, upgrade.getLevelCap());
		var xpCost = upgrade.getRequiredXPForSourceLevel(curLevel);
		drainExperience(xpCost);
		
		
		resultStack = SpectrumEnchantmentHelper.addOrUpgradeEnchantment(resultStack, upgrade.getEnchantment(), targetLevel, false, true).getB();
		setItem(0, resultStack);
		
		// vanilla
		grantPlayerEnchantingAdvancementCriterion(world, ownerUUID, resultStack, xpCost);
		
		// enchantment upgrading criterion
		ServerPlayer serverPlayerEntity = (ServerPlayer) getOwnerIfOnline(world);
		if (serverPlayerEntity != null) {
			var builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
			builder.upgrade(upgrade.getEnchantment(), targetLevel);
			SpectrumAdvancementCriteria.ENCHANTER_UPGRADING.trigger(serverPlayerEntity, builder.toImmutable(), xpCost);
		}
		
		// update the item amount if chain upgrading
		if (recipeMatches(this, level)) {
			craftingTimeTotal = upgrade.getRequiredItemCountForSourceLevel(targetLevel);
		} else {
			currentRecipe = null;
		}
	}
	
	public static Vec3i getItemBowlPositionOffset(int index, int orientation, boolean mirrored) {
		int diff = mirrored ? orientation % 2 == 0 ? 1 : -1 : 0;
		int offset = (orientation * 2 + index + diff) % 8;
		return ITEM_BOWL_OFFSETS.get(offset);
	}
	
	private static boolean recipeMatches(EnchanterBlockEntity blockEntity, Level world) {
		if (blockEntity.currentRecipe == null) {
			return isValidCenterEnchantingSetup(blockEntity);
		}
		if (blockEntity.currentRecipe.value() instanceof EnchanterRecipe recipe) {
			return recipe.matches(blockEntity.virtualInventory.createInput(), world);
		} else if (blockEntity.currentRecipe.value() instanceof EnchantmentUpgradeRecipe recipe) {
			return recipe.matches(blockEntity.virtualInventory.createInput(), world);
		}
		return false;
	}
	
	/**
	 * Calculates and sets a new recipe for the enchanter based on it's inventory
	 *
	 * @param world     The Enchanter World
	 * @param enchanter The Enchanter Block Entity
	 */
	private static void calculateCurrentRecipe(Level world, EnchanterBlockEntity enchanter) {
		if (recipeMatches(enchanter, world)) {
			return;
		}
		
		enchanter.craftingTime = 0;
		var previousRecipe = enchanter.currentRecipe;
		enchanter.currentRecipe = null;
		
		var recipeManager = world.getRecipeManager();
		var upgrade = recipeManager
				.getRecipeFor(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, enchanter.virtualInventory.createInput(), world)
				.orElse(null);
		
		if (upgrade != null) {
			if (enchanter.canOwnerOverenchant || upgrade.value().isInNormalRange(getLevel(enchanter, upgrade.value()))) {
				enchanter.currentRecipe = upgrade;
				enchanter.currentItemProcessingTime = 0;
				
				var level = enchanter.items.get(0).get(DataComponents.STORED_ENCHANTMENTS).getLevel(upgrade.value().getEnchantment());
				enchanter.craftingTimeTotal = upgrade.value().getRequiredItemCountForSourceLevel(level);
				
				EnchanterInventory testInventory = new EnchanterInventory();
				testInventory.setItem(0, enchanter.virtualInventory.getItem(0));
				testInventory.setItem(1, enchanter.virtualInventory.getItem(1));
				enchanter.virtualInventory = testInventory;
			}
			if (enchanter.currentRecipe != previousRecipe) {
				enchanter.updateInClientWorld();
			}
			return;
		}
		
		for (int m = 0; m < 2; m++) {
			for (int o = 0; o < 8; o++) {
				RecipeInput recipeInput = enchanter.virtualInventory.createInput();
				RecipeHolder<EnchanterRecipe> enchanterRecipe = recipeManager
						.getRecipeFor(SpectrumRecipeTypes.ENCHANTER, recipeInput, world)
						.orElse(null);
				
				if (enchanterRecipe != null) {
					enchanter.currentRecipe = enchanterRecipe;
					enchanter.virtualInventoryRecipeOrientation = o;
					enchanter.virtualInventoryRecipeMirrored = m > 0;
					enchanter.craftingTimeTotal = (int) Math.ceil(enchanterRecipe.value().getCraftingTime() / enchanter.upgrades.getEffectiveValue(Upgradeable.UpgradeType.SPEED));
					enchanter.updateInClientWorld();
					return;
				}
				
				enchanter.virtualInventory.rotate();
			}
			enchanter.virtualInventory.mirror();
		}
	}
	
	private static void grantPlayerEnchantingAdvancementCriterion(Level level, UUID playerUUID, ItemStack resultStack, int experience) {
		int levels = ExperienceHelper.getLevelForExperience(experience);
		ServerPlayer serverPlayerEntity = (ServerPlayer) PlayerOwned.getPlayerIfOnline(level, playerUUID);
		if (serverPlayerEntity != null) {
			serverPlayerEntity.awardStat(Stats.ENCHANT_ITEM);
			CriteriaTriggers.ENCHANTED_ITEM.trigger(serverPlayerEntity, resultStack, levels);
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.craftingTime = nbt.getInt("crafting_time");
		this.craftingTimeTotal = nbt.getInt("crafting_time_total");
		this.currentItemProcessingTime = nbt.getInt("current_item_processing_time");
		
		this.inventoryChanged = nbt.getBoolean("inventory_changed");
		this.canOwnerApplyConflictingEnchantments = nbt.getBoolean("owner_can_apply_conflicting_enchantments");
		this.canOwnerOverenchant = nbt.getBoolean("owner_can_overenchant");
		this.virtualInventoryRecipeOrientation = nbt.getInt("virtual_recipe_orientation");
		this.virtualInventoryRecipeMirrored = nbt.getBoolean("virtual_recipe_mirrored");
		this.virtualInventory = new EnchanterInventory();
		ContainerHelper.loadAllItems(nbt, this.virtualInventory.getItems(), registryLookup);
		if (nbt.contains("item_facing", Tag.TAG_STRING)) {
			this.itemFacing = Direction.valueOf(nbt.getString("item_facing").toUpperCase(Locale.ROOT));
		}
		this.ownerUUID = PlayerOwnedWithName.readOwnerUUID(nbt);
		
		this.currentRecipe = null;
		this.currentRecipe = MultiblockCrafter.getRecipeHolderFromNbt(level, nbt);
		if (this.currentRecipe == null && this.level != null && this.level.isClientSide()) {
			stopCraftingMusic();
		}
		
		if (nbt.contains("Upgrades", Tag.TAG_LIST)) {
			this.upgrades = UpgradeHolder.fromNbt(nbt.getList("Upgrades", Tag.TAG_COMPOUND));
		} else {
			this.upgrades = new UpgradeHolder();
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	protected void stopCraftingMusic() {
		CraftingBlockSoundInstance.stopPlayingOnPos(this.worldPosition);
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
 		super.saveAdditional(nbt, registryLookup);
		nbt.putInt("crafting_time", this.craftingTime);
		nbt.putInt("crafting_time_total", this.craftingTimeTotal);
		nbt.putInt("current_item_processing_time", this.currentItemProcessingTime);
		nbt.putInt("virtual_recipe_orientation", this.virtualInventoryRecipeOrientation);
		nbt.putBoolean("virtual_recipe_mirrored", this.virtualInventoryRecipeMirrored);
		nbt.putBoolean("inventory_changed", this.inventoryChanged);
		nbt.putBoolean("owner_can_apply_conflicting_enchantments", this.canOwnerApplyConflictingEnchantments);
		nbt.putBoolean("owner_can_overenchant", this.canOwnerOverenchant);
		ContainerHelper.saveAllItems(nbt, this.virtualInventory.getItems(), registryLookup);
		if (this.itemFacing != null) {
			nbt.putString("item_facing", this.itemFacing.toString().toUpperCase(Locale.ROOT));
		}
		if (this.upgrades != null) {
			nbt.put("Upgrades", this.upgrades.toNbt());
		}
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.id().toString());
		}
	}
	
	public Direction getItemFacingDirection() {
		// if placed via pipe or other sources
		return Objects.requireNonNullElse(this.itemFacing, Direction.NORTH);
	}
	
	public void setItemFacingDirection(Direction facingDirection) {
		this.itemFacing = facingDirection;
	}
	
	private void doItemBowlOrbs(Level world) {
		for (int i = 0; i < 8; i++) {
			BlockPos itemBowlPos = worldPosition.offset(getItemBowlPositionOffset(i, 0, false));
			BlockEntity blockEntity = world.getBlockEntity(itemBowlPos);
			if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				itemBowlBlockEntity.spawnOrbParticles(new Vec3(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1.0, this.worldPosition.getZ() + 0.5));
			}
		}
	}
	
	public boolean drainExperience(int amount) {
		ItemStack experienceProviderStack = getItem(1);
		if (level != null && experienceProviderStack.getItem() instanceof ExperienceStorageItem experienceStorageItem) {
			int currentStoredExperience = ExperienceStorageItem.getStoredExperience(experienceProviderStack);
			if (currentStoredExperience > 0) {
				int amountAfterExperienceMod = getExperienceWithMod(amount, this.upgrades.getEffectiveValue(UpgradeType.EXPERIENCE), level.getRandom());
				int drainedExperience = Math.min(currentStoredExperience, amountAfterExperienceMod);
				
				if (experienceStorageItem instanceof KnowledgeGemItem knowledgeGemItem) {
					if (knowledgeGemItem.changedDisplayTier(currentStoredExperience, currentStoredExperience - drainedExperience)) {
						// There was enough experience drained from the knowledge gem that the visual changed
						// To display the updated knowledge gem size clientside the inventory has to be synched
						// to the clients for rendering purposes
						PlayParticleWithPatternAndVelocityPayload.playParticleWithPatternAndVelocity(null, (ServerLevel) level, new Vec3(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 2.5, this.worldPosition.getZ() + 0.5), ColoredCraftingParticleEffect.LIME, VectorPattern.SIXTEEN, 0.05F);
						this.updateInClientWorld();
					}
				}
				
				this.setChanged();
				return ExperienceStorageItem.removeStoredExperience(experienceProviderStack, drainedExperience);
			}
		}
		return false;
	}
	
	@Override
	public void inventoryChanged() {
		if (level == null) return;
		virtualInventory = new EnchanterInventory(
				this.getItem(0), // center item
				this.getItem(1), // knowledge gem
				getItemBowlStack(level, worldPosition.offset(5, 0, -3)),
				getItemBowlStack(level, worldPosition.offset(5, 0, 3)),
				getItemBowlStack(level, worldPosition.offset(3, 0, 5)),
				getItemBowlStack(level, worldPosition.offset(-3, 0, 5)),
				getItemBowlStack(level, worldPosition.offset(-5, 0, 3)),
				getItemBowlStack(level, worldPosition.offset(-5, 0, -3)),
				getItemBowlStack(level, worldPosition.offset(-3, 0, -5)),
				getItemBowlStack(level, worldPosition.offset(3, 0, -5))
		);
		
		virtualInventory.setChanged();
		inventoryChanged = true;
		currentItemProcessingTime = -1;
		
		super.inventoryChanged();
	}
	
	public ItemStack getItemBowlStack(Level world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			return itemBowlBlockEntity.getItem(0);
		} else {
			return ItemStack.EMPTY;
		}
	}
	
	public void playSound(SoundEvent soundEvent, float volume) {
		if (level == null) return;
		level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), soundEvent, SoundSource.BLOCKS, volume, 0.9F + level.getRandom().nextFloat() * 0.15F);
	}
	
	// PLAYER OWNED
	// "owned" is not to be taken literally here. The owner
	// is always set to the last player interacted with to trigger advancements
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.canOwnerApplyConflictingEnchantments = AdvancementHelper.hasAdvancement(playerEntity, SpectrumAdvancements.APPLY_CONFLICTING_ENCHANTMENTS);
		this.canOwnerOverenchant = AdvancementHelper.hasAdvancement(playerEntity, SpectrumAdvancements.OVERENCHANTING);
		setChanged();
	}
	
	// UPGRADEABLE
	@Override
	public void resetUpgrades() {
		this.upgrades = null;
		this.setChanged();
	}
	
	@Override
	public void calculateUpgrades() {
		this.upgrades = Upgradeable.calculateUpgradeMods4(level, worldPosition, 3, 0, this.ownerUUID);
		this.setChanged();
	}
	
	@Override
	public UpgradeHolder getUpgradeHolder() {
		return this.upgrades;
	}
	
	@Override
	public int [] getSlotsForFace(Direction direction) {
		return direction == Direction.UP ? new int[]{1} : new int[]{0};
	}
	
	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
		return true;
	}
	
	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
}
