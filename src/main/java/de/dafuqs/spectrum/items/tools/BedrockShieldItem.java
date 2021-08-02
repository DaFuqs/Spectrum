package de.dafuqs.spectrum.items.tools;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class BedrockShieldItem extends ShieldItem {

    public BedrockShieldItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

}