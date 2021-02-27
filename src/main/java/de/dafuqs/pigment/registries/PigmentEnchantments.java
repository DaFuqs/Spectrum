package de.dafuqs.pigment.registries;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.enchantments.PestControlEnchantment;
import de.dafuqs.pigment.enchantments.ResonanceEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PigmentEnchantments {

    /*
        Silk Touch, just for different blocks
     */
    public static final Enchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);

    /*
        Kills silverfish from infested blocks
     */
    public static final Enchantment PEST_CONTROL = new PestControlEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);

    public static void register() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "resonance"), RESONANCE);
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "pest_control"), PEST_CONTROL);
    }

}
