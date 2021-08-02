package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.interfaces.PreEnchantedTooltip;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class BedrockCrossbowItem extends CrossbowItem implements PreEnchantedTooltip {

    public BedrockCrossbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }


    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        addPreEnchantedTooltip(tooltip, itemStack);
    }

}