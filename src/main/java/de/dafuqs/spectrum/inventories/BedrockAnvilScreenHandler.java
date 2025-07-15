package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BedrockAnvilScreenHandler extends ItemCombinerMenu {
	
	public static final int MAX_LORE_LENGTH = 200;
	
	public static final int FIRST_INPUT_SLOT_INDEX = 0;
	public static final int SECOND_INPUT_SLOT_INDEX = 1;
	public static final int OUTPUT_SLOT_INDEX = 2;
	
	private final DataSlot levelCost;
	private int repairItemCount;
	private String newItemName;
	private String newLoreString;
	
	public BedrockAnvilScreenHandler(int syncId, Inventory inventory) {
		this(syncId, inventory, ContainerLevelAccess.NULL);
	}
	
	public BedrockAnvilScreenHandler(int syncId, Inventory inventory, ContainerLevelAccess context) {
		super(SpectrumScreenHandlerTypes.BEDROCK_ANVIL, syncId, inventory, context);
		this.levelCost = DataSlot.standalone();
		this.addDataSlot(this.levelCost);
	}
	
	@Override
	protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
		return ItemCombinerMenuSlotDefinition.create()
				.withSlot(0, 27, 47, (stack) -> true)
				.withSlot(1, 76, 47, (stack) -> true)
				.withResultSlot(2, 134, 47)
				.build();
	}
	
	@Override
	public void createInventorySlots(Inventory playerInventory) {
		int i;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 24 + 84 + i * 18));
			}
		}
		
		for (i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 24 + 142));
		}
		
	}
	
	protected boolean isValidBlock(BlockState state) {
		return state.is(BlockTags.ANVIL);
	}
	
	public static int getNextCost(int cost) {
		return cost * 2 + 1;
	}
	
	protected boolean mayPickup(Player player, boolean present) {
		return player.hasInfiniteMaterials() || player.experienceLevel >= this.levelCost.get();
	}
	
	protected void onTake(Player player, ItemStack stack) {
		if (!player.getAbilities().instabuild) {
			player.giveExperienceLevels(-this.levelCost.get());
		}
		
		this.inputSlots.setItem(0, ItemStack.EMPTY);
		if (this.repairItemCount > 0) {
			ItemStack itemStack = this.inputSlots.getItem(1);
			if (!itemStack.isEmpty() && itemStack.getCount() > this.repairItemCount) {
				itemStack.shrink(this.repairItemCount);
				this.inputSlots.setItem(1, itemStack);
			} else {
				this.inputSlots.setItem(1, ItemStack.EMPTY);
			}
		} else {
			this.inputSlots.setItem(1, ItemStack.EMPTY);
		}
		
		this.levelCost.set(0);
		this.access.execute((world, pos) -> world.levelEvent(LevelEvent.SOUND_ANVIL_USED, pos, 0));
	}
	
	public void createResult() {
		boolean combined = false; // We added this line
		
		ItemStack inputStack = this.inputSlots.getItem(0);
		this.levelCost.set(0);  // We changed '1' -> '0'
		int enchantmentLevelCost = 0;
		long repairLevelCost = 0L;
//		int renameCost = 0; // We removed this line - Renames are free
		if (!inputStack.isEmpty() && EnchantmentHelper.canStoreEnchantments(inputStack)) {
			ItemStack outputStack = inputStack.copy();
			ItemStack repairSlotStack = this.inputSlots.getItem(1);
			ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(outputStack));
			repairLevelCost += (long) inputStack.getOrDefault(DataComponents.REPAIR_COST, 0)
					+ (long) repairSlotStack.getOrDefault(DataComponents.REPAIR_COST, 0);
			this.repairItemCount = 0;
			boolean pigmentInRepairSlot = repairSlotStack.getItem() instanceof PigmentItem;
			if (pigmentInRepairSlot) {
				repairItemCount = 1;
			}
			if (!repairSlotStack.isEmpty()) {
				combined = true; // We added this line
				
				boolean enchantedBookInInputSlot = inputStack.is(Items.ENCHANTED_BOOK) && !inputStack.has(DataComponents.STORED_ENCHANTMENTS);
				boolean enchantedBookInRepairSlot = repairSlotStack.is(Items.ENCHANTED_BOOK) && !inputStack.has(DataComponents.STORED_ENCHANTMENTS);
				
				int o;
				int repairItemCount;
				int newOutputStackDamage;
				if (outputStack.isDamageableItem() && outputStack.getItem().isValidRepairItem(inputStack, repairSlotStack)) {
					int toRepair = Math.min(outputStack.getDamageValue(), outputStack.getMaxDamage() / 4);
					if (toRepair <= 0) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
					
					for (repairItemCount = 0; toRepair > 0 && repairItemCount < repairSlotStack.getCount(); ++repairItemCount) {
						newOutputStackDamage = outputStack.getDamageValue() - toRepair;
						outputStack.setDamageValue(newOutputStackDamage);
						++enchantmentLevelCost;
						toRepair = Math.min(outputStack.getDamageValue(), outputStack.getMaxDamage() / 4);
					}
					
					this.repairItemCount = repairItemCount;
				} else {
					if (!pigmentInRepairSlot && !enchantedBookInRepairSlot && (!outputStack.is(repairSlotStack.getItem()) || !outputStack.isDamageableItem())) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
					
					if (outputStack.isDamageableItem() && !enchantedBookInRepairSlot && !pigmentInRepairSlot) {
						int inputItemDurability = inputStack.getMaxDamage() - inputStack.getDamageValue();
						int repairItemDurability = repairSlotStack.getMaxDamage() - repairSlotStack.getDamageValue();
						int toRepair = repairItemDurability + outputStack.getMaxDamage() * 12 / 100;
						int outputItemDurability = inputItemDurability + toRepair;
						int outputItemDamage = outputStack.getMaxDamage() - outputItemDurability;
						if (outputItemDamage < 0) {
							outputItemDamage = 0;
						}
						
						if (outputItemDamage < outputStack.getDamageValue()) {
							outputStack.setDamageValue(outputItemDamage);
							enchantmentLevelCost += 2;
						}
					}
					
					ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(repairSlotStack);
					boolean foundAcceptable = false;
					boolean foundUnacceptable = false;
					
					for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantmentsComponent.entrySet()) {
						Holder<Enchantment> registryEntry = entry.getKey();
						int t = builder.getLevel(registryEntry);
						int newEnchantmentLevel = entry.getIntValue();
						newEnchantmentLevel = t == newEnchantmentLevel ? newEnchantmentLevel + 1 : Math.max(newEnchantmentLevel, t);
						Enchantment enchantment = registryEntry.value();
						boolean itemStackIsAcceptableForStack = inputStack.canBeEnchantedWith(registryEntry, EnchantingContext.ACCEPTABLE);
						if (this.player.getAbilities().instabuild || inputStack.is(Items.ENCHANTED_BOOK)) {
							itemStackIsAcceptableForStack = true;
						}
						
						for (Holder<Enchantment> registryEntry2 : builder.keySet()) {
							if (!registryEntry2.equals(registryEntry) && !Enchantment.areCompatible(registryEntry, registryEntry2)) {
								itemStackIsAcceptableForStack = false;
								++enchantmentLevelCost;
							}
						}
						
						if (!itemStackIsAcceptableForStack) {
							foundUnacceptable = true;
						} else {
							foundAcceptable = true;
							boolean capToMaxLevel = (inputStack.is(Items.ENCHANTED_BOOK) && !inputStack.has(DataComponents.STORED_ENCHANTMENTS)) || !SpectrumCommon.CONFIG.BedrockAnvilCanExceedMaxVanillaEnchantmentLevel; // We added this line
							if (capToMaxLevel && newEnchantmentLevel > enchantment.getMaxLevel()) { // We added 'capToMaxLevel &&'
								newEnchantmentLevel = enchantment.getMaxLevel();
							}
							
							builder.set(registryEntry, newEnchantmentLevel);
							int anvilCost = enchantment.getAnvilCost();
							if (enchantedBookInRepairSlot) {
								anvilCost = Math.max(1, anvilCost / 2);
							}
							
							enchantmentLevelCost += anvilCost * newEnchantmentLevel;
							if (inputStack.getCount() > 1) {
								enchantmentLevelCost = 40;
							}
						}
					}
					
					if (foundUnacceptable && !foundAcceptable) {
						this.resultSlots.setItem(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
				}
			}
			
			if (this.newItemName != null && !StringUtil.isBlank(this.newItemName)) {
				if (!this.newItemName.equals(inputStack.getHoverName().getString())) {
					// We removed these - Renames are free
//					 renameCost = 1;
//					 enchantmentLevelCost += renameCost;
					if (inputStack.getHoverName() instanceof MutableComponent inputText && inputText.getStyle().getColor() != null) {
						outputStack.set(DataComponents.CUSTOM_NAME, Component.literal(this.newItemName).setStyle(Style.EMPTY.withColor(inputText.getStyle().getColor())));
					} else {
						outputStack.set(DataComponents.CUSTOM_NAME, Component.literal(this.newItemName));
					}
				}
			} else if (inputStack.has(DataComponents.CUSTOM_NAME)) {
				// We removed these - Renames are free
// 				 renameCost = 1;
//				 enchantmentLevelCost += renameCost;
				outputStack.remove(DataComponents.CUSTOM_NAME);
			}
			// TODO: we are setting DataComponentTypes.CUSTOM_NAME above, already.
			Component text = outputStack.getHoverName();
			if (pigmentInRepairSlot && text instanceof MutableComponent mutableText) {
				var newColor = ((PigmentItem) repairSlotStack.getItem()).getInkColor().getColorInt();
				Component newName = mutableText.setStyle(mutableText.getStyle().withColor(newColor));
				if (!newName.equals(inputStack.getHoverName())) {
					outputStack.set(DataComponents.CUSTOM_NAME, Component.literal(this.newItemName).withColor(newColor));
				}
			}
			
			// We added this if/elseif block
			if (this.newLoreString != null && !StringUtil.isBlank(this.newLoreString)) {
				List<Component> lore = LoreHelper.getLoreTextArrayFromString(this.newLoreString);
				if (!LoreHelper.equalsLore(lore, inputStack)) {
					LoreHelper.setLore(outputStack, lore);
				}
			} else if (LoreHelper.hasLore(inputStack)) {
				LoreHelper.removeLore(outputStack);
			}
			
			int totalCost = (int) Mth.clamp(repairLevelCost + (long) enchantmentLevelCost, 0L, 2147483647L);
			this.levelCost.set(totalCost);
			if (enchantmentLevelCost < 0) { // We modified this ('<=' -> '<')
				outputStack = ItemStack.EMPTY;
			}
			
			// We removed this - Renames are free and we allow +40lvl enchants
//			if (renameCost == enchantmentLevelCost && renameCost > 0 && this.levelCost.get() >= 40) {
//				this.levelCost.set(39);
//			}
			
			// We removed this - We allow costs greater than 40 levels
//			if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) {
//				itemStack2 = ItemStack.EMPTY;
//			}
			
			if (!combined || pigmentInRepairSlot) { // We added this if - Renames are free
				this.levelCost.set(0);
			} else if (!outputStack.isEmpty()) {
				int repairCost = outputStack.getOrDefault(DataComponents.REPAIR_COST, 0);
				if (repairCost < repairSlotStack.getOrDefault(DataComponents.REPAIR_COST, 0)) {
					repairCost = repairSlotStack.getOrDefault(DataComponents.REPAIR_COST, 0);
				}
				
				if (enchantmentLevelCost > 0) { // We changed 'renameCost != enchantmentLevelCost || renameCost == 0' -> 'enchantmentLevelCost > 0'
					repairCost = getNextCost(repairCost);
					outputStack.set(DataComponents.REPAIR_COST, repairCost);
				}
				
				EnchantmentHelper.setEnchantments(outputStack, builder.toImmutable());
			}
			
			this.resultSlots.setItem(0, outputStack);
			this.broadcastChanges();
		} else {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			this.levelCost.set(0);
		}
	}
	
	public boolean setNewItemName(String newItemName) {
		String string = sanitize(newItemName, AnvilMenu.MAX_NAME_LENGTH);
		if (string != null && !string.equals(this.newItemName)) {
			this.newItemName = string;
			if (this.getSlot(OUTPUT_SLOT_INDEX).hasItem()) {
				ItemStack itemStack = this.getSlot(OUTPUT_SLOT_INDEX).getItem();
				if (StringUtil.isBlank(string)) {
					itemStack.remove(DataComponents.CUSTOM_NAME);
				} else {
					itemStack.set(DataComponents.CUSTOM_NAME, Component.literal(string));
				}
			}
			
			this.createResult();
			return true;
		} else {
			return false;
		}
	}
	
	@Nullable
	private static String sanitize(String name, int maxLength) {
		String string = StringUtil.filterText(name);
		return string.length() <= maxLength ? string : null;
	}
	
	public boolean setNewItemLore(String newLoreString) {
		String string = sanitize(newLoreString, MAX_LORE_LENGTH);
		if (string != null && !string.equals(this.newLoreString)) {
			this.newLoreString = string;
			
			if (this.getSlot(OUTPUT_SLOT_INDEX).hasItem()) {
				ItemStack itemStack = this.getSlot(OUTPUT_SLOT_INDEX).getItem();
				if (StringUtils.isBlank(newLoreString)) {
					LoreHelper.removeLore(itemStack);
				} else {
					LoreHelper.setLore(itemStack, LoreHelper.getLoreTextArrayFromString(this.newLoreString));
				}
			}
			this.createResult();
			return true;
		}
		return false;
	}
	
	public int getLevelCost() {
		return this.levelCost.get();
	}
	
}
