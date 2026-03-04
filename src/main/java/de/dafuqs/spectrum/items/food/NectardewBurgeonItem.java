package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NectardewBurgeonItem extends CloakedItem implements SlotBackgroundEffectProvider {
	
	private final Component lore;
	
	public NectardewBurgeonItem(Properties settings, String lore, ResourceLocation cloakAdvancementIdentifier, Item cloakItem) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
		this.lore = Component.translatable(lore).withStyle(ChatFormatting.GRAY);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(lore);
	}
	
	@Override
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		return isVisibleTo(player) ? SlotEffect.PULSE : SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return isVisibleTo(player) ? SpectrumMobEffects.ETERNAL_SLUMBER_COLOR : 0x0;
	}
}
