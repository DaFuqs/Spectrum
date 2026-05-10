package de.dafuqs.spectrum.helpers;

import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class LoreHelper {
	
	public static List<Component> getLoreTextArrayFromString(String string) {
		List<Component> loreText = new ArrayList<>();
		
		for (String split : string.split("\\\\n")) {
			loreText.add(Component.literal(split));
		}
		
		return loreText;
	}
	
	public static String getStringFromLoreTextArray(List<Component> lore) {
		if (lore.isEmpty()) {
			return "";
		} else {
			StringBuilder loreString = new StringBuilder();
			for (int i = 0; i < lore.size(); i++) {
				loreString.append(lore.get(i).getString());
				if (i != lore.size() - 1) {
					loreString.append("\\n");
				}
			}
			return loreString.toString();
		}
	}
	
	public static void setLore(ItemStack itemStack, @Nullable List<Component> lore) {
		if (lore == null || lore.isEmpty()) {
			itemStack.remove(DataComponents.LORE);
		} else {
			ItemLore component = new ItemLore(lore);
			itemStack.set(DataComponents.LORE, component);
		}
	}
	
	public static void setLore(ItemStack stack, @Nullable Component lore) {
		if (lore == null) {
			stack.remove(DataComponents.LORE);
		} else {
			ItemLore component = new ItemLore(List.of(lore));
			stack.set(DataComponents.LORE, component);
		}
	}
	
	public static void removeLore(ItemStack itemStack) {
		itemStack.remove(DataComponents.LORE);
	}
	
	public static boolean hasLore(ItemStack itemStack) {
		return itemStack.get(DataComponents.LORE) == null;
	}
	
	public static List<Component> getLoreList(ItemStack itemStack) {
		ItemLore component = itemStack.get(DataComponents.LORE);
		if (component == null) {
			return new ArrayList<>();
		}
		return component.lines();
	}
	
	public static boolean equalsLore(List<Component> lore, ItemStack stack) {
		if (hasLore(stack)) {
			ItemLore component = stack.get(DataComponents.LORE);
			if (component == null) {
				return lore.isEmpty();
			}
			return component.lines().equals(lore);
		}
		return false;
	}
	
}
