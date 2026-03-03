package de.dafuqs.spectrum.registries.client;

import com.mojang.blaze3d.shaders.*;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.data_loaders.client.*;
import de.dafuqs.spectrum.deeper_down.client.*;
import de.dafuqs.spectrum.render.biome_rendering.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.particle.render.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.shaders.*;
import de.dafuqs.spectrum.sound.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.player.*;
import org.jetbrains.annotations.*;
import oshi.util.tuples.*;

import java.util.*;

@EventBusSubscriber(modid = SpectrumCommon.MOD_ID, value = Dist.CLIENT)
public class SpectrumClientEventListeners {
	
	private static int lookingAtUniverseSpyholeTicks = 0;
	private static @Nullable BlockHitResult lookingAtUniverseSpyholeHitResult = null;
	private static boolean lookingAtUniverseSpyholeInitialEffectPlayed = false;
	
	public static void register(IEventBus modBus) {
		/* TODO: port
		//DynamicItemRenderer.registerDynamicItemRenderer(SpectrumBlocks.BOTTOMLESS_BUNDLE.asItem(), BottomlessBundleItem.Renderer::new);
		//DynamicItemRenderer.registerDynamicItemRenderer(SpectrumItems.OMNI_ACCELERATOR.asItem(), OmniAcceleratorItem.Renderer::new);
		/*ModelLoadingPlugin.register((ctx) -> {
			ctx.modifyModelAfterBake().register((orig, c) -> {
				ModelResourceLocation id = c.topLevelId();
				if (id instanceof ModelResourceLocation mid && CUSTOM_ITEM_MODELS.contains(mid)) {
					return new DynamicRenderModel(orig);
				}
				return orig;
			});
		});*/
	}
	
