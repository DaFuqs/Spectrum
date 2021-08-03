package de.dafuqs.spectrum.items.tools;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;

public class MultiToolItem extends SpectrumPickaxeItem {

    public MultiToolItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return true;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return miningSpeed;
    }

    /**
     * Invoke shovel, axe and hoe right click actions (in this order)
     * Like stripping logs, tilling grass paths etc.
     * To get tilled earth it has to converted to path and then tilled again
     */
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult actionResult = Items.IRON_SHOVEL.useOnBlock(context);
        if (actionResult == ActionResult.PASS) {
            actionResult = Items.IRON_AXE.useOnBlock(context);
            if(actionResult == ActionResult.PASS) {
                actionResult = Items.IRON_HOE.useOnBlock(context);
            }
        }
        return actionResult;
    }

}