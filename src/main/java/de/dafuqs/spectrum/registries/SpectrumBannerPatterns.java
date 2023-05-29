package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.block.entity.*;
import net.minecraft.registry.tag.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.*;

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
	
	public static final TagKey<BannerPattern> SPECTRUM_LOGO_TAG = of("pattern_item/logo");
	public static final TagKey<BannerPattern> AMETHYST_CLUSTER_TAG = of("pattern_item/amethyst_cluster");
	public static final TagKey<BannerPattern> AMETHYST_SHARD_TAG = of("pattern_item/amethyst_shard");
	
	private static TagKey<BannerPattern> of(String id) {
		return TagKey.of(Registries.BANNER_PATTERN.getKey(), new Identifier(id));
	}
	
	private static RegistryEntry<BannerPattern> registerPattern(String id, String shortId) {
		BannerPattern pattern = Registry.register(Registries.BANNER_PATTERN, SpectrumCommon.locate(id), new BannerPattern(SpectrumCommon.MOD_ID + "_" + shortId));
		return Registries.BANNER_PATTERN.getEntry(Registries.BANNER_PATTERN.getKey(pattern).get()).get();
	}
	
	public static void register() {
		SPECTRUM_LOGO = registerPattern("logo", "l");
		AMETHYST_CLUSTER = registerPattern("amethyst_cluster", "acl");
		AMETHYST_SHARD = registerPattern("amethyst_shard", "as");
		CRAFTING_TABLET = registerPattern("crafting_tablet", "ct");
		FOUR_LEAF_CLOVER = registerPattern("four_leaf_clover", "flc");
		INK_FLASK = registerPattern("ink_flask", "if");
		KNOWLEDGE_GEM = registerPattern("knowledge_gem", "kg");
		MANUAL = registerPattern("guidebook", "man");
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
