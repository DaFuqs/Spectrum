package de.dafuqs.spectrum;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.compat.patchouli.*;
import de.dafuqs.spectrum.compat.reverb.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.energy.InkPowered;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.mixin.accessors.WorldRendererAccessor;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.render.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.progression.toast.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.item.v1.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.resource.*;
import net.fabricmc.loader.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.*;
import net.minecraft.client.world.*;
import net.minecraft.item.*;
import net.minecraft.resource.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.util.shape.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.joml.*;
import oshi.util.tuples.*;

import java.lang.Math;
import java.util.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumClient implements ClientModInitializer, RevealingCallback, ClientAdvancementPacketCallback {

	@Environment(EnvType.CLIENT)
	public static final SkyLerper skyLerper = new SkyLerper();
	public static boolean foodEffectsTooltipsModLoaded = FabricLoader.getInstance().isModLoaded("foodeffecttooltips");
	public static boolean FORCE_TRANSLUCENT = false;

	@Override
	public void onInitializeClient() {
		logInfo("Starting Client Startup");
		
		logInfo("Registering Model Layers...");
		SpectrumModelLayers.register();
		
		logInfo("Setting up Block Rendering...");
		SpectrumBlocks.registerClient();
		
		logInfo("Setting up client side Mod Compat...");
		SpectrumModCompat.registerClient();
		
		logInfo("Setting up Fluid Rendering...");
		SpectrumFluids.registerClient();
		
		logInfo("Setting up GUIs...");
		SpectrumScreenHandlerIDs.register();
		SpectrumScreenHandlerTypes.registerClient();
		
		logInfo("Setting up ItemPredicates...");
		SpectrumModelPredicateProviders.registerClient();

		logInfo("Setting up Block Entity Renderers...");
		SpectrumBlockEntities.registerClient();
		logInfo("Setting up Entity Renderers...");
		SpectrumEntityRenderers.registerClient();

		logInfo("Registering Server to Client Package Receivers...");
		SpectrumS2CPacketReceiver.registerS2CReceivers();
		logInfo("Registering Particle Factories...");
		SpectrumParticleFactories.register();
		
		logInfo("Registering Overlays...");
		HudRenderers.register();
		
		logInfo("Registering Item Tooltips...");
		SpectrumTooltipComponents.registerTooltipComponents();
		
		logInfo("Registering custom Patchouli Pages & Flags...");
		PatchouliPages.register();
		PatchouliFlags.register();
		
		logInfo("Registering Dimension Effects...");
		DDDimension.registerClient();
		DimensionReverb.setup();
		
		logInfo("Registering Event Listeners...");
		ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
			SpectrumColorProviders.registerClient();
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			Pastel.clearClientInstance();
		});

		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (!foodEffectsTooltipsModLoaded && stack.isFood()) {
				if (Registries.ITEM.getId(stack.getItem()).getNamespace().equals(SpectrumCommon.MOD_ID)) {
					TooltipHelper.addFoodComponentEffectTooltip(stack, lines);
				}
			}
			if (stack.isIn(SpectrumItemTags.COMING_SOON_TOOLTIP)) {
				lines.add(Text.translatable("spectrum.tooltip.coming_soon"));
			}
		});

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			((ExtendedParticleManager) MinecraftClient.getInstance().particleManager).render(context.matrixStack(), context.consumers(), context.camera(), context.tickDelta());
		});

		registerBlockOutlineEvent();

		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			ClientPastelNetworkManager networkManager = Pastel.getClientInstance();
			for (PastelNetwork network : networkManager.getNetworks()) {
				Graph<PastelNodeBlockEntity, DefaultEdge> graph = network.getGraph();
				int color = network.getColor();
				float[] colors = PastelRenderHelper.unpackNormalizedColor(color);

				for (DefaultEdge edge : graph.edgeSet()) {
					PastelNodeBlockEntity source = graph.getEdgeSource(edge);
					PastelNodeBlockEntity target = graph.getEdgeTarget(edge);

					final MatrixStack matrices = context.matrixStack();
					final Vec3d pos = context.camera().getPos();
					matrices.push();
					matrices.translate(-pos.x, -pos.y, -pos.z);
					PastelRenderHelper.renderLineTo(context.matrixStack(), context.consumers(), colors, source.getPos(), target.getPos());
					PastelRenderHelper.renderLineTo(context.matrixStack(), context.consumers(), colors, target.getPos(), source.getPos());
					
					if (MinecraftClient.getInstance().options.debugEnabled) {
						Vec3d offset = Vec3d.ofCenter(target.getPos()).subtract(Vec3d.of(source.getPos()));
						Vec3d normalized = offset.normalize();
						Matrix4f positionMatrix = context.matrixStack().peek().getPositionMatrix();
						PastelRenderHelper.renderDebugLine(context.consumers(), color, offset, normalized, positionMatrix);
					}
					matrices.pop();
				}
			}
		});
		
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(ParticleSpawnerParticlesDataLoader.INSTANCE);
		
		logInfo("Registering Armor Renderers...");
		SpectrumArmorRenderers.register();
		
		RevealingCallback.register(this);
		ClientAdvancementPacketCallback.registerCallback(this);
		
		logInfo("Client startup completed!");
	}

	// TODO - consider moving this somewhere else
	private void registerBlockOutlineEvent() {
		WorldRenderEvents.BLOCK_OUTLINE.register((context, hitResult) -> {
			boolean shouldCancel = false;
			var client = MinecraftClient.getInstance();
			if (client.player != null && context.blockOutlines()) {
				for (ItemStack handStack : client.player.getHandItems()) {
					Item handItem = handStack.getItem();
					if (handItem instanceof PlacementStaffItem) {
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
		});
	}

	@Override
	public void trigger(Set<Identifier> advancements, Set<Block> blocks, Set<Item> items, boolean isJoinPacket) {
		if (!isJoinPacket) {
			for (Block block : blocks) {
				if (Registries.BLOCK.getId(block).getNamespace().equals(SpectrumCommon.MOD_ID)) {
					RevelationToast.showRevelationToast(MinecraftClient.getInstance(), new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST.asItem()), SpectrumSoundEvents.NEW_REVELATION);
					break;
				}
			}
		}
	}
	
	@Override
	public void onClientAdvancementPacket(Set<Identifier> gottenAdvancements, Set<Identifier> removedAdvancements, boolean isJoinPacket) {
		if(!isJoinPacket) {
			UnlockToastManager.processAdvancements(gottenAdvancements);
		}
	}

	private boolean renderPlacementStaffOutline(MatrixStack matrices, Camera camera, double d, double e, double f, VertexConsumerProvider consumers, @NotNull BlockHitResult hitResult) {
		ClientWorld world = MinecraftClient.getInstance().world;
		BlockPos lookingAtPos = hitResult.getBlockPos();
		BlockState lookingAtState = world.getBlockState(lookingAtPos);

		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player.isCreative() || BuildingStaffItem.canProcess(lookingAtState, world, lookingAtPos, player)) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Item item = lookingAtBlock.asItem();
			VoxelShape shape = VoxelShapes.empty();

			if (item != Items.AIR) {
				int itemCountInInventory = Integer.MAX_VALUE;
				long inkLimit = Long.MAX_VALUE;
				if (!player.isCreative()) {
					Triplet<Block, Item, Integer> inventoryItemAndCount = BuildingHelper.getBuildingItemCountInInventoryIncludingSimilars(player, lookingAtBlock);
					item = inventoryItemAndCount.getB();
					itemCountInInventory = inventoryItemAndCount.getC();
					inkLimit = InkPowered.getAvailableInk(player, PlacementStaffItem.USED_COLOR) / PlacementStaffItem.INK_COST_PER_BLOCK;
				}

				boolean sneaking = player.isSneaking();
				if (itemCountInInventory == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), 0, false);
				} else if (inkLimit == 0) {
					HudRenderers.setItemStackToRender(new ItemStack(item), 1, true);
				} else {
					long usableCount = Math.min(itemCountInInventory, inkLimit);
					List<BlockPos> positions = BuildingHelper.calculateBuildingStaffSelection(world, lookingAtPos, hitResult.getSide(), usableCount, PlacementStaffItem.getRange(player), !sneaking);
					if (positions.size() > 0) {
						for (BlockPos newPosition : positions) {
							if (world.getWorldBorder().contains(newPosition)) {
								BlockPos testPos = lookingAtPos.subtract(newPosition);
								shape = VoxelShapes.union(shape, lookingAtState.getOutlineShape(world, lookingAtPos, ShapeContext.of(camera.getFocusedEntity())).offset(-testPos.getX(), -testPos.getY(), -testPos.getZ()));
							}
						}

						HudRenderers.setItemStackToRender(new ItemStack(item), positions.size(), false);
						VertexConsumer linesBuffer = consumers.getBuffer(RenderLayer.getLines());
						de.dafuqs.spectrum.mixin.accessors.WorldRendererAccessor.invokeDrawCuboidShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
					}
				}
			}
		}

		return false;
	}

	private boolean renderExchangeStaffOutline(MatrixStack matrices, Camera camera, double d, double e, double f, VertexConsumerProvider consumers, ItemStack exchangeStaffItemStack, WorldRenderContext.BlockOutlineContext hitResult) {
		ClientWorld world = MinecraftClient.getInstance().world;
		BlockPos lookingAtPos = hitResult.blockPos();
		BlockState lookingAtState = hitResult.blockState();

		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player.isCreative() || BuildingStaffItem.canProcess(lookingAtState, world, lookingAtPos, player)) {
			Block lookingAtBlock = lookingAtState.getBlock();
			Optional<Block> exchangeBlock = ExchangeStaffItem.getBlockTarget(exchangeStaffItemStack);
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
						WorldRendererAccessor.invokeDrawCuboidShapeOutline(matrices, linesBuffer, shape, (double) lookingAtPos.getX() - d, (double) lookingAtPos.getY() - e, (double) lookingAtPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
						return true;
					}
				}
			}
		}

		return false;
	}
}