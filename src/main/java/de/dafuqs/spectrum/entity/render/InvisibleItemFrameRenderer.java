/*package de.dafuqs.spectrum.entity.render;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.entity.entity.InvisibleItemFrameEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class InvisibleItemFrameRenderer<T extends ItemFrameEntity> extends ItemFrameEntityRenderer<T> {

    private static final ModelIdentifier NORMAL_FRAME = new ModelIdentifier(new Identifier(SpectrumCommon.MOD_ID, "invisible_item_frame"), "map=false");
    private static final ModelIdentifier MAP_FRAME = new ModelIdentifier(new Identifier("item_frame"), "map=true");

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final ItemRenderer itemRenderer;

    public InvisibleItemFrameRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(T invisibleItemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        if (this.hasLabel(invisibleItemFrameEntity)) {
            this.renderLabelIfPresent(invisibleItemFrameEntity, invisibleItemFrameEntity.getDisplayName(), matrixStack, vertexConsumerProvider, light);
        }

        matrixStack.push();
        Direction direction = invisibleItemFrameEntity.getHorizontalFacing();
        Vec3d vec3d = this.getPositionOffset(invisibleItemFrameEntity, g);
        matrixStack.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());

        matrixStack.translate((double)direction.getOffsetX() * 0.46875D, (double)direction.getOffsetY() * 0.46875D, (double)direction.getOffsetZ() * 0.46875D);
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(invisibleItemFrameEntity.pitch));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - invisibleItemFrameEntity.yaw));

        boolean isInvisible = invisibleItemFrameEntity.isInvisible();
        ItemStack itemStack = invisibleItemFrameEntity.getHeldItemStack();
        if (!isInvisible) {
            BlockRenderManager blockRenderManager = this.client.getBlockRenderManager();
            BakedModelManager bakedModelManager = blockRenderManager.getModels().getModelManager();
            ModelIdentifier modelIdentifier = this.getModelId(invisibleItemFrameEntity, itemStack);
            matrixStack.push();
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            blockRenderManager.getModelRenderer().render(matrixStack.peek(), vertexConsumerProvider.getBuffer(TexturedRenderLayers.getEntityTranslucentCull()), null, bakedModelManager.getModel(modelIdentifier), 1.0F, 1.0F, 1.0F, light, OverlayTexture.DEFAULT_UV);
            matrixStack.pop();
        }

        if (!itemStack.isEmpty()) {
            boolean bl2 = itemStack.isOf(Items.FILLED_MAP);
            if (isInvisible) {
                matrixStack.translate(0.0D, 0.0D, 0.5D);
            } else {
                matrixStack.translate(0.0D, 0.0D, 0.4375D);
            }

            int j = bl2 ? invisibleItemFrameEntity.getRotation() % 4 * 2 : invisibleItemFrameEntity.getRotation();
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float)j * 360.0F / 8.0F));
            if (bl2) {
                matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
                float h = 0.0078125F;
                matrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
                matrixStack.translate(-64.0D, -64.0D, 0.0D);
                Integer integer = FilledMapItem.getMapId(itemStack);
                MapState mapState = FilledMapItem.getMapState(integer, invisibleItemFrameEntity.world);
                matrixStack.translate(0.0D, 0.0D, -1.0D);
                if (mapState != null) {
                    this.client.gameRenderer.getMapRenderer().draw(matrixStack, vertexConsumerProvider, integer, mapState, true, light);
                }
            } else {
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, invisibleItemFrameEntity.getId());
            }
        }

        matrixStack.pop();
    }

    private ModelIdentifier getModelId(T entity, ItemStack stack) {
        if (stack.isOf(Items.FILLED_MAP)) {
            return MAP_FRAME;
        } else {
            return NORMAL_FRAME;
        }
    }

}
*/