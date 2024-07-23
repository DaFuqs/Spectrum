package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.api.render.SlotBackgroundEffectProvider;
import de.dafuqs.spectrum.items.conditional.CloakedItem;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NectardewBurgeonItem extends CloakedItem implements SlotBackgroundEffectProvider {

    private final Text lore;

    public NectardewBurgeonItem(Settings settings, String lore, Identifier cloakAdvancementIdentifier, Item cloakItem) {
        super(settings, cloakAdvancementIdentifier, cloakItem);
        this.lore = Text.translatable(lore).formatted(Formatting.GRAY);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 96;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(lore);
    }

    @Override
    public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
        return isVisibleTo(player) ? SlotEffect.PULSE : SlotEffect.NONE;
    }

    @Override
    public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
        return isVisibleTo(player) ? SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR : 0x0;
    }
}
