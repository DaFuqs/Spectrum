package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.QuickChargeEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(QuickChargeEnchantment.class)
public abstract class QuickChargeMixin extends Enchantment {
    protected QuickChargeMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack) || stack.getItem().equals(SpectrumItems.KNOWLEDGE_GEM);
    }
}
