package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class EverpromiseRibbonItem extends Item implements PrioritizedEntityInteraction {
	
	public EverpromiseRibbonItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		Level world = user.level();
		if (!GenericClaimModsCompat.canInteract(world, entity, user)) {
			return InteractionResult.FAIL;
		}
		if (entity.getType().is(SpectrumEntityTypeTags.EVERPROMISE_RIBBON_BLACKLISTED)) {
			return InteractionResult.FAIL;
		}
		
		if (stack.get(DataComponents.CUSTOM_NAME) != null && !(entity instanceof Player)) {
			if (entity.isAlive()) {
				if (world.isClientSide) {
					Level entityWorld = entity.level();
					RandomSource random = entityWorld.random;
					for (int i = 0; i < 7; ++i) {
						world.addParticle(ParticleTypes.HEART, entity.getRandomX(1.0), entity.getRandomY() + 0.5, entity.getRandomZ(1.0),
								random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02);
					}
				} else {
					EverpromiseRibbonComponent.attachRibbon(entity);
					
					Component newName = stack.getHoverName();
					if (newName instanceof MutableComponent mutableText) {
						newName = Component.literal(mutableText.getString() + " ❣").setStyle(mutableText.getStyle());
					}
					entity.setCustomName(newName);
					if (entity instanceof Mob mobEntity) {
						mobEntity.setPersistenceRequired();
						EntityHelper.addPlayerTrust(mobEntity, user);
					}
				}
				
				if (!user.isCreative()) {
					stack.shrink(1);
				}
			}
			
			return InteractionResult.sidedSuccess(world.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.everpromise_ribbon.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.spectrum.everpromise_ribbon.tooltip2").withStyle(ChatFormatting.GRAY));
	}
	
	
}
