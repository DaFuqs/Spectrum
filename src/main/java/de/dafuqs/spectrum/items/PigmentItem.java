package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.items.conditional.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.*;

import java.util.*;

public class PigmentItem extends CloakedItem implements LoomPatternProvider {
	
	private static final Object2ObjectArrayMap<InkColor, PigmentItem> PIGMENTS = new Object2ObjectArrayMap<>();
	protected final InkColor color;
	
	public PigmentItem(Properties settings, InkColor color, Item cloakItem) {
		super(settings, SpectrumCommon.locate("craft_colored_sapling"), cloakItem);
		this.color = color;
		PIGMENTS.put(color, this);
	}
	
	public InkColor getInkColor() {
		return this.color;
	}
	
	public static PigmentItem byColor(InkColor inkColor) {
		return PIGMENTS.get(inkColor);
	}
	
	@Override
	public ResourceKey<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.PIGMENT;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		addBannerPatternProviderTooltip(tooltip);
	}
	
}
