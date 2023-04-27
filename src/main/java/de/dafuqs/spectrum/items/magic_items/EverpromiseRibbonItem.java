package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

public class EverpromiseRibbonItem extends Item {
	
	public EverpromiseRibbonItem(Settings settings) {
		super(settings);
	}
	
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (stack.hasCustomName() && !(entity instanceof PlayerEntity)) {
			if (!user.world.isClient && entity.isAlive()) {
				BondingRibbonComponent.attachBondingRibbon(entity);
				
				Text newName = stack.getName();
				if (newName instanceof MutableText mutableText) {
					newName = Text.literal(mutableText.getString() + " ❣").setStyle(mutableText.getStyle());
				}
				entity.setCustomName(newName);
				if (entity instanceof MobEntity mobEntity) {
					mobEntity.setPersistent();
					EntityHelper.addPlayerTrust(mobEntity, user);
				}
				
				stack.decrement(1);
			}
			
			return ActionResult.success(user.world.isClient);
		} else {
			return ActionResult.PASS;
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("item.spectrum.everpromise_ribbon.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.everpromise_ribbon.tooltip2").formatted(Formatting.GRAY));
	}
	
	
}
