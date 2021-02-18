package de.dafuqs.pigment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class DefaultEnchants {

    private static final HashMap<Item, DefaultEnchantment> defaultEnchantments = new HashMap<>();

    public static class DefaultEnchantment {
        public Enchantment enchantment;
        public int level;

        public DefaultEnchantment(Enchantment enchantment, int level) {
            this.enchantment = enchantment;
            this.level = level;
        }


    }

    public static void addDefaultEnchantment(Item item, Enchantment enchantment, int level) {
        defaultEnchantments.put(item, new DefaultEnchantment(enchantment, level));
    }

    public static ItemStack getEnchantedItemStack(Item item) {
        ItemStack itemStack = new ItemStack(item);
        if(defaultEnchantments.containsKey(item)) {
            DefaultEnchantment defaultEnchantment = defaultEnchantments.get(item);
            itemStack.addEnchantment(defaultEnchantment.enchantment, defaultEnchantment.level);
        }
        return itemStack;
    }

    public static boolean hasDefaultEnchants(Item item) {
        return defaultEnchantments.containsKey(item);
    }


    public static DefaultEnchantment getDefaultEnchantment(Item item) {
        return defaultEnchantments.getOrDefault(item, null);
    }

}
