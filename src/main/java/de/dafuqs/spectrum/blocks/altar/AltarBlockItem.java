package de.dafuqs.spectrum.blocks.altar;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class AltarBlockItem extends BlockItem {

    private AltarBlock.AltarTier altarTier;

    public AltarBlockItem(Block block, Settings settings, AltarBlock.AltarTier altarTier) {
        super(block, settings);
        this.altarTier = altarTier;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);

        switch (altarTier) {
            case TIER2 -> {
                tooltip.add(new TranslatableText("item.spectrum.altar.tooltip.tier2"));
            }
            case TIER3 -> {
                tooltip.add(new TranslatableText("item.spectrum.altar.tooltip.tier3"));
            }
        }
    }

    public AltarBlock.AltarTier getAltarTier() {
        return this.altarTier;
    }

}
