package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CircletOfArroganceItem extends SpectrumTrinketItem {

    private final static int TRIGGER_EVERY_X_TICKS = 240;
    private final static int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 10;

    public CircletOfArroganceItem(Settings settings) {
        super(settings, SpectrumCommon.locate("unlocks/trinkets/circlet_of_arrogance"));
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
        giveEffect(entity);
        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            SpectrumS2CPacketSender.playDivinityAppliedEffects(serverPlayerEntity);
        }
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        World world = entity.getWorld();
        if (!world.isClient && world.getTime() % TRIGGER_EVERY_X_TICKS == 0) {
            giveEffect(entity);
        }
    }

    private static void giveEffect(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.DIVINITY, EFFECT_DURATION, 0, true, true));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("item.spectrum.circlet_of_arrogance.tooltip").formatted(Formatting.GRAY));
    }
}