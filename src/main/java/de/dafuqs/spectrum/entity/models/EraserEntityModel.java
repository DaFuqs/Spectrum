package de.dafuqs.spectrum.entity.models;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.*;

// ~ XOXO Azzyypaaras ~

//TODO - Clean this up. Blockbench autoexport is capital B bad.
public class EraserEntityModel extends EntityModel<EraserEntity> {


	private final ModelPart body;

	public EraserEntityModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData legs = body.addChild("legs", ModelPartBuilder.create().uv(0, 20).cuboid(-1.5F, -0.25F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.5F, 0.0F));

		ModelPartData rightfang = legs.addChild("rightfang", ModelPartBuilder.create().uv(7, 21).cuboid(-0.5F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.0F, 0.0F, -1.1989F, 1.1409F, -0.6197F));

		ModelPartData rightforefang = rightfang.addChild("rightforefang", ModelPartBuilder.create().uv(6, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.2618F));

		ModelPartData rightstrikeleg = legs.addChild("rightstrikeleg", ModelPartBuilder.create().uv(19, 10).cuboid(-0.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.0F, 0.0F, -0.7935F, 0.8029F, -0.941F));

		ModelPartData rightstrikeforeleg = rightstrikeleg.addChild("rightstrikeforeleg", ModelPartBuilder.create().uv(2, 13).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(4.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));

		ModelPartData rightfrontleg = legs.addChild("rightfrontleg", ModelPartBuilder.create().uv(19, 9).cuboid(-0.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.0F, 0.0F, -0.1896F, 0.1978F, -0.7436F));

		ModelPartData rightfrontforeleg = rightfrontleg.addChild("rightfrontforeleg", ModelPartBuilder.create().uv(0, 13).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(4.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.1745F));

		ModelPartData rightmidleg = legs.addChild("rightmidleg", ModelPartBuilder.create().uv(19, 14).cuboid(-0.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.0F, 0.5F, -0.2452F, -0.4063F, -0.8016F));

		ModelPartData rightmidforeleg = rightmidleg.addChild("rightmidforeleg", ModelPartBuilder.create().uv(0, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));

		ModelPartData cube_r1 = rightmidforeleg.addChild("cube_r1", ModelPartBuilder.create().uv(8, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 1.9635F));

		ModelPartData rightbackleg = legs.addChild("rightbackleg", ModelPartBuilder.create().uv(19, 13).cuboid(-0.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -0.25F, 0.5F, 0.7692F, -0.86F, -0.7762F));

		ModelPartData rightbackforeleg = rightbackleg.addChild("rightbackforeleg", ModelPartBuilder.create().uv(4, 8).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData leftfang = legs.addChild("leftfang", ModelPartBuilder.create().uv(6, 20).cuboid(-2.5F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, -1.1989F, -1.1409F, 0.6197F));

		ModelPartData leftforefang = leftfang.addChild("leftforefang", ModelPartBuilder.create().uv(4, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.2618F));

		ModelPartData leftstrikeleg = legs.addChild("leftstrikeleg", ModelPartBuilder.create().uv(18, 16).cuboid(-4.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, -0.7935F, -0.8029F, 0.941F));

		ModelPartData leftstrikeforeleg = leftstrikeleg.addChild("leftstrikeforeleg", ModelPartBuilder.create().uv(2, 8).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0873F));

		ModelPartData leftfrontleg = legs.addChild("leftfrontleg", ModelPartBuilder.create().uv(18, 15).cuboid(-4.5F, 0.25F, -0.5F, 5.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, -0.1896F, -0.1978F, 0.7436F));

		ModelPartData leftfrontforeleg = leftfrontleg.addChild("leftfrontforeleg", ModelPartBuilder.create().uv(0, 8).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.1745F));

		ModelPartData leftmidleg = legs.addChild("leftmidleg", ModelPartBuilder.create().uv(19, 12).cuboid(-3.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.0F, 0.5F, -0.2452F, 0.4063F, 0.8016F));

		ModelPartData leftmidforeleg = leftmidleg.addChild("leftmidforeleg", ModelPartBuilder.create().uv(14, 19).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, 0.25F, 0.0F, 0.0F, 0.0F, -0.0873F));

		ModelPartData cube_r2 = leftmidforeleg.addChild("cube_r2", ModelPartBuilder.create().uv(2, 21).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, -1.9635F));

		ModelPartData leftbackleg = legs.addChild("leftbackleg", ModelPartBuilder.create().uv(19, 11).cuboid(-3.5F, 0.25F, -0.5F, 4.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -0.25F, 0.5F, 0.7692F, 0.86F, 0.7762F));

		ModelPartData leftbackforeleg = leftbackleg.addChild("leftbackforeleg", ModelPartBuilder.create().uv(6, 3).cuboid(0.0F, 0.0F, -0.5F, 0.0F, 5.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, 0.25F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData torax = body.addChild("torax", ModelPartBuilder.create().uv(16, 17).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.25F, 0.25F, 0.4363F, 0.0F, 0.0F));

		ModelPartData backnettles_r1 = torax.addChild("backnettles_r1", ModelPartBuilder.create().uv(0, 9).cuboid(-3.5F, 1.3505F, 1.125F, 7.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.25F, 0.0F, 0.48F, 0.0F, 0.0F));

		ModelPartData midnettles_r1 = torax.addChild("midnettles_r1", ModelPartBuilder.create().uv(-5, 27).cuboid(-3.5F, 0.8505F, 0.125F, 7.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.25F, 0.0F, 0.6981F, 0.0F, 0.0F));

		ModelPartData forenettles_r1 = torax.addChild("forenettles_r1", ModelPartBuilder.create().uv(0, 15).cuboid(-3.5F, -0.1495F, -0.625F, 7.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.25F, 0.0F, 0.7854F, 0.0F, 0.0F));

		ModelPartData stingers = torax.addChild("stingers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 3.0F));

		ModelPartData stingerplane_r1 = stingers.addChild("stingerplane_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, 0.0F, -1.0F, 7.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.25F, 0.0F));

		ModelPartData nettles = head.addChild("nettles", ModelPartBuilder.create().uv(0, 4).cuboid(-1.5F, -4.0F, 0.0F, 3.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.9F, -1.0F, -0.4363F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
	@Override
	public void setAngles(EraserEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}