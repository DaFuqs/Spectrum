package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class SpectrumContainers {
	
	public static Identifier PAINTBRUSH;
	public static Identifier PEDESTAL;
	public static Identifier CRAFTING_TABLET;
	public static Identifier RESTOCKING_CHEST;
	public static Identifier BEDROCK_ANVIL;
	public static Identifier PARTICLE_SPAWNER;
	public static Identifier COMPACTING_CHEST;
	public static Identifier SUCKING_CHEST;
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
		PAINTBRUSH = new Identifier(SpectrumCommon.MOD_ID, "paintbrush");
		PEDESTAL = new Identifier(SpectrumCommon.MOD_ID, "pedestal");
		CRAFTING_TABLET = new Identifier(SpectrumCommon.MOD_ID, "crafting_tablet");
		RESTOCKING_CHEST = new Identifier(SpectrumCommon.MOD_ID, "restocking_chest");
		BEDROCK_ANVIL = new Identifier(SpectrumCommon.MOD_ID, "bedrock_anvil");
		PARTICLE_SPAWNER = new Identifier(SpectrumCommon.MOD_ID, "particle_spawner");
		COMPACTING_CHEST = new Identifier(SpectrumCommon.MOD_ID, "compacting_chest");
		SUCKING_CHEST = new Identifier(SpectrumCommon.MOD_ID, "sucking_chest");
		POTION_WORKSHOP = new Identifier(SpectrumCommon.MOD_ID, "potion_workshop");
		COLOR_PICKER = new Identifier(SpectrumCommon.MOD_ID, "color_picker");
		CINDERHEARTH = new Identifier(SpectrumCommon.MOD_ID, "cinderhearth");
		
		GENERIC_TIER1_9x3 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier1_9x3");
		GENERIC_TIER2_9x3 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier2_9x3");
		GENERIC_TIER3_9x3 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier3_9x3");
		
		GENERIC_TIER1_9x6 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier1_9x6");
		GENERIC_TIER2_9x6 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier2_9x6");
		GENERIC_TIER3_9x6 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier3_9x6");
		
		GENERIC_TIER1_3X3 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier1_3x3");
		GENERIC_TIER2_3X3 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier2_3x3");
		GENERIC_TIER3_3X3 = new Identifier(SpectrumCommon.MOD_ID, "generic_tier3_3x3");
	}
	
}
