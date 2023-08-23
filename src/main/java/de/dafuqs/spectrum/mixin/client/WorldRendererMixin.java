package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.*;
import net.minecraft.util.shape.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import oshi.util.tuples.*;

import java.util.*;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
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
	private void renderExtendedBlockOutline(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci, Profiler profiler, boolean b, Vec3d vec3d, double x, double y, double z, Matrix4f matrix4f2, boolean b2, Frustum frustum2, float g, boolean b25, boolean b3, VertexConsumerProvider.Immediate immediate) {
		renderedExtendedOutline = false;
		HudRenderers.doNotRenderOverlay();
		
		if (client.player != null && renderBlockOutline) {
			for (ItemStack handStack : client.player.getHandItems()) {
				Item handItem = handStack.getItem();
				if (handItem instanceof ConstructorsStaffItem) {
					HitResult hitResult = this.client.crosshairTarget;
					if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
						renderedExtendedOutline = spectrum$renderPlacementStaffOutline(matrices, camera, x, y, z, immediate, (BlockHitResult) hitResult);
					}
					break;
				} else if (handItem instanceof ExchangeStaffItem) {
					HitResult hitResult = this.client.crosshairTarget;
					if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
						renderedExtendedOutline = spectrum$renderExchangeStaffOutline(matrices, camera, x, y, z, immediate, handStack, (BlockHitResult) hitResult);
					}
					break;
				}
			}
		}
	}
	
	private boolean spectrum$renderPlacementStaffOutline(MatrixStack matrices, Camera camera, double x, double y, double z, VertexConsumerProvider.Immediate immediate, @NotNull BlockHitResult hitResult) {
		BlockPos lookingAtPos = hitResult.getBlockPos();
		BlockState lookingAtState = this.world.getBlockState(lookingAtPos);
		
		ClientPlayerEntity player = client.player;
		if (BuildingStaffItem.canInteractWith(lookingAtState, this.world, lookingAtPos, player)) {
			Block blockToRender = lookingAtState.getBlock();
			Item item = blockToRender.asItem();
			VoxelShape shape = VoxelShapes.empty();
			
			if (item != Items.AIR) {
				int itemCountInInventory = Integer.MAX_VALUE;
				long inkLimit = Long.MAX_VALUE;
				if (!player.isCreative()) {
					Triplet<Block, Item, Integer> inventoryItemAndCount = BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, blockToRender, Integer.MAX_VALUE);
					inkLimit = InkPowered.getAvailableInk(player, ConstructorsStaffItem.USED_COLOR) / ConstructorsStaffItem.INK_COST_PER_BLOCK;
					item = inventoryItemAndCount.getB();
					itemCountInInventory = inventoryItemAndCount.getC();
				}
				
				
				boolean sneaking = player.isSneaking();
				if (itemCountInInventory == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), itemCountInInventory, false);
				} else if (inkLimit == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), itemCountInInventory, true);
				} else {
					long usableCount = Math.min(itemCountInInventory, inkLimit);
					List<BlockPos> positions = BuildingHelper.calculateBuildingStaffSelection(world, lookingAtPos, hitResult.getSide(), usableCount, ConstructorsStaffItem.getRange(player), !sneaking);
					if (positions.size() > 0) {
						return spectrum$renderExtendedOutline(matrices, camera, x, y, z, immediate, lookingAtPos, lookingAtState, item, shape, positions);
					}
				}
			}
		}
		
		return false;
	}
	
	@Unique
	private boolean spectrum$renderExchangeStaffOutline(MatrixStack matrices, Camera camera, double x, double y, double z, VertexConsumerProvider.Immediate immediate, ItemStack exchangeStaffItemStack, @NotNull BlockHitResult hitResult) {
		BlockPos lookingAtPos = hitResult.getBlockPos();
		BlockState lookingAtState = this.world.getBlockState(lookingAtPos);
		
		ClientPlayerEntity player = client.player;
		if (BuildingStaffItem.canInteractWith(lookingAtState, this.world, lookingAtPos, player)) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Optional<Block> exchangeBlock = ExchangeStaffItem.getStoredBlock(exchangeStaffItemStack);
			if (exchangeBlock.isPresent() && exchangeBlock.get() != lookingAtBlock) {
				Item exchangeBlockItem = exchangeBlock.get().asItem();
				VoxelShape shape = VoxelShapes.empty();
				
				if (exchangeBlockItem != Items.AIR) {
					int itemCountInInventory = Integer.MAX_VALUE;
					long inkLimit = Integer.MAX_VALUE;
					if (!player.isCreative()) {
						itemCountInInventory = player.getInventory().count(exchangeBlockItem);
						inkLimit = InkPowered.getAvailableInk(player, ExchangeStaffItem.USED_COLOR) / ExchangeStaffItem.INK_COST_PER_BLOCK;
					}
					
					if (itemCountInInventory == 0) {
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), itemCountInInventory, false);
					} else if (inkLimit == 0) {
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), itemCountInInventory, true);
					} else {
						long usableCount = Math.min(itemCountInInventory, inkLimit);
						List<BlockPos> positions = BuildingHelper.getConnectedBlocks(world, lookingAtPos, usableCount, ExchangeStaffItem.getRange(player));
						return spectrum$renderExtendedOutline(matrices, camera, x, y, z, immediate, lookingAtPos, lookingAtState, exchangeBlockItem, shape, positions);
					}
				}
			}
		}
		
		return false;
	}
	
	@Unique
	private boolean spectrum$renderExtendedOutline(MatrixStack matrices, Camera camera, double x, double y, double z, VertexConsumerProvider.Immediate immediate, BlockPos lookingAtPos, BlockState lookingAtState, Item item, VoxelShape shape, List<BlockPos> positions) {
		for (BlockPos newPosition : positions) {
			if (this.world.getWorldBorder().contains(newPosition)) {
				BlockPos testPos = lookingAtPos.subtract(newPosition);
				shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(this.world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
			}
		}
		
		HudRenderers.setItemStackToRender(new ItemStack(item), positions.size(), false);
		VertexConsumer linesBuffer = immediate.getBuffer(RenderLayer.getLines());
		WorldRendererAccessor.invokeDrawCuboidShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - x, (double) lookingAtPos.getY() - y, (double) lookingAtPos.getZ() - z, 0.0F, 0.0F, 0.0F, 0.4F);
		return true;
	}
	
	@Inject(method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"), cancellable = true)
	private void cancelDrawBlockOutlineIfLargerOneIsDrawnInstead(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
		if (renderedExtendedOutline) {
			ci.cancel();
		}
	}
	
}