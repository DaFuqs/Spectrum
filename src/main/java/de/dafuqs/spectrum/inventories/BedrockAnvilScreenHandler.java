package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.helpers.LoreHelper;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.LiteralText;
import net.minecraft.world.WorldEvents;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BedrockAnvilScreenHandler extends ScreenHandler {
	
	public static final int FIRST_INPUT_SLOT_INDEX = 0;
	public static final int SECOND_INPUT_SLOT_INDEX = 1;
	public static final int OUTPUT_SLOT_INDEX = 2;
	private static final int PLAYER_INVENTORY_START_INDEX = 3;
	private static final int field_30817 = 30;
	private static final int field_30818 = 30;
	private static final int PLAYER_INVENTORY_END_INDEX = 39;
	protected final CraftingResultInventory output = new CraftingResultInventory();
	protected final ScreenHandlerContext context;	protected final Inventory input = new SimpleInventory(2) {
		public void markDirty() {
			super.markDirty();
			onContentChanged(this);
		}
	};
	protected final PlayerEntity player;
	private final Property levelCost;
	private int repairItemCount;
	private String newItemName;
	private String newLoreString;
	public BedrockAnvilScreenHandler(int syncId, PlayerInventory inventory) {
		this(syncId, inventory, ScreenHandlerContext.EMPTY);
	}
	
	public BedrockAnvilScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(SpectrumScreenHandlerTypes.BEDROCK_ANVIL, syncId);
		this.levelCost = Property.create();
		this.addProperty(this.levelCost);
		
		this.context = context;
		this.player = playerInventory.player;
		this.addSlot(new Slot(this.input, FIRST_INPUT_SLOT_INDEX, 27, 47));
		this.addSlot(new Slot(this.input, SECOND_INPUT_SLOT_INDEX, 76, 47));
		this.addSlot(new Slot(this.output, OUTPUT_SLOT_INDEX, 134, 47) {
			public boolean canInsert(ItemStack stack) {
				return false;
			}
			
			public boolean canTakeItems(PlayerEntity playerEntity) {
				return canTakeOutput(playerEntity, this.hasStack());
			}
			
			public void onTakeItem(PlayerEntity player, ItemStack stack) {
				onTakeOutput(player, stack);
			}
		});
		
		int k;
		for (k = 0; k < 3; ++k) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 24 + 84 + k * 18));
			}
		}
		
		for (k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 24 + 142));
		}
	}
	
	public static int getNextCost(int cost) {
		return cost * 2 + 1;
	}
	
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
		if (inventory == this.input) {
			this.updateResult();
		}
	}
	
	public void close(PlayerEntity player) {
		super.close(player);
		this.context.run((world, pos) -> {
			this.dropInventory(player, this.input);
		});
	}
	
	public boolean canUse(PlayerEntity player) {
		return this.context.get((world, pos) -> this.canUse(world.getBlockState(pos)) && player.squaredDistanceTo((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D, true);
	}
	
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index == 2) {
				if (!this.insertItem(itemStack2, 3, 39, true)) {
					return ItemStack.EMPTY;
				}
				
				slot.onQuickTransfer(itemStack2, itemStack);
			} else if (index != 0 && index != 1) {
				if (index >= 3 && index < 39) {
					int i = 0;
					if (!this.insertItem(itemStack2, i, 2, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.insertItem(itemStack2, 3, 39, false)) {
				return ItemStack.EMPTY;
			}
			
			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
			
			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTakeItem(player, itemStack2);
		}
		
		return itemStack;
	}
	
	protected boolean canUse(BlockState state) {
		return state.isIn(BlockTags.ANVIL);
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
		this.context.run((world, pos) -> {
			world.syncWorldEvent(WorldEvents.ANVIL_USED, pos, 0);
		});
	}
	
	public void updateResult() {
		ItemStack itemStack = this.input.getStack(0);
		this.levelCost.set(0);
		int enchantmentLevelCost = 0;
		int repairLevelCost = 0;
		int k = 0;
		if (itemStack.isEmpty()) {
			this.output.setStack(0, ItemStack.EMPTY);
			this.levelCost.set(0);
		} else {
			ItemStack outputStack = itemStack.copy();
			ItemStack repairSlotStack = this.input.getStack(1);
			Map<Enchantment, Integer> enchantmentLevelMap = EnchantmentHelper.get(outputStack);
			repairLevelCost += itemStack.getRepairCost() + (repairSlotStack.isEmpty() ? 0 : repairSlotStack.getRepairCost());
			this.repairItemCount = 0;
			if (!repairSlotStack.isEmpty()) {
				boolean enchantedBookInRepairSlot = repairSlotStack.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(repairSlotStack).isEmpty();
				int o;
				int repairItemCount;
				int newOutputStackDamage;
				if (outputStack.isDamageable() && outputStack.getItem().canRepair(itemStack, repairSlotStack)) {
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
					if (!enchantedBookInRepairSlot && (!outputStack.isOf(repairSlotStack.getItem()) || !outputStack.isDamageable())) {
						this.output.setStack(0, ItemStack.EMPTY);
						this.levelCost.set(0);
						return;
					}
					
					if (outputStack.isDamageable() && !enchantedBookInRepairSlot) {
						o = itemStack.getMaxDamage() - itemStack.getDamage();
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
						boolean itemStackIsAcceptableForStack = enchantment.isAcceptableItem(itemStack);
						if (this.player.getAbilities().creativeMode || itemStack.isOf(Items.ENCHANTED_BOOK)) {
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
							if (newEnchantmentLevel > enchantment.getMaxLevel()) {
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
							if (itemStack.getCount() > 1) {
								enchantmentLevelCost = 40;
							}
						}
					}
				}
			}
			
			boolean renamed = false;
			if (StringUtils.isBlank(this.newItemName)) {
				if (itemStack.hasCustomName()) {
					outputStack.removeCustomName();
				}
			} else if (!this.newItemName.equals(itemStack.getName().getString())) {
				outputStack.setCustomName(new LiteralText(this.newItemName));
				renamed = true;
			}
			
			boolean loreChanged = false;
			if (StringUtils.isBlank(this.newLoreString)) {
				if (LoreHelper.hasLore(itemStack)) {
					LoreHelper.removeLore(outputStack);
					loreChanged = true;
				}
			} else {
				List<LiteralText> lore = LoreHelper.getLoreTextArrayFromString(this.newLoreString);
				if (!LoreHelper.equalsLore(lore, itemStack)) {
					LoreHelper.setLore(outputStack, lore);
					loreChanged = true;
				}
			}
			
			this.levelCost.set(repairLevelCost + enchantmentLevelCost);
			if (enchantmentLevelCost < 0) {
				outputStack = ItemStack.EMPTY;
			}
			
			if (!outputStack.isEmpty()) {
				int repairCost = outputStack.getRepairCost();
				if (!repairSlotStack.isEmpty() && repairCost < repairSlotStack.getRepairCost()) {
					repairCost = repairSlotStack.getRepairCost();
				}
				
				if ((this.levelCost.get() == 0 && (renamed || loreChanged))) {
					// renaming and lore is free
				} else if (k != enchantmentLevelCost) {
					repairCost = getNextCost(repairCost);
					outputStack.setRepairCost(repairCost);
				}
				
				EnchantmentHelper.set(enchantmentLevelMap, outputStack);
			}
			
			this.output.setStack(0, outputStack);
			this.sendContentUpdates();
		}
	}
	
	public void setNewItemName(String newItemName) {
		this.newItemName = newItemName;
		
		if (this.getSlot(2).hasStack()) {
			ItemStack itemStack = this.getSlot(2).getStack();
			if (StringUtils.isBlank(newItemName)) {
				itemStack.removeCustomName();
			} else {
				itemStack.setCustomName(new LiteralText(this.newItemName));
			}
		}
		this.updateResult();
	}
	
	public void setNewItemLore(String newLoreString) {
		this.newLoreString = newLoreString;
		
		if (this.getSlot(2).hasStack()) {
			ItemStack itemStack = this.getSlot(2).getStack();
			if (StringUtils.isBlank(newLoreString)) {
				LoreHelper.removeLore(itemStack);
			} else {
				LoreHelper.setLore(itemStack, LoreHelper.getLoreTextArrayFromString(this.newLoreString));
			}
		}
		this.updateResult();
	}
	
	public int getLevelCost() {
		return this.levelCost.get();
	}
	

	
}
