package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpectrumEnchantmentHelper {
	
	/**
	 * @param stack                     the stack that receives the enchantments
	 * @param enchantment               the enchantment to add
	 * @param level                     the level of the enchantment
	 * @param forceEvenIfNotApplicable  add enchantments to the item, even if the item does usually not support that enchantment
	 * @param allowEnchantmentConflicts add enchantments to the item, even if there are enchantment conflicts
	 * @return the enchanted stack
	 */
	public static ItemStack addOrExchangeEnchantment(ItemStack stack, Enchantment enchantment, int level, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts) {
		// if not forced check if the stack already has enchantments
		// that conflict with the new one
		if (!allowEnchantmentConflicts && hasEnchantmentThatConflictsWith(stack, enchantment)) {
			return stack;
		}
		
		if (stack.isOf(Items.ENCHANTED_BOOK)) {
			// all fine, nothing more to check here. Enchant away!
		} else if (isEnchantableBook(stack)) {
			ItemStack enchantedBookStack = new ItemStack(Items.ENCHANTED_BOOK, stack.getCount());
			enchantedBookStack.setNbt(stack.getNbt());
			stack = enchantedBookStack;
		} else if (!forceEvenIfNotApplicable && !enchantment.isAcceptableItem(stack)) {
			if (stack.getItem() instanceof ExtendedEnchantable extendedEnchantable && extendedEnchantable.getAcceptedEnchantments().contains(enchantment)) {
				// ExtendedEnchantable explicitly states this enchantment is acceptable
			} else {
				// item can not be enchanted with this enchantment
				return stack;
			}
		}
		
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		String nbtString;
		if (stack.isOf(Items.ENCHANTED_BOOK) || stack.isOf(SpectrumItems.ENCHANTMENT_CANVAS)) {
			nbtString = EnchantedBookItem.STORED_ENCHANTMENTS_KEY;
		} else {
			nbtString = ItemStack.ENCHANTMENTS_KEY;
		}
		if (!nbtCompound.contains(nbtString, 9)) {
			nbtCompound.put(nbtString, new NbtList());
		}
		
		Identifier enchantmentIdentifier = Registries.ENCHANTMENT.getId(enchantment);
		NbtList nbtList = nbtCompound.getList(nbtString, 10);
		for (int i = 0; i < nbtList.size(); i++) {
			NbtCompound enchantmentCompound = nbtList.getCompound(i);
			if (enchantmentCompound.contains("id", NbtElement.STRING_TYPE) && Identifier.tryParse(enchantmentCompound.getString("id")).equals(enchantmentIdentifier)) {
				nbtList.remove(i);
				i--;
			}
		}
		
		nbtList.add(EnchantmentHelper.createNbt(EnchantmentHelper.getEnchantmentId(enchantment), (byte) level));
		nbtCompound.put(nbtString, nbtList);
		stack.setNbt(nbtCompound);
		
		return stack;
	}
	
	public static void setStoredEnchantments(Map<Enchantment, Integer> enchantments, ItemStack stack) {
		stack.removeSubNbt(EnchantedBookItem.STORED_ENCHANTMENTS_KEY); // clear existing enchantments
		for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : enchantments.entrySet()) {
			Enchantment enchantment = enchantmentIntegerEntry.getKey();
			if (enchantment != null) {
				EnchantedBookItem.addEnchantment(stack, new EnchantmentLevelEntry(enchantment, enchantmentIntegerEntry.getValue()));
			}
		}
	}
	
	/**
	 * Clears all enchantments of modifiedStack and replaces them with the ones present in enchantmentSourceStacks
	 * The enchantments are applied in order, so if there are conflicts, the first enchant in enchantmentSourceStacks gets chosen
	 *
	 * @param modifiedStack             the stack that receives the enchantments
	 * @param forceEvenIfNotApplicable  add enchantments to the item, even if the item does usually not support that enchantment
	 * @param allowEnchantmentConflicts add enchantments to the item, even if there are enchantment conflicts
	 * @param enchantmentSourceStacks   enchantmentSourceStacks the stacks that supply the enchantments
	 * @return the resulting stack
	 */
	public static ItemStack clearAndCombineEnchantments(ItemStack modifiedStack, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts, ItemStack... enchantmentSourceStacks) {
		EnchantmentHelper.set(Map.of(), modifiedStack); // clear current ones
		for (ItemStack stack : enchantmentSourceStacks) {
			for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.get(stack).entrySet()) {
				modifiedStack = SpectrumEnchantmentHelper.addOrExchangeEnchantment(modifiedStack, entry.getKey(), entry.getValue(), forceEvenIfNotApplicable, allowEnchantmentConflicts);
			}
		}
		return modifiedStack;
	}
	
	/**
	 * Checks if an itemstack can be used as the source to create an enchanted book
	 *
	 * @param stack The itemstack to check
	 * @return true if it is a book that can be turned into an enchanted book by enchanting
	 */
	public static boolean isEnchantableBook(@NotNull ItemStack stack) {
		return stack.isIn(SpectrumItemTags.ENCHANTABLE_BOOKS) || stack.getItem() instanceof BookItem;
	}
	
	public static boolean hasEnchantmentThatConflictsWith(ItemStack itemStack, Enchantment enchantment) {
		Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.get(itemStack);
		for (Enchantment existingEnchantment : existingEnchantments.keySet()) {
			if (!existingEnchantment.equals(enchantment)) {
				if (!existingEnchantment.canCombine(enchantment)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static Map<Enchantment, Integer> collectHighestEnchantments(List<ItemStack> itemStacks) {
		Map<Enchantment, Integer> enchantmentLevelMap = new LinkedHashMap<>();
		
		for (ItemStack itemStack : itemStacks) {
			Map<Enchantment, Integer> itemStackEnchantments = EnchantmentHelper.get(itemStack);
			for (Enchantment enchantment : itemStackEnchantments.keySet()) {
				int level = itemStackEnchantments.get(enchantment);
				if (enchantmentLevelMap.containsKey(enchantment)) {
					int storedLevel = enchantmentLevelMap.get(enchantment);
					if (level > storedLevel) {
						enchantmentLevelMap.put(enchantment, level);
					}
				} else {
					enchantmentLevelMap.put(enchantment, level);
				}
			}
		}
		
		return enchantmentLevelMap;
	}
	
	public static boolean canCombineAny(Map<Enchantment, Integer> existingEnchantments, Map<Enchantment, Integer> newEnchantments) {
		if (existingEnchantments.isEmpty()) {
			return true;
		} else {
			for (Enchantment existingEnchantment : existingEnchantments.keySet()) {
				for (Enchantment newEnchantment : newEnchantments.keySet()) {
					boolean canCurrentCombine = existingEnchantment.canCombine(newEnchantment);
					if (canCurrentCombine) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes the enchantments on a stack of items / enchanted book
	 *
	 * @param itemStack    the stack
	 * @param enchantments the enchantments to remove
	 * @return if >0 enchantments could be removed
	 */
	public static boolean removeEnchantments(@NotNull ItemStack itemStack, Enchantment... enchantments) {
		boolean anySuccess = false;
		for (Enchantment enchantment : enchantments) {
			anySuccess |= removeEnchantment(itemStack, enchantment);
		}
		return anySuccess;
	}
	
	public static boolean removeEnchantment(@NotNull ItemStack itemStack, Enchantment enchantment) {
		NbtCompound compound = itemStack.getNbt();
		if (compound == null) {
			return false;
		}
		
		NbtList enchantmentList;
		if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
			enchantmentList = compound.getList(EnchantedBookItem.STORED_ENCHANTMENTS_KEY, 10);
		} else {
			enchantmentList = compound.getList(ItemStack.ENCHANTMENTS_KEY, 10);
		}
		
		Identifier enchantmentIdentifier = Registries.ENCHANTMENT.getId(enchantment);
		boolean success = false;
		for (int i = 0; i < enchantmentList.size(); i++) {
			NbtCompound currentCompound = enchantmentList.getCompound(i);
			if (currentCompound.contains("id", NbtElement.STRING_TYPE) && Objects.equals(Identifier.tryParse(currentCompound.getString("id")), enchantmentIdentifier)) {
				enchantmentList.remove(i);
				success = true;
				break;
			}
		}
		
		if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
			compound.put(EnchantedBookItem.STORED_ENCHANTMENTS_KEY, enchantmentList);
		} else {
			compound.put(ItemStack.ENCHANTMENTS_KEY, enchantmentList);
			
		}
		itemStack.setNbt(compound);
		
		return success;
	}
	
	public static <T extends Item & ExtendedEnchantable> ItemStack getMaxEnchantedStack(@NotNull T item) {
		ItemStack itemStack = item.getDefaultStack();
		for (Enchantment enchantment : item.getAcceptedEnchantments()) {
			if (enchantment != null) {
				int maxLevel = enchantment.getMaxLevel();
				itemStack = addOrExchangeEnchantment(itemStack, enchantment, maxLevel, true, true);
			}
		}
		return itemStack;
	}
	
	public static int getUsableLevel(SpectrumEnchantment enchantment, ItemStack itemStack, Entity entity) {
		int level = EnchantmentHelper.getLevel(enchantment, itemStack);
		if (level > 0 && !enchantment.canEntityUse(entity)) {
			level = 0;
		}
		return level;
	}
	
}
