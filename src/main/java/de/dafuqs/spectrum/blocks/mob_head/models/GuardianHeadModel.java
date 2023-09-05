package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class GuardianHeadModel extends SpectrumHeadModel {

    public GuardianHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData ModelPartData = ModelData.getRoot();

        ModelPartData head = ModelPartData.addChild(EntityModelPartNames.HEAD, CubeListBuilder.create());

        ModelPartData spikes = head.addChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 0.0F));

        ModelPartData spike_r1 = spikes.addChild("spike_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F)
                .uv(0, 0).cuboid(-1.0F, 8.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        ModelPartData spike_r2 = spikes.addChild("spike_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F)
                .uv(0, 0).cuboid(-1.0F, 8.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData spike_r3 = spikes.addChild("spike_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 8.0F, -1.0F, 2.0F, 9.0F, 2.0F)
                .uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData spike_r4 = spikes.addChild("spike_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 8.0F, -1.0F, 2.0F, 9.0F, 2.0F)
                .uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData body = head.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -6.0F, -8.0F, 12.0F, 12.0F, 16.0F)
                .uv(0, 28).cuboid(-8.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F)
                .uv(0, 28).mirrored().cuboid(6.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F).mirrored(false)
                .uv(16, 40).cuboid(-6.0F, -8.0F, -6.0F, 12.0F, 2.0F, 12.0F)
                .uv(16, 40).mirrored().cuboid(-6.0F, 6.0F, -6.0F, 12.0F, 2.0F, 12.0F).mirrored(false), ModelTransform.NONE);

        return TexturedModelData.of(ModelData, 64, 64);
    }

}