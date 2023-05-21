package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

import java.util.*;

public class EverpromiseRibbonItem extends Item {
	
	public EverpromiseRibbonItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if (stack.hasCustomName() && !(entity instanceof PlayerEntity)) {
			if (entity.isAlive()) {
				if (user.world.isClient) {
					World world = entity.getWorld();
					Random random = world.random;
					for (int i = 0; i < 7; ++i) {
						world.addParticle(ParticleTypes.HEART, entity.getParticleX(1.0), entity.getRandomBodyY() + 0.5, entity.getParticleZ(1.0),
								random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02);
					}
				} else {
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
