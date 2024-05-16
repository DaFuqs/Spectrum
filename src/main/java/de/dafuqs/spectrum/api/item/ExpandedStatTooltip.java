package de.dafuqs.spectrum.api.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ExpandedStatTooltip {

    void expandTooltip(ItemStack stack, @Nullable PlayerEntity player, List<Text> tooltip, TooltipContext context);
}
