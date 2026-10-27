package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.data.worldgen.features.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.block.grower.*;

import java.util.*;

@SuppressWarnings("unused")
public class SpectrumTreeGrowers {
	
	public static final TreeGrower WHITE_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/white"));
	public static final TreeGrower ORANGE_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/orange"));
	public static final TreeGrower MAGENTA_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/magenta"));
	public static final TreeGrower LIGHT_BLUE_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/light_blue"));
	public static final TreeGrower YELLOW_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/yellow"));
	public static final TreeGrower LIME_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/lime"));
	public static final TreeGrower PINK_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/pink"));
	public static final TreeGrower GRAY_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/gray"));
	public static final TreeGrower LIGHT_GRAY_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/light_gray"));
	public static final TreeGrower CYAN_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/cyan"));
	public static final TreeGrower PURPLE_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/purple"));
	public static final TreeGrower BLUE_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/blue"));
	public static final TreeGrower BROWN_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/brown"));
	public static final TreeGrower GREEN_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/green"));
	public static final TreeGrower RED_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/red"));
	public static final TreeGrower BLACK_COLORED_SAPLING_GENERATOR = regular(SpectrumCommon.locate("colored_trees_sapling/black"));
	
	public static final TreeGrower TALL_WHITE_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/white"));
	public static final TreeGrower TALL_ORANGE_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/orange"));
	public static final TreeGrower TALL_MAGENTA_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/magenta"));
	public static final TreeGrower TALL_LIGHT_BLUE_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/light_blue"));
	public static final TreeGrower TALL_YELLOW_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/yellow"));
	public static final TreeGrower TALL_LIME_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/lime"));
	public static final TreeGrower TALL_PINK_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/pink"));
	public static final TreeGrower TALL_GRAY_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/gray"));
	public static final TreeGrower TALL_LIGHT_GRAY_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/light_gray"));
	public static final TreeGrower TALL_CYAN_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/cyan"));
	public static final TreeGrower TALL_PURPLE_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/purple"));
	public static final TreeGrower TALL_BLUE_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/blue"));
	public static final TreeGrower TALL_BROWN_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/brown"));
	public static final TreeGrower TALL_GREEN_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/green"));
	public static final TreeGrower TALL_RED_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/red"));
	public static final TreeGrower TALL_BLACK_COLORED_SAPLING_GENERATOR = tall(SpectrumCommon.locate("colored_trees_sapling_tall/black"));
	
	public static final TreeGrower WEEPING_GALA_SAPLING_GENERATOR = regular(SpectrumCommon.locate("weeping_gala"));
	
	public static TreeGrower regular(ResourceLocation id) {
		return new TreeGrower(id.getPath(), Optional.empty(), Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, id)), Optional.empty());
	}
	
	public static TreeGrower tall(ResourceLocation id) {
		 return new TreeGrower("tall_" + id.getPath(), Optional.empty(), Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, id)), Optional.empty());
	}
	
}
