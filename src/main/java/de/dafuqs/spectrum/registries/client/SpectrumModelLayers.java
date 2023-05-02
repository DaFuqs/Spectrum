package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.mob_head.*;
import de.dafuqs.spectrum.entity.models.*;
import de.dafuqs.spectrum.render.armor.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.util.*;

@Environment(EnvType.CLIENT)
public class SpectrumModelLayers {
	
	/**
	 * Entities
	 */
	public static final EntityModelLayer WOOLY_PIG = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "main");
	public static final EntityModelLayer WOOLY_PIG_HAT = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "hat");
	public static final EntityModelLayer WOOLY_PIG_WOOL = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "wool");
	
	public static final EntityModelLayer MONSTROSITY = new EntityModelLayer(SpectrumCommon.locate("monstrosity"), "main");
	
	/**
	 * Blocks
	 */
	public static final EntityModelLayer EGG_LAYING_WOOLY_PIG_HEAD = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig_head"), "main");
	public static final EntityModelLayer WARDEN_HEAD = new EntityModelLayer(SpectrumCommon.locate("warden_head"), "main");
	
	/**
	 * Armor
	 */
	public static final EntityModelLayer FEET_BEDROCK_LAYER = new EntityModelLayer(SpectrumCommon.locate("bedrock_armor"), "feet");
	public static final EntityModelLayer MAIN_BEDROCK_LAYER = new EntityModelLayer(SpectrumCommon.locate("bedrock_armor"), "main");
	public static final Identifier BEDROCK_ARMOR_LOCATION = SpectrumCommon.locate("textures/armor/bedrock_armor_main.png");
	
	public static void register() {
		EntityModelLayerRegistry.registerModelLayer(WOOLY_PIG, EggLayingWoolyPigEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WOOLY_PIG_HAT, EggLayingWoolyPigHatEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WOOLY_PIG_WOOL, EggLayingWoolyPigWoolEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MONSTROSITY, MonstrosityModel::getTexturedModelData);
		
		EntityModelLayerRegistry.registerModelLayer(EGG_LAYING_WOOLY_PIG_HEAD, EggLayingWoolyPigHeadModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WARDEN_HEAD, WardenHeadModel::getTexturedModelData);
		
		EntityModelLayerRegistry.registerModelLayer(FEET_BEDROCK_LAYER, () -> TexturedModelData.of(BedrockArmorModel.getModelData(), 128, 128));
		EntityModelLayerRegistry.registerModelLayer(MAIN_BEDROCK_LAYER, () -> TexturedModelData.of(BedrockArmorModel.getModelData(), 128, 128));
	}
}
