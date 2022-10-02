package de.dafuqs.spectrum.items.conditional;

import de.dafuqs.spectrum.items.SpectrumBannerPatternItem;
import de.dafuqs.spectrum.items.conditional.CloakedItem;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CloakedItemWithLoomPattern extends CloakedItem implements LoomPatternProvider {
	
	protected final LoomPattern loomPattern;
	
	public CloakedItemWithLoomPattern(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem, LoomPattern loomPattern) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
		this.loomPattern = loomPattern;
	}
	
	@Override
	public LoomPattern getPattern() {
		return loomPattern;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
