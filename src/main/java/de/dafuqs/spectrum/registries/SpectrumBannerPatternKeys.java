package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.block.entity.*;

@SuppressWarnings("unused")
public class SpectrumBannerPatternKeys {
	
	public static ResourceKey<BannerPattern> SPECTRUM_LOGO = of("spectrum_logo");
	public static ResourceKey<BannerPattern> AMETHYST_CLUSTER = of("amethyst_cluster");
	public static ResourceKey<BannerPattern> AMETHYST_SHARD = of("amethyst_shard");
	public static ResourceKey<BannerPattern> CRAFTING_TABLET = of("crafting_tablet");
	public static ResourceKey<BannerPattern> FOUR_LEAF_CLOVER = of("four_leaf_clover");
	public static ResourceKey<BannerPattern> INK_FLASK = of("ink_flask");
	public static ResourceKey<BannerPattern> KNOWLEDGE_GEM = of("knowledge_gem");
	public static ResourceKey<BannerPattern> GUIDEBOOK = of("guidebook");
	public static ResourceKey<BannerPattern> MULTITOOL = of("multitool");
	public static ResourceKey<BannerPattern> NEOLITH = of("neolith");
	public static ResourceKey<BannerPattern> PALETTE = of("palette");
	public static ResourceKey<BannerPattern> PIGMENT = of("pigment");
	public static ResourceKey<BannerPattern> RAW_AZURITE = of("raw_azurite");
	public static ResourceKey<BannerPattern> SHIMMER = of("shimmer");
	public static ResourceKey<BannerPattern> VEGETAL = of("vegetal");
	public static ResourceKey<BannerPattern> BEDROCK_DUST = of("bedrock_dust");
	public static ResourceKey<BannerPattern> SHIMMERSTONE = of("shimmerstone");
	public static ResourceKey<BannerPattern> JADE_VINE = of("jade_vine");
	public static ResourceKey<BannerPattern> ASTROLOGER = of("astrologer");
	public static ResourceKey<BannerPattern> VELVET_ASTROLOGER = of("velvet_astrologer");
	public static ResourceKey<BannerPattern> POISONBLOOM = of("poisonbloom");
	public static ResourceKey<BannerPattern> DEEP_LIGHT = of("deep_light");
	
	private static ResourceKey<BannerPattern> of(String id) {
		return ResourceKey.create(Registries.BANNER_PATTERN, SpectrumCommon.locate(id));
	}

}
