package de.dafuqs.spectrum;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.progression.toast.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.render.armor.*;
import net.minecraft.client.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.client.gui.*;
import net.neoforged.neoforge.common.*;

import java.util.*;

@Mod(value = SpectrumCommon.MOD_ID, dist = Dist.CLIENT)
public class SpectrumClient implements RevealingCallback, ClientAdvancementPacketCallback {
	
	public static final SkyLerper skyLerper = new SkyLerper();
	
	public SpectrumClient(IEventBus modBus, ModContainer modContainer) {
		SpectrumCommon.logInfo("Running Client Startup");
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		
		modBus.addListener(SpectrumBlocks::registerClient);
		modBus.addListener(SpectrumIntegrationPacks::registerClient);
		modBus.addListener(SpectrumModelPredicateProviders::registerClient);
		modBus.addListener(SpectrumEntityRenderers::registerClient);
		modBus.addListener(SpectrumParticleFactories::register);
		
		modBus.addListener(HudRenderers::register);
		NeoForge.EVENT_BUS.addListener(HudRenderers::registerPost);
		
		NeoForge.EVENT_BUS.addListener(HudRenderers::registerPost);
		SpectrumEnvironmentalDataOverrides.register();
		SpectrumClientEventListeners.register(modBus);
		
		if (SpectrumConfig.CONFIG.AddItemTooltips.get()) {
			NeoForge.EVENT_BUS.addListener(SpectrumTooltips::register);
		}
		
		RevealingCallback.register(this);
		ClientAdvancementPacketCallback.registerCallback(this);
		
		modBus.addListener(SpectrumFluids::registerClient);
		modBus.addListener(SpectrumBlockEntities::registerClient);
		modBus.addListener(SpectrumMenuTypes::registerClient);
		modBus.addListener(SpectrumModelLayerLocations::register);
		modBus.addListener(BedrockCapeRenderer::registerLayers);
		
		modBus.addListener(SpectrumColorProviders::registerBlocks);
		modBus.addListener(SpectrumColorProviders::registerItems);
	}
	
	@Override
	public void trigger(Set<ResourceLocation> advancements, Set<Block> blocks, Set<Item> items, boolean isJoinPacket) {
		if (!isJoinPacket) {
			for (Block block : blocks) {
				if (BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(SpectrumCommon.MOD_ID)) {
					RevelationToast.showRevelationToast(Minecraft.getInstance(), new ItemStack(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST.asItem()), SpectrumSoundEvents.NEW_REVELATION);
					break;
				}
			}
		}
	}
	
	@Override
	public void onClientAdvancementPacket(Set<ResourceLocation> gottenAdvancements, Set<ResourceLocation> removedAdvancements, boolean isJoinPacket) {
		if (!isJoinPacket) {
			UnlockToastManager.processAdvancements(gottenAdvancements);
		}
	}
	
}
