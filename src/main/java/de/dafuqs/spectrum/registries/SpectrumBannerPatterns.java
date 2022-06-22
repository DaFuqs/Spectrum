package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatterns;
import net.minecraft.recipe.RecipeSerializer;
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
	public static LoomPattern BEDROCK_DUST;
	public static LoomPattern SHIMMERSTONE;
	public static LoomPattern JADE_VINE;
	
	private static LoomPattern register(String id, LoomPattern loomPattern) {
		return Registry.register(LoomPatterns.REGISTRY, new Identifier(SpectrumCommon.MOD_ID, id), loomPattern);
	}
	
	public static void register() {
		SPECTRUM_LOGO = register("logo", new LoomPattern(true));
		AMETHYST_CLUSTER = register("amethyst_cluster", new LoomPattern(true));
		AMETHYST_SHARD = register("amethyst_shard", new LoomPattern(true));
		CRAFTING_TABLET = register("crafting_tablet", new LoomPattern(true));
		FOUR_LEAF_CLOVER = register("four_leaf_clover", new LoomPattern(true));
		INK_FLASK = register("ink_flask", new LoomPattern(true));
		KNOWLEDGE_GEM = register("knowledge_gem", new LoomPattern(true));
		MANUAL = register("manual", new LoomPattern(true));
		MULTITOOL = register("multitool", new LoomPattern(true));
		NEOLITH = register("neolith", new LoomPattern(true));
		PALETTE = register("palette", new LoomPattern(true));
		PIGMENT = register("pigment", new LoomPattern(true));
		RAW_AZURITE = register("raw_azurite", new LoomPattern(true));
		SHIMMER = register("shimmer", new LoomPattern(true));
		VEGETAL = register("vegetal", new LoomPattern(true));
		BEDROCK_DUST = register("bedrock_dust", new LoomPattern(true));
		SHIMMERSTONE = register("shimmerstone", new LoomPattern(true));
		JADE_VINE = register("jade_vine", new LoomPattern(true));
	}
	
}
