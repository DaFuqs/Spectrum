// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class lizard extends EntityModel<Entity> {
	private final ModelPart torso;
	private final ModelPart rightbackleg;
	private final ModelPart rightbackfrills_r1;
	private final ModelPart leftbackleg;
	private final ModelPart leftbackfrills_r1;
	private final ModelPart rightforeleg;
	private final ModelPart rightfrills_r1;
	private final ModelPart leftforeleg;
	private final ModelPart leftfrills_r1;
	private final ModelPart neartail;
	private final ModelPart midtail;
	private final ModelPart fartail;
	private final ModelPart head;
	private final ModelPart rightfrills_r2;
	private final ModelPart leftfrills_r2;
	private final ModelPart topfrills_r1;
	private final ModelPart jaw;
	public lizard(ModelPart root) {
		this.torso = root.getChild("torso");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData torso = modelPartData.addChild("torso", ModelPartBuilder.create().uv(23, 18).cuboid(-3.5F, -3.0F, -9.0F, 7.0F, 7.0F, 11.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(0.0F, -15.0F, -9.0F, 0.0F, 12.0F, 17.0F, new Dilation(0.0F))
		.uv(48, 13).cuboid(-3.0F, -3.0F, 2.0F, 6.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 19.0F, 0.0F));

		ModelPartData rightbackleg = torso.addChild("rightbackleg", ModelPartBuilder.create().uv(0, 65).cuboid(0.0F, -1.5F, -2.0F, 3.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(9, 2).cuboid(1.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, 2.5F, 6.0F));

		ModelPartData rightbackfrills_r1 = rightbackleg.addChild("rightbackfrills_r1", ModelPartBuilder.create().uv(0, 21).cuboid(0.9F, -3.5F, -3.9F, 0.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -1.0F, 2.0F, 0.0F, 0.3927F, 0.0F));

		ModelPartData leftbackleg = torso.addChild("leftbackleg", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -1.5F, -2.0F, 3.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(9, 0).cuboid(-3.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, 2.5F, 6.0F));

		ModelPartData leftbackfrills_r1 = leftbackleg.addChild("leftbackfrills_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-0.9F, -3.5F, -3.9F, 0.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -1.0F, 2.0F, 0.0F, -0.3927F, 0.0F));

		ModelPartData rightforeleg = torso.addChild("rightforeleg", ModelPartBuilder.create().uv(66, 9).cuboid(0.0F, -1.5F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(9, 3).cuboid(0.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 2.5F, -5.0F));

		ModelPartData rightfrills_r1 = rightforeleg.addChild("rightfrills_r1", ModelPartBuilder.create().uv(31, 60).cuboid(-0.75F, -4.0F, -1.0F, 0.0F, 6.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 0.5F, 0.0F, 0.0F, 0.3927F, 0.0F));

		ModelPartData leftforeleg = torso.addChild("leftforeleg", ModelPartBuilder.create().uv(31, 58).cuboid(-2.0F, -1.5F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
		.uv(9, 1).cuboid(-2.0F, 2.5F, -3.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 2.5F, -5.0F));

		ModelPartData leftfrills_r1 = leftforeleg.addChild("leftfrills_r1", ModelPartBuilder.create().uv(36, 37).cuboid(0.75F, -4.0F, -1.0F, 0.0F, 6.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.5F, 0.0F, 0.0F, -0.3927F, 0.0F));

		ModelPartData neartail = torso.addChild("neartail", ModelPartBuilder.create().uv(18, 44).cuboid(-2.5F, -4.0F, 0.0F, 5.0F, 6.0F, 8.0F, new Dilation(0.0F))
		.uv(44, 48).cuboid(0.0F, -15.0F, 0.0F, 0.0F, 11.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.5F, 8.0F));

		ModelPartData midtail = neartail.addChild("midtail", ModelPartBuilder.create().uv(45, 0).cuboid(-2.0F, -3.0F, 0.0F, 4.0F, 5.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 42).cuboid(0.0F, -14.0F, 0.0F, 0.0F, 11.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 8.0F));

		ModelPartData fartail = midtail.addChild("fartail", ModelPartBuilder.create().uv(56, 28).cuboid(-1.5F, -1.0F, 0.0F, 3.0F, 4.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-7.0F, 1.0F, 0.0F, 14.0F, 0.0F, 17.0F, new Dilation(0.0F))
		.uv(0, 23).cuboid(0.0F, -12.0F, 0.0F, 0.0F, 14.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 8.0F));

		ModelPartData head = torso.addChild("head", ModelPartBuilder.create().uv(11, 58).cuboid(-2.5F, -4.0F, -5.0F, 5.0F, 6.0F, 5.0F, new Dilation(0.0F))
		.uv(44, 44).cuboid(-2.0F, -4.0F, -14.0F, 4.0F, 3.0F, 9.0F, new Dilation(0.0F))
		.uv(26, 21).cuboid(0.0F, -11.0F, -15.0F, 0.0F, 8.0F, 15.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, -9.0F));

		ModelPartData rightfrills_r2 = head.addChild("rightfrills_r2", ModelPartBuilder.create().uv(61, 40).cuboid(-1.9733F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, -4.0F, -5.0F, -0.8281F, 0.001F, 1.5679F));

		ModelPartData leftfrills_r2 = head.addChild("leftfrills_r2", ModelPartBuilder.create().uv(45, 68).cuboid(-6.0267F, -9.9307F, 0.0F, 8.0F, 10.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, -4.0F, -5.0F, -0.8282F, 0.0F, -1.5615F));

		ModelPartData topfrills_r1 = head.addChild("topfrills_r1", ModelPartBuilder.create().uv(60, 56).cuboid(-4.5F, -11.75F, -0.15F, 9.0F, 12.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -5.0F, -0.8727F, 0.0F, 0.0F));

		ModelPartData jaw = head.addChild("jaw", ModelPartBuilder.create().uv(61, 0).cuboid(-1.5F, 0.0F, -5.5F, 3.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, -5.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		torso.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}