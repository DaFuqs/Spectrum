package de.dafuqs.spectrum.items.armor;

import de.dafuqs.spectrum.interfaces.PreEnchantedTooltip;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class SpectrumArmorItem extends ArmorItem implements PreEnchantedTooltip {

    public SpectrumArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        addPreEnchantedTooltip(tooltip, itemStack);
    }

}
