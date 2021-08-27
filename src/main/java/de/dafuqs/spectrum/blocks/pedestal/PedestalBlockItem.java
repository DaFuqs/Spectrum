package de.dafuqs.spectrum.blocks.pedestal;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class PedestalBlockItem extends BlockItem {

    private final PedestalBlock.PedestalVariant pedestalVariant;

    public PedestalBlockItem(Block block, Settings settings, PedestalBlock.PedestalVariant pedestalVariant) {
        super(block, settings);
        this.pedestalVariant = pedestalVariant;
    }

    public PedestalBlock.PedestalVariant getVariant() {
        return this.pedestalVariant;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);

        switch (pedestalVariant) {
            case BASIC_TOPAZ -> {
                tooltip.add(new TranslatableText("item.spectrum.pedestal.tooltip.basic_topaz"));
            }
            case BASIC_AMETHYST -> {
                tooltip.add(new TranslatableText("item.spectrum.pedestal.tooltip.basic_amethyst"));
            }
            case BASIC_CITRINE -> {
                tooltip.add(new TranslatableText("item.spectrum.pedestal.tooltip.basic_citrine"));
            }
            case ALL_BASIC -> {
                tooltip.add(new TranslatableText("item.spectrum.pedestal.tooltip.all_basic"));
            }
            case ONYX -> {
                tooltip.add(new TranslatableText("item.spectrum.pedestal.tooltip.onyx"));
            }
            case MOONSTONE -> {
                tooltip.add(new TranslatableText("item.spectrum.pedestal.tooltip.moonstone"));
            }
        }
    }

}
