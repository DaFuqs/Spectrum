package de.dafuqs.spectrum.helpers;

import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LoreHelper {
	
	public static @NotNull List<LiteralText> getLoreTextArrayFromString(@NotNull String string) {
		List<LiteralText> loreText = new ArrayList<>();
		
		for (String split : string.split("\\\\n")) {
			loreText.add(0, new LiteralText(split));
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
	
	public static void setLore(@NotNull ItemStack itemStack, @Nullable List<LiteralText> lore) {
		NbtCompound nbtCompound = itemStack.getOrCreateSubNbt("display");
		if (lore != null) {
			NbtList nbtList = new NbtList();
			
			for (Text loreText : lore) {
				NbtString nbtString = NbtString.of(Text.Serializer.toJson(loreText));
				nbtList.addElement(0, nbtString);
			}
			
			nbtCompound.put("Lore", nbtList);
		} else {
			nbtCompound.remove("Lore");
		}
	}
	
	public static void removeLore(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getSubNbt("display");
		if (nbtCompound != null) {
			nbtCompound.remove("Lore");
			if (nbtCompound.isEmpty()) {
				itemStack.removeSubNbt("display");
			}
		}
		
		if (itemStack.getNbt() != null && itemStack.getNbt().isEmpty()) {
			itemStack.setNbt(null);
		}
	}
	
	public static boolean hasLore(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getSubNbt("display");
		return nbtCompound != null && nbtCompound.contains("Lore", 8);
	}
	
	public static @NotNull List<Text> getLoreList(@NotNull ItemStack itemStack) {
		List<Text> lore = new ArrayList<>();
		
		NbtCompound nbtCompound = itemStack.getSubNbt("display");
		if (nbtCompound != null && nbtCompound.contains("Lore", 8)) {
			try {
				NbtList nbtList = nbtCompound.getList("Lore", 8);
				for (int i = 0; i < nbtList.size(); i++) {
					String s = nbtList.getString(i);
					Text text = Text.Serializer.fromJson(s);
					lore.add(text);
				}
			} catch (JsonParseException e) {
				nbtCompound.remove("Lore");
			}
		}
		
		return lore;
	}
	
	public static boolean equalsLore(List<LiteralText> lore, ItemStack stack) {
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
	
	public static void setLore(@NotNull ItemStack stack, @Nullable TranslatableText lore) {
		NbtCompound nbtCompound = stack.getOrCreateSubNbt("display");
		if (lore != null) {
			NbtList nbtList = new NbtList();
			nbtList.addElement(0, NbtString.of(Text.Serializer.toJson(lore)));
			nbtCompound.put("Lore", nbtList);
		} else {
			nbtCompound.remove("Lore");
		}
	}
	
}
