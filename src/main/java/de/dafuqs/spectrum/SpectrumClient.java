package de.dafuqs.spectrum;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.progression.toast.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.*;
import de.dafuqs.spectrum.render.capes.*;
import me.shedaniel.autoconfig.*;
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
import net.neoforged.neoforge.event.*;

import java.util.*;
import java.util.function.*;

@Mod(value = SpectrumCommon.MOD_ID, dist = Dist.CLIENT)
public class SpectrumClient implements RevealingCallback, ClientAdvancementPacketCallback {
	
	public static final SkyLerper skyLerper = new SkyLerper();
	
	public SpectrumClient(IEventBus modBus, ModContainer modContainer) {
		SpectrumCommon.logInfo("Starting Client Startup");
		SpectrumBlocks.registerClient();
		SpectrumIntegrationPacks.registerClient();
		SpectrumModelPredicateProviders.registerClient();
		SpectrumEntityRenderers.registerClient();
		modBus.addListener(SpectrumParticleFactories::register);
		
		modBus.addListener(HudRenderers::register);
		NeoForge.EVENT_BUS.addListener(HudRenderers::registerPost);
		
		NeoForge.EVENT_BUS.addListener(HudRenderers::registerPost);
		modBus.addListener(SpectrumTooltipComponents::registerTooltipComponents);
		SpectrumDimensions.registerClient();
		SpectrumClientEventListeners.register(modBus);
		
		if (SpectrumCommon.CONFIG.AddItemTooltips) {
			NeoForge.EVENT_BUS.addListener(SpectrumTooltips::register);
		}
		modBus.addListener(SpectrumArmorRenderers::register);
		WorthinessChecker.init();
		
		RevealingCallback.register(this);
		ClientAdvancementPacketCallback.registerCallback(this);
		
		
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, (modCont, parent) -> AutoConfig.getConfigScreen(SpectrumConfig.class, parent).get());
		
		modBus.addListener(SpectrumFluids::registerClient);
		modBus.addListener(SpectrumBlockEntities::registerClient);
		modBus.register(SpectrumScreenHandlerTypes.class);
		modBus.addListener(SpectrumModelLayers::register);
		
		NeoForge.EVENT_BUS.addListener((Consumer<AddReloadListenerEvent>) event -> {
			event.addListener(ParticleSpawnerParticlesDataLoader.INSTANCE);
		});
		
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
