package de.dafuqs.spectrum.helpers;

import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LoreHelper {
	
	public static @NotNull List<Component> getLoreTextArrayFromString(@NotNull String string) {
		List<Component> loreText = new ArrayList<>();
		
		for (String split : string.split("\\\\n")) {
			loreText.addFirst(Component.literal(split));
		}
		
		return loreText;
	}
	
	public static @NotNull String getStringFromLoreTextArray(@NotNull List<Component> lore) {
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
	
	public static void setLore(@NotNull ItemStack itemStack, @Nullable List<Component> lore) {
		if (lore == null || lore.isEmpty()) {
			itemStack.remove(DataComponents.LORE);
		} else {
			ItemLore component = new ItemLore(lore);
			itemStack.set(DataComponents.LORE, component);
		}
	}
	
	public static void setLore(@NotNull ItemStack stack, @Nullable Component lore) {
		if (lore == null) {
			stack.remove(DataComponents.LORE);
		} else {
			ItemLore component = new ItemLore(List.of(lore));
			stack.set(DataComponents.LORE, component);
		}
	}
	
	public static void removeLore(@NotNull ItemStack itemStack) {
		itemStack.remove(DataComponents.LORE);
	}
	
	public static boolean hasLore(@NotNull ItemStack itemStack) {
		return itemStack.get(DataComponents.LORE) == null;
	}
	
	public static @NotNull List<Component> getLoreList(@NotNull ItemStack itemStack) {
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
