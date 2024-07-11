package de.dafuqs.spectrum;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.ears.*;
import de.dafuqs.spectrum.compat.idwtialsimmoedm.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.progression.toast.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.render.capes.*;
import net.fabricmc.api.*;
import net.fabricmc.loader.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

import java.util.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

@Environment(EnvType.CLIENT)
public class SpectrumClient implements ClientModInitializer, RevealingCallback, ClientAdvancementPacketCallback {

	public static final SkyLerper skyLerper = new SkyLerper();

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

		logInfo("Registering Dimension Effects...");
		SpectrumDimensions.registerClient();

		logInfo("Registering Event Listeners...");
		SpectrumClientEventListeners.register();

		if (CONFIG.AddItemTooltips) {
			SpectrumTooltips.register();
		}
		
		if (FabricLoader.getInstance().isModLoaded("ears")) {
			logInfo("Registering Ears Compat...");
			EarsCompat.register();
		}
		
		if (FabricLoader.getInstance().isModLoaded("idwtialsimmoedm")) {
			logInfo("Registering idwtialsimmoedm Compat...");
			IdwtialsimmoedmCompat.register();
		}

		logInfo("Registering Armor Renderers...");
		SpectrumArmorRenderers.register();
		WorthinessChecker.init();

		RevealingCallback.register(this);
		ClientAdvancementPacketCallback.registerCallback(this);

		logInfo("Client startup completed!");
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
		if (!isJoinPacket) {
			UnlockToastManager.processAdvancements(gottenAdvancements);
		}
	}
	
}
