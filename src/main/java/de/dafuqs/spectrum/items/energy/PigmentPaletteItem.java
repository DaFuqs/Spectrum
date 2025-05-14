package de.dafuqs.spectrum.items.energy;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PigmentPaletteItem extends SpectrumTrinketItem implements InkStorageItem<IndividualCappedInkStorage>, LoomPatternProvider, ExtendedItemBarProvider {
	
	private final long maxEnergyPerColor;
	
	public PigmentPaletteItem(Properties settings, long maxEnergyPerColor) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/pigment_palette"));
		this.maxEnergyPerColor = maxEnergyPerColor;
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.PLAYER_ONLY;
	}
	
	@Override
	public IndividualCappedInkStorage getEnergyStorage(ItemStack itemStack) {
		var storage = itemStack.get(SpectrumDataComponentTypes.INK_STORAGE);
		if (storage != null)
			return new IndividualCappedInkStorage(storage.maxPerColor(), storage.storedEnergy());
		return new IndividualCappedInkStorage(this.maxEnergyPerColor);
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultInstance() {
		return super.getDefaultInstance();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.pigment_palette.tooltip.target").withStyle(ChatFormatting.GRAY));
		getEnergyStorage(stack).addTooltip(tooltip);
		addBannerPatternProviderTooltip(tooltip);
	}
	
	@Override
	public ResourceKey<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.PALETTE;
	}
	
	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}
	
	@Override
	public ExtendedItemBarProvider.BarSignature getSignature(@Nullable Player player, @NotNull ItemStack stack, int index) {
		var storage = getEnergyStorage(stack);
		var colors = new ArrayList<InkColor>();
		
		if (player == null || storage.isEmpty())
			return ExtendedItemBarProvider.PASS;
		
		var time = player.level().getGameTime() % 864000;
		
		for (InkColor inkColor : SpectrumRegistries.INK_COLOR) {
			if (storage.getEnergy(inkColor) > 0)
				colors.add(inkColor);
		}
		
		var progress = Support.getSensiblePercent(storage.getCurrentTotal(), storage.getMaxTotal(), 14);
		if (colors.size() == 1) {
			var color = colors.getFirst();
			return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, color.getColorInt() | 0xFF000000, 2, DEFAULT_BACKGROUND_COLOR);
		}
		
		var delta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
		var curColor = colors.get((int) (time % (30L * colors.size()) / 30));
		var nextColor = colors.get((int) ((time % (30L * colors.size()) / 30 + 1) % colors.size()));
		
		var blendFactor = (((float) time + delta) % 30) / 30F;
		var blendedColor = SpectrumColorHelper.interpolate(curColor.getTextColorVec(), nextColor.getTextColorVec(), blendFactor);
		
		return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, blendedColor, 2, DEFAULT_BACKGROUND_COLOR);
	}
}
