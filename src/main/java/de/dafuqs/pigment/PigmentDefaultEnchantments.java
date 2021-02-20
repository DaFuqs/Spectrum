package de.dafuqs.pigment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class PigmentDefaultEnchantments {

    private static final HashMap<Item, DefaultEnchantment> defaultEnchantments = new HashMap<>();

    public static class DefaultEnchantment {
        public Enchantment enchantment;
        public int level;

        public DefaultEnchantment(Enchantment enchantment, int level) {
            this.enchantment = enchantment;
            this.level = level;
        }
    }

    static void registerDefaultEnchantments() {
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.LOOTING_FALCHION, Enchantments.LOOTING, 3);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.SILKER_PICKAXE, Enchantments.SILK_TOUCH, 1);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.FORTUNE_PICKAXE, Enchantments.FORTUNE, 3);
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
