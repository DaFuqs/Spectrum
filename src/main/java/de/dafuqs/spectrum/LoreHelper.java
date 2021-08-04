package de.dafuqs.spectrum;

import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoreHelper {

    public static ItemStack setLore(ItemStack itemStack, @Nullable Text lore) {
        NbtCompound nbtCompound = itemStack.getOrCreateSubTag("display");
        if (lore != null) {
            NbtList nbtList = new NbtList();

            List<String> splitLore = Arrays.asList(lore.asString().split("\n"));
            for(String loreString : splitLore) {
                NbtString nbtString = NbtString.of(Text.Serializer.toJson(new LiteralText(loreString)));
                nbtList.addElement(0, nbtString);
            }

            nbtCompound.put("Lore", nbtList);
        } else {
            nbtCompound.remove("Lore");
        }

        return itemStack;
    }

    public static void removeLore(ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getSubTag("display");
        if (nbtCompound != null) {
            nbtCompound.remove("Lore");
            if (nbtCompound.isEmpty()) {
                itemStack.removeSubTag("display");
            }
        }

        if (itemStack.getTag() != null && itemStack.getTag().isEmpty()) {
            itemStack.setTag(null);
        }
    }

    public static boolean hasLore(ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getSubTag("display");
        return nbtCompound != null && nbtCompound.contains("Lore", 9);
    }

    public static List<Text> getLoreList(ItemStack itemStack) {
        List<Text> lore = new ArrayList<>();

        NbtCompound nbtCompound = itemStack.getSubTag("display");
        if (nbtCompound != null && nbtCompound.contains("Lore", 9)) {
            try {
                NbtList nbtList = nbtCompound.getList("Lore", 9);
                for(int i = 0; i < nbtList.size(); i++) {
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

    public static boolean equalsLore(String lore, ItemStack stack) {
        if(hasLore(stack)) {
            List<Text> loreList = getLoreList(stack);
            List<String> splitLore = Arrays.asList(lore.split("\n"));

            if(splitLore.size() != loreList.size()) {
                return false;
            }

            for(int i = 0; i < splitLore.size(); i++) {
                if(!splitLore.get(i).equals(loreList.get(i).getString())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
