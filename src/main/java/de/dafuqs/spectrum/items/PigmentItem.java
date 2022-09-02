package de.dafuqs.spectrum.items;

import com.google.common.collect.Maps;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.conditional.CloakedItem;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PigmentItem extends CloakedItem implements LoomPatternProvider {
	
	private static final Map<DyeColor, PigmentItem> PIGMENTS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public PigmentItem(Settings settings, DyeColor color) {
		super(settings, SpectrumCommon.locate("craft_colored_sapling"), DyeItem.byColor(color));
		this.color = color;
		PIGMENTS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static PigmentItem byColor(DyeColor color) {
		return PIGMENTS.get(color);
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return SpectrumBannerPatterns.PIGMENT;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
