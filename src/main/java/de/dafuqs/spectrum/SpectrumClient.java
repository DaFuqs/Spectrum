package de.dafuqs.spectrum;

import de.dafuqs.spectrum.compat.patchouli.PatchouliPages;
import de.dafuqs.spectrum.entity.SpectrumEntityRenderers;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketReceiver;
import de.dafuqs.spectrum.particle.SpectrumParticleFactories;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.SpectrumColorProviders;
import de.dafuqs.spectrum.registries.client.SpectrumItemPredicates;
import de.dafuqs.spectrum.render.GuiOverlay;
import de.dafuqs.spectrum.render.SkyLerper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

import static de.dafuqs.spectrum.SpectrumCommon.logInfo;

public class SpectrumClient implements ClientModInitializer {
	
	@Environment(EnvType.CLIENT)
	public static final SkyLerper skyLerper = new SkyLerper();
	
	@Environment(EnvType.CLIENT)
	public static MinecraftClient minecraftClient;
	
	@Override
	public void onInitializeClient() {
		logInfo("Starting Client Startup");
		
		logInfo("Setting up Block Rendering...");
		SpectrumBlocks.registerClient();
		logInfo("Setting up Fluid Rendering...");
		SpectrumFluids.registerClient();
		
		logInfo("Setting up GUIs...");
		SpectrumContainers.register();
		SpectrumScreenHandlerTypes.registerClient();
		
		logInfo("Setting up ItemPredicates...");
		SpectrumItemPredicates.registerClient();
		
		logInfo("Setting up Block Entity Renderers...");
		SpectrumBlockEntityRegistry.registerClient();
		logInfo("Setting up Entity Renderers...");
		SpectrumEntityRenderers.registerClient();
		
		logInfo("Registering Server to Client Package Receivers...");
		SpectrumS2CPacketReceiver.registerS2CReceivers();
		logInfo("Registering Particle Factories...");
		SpectrumParticleFactories.register();
		
		logInfo("Registering Overlays...");
		GuiOverlay.register();
		
		logInfo("Registering Item Tooltips...");
		SpectrumTooltipComponents.registerTooltipComponents();
		
		logInfo("Registering custom Patchouli Pages...");
		PatchouliPages.register();
		
		logInfo("Registering Event Listeners...");
		ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
			SpectrumClient.minecraftClient = minecraftClient;
			SpectrumColorProviders.registerClient();
		});
		
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (stack.isIn(SpectrumItemTags.COMING_SOON_TOOLTIP)) {
				lines.add(new TranslatableText("spectrum.tooltip.coming_soon"));
			}
		});
		
		logInfo("Client startup completed!");
	}
	
}