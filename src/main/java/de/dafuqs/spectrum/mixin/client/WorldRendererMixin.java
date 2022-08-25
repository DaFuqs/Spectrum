package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.energy.InkPowered;
import de.dafuqs.spectrum.helpers.BuildingHelper;
import de.dafuqs.spectrum.items.magic_items.BuildingStaffItem;
import de.dafuqs.spectrum.items.magic_items.ExchangeStaffItem;
import de.dafuqs.spectrum.items.magic_items.PlacementStaffItem;
import de.dafuqs.spectrum.mixin.accessors.WorldRendererAccessor;
import de.dafuqs.spectrum.render.HudRenderers;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.NotNull;
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
@Mixin(value = WorldRenderer.class)
public abstract class WorldRendererMixin {
	
	@Shadow
	@Final
	private MinecraftClient client;
	
	@Shadow
	private ClientWorld world;
	
	// if the mixin renders a bigger outline than the default 1:1. Cancels default outline
	@Unique
	private boolean renderedExtendedOutline = false;
	
	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void renderExtendedBlockOutline(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci, Profiler profiler, boolean b, Vec3d vec3d, double d, double e, double f, Matrix4f matrix4f2, boolean b2, Frustum frustum2, float g, boolean b25, boolean b3, VertexConsumerProvider.Immediate immediate) {
		renderedExtendedOutline = false;
		HudRenderers.doNotRenderOverlay();
		
		if (client.player != null && renderBlockOutline) {
			for(ItemStack handStack : client.player.getHandItems()) {
				Item handItem = handStack.getItem();
				if (handItem instanceof PlacementStaffItem) {
					HitResult hitResult = this.client.crosshairTarget;
					if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
						renderedExtendedOutline = renderPlacementStaffOutline(matrices, camera, d, e, f, immediate, (BlockHitResult) hitResult);
					}
					break;
				} else if (handItem instanceof ExchangeStaffItem) {
					HitResult hitResult = this.client.crosshairTarget;
					if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
						renderedExtendedOutline = renderExchangeStaffOutline(matrices, camera, d, e, f, immediate, handStack, (BlockHitResult) hitResult);
					}
					break;
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
				int itemCountInInventory = Integer.MAX_VALUE;
				int inkLimit = Integer.MAX_VALUE;
				if (!player.isCreative()) {
					Triplet<Block, Item, Integer> inventoryItemAndCount = BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, lookingAtBlock);
					item = inventoryItemAndCount.getB();
					itemCountInInventory = inventoryItemAndCount.getC();
					inkLimit = (int) InkPowered.getAvailableInk(player, PlacementStaffItem.USED_COLOR) / PlacementStaffItem.INK_COST_PER_BLOCK;
				}
				
				boolean sneaking = player.isSneaking();
				if (itemCountInInventory == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), 0, false);
				} else if(inkLimit == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), 1, true);
				} else {
					int usableCount = Math.min(itemCountInInventory, inkLimit);
					List<BlockPos> positions = BuildingHelper.calculateBuildingStaffSelection(world, lookingAtPos, hitResult.getSide(), usableCount, PlacementStaffItem.getRange(player), !sneaking);
					if (positions.size() > 0) {
						for (BlockPos newPosition : positions) {
							if (this.world.getWorldBorder().contains(newPosition)) {
								BlockPos testPos = lookingAtPos.subtract(newPosition);
								shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(this.world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
							}
						}
						
						HudRenderers.setItemStackToRender(new ItemStack(item), positions.size(), false);
						VertexConsumer linesBuffer = immediate.getBuffer(RenderLayer.getLines());
						WorldRendererAccessor.invokeDrawCuboidShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
					}
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
					int itemCountInInventory = Integer.MAX_VALUE;
					int inkLimit = Integer.MAX_VALUE;
					if (!player.isCreative()) {
						itemCountInInventory = player.getInventory().count(exchangeBlockItem);
						inkLimit = (int) InkPowered.getAvailableInk(player, ExchangeStaffItem.USED_COLOR) / ExchangeStaffItem.INK_COST_PER_BLOCK;
					}
					
					if (itemCountInInventory == 0) {
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), 0, false);
					} else if(inkLimit == 0) {
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), 1, true);
					} else {
						int usableCount = Math.min(itemCountInInventory, inkLimit);
						List<BlockPos> positions = BuildingHelper.getConnectedBlocks(world, lookingAtPos, usableCount, ExchangeStaffItem.getRange(player));
						for (BlockPos newPosition : positions) {
							if (this.world.getWorldBorder().contains(newPosition)) {
								BlockPos testPos = lookingAtPos.subtract(newPosition);
								shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(this.world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
							}
						}
						
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), positions.size(), false);
						VertexConsumer linesBuffer = immediate.getBuffer(RenderLayer.getLines());
						WorldRendererAccessor.invokeDrawCuboidShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
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