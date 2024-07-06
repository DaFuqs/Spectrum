package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class ParrotHeadModel extends SpectrumHeadModel {

    public ParrotHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(2, 2).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F), ModelTransform.NONE);
        modelPartData2.addChild("head2", ModelPartBuilder.create().uv(10, 0).cuboid(-1.0F, -4.0F, -3.0F, 2.0F, 1.0F, 4.0F), ModelTransform.NONE);
        modelPartData2.addChild("beak1", ModelPartBuilder.create().uv(11, 7).cuboid(-0.5F, -3.0F, -2.0F, 1.0F, 2.0F, 1.0F), ModelTransform.NONE);
        modelPartData2.addChild("beak2", ModelPartBuilder.create().uv(16, 7).cuboid(-0.5F, -3.0F, -3.0F, 1.0F, 2.0F, 1.0F), ModelTransform.pivot(0.0F, -0.25F, 0.05F));
        modelPartData2.addChild("feather", ModelPartBuilder.create().uv(2, 18).cuboid(0.0F, -7.0F, -2.0F, 0.0F, 5.0F, 4.0F), ModelTransform.of(0.0F, -0.72F, -0.50F, -0.2214F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }

}