package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import dev.emi.trinkets.api.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class CircletOfArroganceItem extends SpectrumTrinketItem {

    private static final int TRIGGER_EVERY_X_TICKS = 240;
    private static final int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 10;
	
	public CircletOfArroganceItem(Properties settings) {
        super(settings, SpectrumCommon.locate("unlocks/trinkets/circlet_of_arrogance"));
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
        giveEffect(entity);
		if (entity instanceof ServerPlayer serverPlayerEntity) {
			PlayDivinityAppliedEffectsPayload.playDivinityAppliedEffects(serverPlayerEntity);
        }
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
		Level world = entity.level();
		if (!world.isClientSide() && world.getGameTime() % TRIGGER_EVERY_X_TICKS == 0) {
            giveEffect(entity);
        }
    }

    private static void giveEffect(LivingEntity entity) {
		entity.addEffect(new MobEffectInstance(SpectrumStatusEffects.DIVINITY, EFFECT_DURATION, DivinityStatusEffect.CIRCLET_AMPLIFIER, true, true));
    }

    @Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.circlet_of_arrogance.tooltip").withStyle(ChatFormatting.GRAY));
    }
	
}
