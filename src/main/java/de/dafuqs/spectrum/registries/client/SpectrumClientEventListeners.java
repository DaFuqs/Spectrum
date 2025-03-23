package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.particle.render.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.sound.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.item.v1.*;
import net.fabricmc.fabric.api.client.model.loading.v1.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.resource.*;
import net.fabricmc.loader.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.resource.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.biome.*;
import org.jetbrains.annotations.*;
import oshi.util.tuples.*;

import java.util.*;
import java.util.function.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

@Environment(EnvType.CLIENT)
public class SpectrumClientEventListeners {
	
	// TODO: Move to API package
	public static final ObjectOpenHashSet<ModelIdentifier> CUSTOM_ITEM_MODELS = new ObjectOpenHashSet<>();
	
	public static final boolean foodEffectsTooltipsModLoaded = FabricLoader.getInstance().isModLoaded("foodeffecttooltips");
	
	
	private static void registerCustomItemRenderer(String id, Item item, Supplier<DynamicItemRenderer> renderer) {
		CUSTOM_ITEM_MODELS.add(new ModelIdentifier(MOD_ID, id, "inventory"));
		DynamicItemRenderer.RENDERERS.put(item, renderer.get());
	}
	
	public static void register() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ParticleSpawnerParticlesDataLoader.INSTANCE);
		
		registerCustomItemRenderer("bottomless_bundle", SpectrumItems.BOTTOMLESS_BUNDLE, BottomlessBundleItem.Renderer::new);
		registerCustomItemRenderer("omni_accelerator", SpectrumItems.OMNI_ACCELERATOR, OmniAcceleratorItem.Renderer::new);
		
		WorldRenderEvents.START.register(context -> HudRenderers.clearItemStackOverlay());
		WorldRenderEvents.AFTER_ENTITIES.register(context -> ((ExtendedParticleManager) MinecraftClient.getInstance().particleManager).render(context.matrixStack(), context.consumers(), context.camera(), context.tickDelta()));
		WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> Pastel.getClientInstance().renderLines(context));
		WorldRenderEvents.BLOCK_OUTLINE.register(SpectrumClientEventListeners::renderExtendedBlockOutline);
		BiomeAttenuatingSoundInstance.clear();

		ModelLoadingPlugin.register((ctx) -> {
			ctx.modifyModelAfterBake().register((orig, c) -> {
				Identifier id = c.id();
				if (id instanceof ModelIdentifier mid && CUSTOM_ITEM_MODELS.contains(mid)) {
					return new DynamicRenderModel(orig);
				}
				return orig;
			});
		});
		
		ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> SpectrumColorProviders.registerClient());
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> Pastel.clearClientInstance());
		
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (!foodEffectsTooltipsModLoaded && stack.isFood()) {
				if (Registries.ITEM.getId(stack.getItem()).getNamespace().equals(SpectrumCommon.MOD_ID)) {
					TooltipHelper.addFoodComponentEffectTooltip(stack, lines);
				}
			}
			if (stack.isIn(SpectrumItemTags.COMING_SOON_TOOLTIP)) {
				lines.add(Text.translatable("spectrum.tooltip.coming_soon").formatted(Formatting.RED));
			}
		});
		
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			var world = client.world;
			Entity cameraEntity = client.getCameraEntity();
			if (world == null || cameraEntity == null) {
				BiomeAttenuatingSoundInstance.clear();
				BlockAuraSoundInstance.clear();
				return;
			}
			
			RegistryEntry<Biome> biome = world.getBiome(client.getCameraEntity().getBlockPos());

			HowlingSpireEffects.clientTick(world, cameraEntity, biome);
			DarknessEffects.clientTick(world, cameraEntity, biome);
		});
	}
	
	private static boolean renderExtendedBlockOutline(WorldRenderContext context, WorldRenderContext.BlockOutlineContext hitResult) {
		boolean shouldCancel = false;
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null && context.blockOutlines()) {
			for (ItemStack handStack : client.player.getHandItems()) {
				Item handItem = handStack.getItem();
				if (handItem instanceof ConstructorsStaffItem) {
					if (hitResult != null && client.crosshairTarget instanceof BlockHitResult blockHitResult) {
						shouldCancel = renderPlacementStaffOutline(context.matrixStack(), context.camera(), hitResult.cameraX(), hitResult.cameraY(), hitResult.cameraZ(), context.consumers(), blockHitResult);
					}
					break;
				} else if (handItem instanceof ExchangeStaffItem) {
					if (hitResult != null) {
						shouldCancel = renderExchangeStaffOutline(context.matrixStack(), context.camera(), hitResult.cameraX(), hitResult.cameraY(), hitResult.cameraZ(), context.consumers(), handStack, hitResult);
					}
					break;
				}
			}
		}
		
		return !shouldCancel;
	}
	
	private static boolean renderPlacementStaffOutline(MatrixStack matrices, Camera camera, double d, double e, double f, VertexConsumerProvider consumers, @NotNull BlockHitResult hitResult) {
		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;
		PlayerEntity player = client.player;
		if (player == null || world == null)
			return false;
		
		BlockPos lookingAtPos = hitResult.getBlockPos();
		BlockState lookingAtState = world.getBlockState(lookingAtPos);
		
		if (player.getMainHandStack().getItem() instanceof BuildingStaffItem staff && (player.isCreative() || staff.canInteractWith(lookingAtState, world, lookingAtPos, player))) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Item item = lookingAtBlock.asItem();
			VoxelShape shape = VoxelShapes.empty();
			
			if (item != Items.AIR) {
				int itemCountInInventory = Integer.MAX_VALUE;
				long inkLimit = Long.MAX_VALUE;
				if (!player.isCreative()) {
					Triplet<Block, Item, Integer> inventoryItemAndCount = BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, lookingAtBlock, Integer.MAX_VALUE);
					item = inventoryItemAndCount.getB();
					itemCountInInventory = inventoryItemAndCount.getC();
					inkLimit = InkPowered.getAvailableInk(player, ConstructorsStaffItem.USED_COLOR) / ConstructorsStaffItem.INK_COST_PER_BLOCK;
				}
				
				boolean sneaking = player.isSneaking();
				if (itemCountInInventory == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), 0, false);
				} else if (inkLimit == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), 1, true);
				} else {
					long usableCount = Math.min(itemCountInInventory, inkLimit);
					List<BlockPos> positions = BuildingHelper.calculateBuildingStaffSelection(world, lookingAtPos, hitResult.getSide(), usableCount, ConstructorsStaffItem.getRange(player), !sneaking);
					if (!positions.isEmpty()) {
						for (BlockPos newPosition : positions) {
							if (world.getWorldBorder().contains(newPosition)) {
								BlockPos testPos = lookingAtPos.subtract(newPosition);
								shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
							}
						}
						
						HudRenderers.setItemStackToRender(new ItemStack(item), positions.size(), false);
						VertexConsumer linesBuffer = consumers.getBuffer(RenderLayer.getLines());
						WorldRendererAccessor.invokeDrawCuboidShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private static boolean renderExchangeStaffOutline(MatrixStack matrices, Camera camera, double d, double e, double f, VertexConsumerProvider consumers, ItemStack exchangeStaffItemStack, WorldRenderContext.BlockOutlineContext hitResult) {
		MinecraftClient client = MinecraftClient.getInstance();
		ClientWorld world = client.world;
		BlockPos lookingAtPos = hitResult.blockPos();
		BlockState lookingAtState = hitResult.blockState();
		
		PlayerEntity player = client.player;
		
		if (player == null || world == null)
			return false;
		
		if (player.getMainHandStack().getItem() instanceof BuildingStaffItem staff && (player.isCreative() || staff.canInteractWith(lookingAtState, world, lookingAtPos, player))) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Optional<Block> exchangeBlock = ExchangeStaffItem.getStoredBlock(exchangeStaffItemStack);
			if (exchangeBlock.isPresent() && exchangeBlock.get() != lookingAtBlock) {
				Item exchangeBlockItem = exchangeBlock.get().asItem();
				VoxelShape shape = VoxelShapes.empty();
				
				if (exchangeBlockItem != Items.AIR) {
					int itemCountInInventory = Integer.MAX_VALUE;
					long inkLimit = Integer.MAX_VALUE;
					if (!player.isCreative()) {
						PlayerInventory playerInventory = player.getInventory();
						itemCountInInventory = playerInventory.count(exchangeBlockItem);
						for (int i = 0; i < player.getInventory().size(); i++) {
							var currentStack = playerInventory.getStack(i);
							ItemProvider itemProvider = ItemProviderRegistry.getProvider(currentStack);
							if (itemProvider != null) {
								itemCountInInventory += itemProvider.getItemCount(player, currentStack, exchangeBlockItem);
							}
						}
						inkLimit = InkPowered.getAvailableInk(player, ExchangeStaffItem.USED_COLOR) / ExchangeStaffItem.INK_COST_PER_BLOCK;
					}
					
					if (itemCountInInventory == 0) {
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), 0, false);
					} else if (inkLimit == 0) {
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), 1, true);
					} else {
						long usableCount = Math.min(itemCountInInventory, inkLimit);
						List<BlockPos> positions = BuildingHelper.getConnectedBlocks(world, lookingAtPos, usableCount, ExchangeStaffItem.getRange(player));
						for (BlockPos newPosition : positions) {
							if (world.getWorldBorder().contains(newPosition)) {
								BlockPos testPos = lookingAtPos.subtract(newPosition);
								shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
							}
						}
						
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), positions.size(), false);
						VertexConsumer linesBuffer = consumers.getBuffer(RenderLayer.getLines());
						WorldRendererAccessor.invokeDrawCuboidShapeOutline(matrices, linesBuffer, shape,
								(double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e,
								(double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
}
