package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.BuildingHelper;
import de.dafuqs.spectrum.Support;
import de.dafuqs.spectrum.items.magic_items.PlacementStaffItem;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.processing.SupportedOptions;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;
    
    @Shadow private ClientWorld world;
    
    @Shadow private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) { }
    
    // if the mixin renders a bigger outline than the default 1:1. Cancels default outline
    @Unique private boolean renderedExtendedOutline = false;

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderExtendedBlockOutline(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci, Profiler profiler, Vec3d vec3d, double d, double e, double f, Matrix4f matrix4f2, boolean bl, Frustum frustum2, boolean bl3, VertexConsumerProvider.Immediate immediate) {
        if(client.player != null && client.player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() instanceof PlacementStaffItem placementStaffItem) {
            HitResult hitResult = this.client.crosshairTarget;

            if (renderBlockOutline && hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos lookingAtPos = ((BlockHitResult) hitResult).getBlockPos();
                BlockState lookingAtState = this.world.getBlockState(lookingAtPos);
                Block lookingAtBlock = lookingAtState.getBlock();
                
                if(!SpectrumBlockTags.PLACEMENT_STAFF_BLACKLISTED.contains(lookingAtBlock)) {
                    Item item = lookingAtBlock.asItem();
                    VoxelShape shape = VoxelShapes.empty();
                    ClientPlayerEntity player = client.player;
    
                    if (item != Items.AIR) {
                        int itemCountInInventory = player.getInventory().count(item);
                        if (player.isCreative() || itemCountInInventory > 0) {
                            boolean sneaking = player.isSneaking();
                            for (BlockPos newPosition : BuildingHelper.calculateSelection(world, lookingAtPos, ((BlockHitResult) hitResult).getSide(), itemCountInInventory, placementStaffItem.getRange(player), !sneaking)) {
                                if (this.world.getWorldBorder().contains(newPosition)) {
                                    BlockPos testPos = lookingAtPos.subtract(newPosition);
                                    shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(this.world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
                                }
                            }
            
                            VertexConsumer linesBuffer = immediate.getBuffer(RenderLayer.getLines());
                            drawShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
                            renderedExtendedOutline = true;
                        }
                    }
                }
            }
        } else {
            renderedExtendedOutline = false;
        }
    }

    @Inject(method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"), cancellable = true)
    private void cancelDrawBlockOutlineIfLargerOneIsDrawnInstead(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if(renderedExtendedOutline) {
            ci.cancel();
        }
    }
}