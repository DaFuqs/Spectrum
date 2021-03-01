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
        if(item.equals(PigmentItems.DECAY_1_PLACER)) {
            tooltip.add(new TranslatableText("item.pigment.decay_1_placer.tooltip"));
        } else if(item.equals(PigmentItems.DECAY_2_PLACER)) {
            tooltip.add(new TranslatableText("item.pigment.decay_2_placer.tooltip"));
        } else if(item.equals(PigmentItems.DECAY_3_PLACER)) {
            tooltip.add(new TranslatableText("item.pigment.decay_3_placer.tooltip"));
        }

    }

}
