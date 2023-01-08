package de.dafuqs.spectrum;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.compat.patchouli.*;
import de.dafuqs.spectrum.compat.reverb.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.progression.toast.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.registries.client.*;
import de.dafuqs.spectrum.render.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.item.v1.*;
import net.fabricmc.loader.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

import java.util.*;

import static de.dafuqs.spectrum.SpectrumCommon.*;

public class SpectrumClient implements ClientModInitializer, RevealingCallback, ClientAdvancementPacketCallback {
	
	@Environment(EnvType.CLIENT)
	public static final SkyLerper skyLerper = new SkyLerper();
	
	public static boolean foodEffectsTooltipsModLoaded = FabricLoader.getInstance().isModLoaded("foodeffecttooltips");
	
	@Override
	public void onInitializeClient() {
		logInfo("Starting Client Startup");
		
		logInfo("Registering Model Layers...");
		SpectrumModelLayers.register();
		
		logInfo("Setting up Block Rendering...");
		SpectrumBlocks.registerClient();
		
		logInfo("Setting up Fluid Rendering...");
		SpectrumFluids.registerClient();
		
		logInfo("Setting up GUIs...");
		SpectrumScreenHandlerIDs.register();
		SpectrumScreenHandlerTypes.registerClient();
		
		logInfo("Setting up ItemPredicates...");
		SpectrumItemPredicates.registerClient();
		
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

        DimensionReverb.setup();

        logInfo("Registering Event Listeners...");
        ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
            SpectrumColorProviders.registerClient();
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register(minecraftClient -> {
            PastelNetworkManager.clearClientInstance();
        });

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (!foodEffectsTooltipsModLoaded && stack.isFood()) {
                if (Registry.ITEM.getId(stack.getItem()).getNamespace().equals(SpectrumCommon.MOD_ID)) {
                    TooltipHelper.addFoodComponentEffectTooltip(stack, lines);
                }
            }
            if (stack.isIn(SpectrumItemTags.COMING_SOON_TOOLTIP)) {
                lines.add(Text.translatable("spectrum.tooltip.coming_soon"));
            }
        });

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