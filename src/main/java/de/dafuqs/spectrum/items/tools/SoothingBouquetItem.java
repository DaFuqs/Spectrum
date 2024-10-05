package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SoothingBouquetItem extends Item implements SleepAlteringItem, SlotBackgroundEffectProvider {

    private static final MutableText TOOLTIP = Text.translatable("item.spectrum.soothing_bouquet.tooltip");

    public SoothingBouquetItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(TOOLTIP.formatted(Formatting.GRAY));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            var component = MiscPlayerDataComponent.get(player);

            component.setSleepTimers(50, 20 * 6, 0);
            component.setLastSleepItem(this);
			
			player.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.CALMING, 20 * 10, 4)); // TODO: this should probably be a food component, so it shows up as tooltip
            player.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.SOMNOLENCE, 20 * 10, 4));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 50, 3));
        }
        else {
            user.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.SOMNOLENCE, 20 * 15, 2));
            user.sleep(user.getBlockPos());
        }

        world.playSoundFromEntity(null, user, SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundCategory.PLAYERS, 1F, 1.2F);
        return stack;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ENTITY_SNIFFER_SCENTING;
    }

    @Override
    public void applyPenalties(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 15));
    }

    @Override
    public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
        return SlotEffect.BORDER_FADE;
    }

    @Override
    public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
        return SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR;
    }
}
