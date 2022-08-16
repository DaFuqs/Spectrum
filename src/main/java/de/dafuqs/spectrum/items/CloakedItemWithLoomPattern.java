package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.items.conditional.CloakedItem;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CloakedItemWithLoomPattern extends CloakedItem implements LoomPatternProvider {
	
	private final RegistryEntry<BannerPattern> patternItemTag;
	
	public CloakedItemWithLoomPattern(Settings settings, Identifier cloakAdvancementIdentifier, Item cloakItem, RegistryEntry<BannerPattern> patternItemTag) {
		super(settings, cloakAdvancementIdentifier, cloakItem);
		this.patternItemTag = patternItemTag;
	}
	
	@Override
	public RegistryEntry<BannerPattern> getPattern() {
		return patternItemTag;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
	}
	
}
