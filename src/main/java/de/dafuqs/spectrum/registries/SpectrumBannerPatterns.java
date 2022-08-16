package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.tag.BannerPatternTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;

public class SpectrumBannerPatterns {
	
	public static RegistryEntry<BannerPattern> SPECTRUM_LOGO;
	public static RegistryEntry<BannerPattern> AMETHYST_CLUSTER;
	public static RegistryEntry<BannerPattern> AMETHYST_SHARD;
	public static RegistryEntry<BannerPattern> CRAFTING_TABLET;
	public static RegistryEntry<BannerPattern> FOUR_LEAF_CLOVER;
	public static RegistryEntry<BannerPattern> INK_FLASK;
	public static RegistryEntry<BannerPattern> KNOWLEDGE_GEM;
	public static RegistryEntry<BannerPattern> MANUAL;
	public static RegistryEntry<BannerPattern> MULTITOOL;
	public static RegistryEntry<BannerPattern> NEOLITH;
	public static RegistryEntry<BannerPattern> PALETTE;
	public static RegistryEntry<BannerPattern> PIGMENT;
	public static RegistryEntry<BannerPattern> RAW_AZURITE;
	public static RegistryEntry<BannerPattern> SHIMMER;
	public static RegistryEntry<BannerPattern> VEGETAL;
	public static RegistryEntry<BannerPattern> BEDROCK_DUST;
	public static RegistryEntry<BannerPattern> SHIMMERSTONE;
	public static RegistryEntry<BannerPattern> JADE_VINE;
	
	private static RegistryEntry<BannerPattern> registerPattern(String id, String shortId) {
		BannerPattern pattern = Registry.register(Registry.BANNER_PATTERN, new Identifier(SpectrumCommon.MOD_ID, id), new BannerPattern(SpectrumCommon.MOD_ID + "_" + shortId));
		return Registry.BANNER_PATTERN.getEntry(Registry.BANNER_PATTERN.getKey(pattern).get()).get();
	}
	
	public static void register() {
		SPECTRUM_LOGO = registerPattern("logo", "l");
		AMETHYST_CLUSTER = registerPattern("amethyst_cluster", "acl");
		AMETHYST_SHARD = registerPattern("amethyst_shard", "as");
		CRAFTING_TABLET = registerPattern("crafting_tablet", "ct");
		FOUR_LEAF_CLOVER = registerPattern("four_leaf_clover", "flc");
		INK_FLASK = registerPattern("ink_flask", "if");
		KNOWLEDGE_GEM = registerPattern("knowledge_gem", "kg");
		MANUAL = registerPattern("manual", "man");
		MULTITOOL = registerPattern("multitool", "mul");
		NEOLITH = registerPattern("neolith", "neo");
		PALETTE = registerPattern("palette", "pql");
		PIGMENT = registerPattern("pigment", "pg");
		RAW_AZURITE = registerPattern("raw_azurite", "raz");
		SHIMMER = registerPattern("shimmer", "sh");
		VEGETAL = registerPattern("vegetal", "ve");
		BEDROCK_DUST = registerPattern("bedrock_dust", "bd");
		SHIMMERSTONE = registerPattern("shimmerstone", "sp");
		JADE_VINE = registerPattern("jade_vine", "jv");
	}
	
}
