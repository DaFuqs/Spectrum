package de.dafuqs.spectrum;

import de.dafuqs.revelationary.api.revelations.RevealingCallback;
import de.dafuqs.spectrum.compat.patchouli.PatchouliPages;
import de.dafuqs.spectrum.entity.SpectrumEntityRenderers;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketReceiver;
import de.dafuqs.spectrum.particle.SpectrumParticleFactories;
import de.dafuqs.spectrum.progression.toast.RevelationToast;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.SpectrumColorProviders;
import de.dafuqs.spectrum.registries.client.SpectrumItemPredicates;
import de.dafuqs.spectrum.render.HudRenderers;
import de.dafuqs.spectrum.render.SkyLerper;
import de.dafuqs.spectrum.render.SpectrumGeoRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Set;

import static de.dafuqs.spectrum.SpectrumCommon.logInfo;

public class SpectrumClient implements ClientModInitializer, RevealingCallback {
	
	@Environment(EnvType.CLIENT)
	public static final SkyLerper skyLerper = new SkyLerper();
	
	@Environment(EnvType.CLIENT)
	public static MinecraftClient minecraftClient;
	
	@Override
	public void onInitializeClient() {
		logInfo("Starting Client Startup");
		
		logInfo("Setting up Block Rendering...");
		SpectrumBlocks.registerClient();
		// IncubusShaders.enableBloom(); // nonononono. That completely breaks the world with immersive portals. (ㆆ_ㆆ)
		logInfo("Setting up Fluid Rendering...");
		SpectrumFluids.registerClient();
		
		logInfo("Setting up GUIs...");
		SpectrumContainers.register();
		SpectrumScreenHandlerTypes.registerClient();
		
		logInfo("Setting up ItemPredicates...");
		SpectrumItemPredicates.registerClient();
		
		logInfo("Setting up Block Entity Renderers...");
		SpectrumBlockEntities.registerClient();
		logInfo("Setting up Entity Renderers...");
		SpectrumEntityRenderers.registerClient();
		logInfo("Setting up Geckolib Renderers...");
		SpectrumGeoRenderers.register();
		
		logInfo("Registering Server to Client Package Receivers...");
		SpectrumS2CPacketReceiver.registerS2CReceivers();
		logInfo("Registering Particle Factories...");
		SpectrumParticleFactories.register();
		
		logInfo("Registering Overlays...");
		HudRenderers.register();
		
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
		RevealingCallback.register(this);
		
		logInfo("Client startup completed!");
	}
	
	@Override
	public void trigger(Set<Identifier> advancements, Set<Block> blocks, Set<Item> items, boolean isJoinPacket) {
		if(!isJoinPacket) {
			for (Block block : blocks) {
				if (Registry.BLOCK.getId(block).getNamespace().equals(SpectrumCommon.MOD_ID)) {
					RevelationToast.showRevelationToast(MinecraftClient.getInstance(), new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST.asItem()), SpectrumSoundEvents.NEW_REVELATION);
					break;
				}
			}
		}
	}
	
}