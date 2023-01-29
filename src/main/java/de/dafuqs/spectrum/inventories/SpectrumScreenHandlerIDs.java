package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.*;
import net.minecraft.util.*;

public class SpectrumScreenHandlerIDs {
	
	public static Identifier PAINTBRUSH;
	public static Identifier WORKSTAFF;
	
	public static Identifier PEDESTAL;
	public static Identifier CRAFTING_TABLET;
	public static Identifier RESTOCKING_CHEST;
	public static Identifier BEDROCK_ANVIL;
	public static Identifier PARTICLE_SPAWNER;
	public static Identifier COMPACTING_CHEST;
	public static Identifier BLACK_HOLE_CHEST;
	public static Identifier POTION_WORKSHOP;
	public static Identifier COLOR_PICKER;
	public static Identifier CINDERHEARTH;
	
	public static Identifier GENERIC_TIER1_9x3;
	public static Identifier GENERIC_TIER2_9x3;
	public static Identifier GENERIC_TIER3_9x3;
	
	public static Identifier GENERIC_TIER1_9x6;
	public static Identifier GENERIC_TIER2_9x6;
	public static Identifier GENERIC_TIER3_9x6;
	
	public static Identifier GENERIC_TIER1_3X3;
	public static Identifier GENERIC_TIER2_3X3;
	public static Identifier GENERIC_TIER3_3X3;
	
	public static void register() {
		PAINTBRUSH = SpectrumCommon.locate("paintbrush");
		WORKSTAFF = SpectrumCommon.locate("workstaff");
		
		PEDESTAL = SpectrumCommon.locate("pedestal");
		CRAFTING_TABLET = SpectrumCommon.locate("crafting_tablet");
		RESTOCKING_CHEST = SpectrumCommon.locate("restocking_chest");
		BEDROCK_ANVIL = SpectrumCommon.locate("bedrock_anvil");
		PARTICLE_SPAWNER = SpectrumCommon.locate("particle_spawner");
		COMPACTING_CHEST = SpectrumCommon.locate("compacting_chest");
		BLACK_HOLE_CHEST = SpectrumCommon.locate("sucking_chest");
		POTION_WORKSHOP = SpectrumCommon.locate("potion_workshop");
		COLOR_PICKER = SpectrumCommon.locate("color_picker");
		CINDERHEARTH = SpectrumCommon.locate("cinderhearth");
		
		GENERIC_TIER1_9x3 = SpectrumCommon.locate("generic_tier1_9x3");
		GENERIC_TIER2_9x3 = SpectrumCommon.locate("generic_tier2_9x3");
		GENERIC_TIER3_9x3 = SpectrumCommon.locate("generic_tier3_9x3");
		
		GENERIC_TIER1_9x6 = SpectrumCommon.locate("generic_tier1_9x6");
		GENERIC_TIER2_9x6 = SpectrumCommon.locate("generic_tier2_9x6");
		GENERIC_TIER3_9x6 = SpectrumCommon.locate("generic_tier3_9x6");
		
		GENERIC_TIER1_3X3 = SpectrumCommon.locate("generic_tier1_3x3");
		GENERIC_TIER2_3X3 = SpectrumCommon.locate("generic_tier2_3x3");
		GENERIC_TIER3_3X3 = SpectrumCommon.locate("generic_tier3_3x3");
	}
	
}
