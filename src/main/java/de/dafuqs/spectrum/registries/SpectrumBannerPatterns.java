package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpectrumBannerPatterns {
	
	public static LoomPattern SPECTRUM_LOGO;
	public static LoomPattern AMETHYST_CLUSTER;
	public static LoomPattern AMETHYST_SHARD;
	public static LoomPattern CRAFTING_TABLET;
	public static LoomPattern FOUR_LEAF_CLOVER;
	public static LoomPattern INK_FLASK;
	public static LoomPattern KNOWLEDGE_GEM;
	public static LoomPattern MANUAL;
	public static LoomPattern MULTITOOL;
	public static LoomPattern NEOLITH;
	public static LoomPattern PALETTE;
	public static LoomPattern PIGMENT;
	public static LoomPattern RAW_AZURITE;
	public static LoomPattern SHIMMER;
	public static LoomPattern VEGETAL;
	
	public static void register() {
		SPECTRUM_LOGO = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "logo"), new LoomPattern(true));
		AMETHYST_CLUSTER = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "amethyst_cluster"), new LoomPattern(true));
		AMETHYST_SHARD = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "amethyst_shard"), new LoomPattern(true));
		CRAFTING_TABLET = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "crafting_tablet"), new LoomPattern(true));
		FOUR_LEAF_CLOVER = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "four_leaf_clover"), new LoomPattern(true));
		INK_FLASK = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "ink_flask"), new LoomPattern(true));
		KNOWLEDGE_GEM = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "knowledge_gem"), new LoomPattern(true));
		MANUAL = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "manual"), new LoomPattern(true));
		MULTITOOL = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "multitool"), new LoomPattern(true));
		NEOLITH = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "neolith"), new LoomPattern(true));
		PALETTE = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "palette"), new LoomPattern(true));
		PIGMENT = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "pigment"), new LoomPattern(true));
		RAW_AZURITE = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "raw_azurite"), new LoomPattern(true));
		SHIMMER = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "shimmer"), new LoomPattern(true));
		VEGETAL = Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, "vegetal"), new LoomPattern(true));
	}
	
}
