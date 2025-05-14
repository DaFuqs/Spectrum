package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.block.grower.*;

import java.util.*;

@SuppressWarnings("unused")
public class SpectrumSaplingGenerators {
	
	public static final ResourceLocation WHITE_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/white");
	public static final TreeGrower WHITE_COLORED_SAPLING_GENERATOR = registerRegular(WHITE_COLORED_SAPLING_ID);
	public static final ResourceLocation ORANGE_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/orange");
	public static final TreeGrower ORANGE_COLORED_SAPLING_GENERATOR = registerRegular(ORANGE_COLORED_SAPLING_ID);
	public static final ResourceLocation MAGENTA_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/magenta");
	public static final TreeGrower MAGENTA_COLORED_SAPLING_GENERATOR = registerRegular(MAGENTA_COLORED_SAPLING_ID);
	public static final ResourceLocation LIGHT_BLUE_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/light_blue");
	public static final TreeGrower LIGHT_BLUE_COLORED_SAPLING_GENERATOR = registerRegular(LIGHT_BLUE_COLORED_SAPLING_ID);
	public static final ResourceLocation YELLOW_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/yellow");
	public static final TreeGrower YELLOW_COLORED_SAPLING_GENERATOR = registerRegular(YELLOW_COLORED_SAPLING_ID);
	public static final ResourceLocation LIME_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/lime");
	public static final TreeGrower LIME_COLORED_SAPLING_GENERATOR = registerRegular(LIME_COLORED_SAPLING_ID);
	public static final ResourceLocation PINK_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/pink");
	public static final TreeGrower PINK_COLORED_SAPLING_GENERATOR = registerRegular(PINK_COLORED_SAPLING_ID);
	public static final ResourceLocation GRAY_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/gray");
	public static final TreeGrower GRAY_COLORED_SAPLING_GENERATOR = registerRegular(GRAY_COLORED_SAPLING_ID);
	public static final ResourceLocation LIGHT_GRAY_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/light_gray");
	public static final TreeGrower LIGHT_GRAY_COLORED_SAPLING_GENERATOR = registerRegular(LIGHT_GRAY_COLORED_SAPLING_ID);
	public static final ResourceLocation CYAN_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/cyan");
	public static final TreeGrower CYAN_COLORED_SAPLING_GENERATOR = registerRegular(CYAN_COLORED_SAPLING_ID);
	public static final ResourceLocation PURPLE_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/purple");
	public static final TreeGrower PURPLE_COLORED_SAPLING_GENERATOR = registerRegular(PURPLE_COLORED_SAPLING_ID);
	public static final ResourceLocation BLUE_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/blue");
	public static final TreeGrower BLUE_COLORED_SAPLING_GENERATOR = registerRegular(BLUE_COLORED_SAPLING_ID);
	public static final ResourceLocation BROWN_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/brown");
	public static final TreeGrower BROWN_COLORED_SAPLING_GENERATOR = registerRegular(BROWN_COLORED_SAPLING_ID);
	public static final ResourceLocation GREEN_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/green");
	public static final TreeGrower GREEN_COLORED_SAPLING_GENERATOR = registerRegular(GREEN_COLORED_SAPLING_ID);
	public static final ResourceLocation RED_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/red");
	public static final TreeGrower RED_COLORED_SAPLING_GENERATOR = registerRegular(RED_COLORED_SAPLING_ID);
	public static final ResourceLocation BLACK_COLORED_SAPLING_ID = SpectrumCommon.locate("colored_trees/black");
	public static final TreeGrower BLACK_COLORED_SAPLING_GENERATOR = registerRegular(BLACK_COLORED_SAPLING_ID);
	
	public static final ResourceLocation WEEPING_GALA_SAPLING_ID = SpectrumCommon.locate("weeping_gala");
	public static final TreeGrower WEEPING_GALA_SAPLING_GENERATOR = registerRegular(WEEPING_GALA_SAPLING_ID);
	
	public static TreeGrower registerRegular(ResourceLocation id) {
		return new TreeGrower(id.getPath(), Optional.empty(), Optional.of(ResourceKey.create(Registries.CONFIGURED_FEATURE, id)), Optional.empty());
    }

}
