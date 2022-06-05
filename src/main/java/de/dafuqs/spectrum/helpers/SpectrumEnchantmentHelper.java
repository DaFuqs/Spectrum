package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.BookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SpectrumEnchantmentHelper {
	
	
	public static ItemStack addOrExchangeEnchantment(ItemStack itemStack, Enchantment enchantment, int level, boolean forceEvenIfNotApplicable, boolean allowEnchantmentConflicts) {
		Identifier enchantmentIdentifier = Registry.ENCHANTMENT.getId(enchantment);
		
		if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
			// all fine, nothing more to check here. Enchant away!
		} else if (isEnchantableBook(itemStack)) {
			ItemStack enchantedBookStack = new ItemStack(Items.ENCHANTED_BOOK, itemStack.getCount());
			enchantedBookStack.setNbt(itemStack.getNbt());
			itemStack = enchantedBookStack;
		} else if (itemStack.getItem() instanceof EnchanterEnchantable enchanterEnchantable) {
			if (!enchanterEnchantable.canAcceptEnchantment(enchantment)) {
				return itemStack;
			}
		} else if (!forceEvenIfNotApplicable && !enchantment.isAcceptableItem(itemStack)) {
			// item can not be enchanted with this enchantment
			return itemStack;
		}
		
		// if not forced check if the stack already has enchantments
		// that conflict with the new one
		if (!allowEnchantmentConflicts && hasEnchantmentThatConflictsWith(itemStack, enchantment)) {
			return itemStack;
		}
		
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		String nbtString;
		if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
			nbtString = "StoredEnchantments";
		} else {
			nbtString = "Enchantments";
		}
		if (!nbtCompound.contains(nbtString, 9)) {
			nbtCompound.put(nbtString, new NbtList());
		}
		
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
		itemStack.setNbt(nbtCompound);
		
		return itemStack;
	}
	
	/**
	 * Checks if an itemstack can be used as the source to create an enchanted book
	 *
	 * @param itemStack The itemstack to check
	 * @return true if it is a book that can be turned into an enchanted book by enchanting
	 */
	public static boolean isEnchantableBook(@NotNull ItemStack itemStack) {
		return itemStack.isIn(SpectrumItemTags.ENCHANTABLE_BOOKS) || itemStack.getItem() instanceof BookItem;
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
		Map<Enchantment, Integer> enchantmentLevelMap = new HashMap<>();
		
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
	
	public static @NotNull ItemStack removeEnchantments(@NotNull ItemStack itemStack) {
		if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
			return new ItemStack(Items.BOOK);
		} else {
			itemStack.removeSubNbt("Enchantments");
			itemStack.removeSubNbt("StoredEnchantments");
			return itemStack;
		}
	}
	
	public static @NotNull ItemStack removeEnchantment(@NotNull ItemStack itemStack, Enchantment enchantment) {
		NbtCompound compound = itemStack.getNbt();
		if (compound == null) {
			return itemStack;
		}
		
		NbtList enchantmentList;
		if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
			enchantmentList = compound.getList("StoredEnchantments", 10);
		} else {
			enchantmentList = compound.getList("Enchantments", 10);
		}
		
		Identifier enchantmentIdentifier = Registry.ENCHANTMENT.getId(enchantment);
		for (int i = 0; i < enchantmentList.size(); i++) {
			NbtCompound currentCompound = enchantmentList.getCompound(i);
			if (currentCompound.contains("id", NbtElement.STRING_TYPE) && Objects.equals(Identifier.tryParse(currentCompound.getString("id")), enchantmentIdentifier)) {
				enchantmentList.remove(i);
				break;
			}
		}
		
		if (itemStack.isOf(Items.ENCHANTED_BOOK)) {
			compound.put("StoredEnchantments", enchantmentList);
		} else {
			compound.put("Enchantments", enchantmentList);
			
		}
		itemStack.setNbt(compound);
		
		return itemStack;
	}
	
	public static ItemStack getMaxEnchantedStack(@NotNull Item item, Enchantment... enchantments) {
		ItemStack itemStack = item.getDefaultStack();
		for (Enchantment enchantment : enchantments) {
			int maxLevel = enchantment.getMaxLevel();
			itemStack = addOrExchangeEnchantment(itemStack, enchantment, maxLevel, true, true);
		}
		return itemStack;
	}
	
}
