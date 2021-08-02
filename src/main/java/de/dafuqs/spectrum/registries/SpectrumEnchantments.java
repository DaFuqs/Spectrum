package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enchantments.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumEnchantments {

    public static final Enchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Silk Touch, just for different blocks
    public static final Enchantment PEST_CONTROL = new PestControlEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND); // Kills silverfish when mining infested blocks
    public static final Enchantment AUTO_SMELT = new AutoSmeltEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // applies smelting recipe before dropping items after mining
    public static final Enchantment INVENTORY_INSERTION = new InventoryInsertionEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // don't drop items into the world, add to inv instead
    public static final Enchantment VOIDING = new VoidingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Voids all items mined
    public static final Enchantment EXUBERANCE = new ExuberanceEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlot.MAINHAND); // Drops more XP on kill
    public static final Enchantment TREASURE_HUNTER = new TreasureHunterEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND); // Drops mob heads

    public static void register() {
        if(SpectrumCommon.CONFIG.ResonanceEnchantmentEnabled) {
            Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "resonance"), RESONANCE);
        }

        if(SpectrumCommon.CONFIG.PestControlEnchantmentEnabled) {
            Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "pest_control"), PEST_CONTROL);
        }
        if(SpectrumCommon.CONFIG.AutoSmeltEnchantmentEnabled) {
            Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "autosmelt"), AUTO_SMELT);
        }
        if(SpectrumCommon.CONFIG.InventoryInsertionEnchantmentEnabled) {
            Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "inventory_insertion"), INVENTORY_INSERTION);
        }
        if(SpectrumCommon.CONFIG.VoidingEnchantmentEnabled) {
            Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "voiding"), VOIDING);
        }
        if(SpectrumCommon.CONFIG.ExuberanceEnchantmentEnabled) {
            Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "exuberance"), EXUBERANCE);
        }
        if(SpectrumCommon.CONFIG.TreasureHunterEnchantmentEnabled) {
            Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "treasure_hunter"), TREASURE_HUNTER);
        }
    }

}
