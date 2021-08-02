package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.interfaces.PreEnchantedTooltip;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.List;

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