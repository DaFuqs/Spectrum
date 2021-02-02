package de.dafuqs.pigment.enchantments;

import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PigmentEnchantments {

    public static final Enchantment RESONANCE = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);

    public static void register() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(PigmentCommon.MOD_ID, "resonance"), RESONANCE);
    }

}
