package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.mob_head.EggLayingWoolyPigHeadModel;
import de.dafuqs.spectrum.entity.models.EggLayingWoolyPigEntityModel;
import de.dafuqs.spectrum.entity.models.EggLayingWoolyPigHatEntityModel;
import de.dafuqs.spectrum.entity.models.EggLayingWoolyPigWoolEntityModel;
import de.dafuqs.spectrum.render.armor.BedrockArmorModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class SpectrumModelLayers {

    /**
     * Animals
     */
    public static final EntityModelLayer WOOLY_PIG = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "main");
    public static final EntityModelLayer WOOLY_PIG_HAT = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "hat");
    public static final EntityModelLayer WOOLY_PIG_WOOL = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig"), "wool");
    
    /**
     * Blocks
     */
    public static final EntityModelLayer EGG_LAYING_WOOLY_PIG_HEAD = new EntityModelLayer(SpectrumCommon.locate("egg_laying_wooly_pig_head"), "main");
    
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
        EntityModelLayerRegistry.registerModelLayer(EGG_LAYING_WOOLY_PIG_HEAD, EggLayingWoolyPigHeadModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(FEET_BEDROCK_LAYER, () -> TexturedModelData.of(BedrockArmorModel.getModelData(), 128, 128));
        EntityModelLayerRegistry.registerModelLayer(MAIN_BEDROCK_LAYER, () -> TexturedModelData.of(BedrockArmorModel.getModelData(), 128, 128));
    }
}
