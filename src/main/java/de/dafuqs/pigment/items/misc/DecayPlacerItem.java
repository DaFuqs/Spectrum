package de.dafuqs.pigment.items.misc;

import de.dafuqs.pigment.registries.PigmentItems;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class DecayPlacerItem extends AliasedBlockItem {

    public DecayPlacerItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);

        Item item = itemStack.getItem();
        if(item.equals(PigmentItems.BOTTLE_OF_FADING)) {
            tooltip.add(new TranslatableText("item.pigment.bottle_of_fading.tooltip"));
        } else if(item.equals(PigmentItems.BOTTLE_OF_FAILING)) {
            tooltip.add(new TranslatableText("item.pigment.bottle_of_failing.tooltip"));
        } else if(item.equals(PigmentItems.BOTTLE_OF_RUIN)) {
            tooltip.add(new TranslatableText("item.pigment.bottle_of_ruin.tooltip"));
        }

    }

}
