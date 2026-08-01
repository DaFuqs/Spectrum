package de.dafuqs.spectrum.items.ink;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;
import org.jspecify.annotations.*;

import java.util.*;

public class ArtistsPaletteItem extends SpectrumCurioItem implements InkStorageItem<TotalCappedElementalMixingInkStorage>, LoomPatternProvider, ExtendedItemBarProvider {
	
	private final long maxEnergyTotal;
	
	public ArtistsPaletteItem(Properties settings, long maxEnergyTotal) {
		super(settings, SpectrumCommon.locate("unlocks/trinkets/artists_palette"));
		this.maxEnergyTotal = maxEnergyTotal;
	}
	
	@Override
	public Drainability getDrainability() {
		return Drainability.PLAYER_ONLY;
	}
	
	@Override
	public TotalCappedElementalMixingInkStorage getEnergyStorage(ItemStack itemStack) {
		var storage = itemStack.get(SpectrumDataComponentTypes.INK_STORAGE);
		if (storage != null)
			return new TotalCappedElementalMixingInkStorage(storage.maxEnergyTotal(), storage.storedEnergy());
		return new TotalCappedElementalMixingInkStorage(this.maxEnergyTotal, Map.of());
	}
	
	// Omitting this would crash outside the dev env o.O
	@Override
	public ItemStack getDefaultInstance() {
		return super.getDefaultInstance();
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.pigment_palette.tooltip.target").withStyle(ChatFormatting.GRAY));
		tooltip.addAll(getEnergyStorage(stack).getTooltip());
		addBannerPatternProviderTooltip(tooltip);
	}
	
	@Override
	public ResourceKey<BannerPattern> getPattern() {
		return SpectrumBannerPatternKeys.PALETTE;
	}
	
	@Override
	public int barCount(ItemStack stack) {
		return 1;
	}
	
	@Override
	public ExtendedItemBarProvider.BarSignature getSignature(@Nullable Player player, ItemStack stack, int index) {
		var storage = getEnergyStorage(stack);
		var colors = new ArrayList<InkColor>();
		
		if (player == null || storage.isEmpty())
			return ExtendedItemBarProvider.PASS;
		
		var time = player.level().getGameTime() % 864000;
		
		for (InkColor inkColor : InkColors.elementals()) {
			if (storage.getEnergy(inkColor) > 0)
				colors.add(inkColor);
		}
		
		var progress = Support.getSensiblePercent(storage.getCurrentTotal(), storage.getMaxTotal(), 14);
		if (colors.size() == 1) {
			var color = colors.getFirst();
			return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, color.getColorInt() | 0xFF000000, 2, DEFAULT_BACKGROUND_COLOR);
		}
		
		if (colors.isEmpty()) return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, 0xFF000000, 2, DEFAULT_BACKGROUND_COLOR);
		
		var delta = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
		var curColor = colors.get((int) (time % (30L * colors.size()) / 30));
		var nextColor = colors.get((int) ((time % (30L * colors.size()) / 30 + 1) % colors.size()));
		
		
		var blendFactor = (((float) time + delta) % 30) / 30F;
		var blendedColor = SpectrumColorHelper.interpolate(curColor.getTextColorVec(), nextColor.getTextColorVec(), blendFactor);
		
		return new ExtendedItemBarProvider.BarSignature(1, 13, 14, progress, 1, blendedColor, 2, DEFAULT_BACKGROUND_COLOR);
	}
	
}
