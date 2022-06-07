package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.helpers.BuildingHelper;
import de.dafuqs.spectrum.interfaces.WorldRendererAccessor;
import de.dafuqs.spectrum.items.magic_items.BuildingStaffItem;
import de.dafuqs.spectrum.items.magic_items.ExchangeStaffItem;
import de.dafuqs.spectrum.items.magic_items.PlacementStaffItem;
import de.dafuqs.spectrum.render.GuiOverlay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import oshi.util.tuples.Triplet;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(value = WorldRenderer.class, priority = 900)
public abstract class WorldRendererMixin implements WorldRendererAccessor {
	
	@Shadow
	@Final
	private MinecraftClient client;
	
	@Shadow
	private ClientWorld world;
	// if the mixin renders a bigger outline than the default 1:1. Cancels default outline
	@Unique
	private boolean renderedExtendedOutline = false;
	@Shadow
	private BuiltChunkStorage chunks;
	
	@Shadow
	private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
	}
	
	@Shadow
	public abstract void scheduleTerrainUpdate();
	
	@Shadow
	public abstract void playSong(@Nullable SoundEvent song, BlockPos songPosition);
	
	/**
	 * When triggered on client side lets the client redraw ALL chunks
	 * Warning: Costly + LagSpike!
	 */
	public void rebuildAllChunks() {
		if (MinecraftClient.getInstance().world != null) {
			if (MinecraftClient.getInstance().worldRenderer != null && MinecraftClient.getInstance().player != null) {
				for (ChunkBuilder.BuiltChunk chunk : this.chunks.chunks) {
					chunk.scheduleRebuild(true);
				}
				scheduleTerrainUpdate();
			}
		}
	}
	
	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void renderExtendedBlockOutline(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci, Profiler profiler, boolean b, Vec3d vec3d, double d, double e, double f, Matrix4f matrix4f2, boolean b2, Frustum frustum2, float g, boolean b25, boolean b3, VertexConsumerProvider.Immediate immediate, HitResult hitResult2) {
		renderedExtendedOutline = false;
		GuiOverlay.doNotRenderOverlay();
		
		if (client.player != null && renderBlockOutline) {
			ItemStack handItemStack = client.player.getEquippedStack(EquipmentSlot.MAINHAND);
			Item handItem = handItemStack.getItem();
			if (handItem instanceof PlacementStaffItem) {
				HitResult hitResult = this.client.crosshairTarget;
				if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
					renderedExtendedOutline = renderPlacementStaffOutline(matrices, camera, d, e, f, immediate, (BlockHitResult) hitResult);
				}
			} else if (handItem instanceof ExchangeStaffItem) {
				HitResult hitResult = this.client.crosshairTarget;
				if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
					renderedExtendedOutline = renderExchangeStaffOutline(matrices, camera, d, e, f, immediate, handItemStack, (BlockHitResult) hitResult);
				}
			}
		}
	}
	
	private boolean renderPlacementStaffOutline(MatrixStack matrices, Camera camera, double d, double e, double f, VertexConsumerProvider.Immediate immediate, @NotNull BlockHitResult hitResult) {
		BlockPos lookingAtPos = hitResult.getBlockPos();
		BlockState lookingAtState = this.world.getBlockState(lookingAtPos);
		
		ClientPlayerEntity player = client.player;
		if (player.isCreative() || !BuildingStaffItem.isBlacklisted(lookingAtState)) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Item item = lookingAtBlock.asItem();
			VoxelShape shape = VoxelShapes.empty();
			
			if (item != Items.AIR) {
				int itemCountInInventory;
				if (player.isCreative()) {
					itemCountInInventory = Integer.MAX_VALUE;
				} else {
					Triplet<Block, Item, Integer> inventoryItemAndCount = BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, lookingAtBlock);
					item = inventoryItemAndCount.getB();
					itemCountInInventory = inventoryItemAndCount.getC();
				}
				
				boolean sneaking = player.isSneaking();
				List<BlockPos> positions = BuildingHelper.calculateBuildingStaffSelection(world, lookingAtPos, hitResult.getSide(), itemCountInInventory, PlacementStaffItem.getRange(player), !sneaking);
				if (itemCountInInventory == 0) {
					GuiOverlay.setItemStackToRender(new ItemStack(item), 0);
				} else if (positions.size() > 0) {
					for (BlockPos newPosition : positions) {
						if (this.world.getWorldBorder().contains(newPosition)) {
							BlockPos testPos = lookingAtPos.subtract(newPosition);
							shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(this.world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
						}
					}
					
					GuiOverlay.setItemStackToRender(new ItemStack(item), positions.size());
					
					VertexConsumer linesBuffer = immediate.getBuffer(RenderLayer.getLines());
					drawShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
					return true;
				} else {
					GuiOverlay.setItemStackToRender(new ItemStack(item), 0);
				}
			}
		}
		
		return false;
	}
	
	private boolean renderExchangeStaffOutline(MatrixStack matrices, Camera camera, double d, double e, double f, VertexConsumerProvider.Immediate immediate, ItemStack exchangeStaffItemStack, @NotNull BlockHitResult hitResult) {
		BlockPos lookingAtPos = hitResult.getBlockPos();
		BlockState lookingAtState = this.world.getBlockState(lookingAtPos);
		
		ClientPlayerEntity player = client.player;
		if (player.isCreative() || !BuildingStaffItem.isBlacklisted(lookingAtState)) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Optional<Block> exchangeBlock = ExchangeStaffItem.getBlockTarget(exchangeStaffItemStack);
			if (exchangeBlock.isPresent() && exchangeBlock.get() != lookingAtBlock) {
				Item exchangeBlockItem = exchangeBlock.get().asItem();
				VoxelShape shape = VoxelShapes.empty();
				
				if (exchangeBlockItem != Items.AIR) {
					int itemCountInInventory;
					if (player.isCreative()) {
						itemCountInInventory = Integer.MAX_VALUE;
					} else {
						itemCountInInventory = player.getInventory().count(exchangeBlockItem);
					}
					
					if (itemCountInInventory > 0) {
						List<BlockPos> positions = BuildingHelper.getConnectedBlocks(world, lookingAtPos, itemCountInInventory, ExchangeStaffItem.getRange(player));
						for (BlockPos newPosition : positions) {
							if (this.world.getWorldBorder().contains(newPosition)) {
								BlockPos testPos = lookingAtPos.subtract(newPosition);
								shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(this.world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
							}
						}
						
						GuiOverlay.setItemStackToRender(new ItemStack(exchangeBlockItem), positions.size());
						
						VertexConsumer linesBuffer = immediate.getBuffer(RenderLayer.getLines());
						drawShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
					} else {
						GuiOverlay.setItemStackToRender(new ItemStack(exchangeBlockItem), 0);
					}
				}
			}
		}
		
		return false;
	}
	
	@Inject(method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"), cancellable = true)
	private void cancelDrawBlockOutlineIfLargerOneIsDrawnInstead(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
		if (renderedExtendedOutline) {
			ci.cancel();
		}
	}
	
}