	@SubscribeEvent
	private static void renderFog(ViewportEvent.RenderFog event) {
		EnvironmentalRendering.RenderState state = EnvironmentalRendering.getRenderState();
		if (state.active()) {
			if (state.ultradark()) {
				event.setFogShape(FogShape.SPHERE);
			}
			EnvironmentalData currentEnvironmentalData = EnvironmentalRendering.getCurrentEnvironmentalData();
			event.setNearPlaneDistance(event.getNearPlaneDistance() * currentEnvironmentalData.nearFogDistanceMultiplier());
			event.setFarPlaneDistance(event.getFarPlaneDistance() * currentEnvironmentalData.farFogDistanceMultiplier());
			
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	private static void computeFogColor(ViewportEvent.ComputeFogColor event) {
		if (!EnvironmentalRendering.getRenderState().active()) {
			return;
		}
		
		EnvironmentalData environmentalData = EnvironmentalRendering.getCurrentEnvironmentalData();
		if (environmentalData != EnvironmentalData.NOOP) {
			EnvironmentalRendering.applyColor(event, environmentalData);
		}
	}
	
	@SubscribeEvent
	private static void onDrawTooltips(RenderTooltipEvent.GatherComponents event) {
		ItemStack stack = event.getItemStack();
		
		if (stack.has(DataComponents.FOOD)) {
			if (BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals(SpectrumCommon.MOD_ID)) {
				TooltipHelper.addFoodComponentEffectTooltip(stack, event.getTooltipElements(), Item.TooltipContext.EMPTY.tickRate());
			}
		}
		if (stack.is(SpectrumItemTags.COMING_SOON_TOOLTIP)) {
			event.getTooltipElements().add(Either.left(Component.translatable("spectrum.tooltip.coming_soon").withStyle(ChatFormatting.RED)));
		}
	}
	
	@SubscribeEvent
	private static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
		Pastel.clearClientInstance();
	}
	
	@SubscribeEvent
	private static void afterClientTick(ClientTickEvent.Post event) {
		Minecraft client = Minecraft.getInstance();
		ClientLevel world = client.level;
		Entity cameraEntity = client.getCameraEntity();
		if (world == null || cameraEntity == null) {
			BiomeAttenuatingSoundInstance.clear();
			BlockAuraSoundInstance.clear();
			return;
		}
		
		if (SpectrumCommon.CONFIG.PostProcess) {
			SpectrumShaders.updateShaders(client, world);
		} else {
			SpectrumShaders.disableShaders();
		}
		
		if(Minecraft.getInstance().isPaused()) {
			return;
		}
		
		Holder<Biome> biome = world.getBiome(client.getCameraEntity().blockPosition());
		HowlingSpireEffects.clientTick(world, cameraEntity, biome);
		EnvironmentalRendering.tick(cameraEntity);
		
		// Looking at a Universe Spyhole
		if (lookingAtUniverseSpyholeTicks > 0 && lookingAtUniverseSpyholeHitResult != null) {
			playLookingAtUniverseSpyholeParticles(cameraEntity, world);
		}
	}
	
	private static void playLookingAtUniverseSpyholeParticles(Entity cameraEntity, ClientLevel world) {
		int particleCountPerSide;
		ParticleOptions particleType = ParticleTypes.PORTAL;
		if (!lookingAtUniverseSpyholeInitialEffectPlayed) {
			particleType = ParticleTypes.REVERSE_PORTAL;
			cameraEntity.playSound(SpectrumSoundEvents.SOFT_HUM, 1.0F, 1.0F);
			particleCountPerSide = 20;
			lookingAtUniverseSpyholeInitialEffectPlayed = true;
		} else {
			particleCountPerSide = Math.max(1, 10 - lookingAtUniverseSpyholeTicks);
		}
		
		BlockPos pos = lookingAtUniverseSpyholeHitResult.getBlockPos();
		RandomSource random = world.getRandom();
		for (int i = 0; i < particleCountPerSide; i++) {
			for (Direction direction : Direction.values()) {
				BlockPos blockPos = pos.relative(direction);
				BlockState blockState = world.getBlockState(blockPos);
				if (!blockState.isFaceSturdy(world, blockPos, direction.getOpposite())) {
					double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5 + direction.getStepX() * 0.6;
					double e = direction.getStepY() == 0 ? random.nextDouble() : 0.5 + direction.getStepY() * 0.6;
					double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + direction.getStepZ() * 0.6;
					world.addParticle(particleType, pos.getX() + d, pos.getY() + e, pos.getZ() + f, 0.0, 0.0, 0.0);
				}
			}
		}
	}
	
	@SubscribeEvent
	private static void onWorldRenderStart(RenderLevelStageEvent event) {
		RenderLevelStageEvent.Stage stage = event.getStage();
		
		if (stage == RenderLevelStageEvent.Stage.AFTER_SKY) {
			HudRenderers.clearItemStackOverlay();
			
			Minecraft client = Minecraft.getInstance();
			if (client.player != null) {
				
				boolean lookingAtUniverseSpyhole = (client.hitResult instanceof BlockHitResult blockHitResult) &&
						client.player.level().getBlockState(blockHitResult.getBlockPos()).getBlock() == SpectrumBlocks.UNIVERSE_SPYHOLE.get();
				
				if (lookingAtUniverseSpyhole) {
					lookingAtUniverseSpyholeTicks++;
					lookingAtUniverseSpyholeHitResult = (BlockHitResult) client.hitResult;
				} else {
					lookingAtUniverseSpyholeTicks = 0;
					lookingAtUniverseSpyholeHitResult = null;
					lookingAtUniverseSpyholeInitialEffectPlayed = false;
				}
				
				boolean newSmartCull = !lookingAtUniverseSpyhole;
				if (client.smartCull != newSmartCull) {
					client.smartCull = newSmartCull;
					if (!newSmartCull) {
						client.levelRenderer.needsUpdate(); // we need to draw caves etc...
					}
				}
			}
			return;
		}
		
		Minecraft minecraft = Minecraft.getInstance();
		MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
		
		if (stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			((ExtendedParticleManager) minecraft.particleEngine).render(event.getPoseStack(), bufferSource, event.getCamera(), event.getPartialTick().getGameTimeDeltaTicks());
		} else if (stage == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
			Entity focusedEntity = event.getCamera().getEntity();
			
			if (focusedEntity instanceof LivingEntity livingEntity) {
				boolean paintbrushInHand = livingEntity.getMainHandItem().getItem() instanceof PaintbrushItem;
				Pastel.getClientInstance().renderLines(minecraft.level, event.getPoseStack(), bufferSource, event.getCamera(), livingEntity, paintbrushInHand);
			}
		}
	}
	
	@SubscribeEvent
	private static void onReloadClientResources(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(ParticleSpawnerParticlesDataLoader.INSTANCE);
		event.registerReloadListener(BiomeRenderingDataLoader.INSTANCE);
		event.registerReloadListener(new ResourceManagerReloadListener() {
			@Override
			public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
				BiomeAttenuatingSoundInstance.clear();
			}
			
			@Override
			public @NotNull String getName() {
				return SpectrumCommon.MOD_ID + ":cache_clearer_client";
			}
		});
	}
	
	@SubscribeEvent
	private static void onRenderBlockOutlines(RenderHighlightEvent.Block event) {
		boolean shouldCancel = false;
		BlockHitResult target = event.getTarget();
		Camera camera = event.getCamera();
		
		Minecraft client = Minecraft.getInstance();
		if (client.player != null) {
			for (ItemStack handStack : client.player.getHandSlots()) {
				Item handItem = handStack.getItem();
				if (handItem instanceof ConstructorsStaffItem) {
					shouldCancel = renderPlacementStaffOutline(event.getPoseStack(), camera, camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, event.getMultiBufferSource(), target);
					break;
				} else if (handItem instanceof ExchangeStaffItem) {
					shouldCancel = renderExchangeStaffOutline(event.getPoseStack(), camera, camera.getPosition().x, camera.getPosition().y, camera.getPosition().z, event.getMultiBufferSource(), handStack, target);
					break;
				}
			}
		}
		
		event.setCanceled(shouldCancel);
	}
	
