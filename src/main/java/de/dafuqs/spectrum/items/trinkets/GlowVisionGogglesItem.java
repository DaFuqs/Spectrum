package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.world.*;

import java.util.*;

public class GlowVisionGogglesItem extends SpectrumTrinketItem {
	
	public static ItemStack COST = new ItemStack(Items.GLOW_INK_SAC, 1);
	
	public GlowVisionGogglesItem(Settings settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/glow_vision_goggles"));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		World world = entity.getWorld();
		if (world != null && world.getTimeOfDay() % 20 == 0) {
			if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
				int lightLevelAtPlayerPos = world.getLightLevel(serverPlayerEntity.getBlockPos());
				
				if (lightLevelAtPlayerPos < 7) {
					StatusEffectInstance nightVisionInstance = serverPlayerEntity.getStatusEffect(StatusEffects.NIGHT_VISION);
					if (nightVisionInstance == null || nightVisionInstance.getDuration() < 10 * 20) { // prevent "night vision running out" flashing
						// no / short night vision => search for glow ink sac and add night vision if found
						
						if (InventoryHelper.removeFromInventoryWithRemainders(serverPlayerEntity, COST)) {
							StatusEffectInstance newNightVisionInstance = new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * SpectrumCommon.CONFIG.GlowVisionGogglesDuration);
							serverPlayerEntity.addStatusEffect(newNightVisionInstance);
							world.playSoundFromEntity(null, serverPlayerEntity, SpectrumSoundEvents.ITEM_ARMOR_EQUIP_GLOW_VISION, SoundCategory.PLAYERS, 0.2F, 1.0F);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		tooltip.add(Text.translatable("item.spectrum.glow_vision_goggles.tooltip"));
	}
	
}
