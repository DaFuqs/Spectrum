package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.block.*;
import net.minecraft.component.*;
import net.minecraft.component.type.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.apache.commons.lang3.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class BedrockAnvilScreenHandler extends ForgingScreenHandler {
	
	public static final int MAX_LORE_LENGTH = 200;
	
	public static final int FIRST_INPUT_SLOT_INDEX = 0;
	public static final int SECOND_INPUT_SLOT_INDEX = 1;
	public static final int OUTPUT_SLOT_INDEX = 2;
	
	private final Property levelCost;
	private int repairItemCount;
	private String newItemName;
	private String newLoreString;
	
	public BedrockAnvilScreenHandler(int syncId, PlayerInventory inventory) {
		this(syncId, inventory, ScreenHandlerContext.EMPTY);
	}
	
	public BedrockAnvilScreenHandler(int syncId, PlayerInventory inventory, ScreenHandlerContext context) {
		super(SpectrumScreenHandlerTypes.BEDROCK_ANVIL, syncId, inventory, context);
		this.levelCost = Property.create();
		this.addProperty(this.levelCost);
	}
	
	@Override
	protected ForgingSlotsManager getForgingSlotsManager() {
		return ForgingSlotsManager.create()
				.input(0, 27, 47, (stack) -> true)
				.input(1, 76, 47, (stack) -> true)
				.output(2, 134, 47)
				.build();
	}
	
	@Override
	public void addPlayerInventorySlots(PlayerInventory playerInventory) {
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
	
	protected boolean canUse(BlockState state) {
		return state.isIn(BlockTags.ANVIL);
	}
	
	public static int getNextCost(int cost) {
		return cost * 2 + 1;
	}
	
	protected boolean canTakeOutput(PlayerEntity player, boolean present) {
		return player.isInCreativeMode() || player.experienceLevel >= this.levelCost.get();
	}
	
	protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
		if (!player.getAbilities().creativeMode) {
			player.addExperienceLevels(-this.levelCost.get());
		}
		
		this.input.setStack(0, ItemStack.EMPTY);
		if (this.repairItemCount > 0) {
			ItemStack itemStack = this.input.getStack(1);
			if (!itemStack.isEmpty() && itemStack.getCount() > this.repairItemCount) {
				itemStack.decrement(this.repairItemCount);
				this.input.setStack(1, itemStack);
			} else {
				this.input.setStack(1, ItemStack.EMPTY);
			}
		} else {
			this.input.setStack(1, ItemStack.EMPTY);
		}
		
		this.levelCost.set(0);
		this.context.run((world, pos) -> world.syncWorldEvent(WorldEvents.ANVIL_USED, pos, 0));
	}
	
	public void updateResult() {
		boolean combined = false; // We added this line
		
		ItemStack inputStack = this.input.getStack(0);
		this.levelCost.set(0);  // We changed '1' -> '0'
		int enchantmentLevelCost = 0;
		long repairLevelCost = 0L;
//		int renameCost = 0; // We removed this line - Renames are free
		if (!inputStack.isEmpty() && EnchantmentHelper.canHaveEnchantments(inputStack)) {
			ItemStack outputStack = inputStack.copy();
			ItemStack repairSlotStack = this.input.getStack(1);
			ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(outputStack));
			repairLevelCost += (long) inputStack.getOrDefault(DataComponentTypes.REPAIR_COST, 0)
					+ (long) repairSlotStack.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
			this.repairItemCount = 0;
			boolean pigmentInRepairSlot = repairSlotStack.getItem() instanceof PigmentItem;
			if (pigmentInRepairSlot) {
				repairItemCount = 1;
			}
			if (!repairSlotStack.isEmpty()) {
				combined = true; // We added this line
				
				boolean enchantedBookInInputSlot = inputStack.isOf(Items.ENCHANTED_BOOK) && !inputStack.contains(DataComponentTypes.STORED_ENCHANTMENTS);
				boolean enchantedBookInRepairSlot = repairSlotStack.isOf(Items.ENCHANTED_BOOK) && !inputStack.contains(DataComponentTypes.STORED_ENCHANTMENTS);
				
				int o;
				int repairItemCount;
				int newOutputStackDamage;
				if (outputStack.isDamageable() && outputStack.getItem().canRepair(inputStack, repairSlotStack)) {
					int toRepair = Math.min(outputStack.getDamage(), outputStack.getMaxDamage() / 4);
					if (toRepair <= 0) {
						this.output.setStack(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
					
					for (repairItemCount = 0; toRepair > 0 && repairItemCount < repairSlotStack.getCount(); ++repairItemCount) {
						newOutputStackDamage = outputStack.getDamage() - toRepair;
						outputStack.setDamage(newOutputStackDamage);
						++enchantmentLevelCost;
						toRepair = Math.min(outputStack.getDamage(), outputStack.getMaxDamage() / 4);
					}
					
					this.repairItemCount = repairItemCount;
				} else {
					if (!pigmentInRepairSlot && !enchantedBookInRepairSlot && (!outputStack.isOf(repairSlotStack.getItem()) || !outputStack.isDamageable())) {
						this.output.setStack(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
					
					if (outputStack.isDamageable() && !enchantedBookInRepairSlot && !pigmentInRepairSlot) {
						int inputItemDurability = inputStack.getMaxDamage() - inputStack.getDamage();
						int repairItemDurability = repairSlotStack.getMaxDamage() - repairSlotStack.getDamage();
						int toRepair = repairItemDurability + outputStack.getMaxDamage() * 12 / 100;
						int outputItemDurability = inputItemDurability + toRepair;
						int outputItemDamage = outputStack.getMaxDamage() - outputItemDurability;
						if (outputItemDamage < 0) {
							outputItemDamage = 0;
						}
						
						if (outputItemDamage < outputStack.getDamage()) {
							outputStack.setDamage(outputItemDamage);
							enchantmentLevelCost += 2;
						}
					}
					
					ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(repairSlotStack);
					boolean foundAcceptable = false;
					boolean foundUnacceptable = false;
					
					for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
						RegistryEntry<Enchantment> registryEntry = entry.getKey();
						int t = builder.getLevel(registryEntry);
						int newEnchantmentLevel = entry.getIntValue();
						newEnchantmentLevel = t == newEnchantmentLevel ? newEnchantmentLevel + 1 : Math.max(newEnchantmentLevel, t);
						Enchantment enchantment = registryEntry.value();
						boolean itemStackIsAcceptableForStack = inputStack.canBeEnchantedWith(registryEntry, EnchantingContext.ACCEPTABLE);
						if (this.player.getAbilities().creativeMode || inputStack.isOf(Items.ENCHANTED_BOOK)) {
							itemStackIsAcceptableForStack = true;
						}
						
						for (RegistryEntry<Enchantment> registryEntry2 : builder.getEnchantments()) {
							if (!registryEntry2.equals(registryEntry) && !Enchantment.canBeCombined(registryEntry, registryEntry2)) {
								itemStackIsAcceptableForStack = false;
								++enchantmentLevelCost;
							}
						}
						
						if (!itemStackIsAcceptableForStack) {
							foundUnacceptable = true;
						} else {
							foundAcceptable = true;
							boolean capToMaxLevel = (inputStack.isOf(Items.ENCHANTED_BOOK) && !inputStack.contains(DataComponentTypes.STORED_ENCHANTMENTS)) || !SpectrumCommon.CONFIG.BedrockAnvilCanExceedMaxVanillaEnchantmentLevel; // We added this line
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
						this.output.setStack(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
				}
			}
			
			if (this.newItemName != null && !StringHelper.isBlank(this.newItemName)) {
				if (!this.newItemName.equals(inputStack.getName().getString())) {
					// We removed these - Renames are free
//					 renameCost = 1;
//					 enchantmentLevelCost += renameCost;
					if (inputStack.getName() instanceof MutableText inputText && inputText.getStyle().getColor() != null) {
						outputStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName).setStyle(Style.EMPTY.withColor(inputText.getStyle().getColor())));
					} else {
						outputStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName));
					}
				}
			} else if (inputStack.contains(DataComponentTypes.CUSTOM_NAME)) {
				// We removed these - Renames are free
// 				 renameCost = 1;
//				 enchantmentLevelCost += renameCost;
				outputStack.remove(DataComponentTypes.CUSTOM_NAME);
			}
			// TODO: we are setting DataComponentTypes.CUSTOM_NAME above, already.
			Text text = outputStack.getName();
			if (pigmentInRepairSlot && text instanceof MutableText mutableText) {
				var newColor = ((PigmentItem) repairSlotStack.getItem()).getInkColor().getColorInt();
				Text newName = mutableText.setStyle(mutableText.getStyle().withColor(newColor));
				if (!newName.equals(inputStack.getName())) {
					outputStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.newItemName).withColor(newColor));
				}
			}
			
			// We added this if/elseif block
			if (this.newLoreString != null && !StringHelper.isBlank(this.newLoreString)) {
				List<Text> lore = LoreHelper.getLoreTextArrayFromString(this.newLoreString);
				if (!LoreHelper.equalsLore(lore, inputStack)) {
					LoreHelper.setLore(outputStack, lore);
				}
			} else if (LoreHelper.hasLore(inputStack)) {
				LoreHelper.removeLore(outputStack);
			}
			
			int totalCost = (int) MathHelper.clamp(repairLevelCost + (long) enchantmentLevelCost, 0L, 2147483647L);
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
				int repairCost = outputStack.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
				if (repairCost < repairSlotStack.getOrDefault(DataComponentTypes.REPAIR_COST, 0)) {
					repairCost = repairSlotStack.getOrDefault(DataComponentTypes.REPAIR_COST, 0);
				}
				
				if (enchantmentLevelCost > 0) { // We changed 'renameCost != enchantmentLevelCost || renameCost == 0' -> 'enchantmentLevelCost > 0'
					repairCost = getNextCost(repairCost);
					outputStack.set(DataComponentTypes.REPAIR_COST, repairCost);
				}
				
				EnchantmentHelper.set(outputStack, builder.build());
			}
			
			this.output.setStack(0, outputStack);
			this.sendContentUpdates();
		} else {
			this.output.setStack(0, ItemStack.EMPTY);
			this.levelCost.set(0);
		}
	}
	
	public boolean setNewItemName(String newItemName) {
		String string = sanitize(newItemName, AnvilScreenHandler.MAX_NAME_LENGTH);
		if (string != null && !string.equals(this.newItemName)) {
			this.newItemName = string;
			if (this.getSlot(OUTPUT_SLOT_INDEX).hasStack()) {
				ItemStack itemStack = this.getSlot(OUTPUT_SLOT_INDEX).getStack();
				if (StringHelper.isBlank(string)) {
					itemStack.remove(DataComponentTypes.CUSTOM_NAME);
				} else {
					itemStack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(string));
				}
			}
			
			this.updateResult();
			return true;
		} else {
			return false;
		}
	}
	
	@Nullable
	private static String sanitize(String name, int maxLength) {
		String string = StringHelper.stripInvalidChars(name);
		return string.length() <= maxLength ? string : null;
	}
	
	public boolean setNewItemLore(String newLoreString) {
		String string = sanitize(newLoreString, MAX_LORE_LENGTH);
		if (string != null && !string.equals(this.newLoreString)) {
			this.newLoreString = string;
			
			if (this.getSlot(OUTPUT_SLOT_INDEX).hasStack()) {
				ItemStack itemStack = this.getSlot(OUTPUT_SLOT_INDEX).getStack();
				if (StringUtils.isBlank(newLoreString)) {
					LoreHelper.removeLore(itemStack);
				} else {
					LoreHelper.setLore(itemStack, LoreHelper.getLoreTextArrayFromString(this.newLoreString));
				}
			}
			this.updateResult();
			return true;
		}
		return false;
	}
	
	public int getLevelCost() {
		return this.levelCost.get();
	}
	
}
