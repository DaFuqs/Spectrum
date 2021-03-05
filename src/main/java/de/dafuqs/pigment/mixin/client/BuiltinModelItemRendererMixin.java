package de.dafuqs.pigment.mixin.client;

import de.dafuqs.pigment.blocks.head.PigmentSkullBlock;
import de.dafuqs.pigment.blocks.head.PigmentSkullBlockEntityRenderer;
import de.dafuqs.pigment.blocks.head.PigmentWallSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

    private Map<PigmentSkullBlock.Type, SkullBlockEntityModel> pigmentSkullModels;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void getModel(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelLoader entityModelLoader, CallbackInfo ci) {
        PigmentSkullBlockEntityRenderer.setModelLoader(entityModelLoader);
    }


    @Inject(method = "render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At("HEAD"), cancellable = true)
    private void getModel(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof PigmentSkullBlock || block instanceof PigmentWallSkullBlock) {
                PigmentSkullBlock.Type skullType = (PigmentSkullBlock.Type) ((PigmentSkullBlock)block).getSkullType();
                SkullBlockEntityModel skullBlockEntityModel = this.pigmentSkullModels.get(skullType);
                RenderLayer renderLayer = PigmentSkullBlockEntityRenderer.getRenderLayer(skullType);
                SkullBlockEntityRenderer.renderSkull(null, 180.0F, 0.0F, matrices, vertexConsumers, light, skullBlockEntityModel, renderLayer);

                ci.cancel();
            }
        }
    }

    @Inject(method = "Lnet/minecraft/client/render/item/BuiltinModelItemRenderer;apply(Lnet/minecraft/resource/ResourceManager;)V", at = @At("TAIL"))
    public void apply(net.minecraft.resource.ResourceManager manager, CallbackInfo ci) {
        this.pigmentSkullModels = PigmentSkullBlockEntityRenderer.getModels();
    }

}
