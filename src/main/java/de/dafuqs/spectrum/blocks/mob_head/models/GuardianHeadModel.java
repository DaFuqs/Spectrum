package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class GuardianHeadModel extends SpectrumHeadModel {

    public GuardianHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        ModelPartData head = modelPartData.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create(),
                ModelTransform.NONE
        );
    
        head.addChild(
                "body",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-6.0F, -6.0F, -8.0F, 12.0F, 12.0F, 16.0F, new Dilation(0.0F))
                        .uv(0, 28).cuboid(-8.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new Dilation(0.0F))
                        .uv(0, 28).mirrored().cuboid(6.0F, -6.0F, -6.0F, 2.0F, 12.0F, 12.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(16, 40).cuboid(-6.0F, -8.0F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.0F))
                        .uv(16, 40).mirrored().cuboid(-6.0F, 6.0F, -6.0F, 12.0F, 2.0F, 12.0F, new Dilation(0.0F)).mirrored(false),
                ModelTransform.pivot(0.0F, -8.0F, 0.0F)
        );
    
        ModelPartData spikes = head.addChild("spikes", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -8.0F, 0.0F));
        spikes.addChild("spike_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
        spikes.addChild("spike_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));
        spikes.addChild("spike_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, 0.0F));
        spikes.addChild("spike_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3562F));
        spikes.addChild("spike_r5", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));
        spikes.addChild("spike_r6", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F));
        spikes.addChild("spike_r7", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
        spikes.addChild("spike_r8", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
    
        return TexturedModelData.of(modelData, 64, 64);
    }

}