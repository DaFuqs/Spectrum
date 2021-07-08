package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.enchantments.PigmentEnchantments;
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

    public static void registerDefaultEnchantments() {
        // early game tools
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.LOOTING_FALCHION, Enchantments.LOOTING, 3);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.SILKER_PICKAXE, Enchantments.SILK_TOUCH, 1);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.FORTUNE_PICKAXE, Enchantments.FORTUNE, 3);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.MULTITOOL, Enchantments.EFFICIENCY, 1);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.VOIDING_PICKAXE, PigmentEnchantments.VOIDING, 1);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.RESONANT_PICKAXE, PigmentEnchantments.RESONANCE, 1);

        // Bedrock tools
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_PICKAXE, Enchantments.SILK_TOUCH, 1);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_AXE, Enchantments.EFFICIENCY, 5);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_SHOVEL, Enchantments.EFFICIENCY, 5);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_SWORD, Enchantments.LOOTING, 3);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_BOW, Enchantments.POWER, 5);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_CROSSBOW, Enchantments.QUICK_CHARGE, 3);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_SHEARS, Enchantments.SILK_TOUCH, 5);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_HOE, Enchantments.FORTUNE, 3);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_FISHING_ROD, Enchantments.LUCK_OF_THE_SEA, 3);

        // bedrock armor
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_HELMET, Enchantments.PROJECTILE_PROTECTION, 4);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_CHESTPLATE, Enchantments.PROTECTION, 4);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_LEGGINGS, Enchantments.BLAST_PROTECTION, 4);
        PigmentDefaultEnchantments.addDefaultEnchantment(PigmentItems.BEDROCK_BOOTS, Enchantments.FIRE_PROTECTION, 4);


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
