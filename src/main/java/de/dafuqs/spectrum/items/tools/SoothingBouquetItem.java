package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.SleepAlteringItem;
import de.dafuqs.spectrum.api.render.SlotBackgroundEffectProvider;
import de.dafuqs.spectrum.cca.MiscPlayerDataComponent;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

            player.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.CALMING, 20 * 10, 4));
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
