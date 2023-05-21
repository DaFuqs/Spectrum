package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.block.entity.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;

public class SpectrumBannerPatternTags {
	
	public static TagKey<BannerPattern> SPECTRUM_LOGO_TAG = of("pattern_item/logo");
	public static TagKey<BannerPattern> AMETHYST_CLUSTER_TAG = of("pattern_item/amethyst_cluster");
	public static TagKey<BannerPattern> AMETHYST_SHARD_TAG = of("pattern_item/amethyst_shard");
	
	private static TagKey<BannerPattern> of(String id) {
		return TagKey.of(Registry.BANNER_PATTERN_KEY, SpectrumCommon.locate(id));
	}
	
}
