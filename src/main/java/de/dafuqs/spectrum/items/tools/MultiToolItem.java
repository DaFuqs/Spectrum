package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiToolItem extends MiningToolItem {
	
	public MultiToolItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(attackDamage, attackSpeed, material, SpectrumBlockTags.MULTITOOL_MINEABLE, settings);
	}
	
	/**
	 * Invoke shovel, axe and hoe right click actions (in this order)
	 * Like stripping logs, tilling grass paths etc.
	 * To get farmland it has to be converted to path and then tilled again
	 */
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult actionResult = ActionResult.PASS;
		
		if (canTill(context.getStack())) {
			actionResult = Items.IRON_SHOVEL.useOnBlock(context);
			if (!actionResult.isAccepted()) {
				actionResult = Items.IRON_AXE.useOnBlock(context);
				if (!actionResult.isAccepted()) {
					actionResult = Items.IRON_HOE.useOnBlock(context);
				}
			}
		}
		
		if (actionResult.isAccepted()) {
			return actionResult;
		} else {
			return super.useOnBlock(context);
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		if (canTill(stack)) {
			tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.right_click_actions").formatted(Formatting.GRAY));
		} else {
			tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.right_click_actions_disabled").formatted(Formatting.DARK_RED));
		}
	}
	
	public boolean canTill(ItemStack stack) {
		return true;
	}
	
}