	private static boolean renderPlacementStaffOutline(PoseStack matrices, Camera camera, double d, double e, double f, MultiBufferSource consumers, @NotNull BlockHitResult hitResult) {
		Minecraft client = Minecraft.getInstance();
		ClientLevel world = client.level;
		Player player = client.player;
		if (player == null || world == null) return false;
		
		BlockPos lookingAtPos = hitResult.getBlockPos();
		BlockState lookingAtState = world.getBlockState(lookingAtPos);
		
		if (player.getMainHandItem().getItem() instanceof BuildingStaffItem staff && (player.isCreative() || staff.canInteractWith(lookingAtState, world, lookingAtPos, player))) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Item item = lookingAtBlock.asItem();
			VoxelShape shape = Shapes.empty();
			
			if (item != Items.AIR) {
				int itemCountInInventory = Integer.MAX_VALUE;
				long inkLimit = Long.MAX_VALUE;
				if (!player.isCreative()) {
					Triplet<Block, Item, Integer> inventoryItemAndCount = BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, lookingAtBlock, Integer.MAX_VALUE);
					item = inventoryItemAndCount.getB();
					itemCountInInventory = inventoryItemAndCount.getC();
					inkLimit = InkPowered.getAvailableInk(player, ConstructorsStaffItem.USED_COLOR) / ConstructorsStaffItem.INK_COST_PER_BLOCK;
				}
				
				boolean sneaking = player.isShiftKeyDown();
				if (itemCountInInventory == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), 0, false);
				} else if (inkLimit == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), 1, true);
				} else {
					long usableCount = Math.min(itemCountInInventory, inkLimit);
					List<BlockPos> positions = BuildingHelper.calculateBuildingStaffSelection(world, lookingAtPos, hitResult.getDirection(), usableCount, ConstructorsStaffItem.getRange(player), !sneaking);
					if (!positions.isEmpty()) {
						for (BlockPos newPosition : positions) {
							if (world.getWorldBorder().isWithinBounds(newPosition)) {
								BlockPos testPos = lookingAtPos.subtract(newPosition);
								shape = Shapes.or(shape, lookingAtState.getShape(world, lookingAtPos, CollisionContext.of(camera.getEntity())).move(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
							}
						}
						
						HudRenderers.setItemStackToRender(new ItemStack(item), positions.size(), false);
						VertexConsumer linesBuffer = consumers.getBuffer(RenderType.lines());
						WorldRendererAccessor.invokeRenderShape(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private static boolean renderExchangeStaffOutline(PoseStack matrices, Camera camera, double d, double e, double f, MultiBufferSource consumers, ItemStack exchangeStaffItemStack, BlockHitResult hitResult) {
		Minecraft client = Minecraft.getInstance();
		ClientLevel world = client.level;
		BlockPos lookingAtPos = hitResult.getBlockPos();
		BlockState lookingAtState = world.getBlockState(lookingAtPos);
		
		Player player = client.player;
		
		if (player == null) return false;
		
		if (player.getMainHandItem().getItem() instanceof BuildingStaffItem staff && (player.isCreative() || staff.canInteractWith(lookingAtState, world, lookingAtPos, player))) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Optional<Block> exchangeBlock = ExchangeStaffItem.getStoredBlock(exchangeStaffItemStack);
			if (exchangeBlock.isPresent() && exchangeBlock.get() != lookingAtBlock) {
				Item exchangeBlockItem = exchangeBlock.get().asItem();
				VoxelShape shape = Shapes.empty();
				
				if (exchangeBlockItem != Items.AIR) {
					int itemCountInInventory = Integer.MAX_VALUE;
					long inkLimit = Integer.MAX_VALUE;
					if (!player.isCreative()) {
						Inventory playerInventory = player.getInventory();
						itemCountInInventory = playerInventory.countItem(exchangeBlockItem);
						for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
							var currentStack = playerInventory.getItem(i);
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
							if (world.getWorldBorder().isWithinBounds(newPosition)) {
								BlockPos testPos = lookingAtPos.subtract(newPosition);
								shape = Shapes.or(shape, lookingAtState.getShape(world, lookingAtPos, CollisionContext.of(camera.getEntity())).move(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
							}
						}
						
						HudRenderers.setItemStackToRender(new ItemStack(exchangeBlockItem), positions.size(), false);
						VertexConsumer linesBuffer = consumers.getBuffer(RenderType.lines());
						WorldRendererAccessor.invokeRenderShape(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@SubscribeEvent
	private static void hardcoreHearts(PlayerHeartTypeEvent event) {
		Player player = event.getEntity();
		if(player.hasEffect(SpectrumStatusEffects.DEADLY_POISON)) {
			event.setType(Gui.HeartType.POISIONED);
		}
	}
	
}
