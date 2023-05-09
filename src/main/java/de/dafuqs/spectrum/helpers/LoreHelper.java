package de.dafuqs.spectrum.helpers;

import com.google.gson.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LoreHelper {
	
	public static @NotNull List<Text> getLoreTextArrayFromString(@NotNull String string) {
		List<Text> loreText = new ArrayList<>();
		
		for (String split : string.split("\\\\n")) {
			loreText.add(0, Text.literal(split));
		}
		
		return loreText;
	}
	
	public static @NotNull String getStringFromLoreTextArray(@NotNull List<Text> lore) {
		if (lore.size() == 0) {
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
	
	public static void setLore(@NotNull ItemStack itemStack, @Nullable List<Text> lore) {
		NbtCompound nbtCompound = itemStack.getOrCreateSubNbt(ItemStack.DISPLAY_KEY);
		if (lore != null) {
			NbtList nbtList = new NbtList();
			
			for (Text loreText : lore) {
				NbtString nbtString = NbtString.of(Text.Serializer.toJson(loreText));
				nbtList.addElement(0, nbtString);
			}
			
			nbtCompound.put(ItemStack.LORE_KEY, nbtList);
		} else {
			nbtCompound.remove(ItemStack.LORE_KEY);
		}
	}
	
	public static void removeLore(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getSubNbt(ItemStack.DISPLAY_KEY);
		if (nbtCompound != null) {
			nbtCompound.remove(ItemStack.LORE_KEY);
			if (nbtCompound.isEmpty()) {
				itemStack.removeSubNbt(ItemStack.DISPLAY_KEY);
			}
		}
		
		if (itemStack.getNbt() != null && itemStack.getNbt().isEmpty()) {
			itemStack.setNbt(null);
		}
	}
	
	public static boolean hasLore(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getSubNbt(ItemStack.DISPLAY_KEY);
		return nbtCompound != null && nbtCompound.contains(ItemStack.LORE_KEY, 8);
	}
	
	public static @NotNull List<Text> getLoreList(@NotNull ItemStack itemStack) {
		List<Text> lore = new ArrayList<>();
		
		NbtCompound nbtCompound = itemStack.getSubNbt(ItemStack.DISPLAY_KEY);
		if (nbtCompound != null && nbtCompound.contains(ItemStack.LORE_KEY, 8)) {
			try {
				NbtList nbtList = nbtCompound.getList(ItemStack.LORE_KEY, 8);
				for (int i = 0; i < nbtList.size(); i++) {
					String s = nbtList.getString(i);
					Text text = Text.Serializer.fromJson(s);
					lore.add(text);
				}
			} catch (JsonParseException e) {
				nbtCompound.remove(ItemStack.LORE_KEY);
			}
		}
		
		return lore;
	}
	
	public static boolean equalsLore(List<Text> lore, ItemStack stack) {
		if (hasLore(stack)) {
			List<Text> loreList = getLoreList(stack);
			
			if (lore.size() != loreList.size()) {
				return false;
			}
			
			for (int i = 0; i < lore.size(); i++) {
				if (!lore.get(i).equals(loreList.get(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static void setLore(@NotNull ItemStack stack, @Nullable Text lore) {
		NbtCompound nbtCompound = stack.getOrCreateSubNbt(ItemStack.DISPLAY_KEY);
		if (lore != null) {
			NbtList nbtList = new NbtList();
			nbtList.addElement(0, NbtString.of(Text.Serializer.toJson(lore)));
			nbtCompound.put(ItemStack.LORE_KEY, nbtList);
		} else {
			nbtCompound.remove(ItemStack.LORE_KEY);
		}
	}
	
}
