package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumBannerPatternTags {
	
	public static TagKey<BannerPattern> SPECTRUM_LOGO_TAG = of("pattern_item/logo");
	public static TagKey<BannerPattern> AMETHYST_CLUSTER_TAG = of("pattern_item/amethyst_cluster");
	public static TagKey<BannerPattern> AMETHYST_SHARD_TAG = of("pattern_item/amethyst_shard");
	
	private static TagKey<BannerPattern> of(String id) {
		return TagKey.of(Registry.BANNER_PATTERN_KEY, SpectrumCommon.locate(id));
	}
	
}
