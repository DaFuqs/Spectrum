package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import top.theillusivec4.curios.api.*;

import java.util.*;

public class CircletOfArroganceItem extends SpectrumCurioItem {
	
	private static final int TRIGGER_EVERY_X_TICKS = 240;
	private static final int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 10;
	
	public CircletOfArroganceItem(Properties settings) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/circlet_of_arrogance"));
	}
	
	@Override
	public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
		super.onEquip(slotContext, prevStack, stack);
		LivingEntity entity = slotContext.entity();
		
		giveEffect(entity);
		if (entity instanceof ServerPlayer serverPlayerEntity) {
			PlayDivinityAppliedEffectsPayload.playDivinityAppliedEffects(serverPlayerEntity);
		}
	}
	
	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		super.curioTick(slotContext, stack);
		
		Level level = slotContext.entity().level();
		if (!level.isClientSide && level.getGameTime() % TRIGGER_EVERY_X_TICKS == 0) {
			giveEffect(slotContext.entity());
		}
	}
	
	private static void giveEffect(LivingEntity entity) {
		entity.addEffect(new MobEffectInstance(SpectrumMobEffects.DIVINITY, EFFECT_DURATION, DivinityStatusEffect.CIRCLET_AMPLIFIER, true, true));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.circlet_of_arrogance.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}
