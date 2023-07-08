package de.dafuqs.spectrum;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.biome_makeover.*;
import de.dafuqs.spectrum.compat.ears.*;
import de.dafuqs.spectrum.compat.patchouli.*;
import de.dafuqs.spectrum.compat.reverb.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.deeper_down.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
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
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.resource.*;
import net.minecraft.tag.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumClient implements ClientModInitializer, RevealingCallback, ClientAdvancementPacketCallback {

	@Environment(EnvType.CLIENT)
	public static final SkyLerper skyLerper = new SkyLerper();
	public static final boolean foodEffectsTooltipsModLoaded = FabricLoader.getInstance().isModLoaded("foodeffecttooltips");
	public static boolean FORCE_TRANSLUCENT = false;

	@Override
	public void onInitializeClient() {
		logInfo("Starting Client Startup");
		
		logInfo("Registering Model Layers...");
		SpectrumModelLayers.register();
		
		logInfo("Setting up Block Rendering...");
		SpectrumBlocks.registerClient();
		
		logInfo("Setting up client side Mod Compat...");
		SpectrumIntegrationPacks.registerClient();
		
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
		ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> SpectrumColorProviders.registerClient());
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> Pastel.clearClientInstance());
		
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (!foodEffectsTooltipsModLoaded && stack.isFood()) {
				if (Registry.ITEM.getId(stack.getItem()).getNamespace().equals(SpectrumCommon.MOD_ID)) {
					TooltipHelper.addFoodComponentEffectTooltip(stack, lines);
				}
			}
			if (stack.isIn(SpectrumItemTags.COMING_SOON_TOOLTIP)) {
				lines.add(Text.translatable("spectrum.tooltip.coming_soon").formatted(Formatting.GRAY));
			}
		});
		
		if (CONFIG.AddItemTooltips) {
			ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
				NbtCompound nbt = stack.getNbt();
				if (nbt != null) {
					if (stack.isOf(Blocks.SCULK_SHRIEKER.asItem()) && nbt.contains("BlockStateTag")) {
						NbtCompound blockStateTag = nbt.getCompound("BlockStateTag");
						if (Boolean.parseBoolean(blockStateTag.getString("can_summon"))) {
							lines.add(Text.translatable("spectrum.tooltip.able_to_summon_warden").formatted(Formatting.GRAY));
						}
					}
					if (nbt.getBoolean(BiomeMakeoverCompat.CURSED_TAG)) {
						lines.add(Text.translatable("spectrum.tooltip.biomemakeover_cursed").formatted(Formatting.GRAY));
					}
					if (stack.isIn(ItemTags.SIGNS) && nbt.contains("BlockEntityTag", NbtElement.COMPOUND_TYPE)) {
						NbtCompound blockEntityTag = nbt.getCompound("BlockEntityTag");
						Style style = Style.EMPTY.withColor(DyeColor.byName(blockEntityTag.getString("Color"), DyeColor.WHITE).getSignColor());
						for (String textKey : new String[]{"Text1", "Text2", "Text3", "Text4"}) {
							MutableText text = Text.Serializer.fromJson(blockEntityTag.getString(textKey));
							if (text != null) {
								lines.add(text.setStyle(style));
							}
						}
					}
				}
			});
		}
		
		if (FabricLoader.getInstance().isModLoaded("ears")) {
			logInfo("Registering Ears Compat...");
			EarsCompat.register();
		}
		
		WorldRenderEvents.AFTER_ENTITIES.register(context -> ((ExtendedParticleManager) MinecraftClient.getInstance().particleManager).render(context.matrixStack(), context.consumers(), context.camera(), context.tickDelta()));
		
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
		
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ParticleSpawnerParticlesDataLoader.INSTANCE);
		
		logInfo("Registering Armor Renderers...");
		SpectrumArmorRenderers.register();
		
		RevealingCallback.register(this);
		ClientAdvancementPacketCallback.registerCallback(this);
		
		logInfo("Client startup completed!");
	}

	@Override
	public void trigger(Set<Identifier> advancements, Set<Block> blocks, Set<Item> items, boolean isJoinPacket) {
		if (!isJoinPacket) {
			for (Block block : blocks) {
				if (Registry.BLOCK.getId(block).getNamespace().equals(SpectrumCommon.MOD_ID)) {
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
	
}