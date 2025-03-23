package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.fabric.api.registry.*;
import net.minecraft.util.*;

public class SpectrumCompostableBlocks {
	
	private static final float LOW = 0.3F;
	private static final float MEDIUM = 0.5F;
	private static final float HIGH = 0.65F;
	private static final float HIGHER = 0.85F;
	private static final float ALWAYS = 1.0F;
	
	public static void register() {
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.VEGETAL, ALWAYS);
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.BONE_ASH, ALWAYS);
		
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.CLOVER, MEDIUM);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.FOUR_LEAF_CLOVER, MEDIUM);

		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.BLOOD_ORCHID_PETAL, LOW);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.BLOOD_ORCHID, HIGH);
		
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.HIBERNATING_JADE_VINE_BULB, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.GERMINATED_JADE_VINE_BULB, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.JADE_VINE_PETALS, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.JADE_VINE_PETAL_BLOCK, ALWAYS);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.JADE_VINE_PETAL_CARPET, HIGH);

		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.VARIA_SPROUT, MEDIUM);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_SPRIG, LOW);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.WEEPING_GALA_LEAVES, LOW);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.JADEITE_LOTUS_BULB, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.JADEITE_LOTUS_FLOWER, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.JADEITE_PETALS, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.JADEITE_PETAL_BLOCK, ALWAYS);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.JADEITE_PETAL_CARPET, HIGH);
		
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SWEET_PEA, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.APRICOTTI, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.HUMMING_BELL, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.RESONANT_LILY, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.MOSS_BALL, MEDIUM);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.GIANT_MOSS_BALL, ALWAYS);

		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.NIGHTDEW_SPROUT, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.NECTARDEW_BURGEON, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.NEPHRITE_BLOSSOM_BULB, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.NEPHRITE_BLOSSOM_LEAVES, LOW);
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.FISSURE_PLUM, HIGH);
		
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.ALOE_LEAF, MEDIUM);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.BRISTLE_SPROUTS, MEDIUM);
		CompostingChanceRegistry.INSTANCE.add(SpectrumItems.SAWBLADE_HOLLY_BERRY, HIGH);

		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SNAPPING_IVY, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SMALL_RED_DRAGONJAG, LOW);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SMALL_YELLOW_DRAGONJAG, LOW);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SMALL_PINK_DRAGONJAG, LOW);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SMALL_PURPLE_DRAGONJAG, LOW);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SMALL_BLACK_DRAGONJAG, LOW);
		
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SLATE_NOXSHROOM, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.EBONY_NOXSHROOM, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.IVORY_NOXSHROOM, HIGH);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.CHESTNUT_NOXSHROOM, HIGH);
		
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SLATE_NOXCAP_BLOCK, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.SLATE_NOXCAP_GILLS, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.EBONY_NOXCAP_BLOCK, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.EBONY_NOXCAP_GILLS, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.IVORY_NOXCAP_BLOCK, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.IVORY_NOXCAP_GILLS, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.CHESTNUT_NOXCAP_BLOCK, HIGHER);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.CHESTNUT_NOXCAP_GILLS, HIGHER);
		
		for (DyeColor dyeColor : ColorHelper.VANILLA_DYE_COLORS) {
			CompostingChanceRegistry.INSTANCE.add(ColoredSaplingBlock.byColor(dyeColor), LOW);
			CompostingChanceRegistry.INSTANCE.add(ColoredLeavesBlock.byColor(dyeColor), LOW);
		}
	}
	
}
