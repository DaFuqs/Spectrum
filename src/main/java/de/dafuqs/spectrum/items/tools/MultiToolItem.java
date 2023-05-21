package de.dafuqs.spectrum.items.tools;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MultiToolItem extends PickaxeItem {
	
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
	
	public static boolean canTill(NbtCompound nbt) {
		return true;
	}
	
}