package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import net.minecraft.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.apache.commons.lang3.*;

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
	
	@Override
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
		if (inventory == this.input) {
			this.updateResult();
		}
	}
	
	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		this.context.run((world, pos) -> this.dropInventory(player, this.input));
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return this.context.get((world, pos) -> this.canUse(world.getBlockState(pos)) && player.squaredDistanceTo((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D, true);
	}
	
	protected boolean canTakeOutput(PlayerEntity player, boolean present) {
		return player.getAbilities().creativeMode || player.experienceLevel >= this.levelCost.get();
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
		boolean combined = false;
		
		ItemStack inputStack = this.input.getStack(0);
		this.levelCost.set(0);
		int enchantmentLevelCost = 0;
		int repairLevelCost = 0;
		int k = 0;
		if (inputStack.isEmpty()) {
			this.output.setStack(0, ItemStack.EMPTY);
			this.levelCost.set(0);
		} else {
			ItemStack outputStack = inputStack.copy();
			ItemStack repairSlotStack = this.input.getStack(1);
			Map<Enchantment, Integer> enchantmentLevelMap = EnchantmentHelper.get(outputStack);
			repairLevelCost += inputStack.getRepairCost() + (repairSlotStack.isEmpty() ? 0 : repairSlotStack.getRepairCost());
			this.repairItemCount = 0;
			boolean pigmentInRepairSlot = repairSlotStack.getItem()  instanceof PigmentItem;
			if(pigmentInRepairSlot)
			{
				repairItemCount = 1;
			}
			if (!repairSlotStack.isEmpty()) {
				combined = true;
				
				boolean enchantedBookInInputSlot = inputStack.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(inputStack).isEmpty();
				boolean enchantedBookInRepairSlot = repairSlotStack.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(repairSlotStack).isEmpty();
				

				int o;
				int repairItemCount;
				int newOutputStackDamage;
				if (outputStack.isDamageable() && outputStack.getItem().canRepair(inputStack, repairSlotStack)) {
					o = Math.min(outputStack.getDamage(), outputStack.getMaxDamage() / 4);
					if (o <= 0) {
						this.output.setStack(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
					
					for (repairItemCount = 0; o > 0 && repairItemCount < repairSlotStack.getCount(); ++repairItemCount) {
						newOutputStackDamage = outputStack.getDamage() - o;
						outputStack.setDamage(newOutputStackDamage);
						++enchantmentLevelCost;
						o = Math.min(outputStack.getDamage(), outputStack.getMaxDamage() / 4);
					}
					
					this.repairItemCount = repairItemCount;
				} else {
					if (!pigmentInRepairSlot && !enchantedBookInRepairSlot && (!outputStack.isOf(repairSlotStack.getItem()) || !outputStack.isDamageable())) {
						this.output.setStack(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
					
					if (outputStack.isDamageable() && !enchantedBookInRepairSlot && !pigmentInRepairSlot) {
						o = inputStack.getMaxDamage() - inputStack.getDamage();
						repairItemCount = repairSlotStack.getMaxDamage() - repairSlotStack.getDamage();
						newOutputStackDamage = repairItemCount + outputStack.getMaxDamage() * 12 / 100;
						int r = o + newOutputStackDamage;
						int s = outputStack.getMaxDamage() - r;
						if (s < 0) {
							s = 0;
						}
						
						if (s < outputStack.getDamage()) {
							outputStack.setDamage(s);
							enchantmentLevelCost += 2;
						}
					}
					
					Map<Enchantment, Integer> currentEnchantments = EnchantmentHelper.get(repairSlotStack);
					boolean bl2 = false;
					boolean bl3 = false;
					Iterator<Enchantment> enchantmentIterator = currentEnchantments.keySet().iterator();
					
					label155:
					while (true) {
						Enchantment enchantment;
						do {
							if (!enchantmentIterator.hasNext()) {
								if (bl3 && !bl2) {
									this.output.setStack(0, ItemStack.EMPTY);
									this.levelCost.set(0);
									return;
								}
								break label155;
							}
							enchantment = enchantmentIterator.next();
						} while (enchantment == null);
						
						int t = enchantmentLevelMap.getOrDefault(enchantment, 0);
						int newEnchantmentLevel = currentEnchantments.get(enchantment);
						newEnchantmentLevel = t == newEnchantmentLevel ? newEnchantmentLevel + 1 : Math.max(newEnchantmentLevel, t);
						boolean itemStackIsAcceptableForStack = enchantment.isAcceptableItem(inputStack);
						if (this.player.getAbilities().creativeMode || inputStack.isOf(Items.ENCHANTED_BOOK)) {
							itemStackIsAcceptableForStack = true;
						}
						
						for (Enchantment enchantment2 : enchantmentLevelMap.keySet()) {
							if (enchantment2 != enchantment && !enchantment.canCombine(enchantment2)) {
								itemStackIsAcceptableForStack = false;
								++enchantmentLevelCost;
							}
						}
						
						if (!itemStackIsAcceptableForStack) {
							bl3 = true;
						} else {
							bl2 = true;
							boolean capToMaxLevel = enchantedBookInInputSlot || !SpectrumCommon.CONFIG.BedrockAnvilCanExceedMaxVanillaEnchantmentLevel;
							if (capToMaxLevel && newEnchantmentLevel > enchantment.getMaxLevel()) {
								newEnchantmentLevel = enchantment.getMaxLevel();
							}
							
							enchantmentLevelMap.put(enchantment, newEnchantmentLevel);
							int enchantmentRarityInt = switch (enchantment.getRarity()) {
								case COMMON -> 1;
								case UNCOMMON -> 2;
								case RARE -> 4;
								case VERY_RARE -> 8;
							};
							
							if (enchantedBookInRepairSlot) {
								enchantmentRarityInt = Math.max(1, enchantmentRarityInt / 2);
							}
							
							enchantmentLevelCost += enchantmentRarityInt * newEnchantmentLevel;
							if (inputStack.getCount() > 1) {
								enchantmentLevelCost = 40;
							}
						}
					}
				}
			}
			
			if (this.newItemName != null && !Util.isBlank(this.newItemName)) {
				if (!this.newItemName.equals(inputStack.getName().getString())) {
					if(inputStack.getName() instanceof MutableText inputText && inputText.getStyle().getColor()!= null)
					{
						outputStack.setCustomName(Text.literal(this.newItemName).setStyle(Style.EMPTY.withColor(inputText.getStyle().getColor())));
					}
					else{
						outputStack.setCustomName(Text.literal(this.newItemName));
					}
				}
			} else if (inputStack.hasCustomName()) {
				outputStack.removeCustomName();
			}
			Text text = outputStack.getName();
			if(pigmentInRepairSlot && text instanceof MutableText mutableText)
			{
				TextColor newColor = TextColor.fromRgb(ColorHelper.getInt(((PigmentItem)repairSlotStack.getItem()).getColor()));
				Text newName = mutableText.setStyle(mutableText.getStyle().withColor(newColor));
				if(!newName.equals(inputStack.getName()))
				{
					outputStack.setCustomName(newName);
				}
			}
			
			if (this.newLoreString != null && !Util.isBlank(this.newLoreString)) {
				List<Text> lore = LoreHelper.getLoreTextArrayFromString(this.newLoreString);
				if (!LoreHelper.equalsLore(lore, inputStack)) {
					LoreHelper.setLore(outputStack, lore);
				}
			} else if (inputStack.hasCustomName()) {
				LoreHelper.removeLore(outputStack);
			}
			
			this.levelCost.set(repairLevelCost + enchantmentLevelCost);
			if (enchantmentLevelCost < 0) {
				outputStack = ItemStack.EMPTY;
			}
			
			if (!combined || pigmentInRepairSlot) {
				// renaming and lore is free
				this.levelCost.set(0);
			} else if (!outputStack.isEmpty()) {
				int repairCost = outputStack.getRepairCost();
				if (!repairSlotStack.isEmpty() && repairCost < repairSlotStack.getRepairCost()) {
					repairCost = repairSlotStack.getRepairCost();
				}
				if (k != enchantmentLevelCost) {
					repairCost = getNextCost(repairCost);
					outputStack.setRepairCost(repairCost);
				}
				EnchantmentHelper.set(enchantmentLevelMap, outputStack);
			}
			
			this.output.setStack(0, outputStack);
			this.sendContentUpdates();
		}
	}
	
	public boolean setNewItemName(String newItemName) {
		String string = sanitize(newItemName, AnvilScreenHandler.MAX_NAME_LENGTH);
		if (!string.equals(this.newItemName)) {
			this.newItemName = string;
			if (this.getSlot(2).hasStack()) {
				ItemStack itemStack = this.getSlot(2).getStack();
				if (Util.isBlank(string)) {
					itemStack.removeCustomName();
				} else {
					itemStack.setCustomName(Text.literal(string));
				}
			}
			
			this.updateResult();
			return true;
		} else {
			return false;
		}
	}
	
	private static String sanitize(String name, int maxLength) {
		String s = SharedConstants.stripInvalidChars(name);
		return s.length() > maxLength ? s.substring(0, maxLength) : s;
	}
	
	public boolean setNewItemLore(String newLoreString) {
		String string = sanitize(newLoreString, MAX_LORE_LENGTH);
		if (!string.equals(this.newLoreString)) {
			this.newLoreString = string;
			
			if (this.getSlot(2).hasStack()) {
				ItemStack itemStack = this.getSlot(2).getStack();
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
