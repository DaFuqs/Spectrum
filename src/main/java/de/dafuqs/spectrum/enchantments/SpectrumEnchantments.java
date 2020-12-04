package de.dafuqs.spectrum.enchantments;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumEnchantments {

    public static final Enchantment SUPER_SILK = new ResonanceEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND);

    public static void register() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(SpectrumCommon.MOD_ID, "resonance"), SUPER_SILK);
    }

}
