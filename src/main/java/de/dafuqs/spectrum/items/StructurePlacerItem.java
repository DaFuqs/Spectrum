package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.registries.SpectrumMultiBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;

public class StructurePlacerItem extends Item {

    public StructurePlacerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        SpectrumMultiBlocks.testMultiblock.place(context.getWorld(), context.getBlockPos().up(), BlockRotation.NONE);
        return ActionResult.PASS;
    }


}
