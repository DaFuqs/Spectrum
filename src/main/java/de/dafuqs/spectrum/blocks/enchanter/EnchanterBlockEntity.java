package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.item_bowl.ItemBowlBlockEntity;
import de.dafuqs.spectrum.blocks.upgrade.Upgradeable;
import de.dafuqs.spectrum.enchantments.SpectrumEnchantment;
import de.dafuqs.spectrum.helpers.ExperienceHelper;
import de.dafuqs.spectrum.helpers.SpectrumEnchantmentHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.items.magic_items.KnowledgeGemItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.ParticlePattern;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.enchanter.EnchanterRecipe;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.EnchantmentUpgradeRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BookItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnchanterBlockEntity extends BlockEntity implements PlayerOwned, Upgradeable {
	
	public static final List<Vec3i> itemBowlOffsets = new ArrayList<>() {{
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
	public static final Identifier APPLY_CONFLICTING_ENCHANTMENTS_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_conflicted_enchanting_with_enchanter");
	public static final Identifier OVERENCHANTING_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "milestones/unlock_overenchanting_with_enchanter");
	
	public static final int INVENTORY_SIZE = 2; // 0: any itemstack, 1: Knowledge Gem;
	
	protected UUID ownerUUID;
	protected SimpleInventory inventory;
	
	protected boolean canOwnerApplyConflictingEnchantments;
	protected boolean canOwnerOverenchant;
	
	// since the item bowls around the enchanter hold some items themselves
	// they get cached here for faster recipe lookup
	// virtualInventoryRecipeOrientation is the order the items are ordered for the recipe to match (rotations from 0-3)
	protected SimpleInventory virtualInventoryIncludingBowlStacks;
	protected int virtualInventoryRecipeOrientation;
	
	protected boolean inventoryChanged;
	private Map<Upgradeable.UpgradeType, Double> upgrades;
	
	private Recipe currentRecipe;
	private int craftingTime;
	private int craftingTimeTotal;
	private int currentItemProcessingTime;
	
	@Nullable
	private Direction itemFacing; // for rendering the item on the enchanter only
	
	public EnchanterBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntityRegistry.ENCHANTER, pos, state);
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.virtualInventoryIncludingBowlStacks = new SimpleInventory(INVENTORY_SIZE + 8);
		this.currentItemProcessingTime = -1;
	}
	
	public static void clientTick(World world, BlockPos blockPos, BlockState blockState, @NotNull EnchanterBlockEntity enchanterBlockEntity) {
		if (enchanterBlockEntity.currentRecipe != null) {
			ItemStack experienceStack = enchanterBlockEntity.getInventory().getStack(1);
			if (!experienceStack.isEmpty() && experienceStack.getItem() instanceof ExperienceStorageItem) {
				int experience = ExperienceStorageItem.getStoredExperience(experienceStack);
				int amount = ExperienceHelper.getExperienceOrbSizeForExperience(experience);
				
				if (world.random.nextInt(10) < amount) {
					float randomX = 0.2F + world.getRandom().nextFloat() * 0.6F;
					float randomZ = 0.2F + world.getRandom().nextFloat() * 0.6F;
					float randomY = -0.1F + world.getRandom().nextFloat() * 0.4F;
					world.addParticle(SpectrumParticleTypes.LIME_SPARKLE_RISING, blockPos.getX() + randomX, blockPos.getY() + 2.5 + randomY, blockPos.getZ() + randomZ, 0.0D, -0.1D, 0.0D);
				}
			}
		} else if (enchanterBlockEntity.currentItemProcessingTime > -1) {
			float randomX = 0.2F + world.getRandom().nextFloat() * 0.6F;
			float randomZ = 0.2F + world.getRandom().nextFloat() * 0.6F;
			float randomY = -0.2F + world.getRandom().nextFloat() * 0.4F;
			world.addParticle(SpectrumParticleTypes.LIME_SPARKLE_RISING, blockPos.getX() + randomX, blockPos.getY() + 2.5 + randomY, blockPos.getZ() + randomZ, 0.0D, -0.1D, 0.0D);
			
			if (world.getTime() % 12 == 0) {
				((ClientWorld) world).playSound(enchanterBlockEntity.pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F, true);
				enchanterBlockEntity.doItemBowlOrbs(world);
			}
		}
	}
	
	public static void serverTick(World world, BlockPos blockPos, BlockState blockState, @NotNull EnchanterBlockEntity enchanterBlockEntity) {
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
					SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) enchanterBlockEntity.world, enchanterBlockEntity.pos);
					return;
				}
			}
			if (enchanterBlockEntity.craftingTime == 1) {
				SpectrumS2CPacketSender.sendPlayBlockBoundSoundInstance(SpectrumSoundEvents.ENCHANTER_WORKING, (ServerWorld) enchanterBlockEntity.world, enchanterBlockEntity.pos, Integer.MAX_VALUE);
			}
			
			if (enchanterBlockEntity.currentRecipe instanceof EnchanterRecipe enchanterRecipe) {
				enchanterBlockEntity.craftingTime++;
				
				// looks cooler this way
				if (enchanterBlockEntity.craftingTime == enchanterBlockEntity.craftingTimeTotal - 20) {
					enchanterBlockEntity.doItemBowlOrbs(world);
				} else if (enchanterBlockEntity.craftingTime == enchanterBlockEntity.craftingTimeTotal) {
					playCraftingFinishedEffects(enchanterBlockEntity);
					craftEnchanterRecipe(world, enchanterBlockEntity, enchanterRecipe);
					craftingSuccess = true;
				}
				enchanterBlockEntity.markDirty();
			} else if (enchanterBlockEntity.currentRecipe instanceof EnchantmentUpgradeRecipe enchantmentUpgradeRecipe) {
				enchanterBlockEntity.currentItemProcessingTime++;
				if (enchanterBlockEntity.currentItemProcessingTime == REQUIRED_TICKS_FOR_EACH_EXPERIENCE_POINT) {
					enchanterBlockEntity.currentItemProcessingTime = 0;
					
					int consumedItems = tickEnchantmentUpgradeRecipe(world, enchanterBlockEntity, enchantmentUpgradeRecipe, enchanterBlockEntity.craftingTimeTotal - enchanterBlockEntity.craftingTime);
					enchanterBlockEntity.craftingTime += consumedItems;
					if (enchanterBlockEntity.craftingTime >= enchanterBlockEntity.craftingTimeTotal) {
						playCraftingFinishedEffects(enchanterBlockEntity);
						craftEnchantmentUpgradeRecipe(world, enchanterBlockEntity, enchantmentUpgradeRecipe);
						craftingSuccess = true;
					}
				}
				enchanterBlockEntity.markDirty();
			} else if (enchanterBlockEntity.currentItemProcessingTime > -1) {
				int speedTicks = Support.getIntFromDecimalWithChance(enchanterBlockEntity.upgrades.get(UpgradeType.SPEED), world.random);
				enchanterBlockEntity.craftingTime += speedTicks;
				if (world.getTime() % REQUIRED_TICKS_FOR_EACH_EXPERIENCE_POINT == 0) {
					// in-code recipe for item + books => enchanted item
					boolean drained = enchanterBlockEntity.drainExperience(speedTicks);
					if (!drained) {
						enchanterBlockEntity.currentItemProcessingTime = -1;
						enchanterBlockEntity.updateInClientWorld();
					}
				}
				if (enchanterBlockEntity.currentItemProcessingTime > 0 && enchanterBlockEntity.craftingTime >= enchanterBlockEntity.currentItemProcessingTime) {
					playCraftingFinishedEffects(enchanterBlockEntity);
					enchantCenterItem(enchanterBlockEntity);
					
					enchanterBlockEntity.currentItemProcessingTime = -1;
					enchanterBlockEntity.craftingTime = 0;
					enchanterBlockEntity.updateInClientWorld();
					
					craftingSuccess = true;
				}
				enchanterBlockEntity.markDirty();
			}
			
			if (craftingSuccess) {
				enchanterBlockEntity.currentItemProcessingTime = -1;
				enchanterBlockEntity.craftingTime = 0;
				enchanterBlockEntity.inventoryChanged();
			}
		} else {
			SpectrumS2CPacketSender.sendCancelBlockBoundSoundInstance((ServerWorld) enchanterBlockEntity.world, enchanterBlockEntity.pos);
		}
	}
	
	/**
	 * For an enchanting setup to be valid there has to be an enchantable stack in the center, an ExperienceStorageItem
	 * and Enchanted Books in the Item Bowls
	 *
	 * @param enchanterBlockEntity The Enchanter to check
	 * @return True if the enchanters inventory matches an enchanting setup
	 */
	public static boolean isValidCenterEnchantingSetup(@NotNull EnchanterBlockEntity enchanterBlockEntity) {
		ItemStack centerStack = enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack(0);
		boolean isEnchantableBookInCenter = SpectrumEnchantmentHelper.isEnchantableBook(centerStack);
		if (!centerStack.isEmpty() && (isEnchantableBookInCenter || centerStack.getItem().isEnchantable(centerStack) || centerStack.getItem() instanceof EnchanterEnchantable) && enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack(1).getItem() instanceof ExperienceStorageItem) {
			// gilded books can copy enchantments from any source item
			boolean centerStackIsGildedBook = centerStack.isOf(SpectrumItems.GILDED_BOOK);
			boolean enchantedBookWithAdditionalEnchantmentsFound = false;
			Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.get(centerStack);
			for (int i = 0; i < 8; i++) {
				ItemStack virtualSlotStack = enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack(2 + i);
				if (virtualSlotStack.isEmpty()) {
					// empty slots do not count
				} else if (centerStackIsGildedBook || virtualSlotStack.getItem() instanceof EnchantedBookItem) {
					Map<Enchantment, Integer> currentEnchantedBookEnchantments = EnchantmentHelper.get(virtualSlotStack);
					for (Enchantment enchantment : currentEnchantedBookEnchantments.keySet()) {
						if ((isEnchantableBookInCenter || (centerStack.getItem() instanceof EnchanterEnchantable enchanterEnchantable && enchanterEnchantable.canAcceptEnchantment(enchantment)) || enchantment.isAcceptableItem(centerStack)) && (!existingEnchantments.containsKey(enchantment) || existingEnchantments.get(enchantment) < currentEnchantedBookEnchantments.get(enchantment))) {
							if (enchanterBlockEntity.canOwnerApplyConflictingEnchantments) {
								enchantedBookWithAdditionalEnchantmentsFound = true;
								break;
							} else if (SpectrumEnchantmentHelper.canCombineAny(existingEnchantments, currentEnchantedBookEnchantments)) {
								enchantedBookWithAdditionalEnchantmentsFound = true;
								break;
							}
						}
					}
				} else {
					return false;
				}
			}
			return enchantedBookWithAdditionalEnchantmentsFound;
		}
		return false;
	}
	
	public static void playCraftingFinishedEffects(@NotNull EnchanterBlockEntity enchanterBlockEntity) {
		enchanterBlockEntity.world.playSound(null, enchanterBlockEntity.pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
		
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) enchanterBlockEntity.world,
				new Vec3d(enchanterBlockEntity.pos.getX() + 0.5D, enchanterBlockEntity.pos.getY() + 0.5, enchanterBlockEntity.pos.getZ() + 0.5D),
				SpectrumParticleTypes.LIME_SPARKLE_RISING, 75, new Vec3d(0.5D, 0.5D, 0.5D),
				new Vec3d(0.1D, -0.1D, 0.1D));
	}
	
	private static boolean checkRecipeRequirements(World world, BlockPos blockPos, @NotNull EnchanterBlockEntity enchanterBlockEntity) {
		PlayerEntity lastInteractedPlayer = PlayerOwned.getPlayerEntityIfOnline(world, enchanterBlockEntity.ownerUUID);
		
		if (lastInteractedPlayer == null) {
			return false;
		}
		
		boolean playerCanCraft = true;
		if (enchanterBlockEntity.currentRecipe instanceof EnchanterRecipe enchanterRecipe) {
			playerCanCraft = enchanterRecipe.canPlayerCraft(lastInteractedPlayer);
		} else if (enchanterBlockEntity.currentRecipe instanceof EnchantmentUpgradeRecipe enchantmentUpgradeRecipe) {
			playerCanCraft = enchantmentUpgradeRecipe.canPlayerCraft(lastInteractedPlayer) && (enchanterBlockEntity.canOwnerOverenchant || !enchantmentUpgradeRecipe.requiresUnlockedOverEnchanting());
		}
		boolean structureComplete = EnchanterBlock.verifyStructure(world, blockPos, null);
		
		if (!playerCanCraft || !structureComplete) {
			if (!structureComplete) {
				world.playSound(null, enchanterBlockEntity.getPos(), SpectrumSoundEvents.CRAFTING_ABORTED, SoundCategory.BLOCKS, 0.9F + enchanterBlockEntity.world.random.nextFloat() * 0.2F, 0.9F + enchanterBlockEntity.world.random.nextFloat() * 0.2F);
				world.playSound(null, enchanterBlockEntity.getPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.9F + enchanterBlockEntity.world.random.nextFloat() * 0.2F, 0.5F + enchanterBlockEntity.world.random.nextFloat() * 0.2F);
				EnchanterBlock.scatterContents(world, blockPos);
			}
			return false;
		}
		return true;
	}
	
	public static void enchantCenterItem(@NotNull EnchanterBlockEntity enchanterBlockEntity) {
		ItemStack centerStack = enchanterBlockEntity.getInventory().getStack(0);
		ItemStack centerStackCopy = enchanterBlockEntity.getInventory().getStack(0).copy();
		Map<Enchantment, Integer> highestEnchantments = getHighestEnchantmentsInItemBowls(enchanterBlockEntity);
		
		for (Enchantment enchantment : highestEnchantments.keySet()) {
			centerStackCopy = SpectrumEnchantmentHelper.addOrExchangeEnchantment(centerStackCopy, enchantment, highestEnchantments.get(enchantment), false, enchanterBlockEntity.canOwnerApplyConflictingEnchantments);
		}
		
		if (centerStack.getCount() > 1) {
			centerStackCopy.setCount(1);
			spawnOutputAsItemEntity(enchanterBlockEntity.world, enchanterBlockEntity, centerStackCopy);
			centerStack.decrement(1);
		} else {
			enchanterBlockEntity.getInventory().setStack(0, centerStackCopy);
		}
		
		// vanilla
		int spentExperience = enchanterBlockEntity.currentItemProcessingTime / EnchanterBlockEntity.REQUIRED_TICKS_FOR_EACH_EXPERIENCE_POINT;
		grantPlayerEnchantingAdvancementCriterion(enchanterBlockEntity.world, enchanterBlockEntity.ownerUUID, centerStackCopy, spentExperience);
		
		// enchanter enchanting criterion
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(enchanterBlockEntity.world, enchanterBlockEntity.ownerUUID);
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.ENCHANTER_ENCHANTING.trigger(serverPlayerEntity, centerStackCopy, spentExperience);
		}
	}
	
	public static Map<Enchantment, Integer> getHighestEnchantmentsInItemBowls(@NotNull EnchanterBlockEntity enchanterBlockEntity) {
		List<ItemStack> bowlStacks = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			bowlStacks.add(enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack(2 + i));
		}
		
		return SpectrumEnchantmentHelper.collectHighestEnchantments(bowlStacks);
	}
	
	public static int getRequiredExperienceToEnchantCenterItem(@NotNull EnchanterBlockEntity enchanterBlockEntity) {
		boolean valid = false;
		ItemStack centerStackCopy = enchanterBlockEntity.inventory.getStack(0).copy();
		if (!centerStackCopy.isEmpty() && (centerStackCopy.getItem().isEnchantable(centerStackCopy) || SpectrumEnchantmentHelper.isEnchantableBook(centerStackCopy) || centerStackCopy.getItem() instanceof EnchanterEnchantable)) {
			Map<Enchantment, Integer> highestEnchantmentLevels = getHighestEnchantmentsInItemBowls(enchanterBlockEntity);
			int requiredExperience = 0;
			for (Enchantment enchantment : highestEnchantmentLevels.keySet()) {
				
				int enchantmentLevel = highestEnchantmentLevels.get(enchantment);
				int currentRequired = getRequiredExperienceToEnchantWithEnchantment(centerStackCopy, enchantment, enchantmentLevel, enchanterBlockEntity.canOwnerApplyConflictingEnchantments);
				centerStackCopy = SpectrumEnchantmentHelper.addOrExchangeEnchantment(centerStackCopy, enchantment, enchantmentLevel, false, enchanterBlockEntity.canOwnerApplyConflictingEnchantments);
				if (currentRequired > 0) {
					requiredExperience += currentRequired;
					valid = true;
				} else {
					requiredExperience += 50; // conflicting enchantments (like more enchantments in a book where not all can be applied cost extra
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
	 * Returns the experience required to enchant the given itemStack with the enchantment at that level
	 * Returns -1 if the enchantment is not valid for that stack or the item can not be enchanted
	 *
	 * @param itemStack   The item stack to enchant
	 * @param enchantment The enchantment
	 * @param level       The enchantments level
	 * @return The required experience to enchant. -1 if the enchantment is not applicable
	 */
	public static int getRequiredExperienceToEnchantWithEnchantment(ItemStack itemStack, Enchantment enchantment, int level, boolean allowEnchantmentConflicts) {
		int existingLevel = EnchantmentHelper.getLevel(enchantment, itemStack);
		if (existingLevel >= level) {
			return -1;
		}
		
		boolean conflicts = SpectrumEnchantmentHelper.hasEnchantmentThatConflictsWith(itemStack, enchantment);
		if (conflicts && !allowEnchantmentConflicts) {
			return -1;
		}
		
		Integer requiredExperience = getEnchantingPrice(itemStack, enchantment, level);
		if (conflicts) {
			requiredExperience *= 4;
		}
		return requiredExperience;
	}
	
	public static Integer getEnchantingPrice(ItemStack itemStack, Enchantment enchantment, int level) {
		int enchantability = Math.max(1, itemStack.getItem().getEnchantability()); // items like Elytras have an enchantability of 0, but can get unbreaking
		if (enchantment.isAcceptableItem(itemStack) || itemStack.getItem() instanceof BookItem || (itemStack.getItem() instanceof EnchanterEnchantable enchanterEnchantable && (enchanterEnchantable.canAcceptEnchantment(enchantment)))) {
			return getRequiredExperienceForEnchantment(enchantability, enchantment, level);
		}
		return -1;
	}
	
	public static int getRequiredExperienceForEnchantment(int enchantability, Enchantment enchantment, int level) {
		if (enchantability > 0) {
			int rarityCost;
			Enchantment.Rarity rarity = enchantment.getRarity();
			switch (rarity) {
				case COMMON -> {
					rarityCost = 10;
				}
				case UNCOMMON -> {
					rarityCost = 25;
				}
				case RARE -> {
					rarityCost = 50;
				}
				default -> {
					rarityCost = 80;
				}
			}
			
			float levelCost = level + ((float) level / enchantment.getMaxLevel()); // the higher the level, the pricier. But not as bad for enchantments with high max levels
			float specialMulti = enchantment.isTreasure() ? 2.0F : enchantment.isCursed() ? 1.5F : 1.0F;
			float selectionAvailabilityMod = 1.0F;
			if (!(enchantment instanceof SpectrumEnchantment)) {
				selectionAvailabilityMod = (enchantment.isAvailableForRandomSelection() ? 0.5F : 0.75F) + (enchantment.isAvailableForEnchantedBookOffer() ? 0.5F : 0.75F);
			}
			float enchantabilityMod = (4.0F / (2 + enchantability)) * 4.0F;
			return (int) Math.floor(rarityCost * levelCost * specialMulti * selectionAvailabilityMod * enchantabilityMod);
		}
		return -1;
	}
	
	public static int getExperienceWithMod(int experience, double mod, Random random) {
		double modNormalized = 1.0 / (1.0 + Math.log10(mod));
		return Support.getIntFromDecimalWithChance(experience * modNormalized, random);
	}
	
	public static void craftEnchanterRecipe(World world, @NotNull EnchanterBlockEntity enchanterBlockEntity, @NotNull EnchanterRecipe enchanterRecipe) {
		enchanterBlockEntity.drainExperience(enchanterRecipe.getRequiredExperience());
		
		// decrement stacks in item bowls
		for (int i = 0; i < 8; i++) {
			int resultAmountAfterEfficiencyMod = 1;
			if (!enchanterRecipe.areYieldAndEfficiencyUpgradesDisabled() && enchanterBlockEntity.upgrades.get(UpgradeType.EFFICIENCY) != 1.0) {
				double efficiencyModifier = 1.0 / enchanterBlockEntity.upgrades.get(UpgradeType.EFFICIENCY);
				resultAmountAfterEfficiencyMod = Support.getIntFromDecimalWithChance(efficiencyModifier, enchanterBlockEntity.world.random);
			}
			
			if (resultAmountAfterEfficiencyMod > 0) {
				// since this recipe uses 1 item in each slot we can just iterate them all and decrement with 1
				BlockPos itemBowlPos = enchanterBlockEntity.pos.add(getItemBowlPositionOffset(i, enchanterBlockEntity.virtualInventoryRecipeOrientation));
				BlockEntity blockEntity = world.getBlockEntity(itemBowlPos);
				if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
					itemBowlBlockEntity.decrementBowlStack(enchanterBlockEntity.pos, resultAmountAfterEfficiencyMod, false);
					itemBowlBlockEntity.updateInClientWorld();
				}
			}
		}
		
		// if there is room: place the output on the table
		// otherwise: pop it off
		ItemStack resultStack = enchanterRecipe.getOutput().copy();
		ItemStack existingCenterStack = enchanterBlockEntity.getInventory().getStack(0);
		
		if (!enchanterRecipe.areYieldAndEfficiencyUpgradesDisabled() && enchanterBlockEntity.upgrades.get(UpgradeType.YIELD) != 1.0) {
			int resultCountMod = Support.getIntFromDecimalWithChance(resultStack.getCount() * enchanterBlockEntity.upgrades.get(UpgradeType.YIELD), world.random);
			resultStack.setCount(resultCountMod);
		}
		
		if (existingCenterStack.getCount() > 1) {
			existingCenterStack.decrement(1);
			spawnItemStackAsEntitySplitViaMaxCount(world, enchanterBlockEntity.pos, resultStack, resultStack.getCount());
		} else {
			enchanterBlockEntity.getInventory().setStack(0, resultStack);
		}
		
		// vanilla
		grantPlayerEnchantingAdvancementCriterion(world, enchanterBlockEntity.ownerUUID, resultStack, enchanterRecipe.getRequiredExperience());
		
		// enchanter crafting criterion
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(world, enchanterBlockEntity.ownerUUID);
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.ENCHANTER_CRAFTING.trigger(serverPlayerEntity, resultStack, enchanterRecipe.getRequiredExperience());
		}
	}
	
	public static void spawnItemStackAsEntitySplitViaMaxCount(World world, BlockPos blockPos, ItemStack itemStack, int amount) {
		while (amount > 0) {
			int currentAmount = Math.min(amount, itemStack.getMaxCount());
			
			ItemStack resultStack = itemStack.copy();
			resultStack.setCount(currentAmount);
			ItemEntity itemEntity = new ItemEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, resultStack);
			itemEntity.setVelocity(0, 0.3, 0);
			world.spawnEntity(itemEntity);
			
			amount -= currentAmount;
		}
	}
	
	public static void spawnOutputAsItemEntity(World world, @NotNull EnchanterBlockEntity enchanterBlockEntity, ItemStack outputItemStack) {
		ItemEntity itemEntity = new ItemEntity(world, enchanterBlockEntity.pos.getX() + 0.5, enchanterBlockEntity.pos.getY() + 1, enchanterBlockEntity.pos.getZ() + 0.5, outputItemStack);
		itemEntity.addVelocity(0, 0.1, 0);
		world.spawnEntity(itemEntity);
	}
	
	public static int tickEnchantmentUpgradeRecipe(World world, @NotNull EnchanterBlockEntity enchanterBlockEntity, @NotNull EnchantmentUpgradeRecipe enchantmentUpgradeRecipe, int itemsToConsumeLeft) {
		int itemCountToConsume = Math.min(itemsToConsumeLeft, Support.getIntFromDecimalWithChance(enchanterBlockEntity.upgrades.get(UpgradeType.SPEED), world.random));
		
		int consumedAmount = 0;
		int bowlsChecked = 0;
		int randomBowlPosition = world.random.nextInt(8);
		
		int itemCountToConsumeAfterMod = itemCountToConsume;
		if (enchanterBlockEntity.upgrades.get(UpgradeType.EFFICIENCY) != 1.0) {
			itemCountToConsumeAfterMod = Support.getIntFromDecimalWithChance(itemCountToConsume / enchanterBlockEntity.upgrades.get(UpgradeType.EFFICIENCY), world.random);
		}
		
		// cycle at least once for fancy particles
		while ((consumedAmount < itemCountToConsumeAfterMod && bowlsChecked < 8) || (itemCountToConsumeAfterMod == 0 & consumedAmount == 0)) {
			Vec3i bowlOffset = getItemBowlPositionOffset(randomBowlPosition + bowlsChecked, enchanterBlockEntity.virtualInventoryRecipeOrientation);
			
			BlockEntity blockEntity = world.getBlockEntity(enchanterBlockEntity.pos.add(bowlOffset));
			if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				if (itemCountToConsumeAfterMod == 0) {
					itemBowlBlockEntity.doEnchantingEffects(enchanterBlockEntity.pos);
					consumedAmount += itemCountToConsume;
				} else {
					int decrementedAmount = itemBowlBlockEntity.decrementBowlStack(enchanterBlockEntity.pos, itemCountToConsumeAfterMod, true);
					consumedAmount += decrementedAmount;
				}
			}
			bowlsChecked++;
		}
		
		return consumedAmount;
	}
	
	public static void craftEnchantmentUpgradeRecipe(World world, @NotNull EnchanterBlockEntity enchanterBlockEntity, @NotNull EnchantmentUpgradeRecipe enchantmentUpgradeRecipe) {
		enchanterBlockEntity.drainExperience(enchantmentUpgradeRecipe.getRequiredExperience());
		
		ItemStack resultStack = enchanterBlockEntity.getInventory().getStack(0);
		resultStack = SpectrumEnchantmentHelper.addOrExchangeEnchantment(resultStack, enchantmentUpgradeRecipe.getEnchantment(), enchantmentUpgradeRecipe.getEnchantmentDestinationLevel(), false, true);
		enchanterBlockEntity.getInventory().setStack(0, resultStack);
		
		// vanilla
		grantPlayerEnchantingAdvancementCriterion(world, enchanterBlockEntity.ownerUUID, resultStack, enchantmentUpgradeRecipe.getRequiredExperience());
		
		// enchantment upgrading criterion
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(world, enchanterBlockEntity.ownerUUID);
		if (serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.ENCHANTER_UPGRADING.trigger(serverPlayerEntity, enchantmentUpgradeRecipe.getEnchantment(), enchantmentUpgradeRecipe.getEnchantmentDestinationLevel(), enchantmentUpgradeRecipe.getRequiredExperience());
		}
	}
	
	public static Vec3i getItemBowlPositionOffset(int index, int orientation) {
		int offset = (orientation * 2 + index) % 8;
		return itemBowlOffsets.get(offset);
	}
	
	/**
	 * Calculates and sets a new recipe for the enchanter based on it's inventory
	 *
	 * @param world                The Enchanter World
	 * @param enchanterBlockEntity The Enchanter Block Entity
	 * @return False if the previously valid recipe was changed
	 */
	private static boolean calculateCurrentRecipe(@NotNull World world, @NotNull EnchanterBlockEntity enchanterBlockEntity) {
		if (enchanterBlockEntity.currentRecipe != null) {
			if (enchanterBlockEntity.currentRecipe.matches(enchanterBlockEntity.virtualInventoryIncludingBowlStacks, world)) {
				return true;
			}
		}
		
		enchanterBlockEntity.craftingTime = 0;
		Recipe previousRecipe = enchanterBlockEntity.currentRecipe;
		enchanterBlockEntity.currentRecipe = null;
		int previousOrientation = enchanterBlockEntity.virtualInventoryRecipeOrientation;
		
		SimpleInventory recipeTestInventory = new SimpleInventory(enchanterBlockEntity.virtualInventoryIncludingBowlStacks.size());
		recipeTestInventory.setStack(0, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack(0));
		recipeTestInventory.setStack(1, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack(1));
		// Cycle through 4 phases of recipe orientations
		// This way the player can arrange the ingredients in the item bowls as he likes
		// without resorting to specifying a fixed orientation
		EnchantmentUpgradeRecipe enchantmentUpgradeRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, enchanterBlockEntity.virtualInventoryIncludingBowlStacks, world).orElse(null);
		if (enchantmentUpgradeRecipe == null) {
			for (int orientation = 0; orientation < 4; orientation++) {
				int offset = orientation * 2;
				recipeTestInventory.setStack(2, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack((offset + 8) % 8 + 2));
				recipeTestInventory.setStack(3, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack((offset + 1 + 8) % 8 + 2));
				recipeTestInventory.setStack(4, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack((offset + 2 + 8) % 8 + 2));
				recipeTestInventory.setStack(5, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack((offset + 3 + 8) % 8 + 2));
				recipeTestInventory.setStack(6, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack((offset + 4 + 8) % 8 + 2));
				recipeTestInventory.setStack(7, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack((offset + 5 + 8) % 8 + 2));
				recipeTestInventory.setStack(8, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack((offset + 6 + 8) % 8 + 2));
				recipeTestInventory.setStack(9, enchanterBlockEntity.virtualInventoryIncludingBowlStacks.getStack((offset + 7 + 8) % 8 + 2));
				
				EnchanterRecipe enchanterRecipe = world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.ENCHANTER, recipeTestInventory, world).orElse(null);
				if (enchanterRecipe != null) {
					enchanterBlockEntity.currentRecipe = enchanterRecipe;
					enchanterBlockEntity.virtualInventoryRecipeOrientation = orientation;
					enchanterBlockEntity.virtualInventoryIncludingBowlStacks = recipeTestInventory;
					enchanterBlockEntity.craftingTimeTotal = (int) Math.ceil(enchanterRecipe.getCraftingTime() / enchanterBlockEntity.upgrades.get(Upgradeable.UpgradeType.SPEED));
					break;
				}
			}
		} else {
			if (enchanterBlockEntity.canOwnerOverenchant || !enchantmentUpgradeRecipe.requiresUnlockedOverEnchanting()) {
				enchanterBlockEntity.currentRecipe = enchantmentUpgradeRecipe;
				enchanterBlockEntity.currentItemProcessingTime = 0;
				enchanterBlockEntity.virtualInventoryRecipeOrientation = previousOrientation;
				enchanterBlockEntity.virtualInventoryIncludingBowlStacks = recipeTestInventory;
				enchanterBlockEntity.craftingTimeTotal = enchantmentUpgradeRecipe.getRequiredItemCount();
			}
		}
		
		if (enchanterBlockEntity.currentRecipe != previousRecipe) {
			enchanterBlockEntity.updateInClientWorld();
		}
		
		return false;
	}
	
	private static void grantPlayerEnchantingAdvancementCriterion(World world, UUID playerUUID, ItemStack resultStack, int experience) {
		int levels = ExperienceHelper.getLevelForExperience(experience);
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) PlayerOwned.getPlayerEntityIfOnline(world, playerUUID);
		if (serverPlayerEntity != null) {
			serverPlayerEntity.incrementStat(Stats.ENCHANT_ITEM);
			Criteria.ENCHANTED_ITEM.trigger(serverPlayerEntity, resultStack, levels);
		}
	}
	
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.craftingTime = nbt.getInt("crafting_time");
		this.craftingTimeTotal = nbt.getInt("crafting_time_total");
		this.currentItemProcessingTime = nbt.getInt("current_item_processing_time");
		
		this.inventory = new SimpleInventory(INVENTORY_SIZE);
		this.inventoryChanged = nbt.getBoolean("inventory_changed");
		this.canOwnerApplyConflictingEnchantments = nbt.getBoolean("owner_can_apply_conflicting_enchantments");
		this.canOwnerOverenchant = nbt.getBoolean("owner_can_overenchant");
		this.inventory.readNbtList(nbt.getList("inventory", 10));
		this.virtualInventoryRecipeOrientation = nbt.getInt("virtual_recipe_orientation");
		this.virtualInventoryIncludingBowlStacks = new SimpleInventory(INVENTORY_SIZE + 8);
		this.virtualInventoryIncludingBowlStacks.readNbtList(nbt.getList("virtual_inventory", 10));
		if (nbt.contains("item_facing", NbtElement.STRING_TYPE)) {
			this.itemFacing = Direction.valueOf(nbt.getString("item_facing").toUpperCase(Locale.ROOT));
		}
		if (nbt.contains("OwnerUUID")) {
			this.ownerUUID = nbt.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
		if (nbt.contains("CurrentRecipe")) {
			String recipeString = nbt.getString("CurrentRecipe");
			if (!recipeString.isEmpty()) {
				Optional<? extends Recipe> optionalRecipe = Optional.empty();
				if (world != null) {
					optionalRecipe = world.getRecipeManager().get(new Identifier(recipeString));
				}
				if (optionalRecipe.isPresent() && (optionalRecipe.get() instanceof EnchanterRecipe || optionalRecipe.get() instanceof EnchantmentUpgradeRecipe)) {
					this.currentRecipe = optionalRecipe.get();
				} else {
					this.currentRecipe = null;
				}
			} else {
				this.currentRecipe = null;
			}
		} else {
			this.currentRecipe = null;
		}
		if (nbt.contains("Upgrades", NbtElement.LIST_TYPE)) {
			this.upgrades = Upgradeable.fromNbt(nbt.getList("Upgrades", NbtElement.COMPOUND_TYPE));
		}
	}
	
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("crafting_time", this.craftingTime);
		nbt.putInt("crafting_time_total", this.craftingTimeTotal);
		nbt.putInt("current_item_processing_time", this.currentItemProcessingTime);
		
		nbt.put("inventory", this.inventory.toNbtList());
		nbt.putInt("virtual_recipe_orientation", this.virtualInventoryRecipeOrientation);
		nbt.putBoolean("inventory_changed", this.inventoryChanged);
		nbt.putBoolean("owner_can_apply_conflicting_enchantments", this.canOwnerApplyConflictingEnchantments);
		nbt.putBoolean("owner_can_overenchant", this.canOwnerOverenchant);
		nbt.put("virtual_inventory", this.virtualInventoryIncludingBowlStacks.toNbtList());
		if (this.itemFacing != null) {
			nbt.putString("item_facing", this.itemFacing.toString().toUpperCase(Locale.ROOT));
		}
		if (this.upgrades != null) {
			nbt.put("Upgrades", Upgradeable.toNbt(this.upgrades));
		}
		if (this.ownerUUID != null) {
			nbt.putUuid("OwnerUUID", this.ownerUUID);
		}
		if (this.currentRecipe != null) {
			nbt.putString("CurrentRecipe", this.currentRecipe.getId().toString());
		}
	}
	
	// Called when the chunk is first loaded to initialize this be
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbtCompound = new NbtCompound();
		this.writeNbt(nbtCompound);
		return nbtCompound;
	}
	
	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
	
	public void updateInClientWorld() {
		world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public Direction getItemFacingDirection() {
		// if placed via pipe or other sources
		return Objects.requireNonNullElse(this.itemFacing, Direction.NORTH);
	}
	
	public void setItemFacingDirection(Direction facingDirection) {
		this.itemFacing = facingDirection;
	}
	
	private void doItemBowlOrbs(World world) {
		for (int i = 0; i < 8; i++) {
			BlockPos itemBowlPos = pos.add(getItemBowlPositionOffset(i, 0));
			BlockEntity blockEntity = world.getBlockEntity(itemBowlPos);
			if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
				itemBowlBlockEntity.doEnchantingEffects(pos);
			}
		}
	}
	
	public boolean drainExperience(int amount) {
		ItemStack experienceProviderStack = getInventory().getStack(1);
		if (experienceProviderStack.getItem() instanceof ExperienceStorageItem experienceStorageItem) {
			int currentStoredExperience = ExperienceStorageItem.getStoredExperience(experienceProviderStack);
			if (currentStoredExperience > 0) {
				int amountAfterExperienceMod = getExperienceWithMod(amount, this.upgrades.get(UpgradeType.EXPERIENCE), world.random);
				int drainedExperience = Math.min(currentStoredExperience, amountAfterExperienceMod);
				
				if (experienceStorageItem instanceof KnowledgeGemItem knowledgeGemItem) {
					if (knowledgeGemItem.changedDisplayTier(currentStoredExperience, currentStoredExperience - drainedExperience)) {
						// There was enough experience drained from the knowledge gem that the visual changed
						// To display the updated knowledge gem size clientside the inventory has to be synched
						// to the clients for rendering purposes
						SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(null, (ServerWorld) world, new Vec3d(this.pos.getX() + 0.5, this.pos.getY() + 2.5, this.pos.getZ() + 0.5), SpectrumParticleTypes.LIME_CRAFTING, ParticlePattern.SIXTEEN, 0.05F);
						this.updateInClientWorld();
					}
				}
				
				this.markDirty();
				return ExperienceStorageItem.removeStoredExperience(experienceProviderStack, drainedExperience);
			}
		}
		return false;
	}
	
	public void inventoryChanged() {
		virtualInventoryIncludingBowlStacks = new SimpleInventory(INVENTORY_SIZE + 8);
		virtualInventoryIncludingBowlStacks.setStack(0, this.inventory.getStack(0)); // center item
		virtualInventoryIncludingBowlStacks.setStack(1, this.inventory.getStack(1)); // knowledge gem
		virtualInventoryIncludingBowlStacks.setStack(2, getItemBowlStack(this.world, pos.add(5, 0, -3)));
		virtualInventoryIncludingBowlStacks.setStack(3, getItemBowlStack(this.world, pos.add(5, 0, 3)));
		virtualInventoryIncludingBowlStacks.setStack(4, getItemBowlStack(this.world, pos.add(3, 0, 5)));
		virtualInventoryIncludingBowlStacks.setStack(5, getItemBowlStack(this.world, pos.add(-3, 0, 5)));
		virtualInventoryIncludingBowlStacks.setStack(6, getItemBowlStack(this.world, pos.add(-5, 0, 3)));
		virtualInventoryIncludingBowlStacks.setStack(7, getItemBowlStack(this.world, pos.add(-5, 0, -3)));
		virtualInventoryIncludingBowlStacks.setStack(8, getItemBowlStack(this.world, pos.add(-3, 0, -5)));
		virtualInventoryIncludingBowlStacks.setStack(9, getItemBowlStack(this.world, pos.add(3, 0, -5)));
		
		currentItemProcessingTime = -1;
		
		this.markDirty();
		this.inventory.markDirty();
		this.virtualInventoryIncludingBowlStacks.markDirty();
		this.inventoryChanged = true;
	}
	
	public ItemStack getItemBowlStack(World world, BlockPos blockPos) {
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof ItemBowlBlockEntity itemBowlBlockEntity) {
			return itemBowlBlockEntity.getInventory().getStack(0);
		} else {
			return ItemStack.EMPTY;
		}
	}
	
	public void playSound(SoundEvent soundEvent, float volume) {
		Random random = world.random;
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, volume, 0.9F + random.nextFloat() * 0.15F);
	}
	
	// PLAYER OWNED
	// "owned" is not to be taken literally here. The owner
	// is always set to the last player interacted with to trigger advancements
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
		this.canOwnerApplyConflictingEnchantments = Support.hasAdvancement(playerEntity, APPLY_CONFLICTING_ENCHANTMENTS_ADVANCEMENT_IDENTIFIER);
		this.canOwnerOverenchant = Support.hasAdvancement(playerEntity, OVERENCHANTING_ADVANCEMENT_IDENTIFIER);
	}
	
	// UPGRADEABLE
	public void resetUpgrades() {
		this.upgrades = null;
		this.markDirty();
	}
	
	public void calculateUpgrades() {
		Pair<Integer, Map<UpgradeType, Double>> upgrades = Upgradeable.checkUpgradeMods4(world, pos, 3, 0);
		this.upgrades = upgrades.getRight();
		this.markDirty();
	}
	
}