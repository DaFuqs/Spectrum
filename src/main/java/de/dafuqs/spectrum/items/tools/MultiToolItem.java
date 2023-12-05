package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
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
	 * To get tilled earth it has to converted to path and then tilled again
	 */
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult actionResult = Items.IRON_SHOVEL.useOnBlock(context);
		if (actionResult == ActionResult.PASS) {
			actionResult = Items.IRON_AXE.useOnBlock(context);
			if (actionResult == ActionResult.PASS) {
				actionResult = Items.IRON_HOE.useOnBlock(context);
			}
		}
		return actionResult;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		
		if (canTill(stack.getNbt())) {
			tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.right_click_actions").formatted(Formatting.GRAY));
		} else {
			tooltip.add(Text.translatable("item.spectrum.workstaff.tooltip.right_click_actions_disabled").formatted(Formatting.DARK_RED));
		}
	}
	
	public boolean canTill(NbtCompound nbt) {
		return true;
	}
	
}