package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.EggLayingWoolyPigEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;

@Environment(EnvType.CLIENT)
public class EggLayingWoolyPigEntityModelOld<T extends EggLayingWoolyPigEntity> extends QuadrupedEntityModel<T> {
    
    private float headPitchModifier;

    public EggLayingWoolyPigEntityModelOld(ModelPart root) {
        super(root, false, 8.0F, 4.0F, 2.0F, 2.0F, 24);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = QuadrupedEntityModel.getModelData(12, Dilation.NONE);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 8.0F), ModelTransform.pivot(0.0F, 6.0F, -8.0F));
        modelPartData.addChild("body", ModelPartBuilder.create().uv(28, 8).cuboid(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F), ModelTransform.of(0.0F, 5.0F, 2.0F, 1.5707964F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    public void animateModel(T entity, float f, float g, float h) {
        super.animateModel(entity, f, g, h);
        this.head.pivotY = 6.0F + entity.getNeckAngle(h) * 9.0F;
        this.headPitchModifier = entity.getHeadAngle(h);
    }

    public void setAngles(T entity, float f, float g, float h, float i, float j) {
        super.setAngles(entity, f, g, h, i, j);
        this.head.pitch = this.headPitchModifier;
    }
    
}