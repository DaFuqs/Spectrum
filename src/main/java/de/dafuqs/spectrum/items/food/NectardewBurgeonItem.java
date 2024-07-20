package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.api.render.SlotBackgroundEffectProvider;
import de.dafuqs.spectrum.items.ItemWithTooltip;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NectardewBurgeonItem extends ItemWithTooltip implements SlotBackgroundEffectProvider {

    public NectardewBurgeonItem(Settings settings, String tooltip) {
        super(settings, tooltip);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 96;
    }

    @Override
    public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
        return SlotEffect.PULSE;
    }

    @Override
    public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
        return SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR;
    }
}
