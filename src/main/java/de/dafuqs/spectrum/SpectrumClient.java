package de.dafuqs.spectrum;

import de.dafuqs.spectrum.compat.patchouli.PatchouliPages;
import de.dafuqs.spectrum.entity.SpectrumEntityRenderers;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.items.magic_items.EnderSpliceItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleFactories;
import de.dafuqs.spectrum.progression.ToggleableBlockColorProvider;
import de.dafuqs.spectrum.progression.ToggleableItemColorProvider;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;

import static de.dafuqs.spectrum.SpectrumCommon.log;

public class SpectrumClient implements ClientModInitializer {
	
	public static ToggleableBlockColorProvider coloredLeavesBlockColorProvider;
	public static ToggleableItemColorProvider coloredLeavesItemColorProvider;

	@Environment(EnvType.CLIENT)
	public static MinecraftClient minecraftClient;

	@Override
	public void onInitializeClient() {
		log(Level.INFO, "Starting Client Startup");

		log(Level.INFO, "Setting up Block Rendering...");
		SpectrumBlocks.registerClient();
		log(Level.INFO, "Setting up Fluid Rendering...");
		SpectrumFluids.registerClient();

		log(Level.INFO, "Setting up GUIs...");
		SpectrumContainers.register();
		SpectrumScreenHandlerTypes.registerClient();

		log(Level.INFO, "Setting up ItemPredicates...");
		registerBowPredicates(SpectrumItems.BEDROCK_BOW);
		registerCrossbowPredicates(SpectrumItems.BEDROCK_CROSSBOW);
		registerFishingRodPredicates(SpectrumItems.BEDROCK_FISHING_ROD);
		registerEnderSplicePredicates(SpectrumItems.ENDER_SPLICE);
		registerAnimatedWandPredicates(SpectrumItems.NATURES_STAFF);
		registerAnimatedWandPredicates(SpectrumItems.LIGHT_STAFF);
		registerKnowledgeDropPredicates(SpectrumItems.KNOWLEDGE_GEM);

		log(Level.INFO, "Setting up Block Entity Renderers...");
		SpectrumBlockEntityRegistry.registerClient();
		log(Level.INFO, "Setting up Entity Renderers...");
		SpectrumEntityRenderers.registerClient();

		log(Level.INFO, "Registering Server to Client Package Receivers...");
		SpectrumS2CPackets.registerS2CReceivers();
		log(Level.INFO, "Registering Particle Factories...");
		SpectrumParticleFactories.register();

		log(Level.INFO, "Registering Overlays...");
		GuiOverlay.register();
		
		SpectrumTooltipComponents.registerTooltipComponents();

		ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
			SpectrumClient.minecraftClient = minecraftClient;
			registerColorProviders();
		});

		log(Level.INFO, "Registering custom Patchouli Pages...");
		PatchouliPages.register();

		log(Level.INFO, "Client startup completed!");
	}

	private static void registerColorProviders() {
		log(Level.INFO, "Registering Block and Item Color Providers...");
		
		// Biome Colors for colored leaves items and blocks
		// They don't use it, but their decay as oak leaves do
		BlockColorProvider leavesBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.OAK_LEAVES);
		ItemColorProvider leavesItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.OAK_LEAVES);
		
		if(leavesBlockColorProvider != null && leavesItemColorProvider != null) {
			coloredLeavesBlockColorProvider = new ToggleableBlockColorProvider(leavesBlockColorProvider);
			coloredLeavesItemColorProvider = new ToggleableItemColorProvider(leavesItemColorProvider);
			
			for (DyeColor dyeColor : DyeColor.values()) {
				Block block = SpectrumBlocks.getColoredLeavesBlock(dyeColor);
				ColorProviderRegistry.BLOCK.register(coloredLeavesBlockColorProvider, block);
				ColorProviderRegistry.ITEM.register(coloredLeavesItemColorProvider, block);
			}
		}

		BlockColorProvider grassBlockColorProvider = ColorProviderRegistry.BLOCK.get(Blocks.GRASS);
		ItemColorProvider grassItemColorProvider = ColorProviderRegistry.ITEM.get(Blocks.GRASS.asItem());
		
		if(grassBlockColorProvider != null && grassItemColorProvider != null) {
			ColorProviderRegistry.BLOCK.register(grassBlockColorProvider, SpectrumBlocks.CLOVER);
			ColorProviderRegistry.BLOCK.register(grassBlockColorProvider, SpectrumBlocks.FOUR_LEAF_CLOVER);
			//ColorProviderRegistry.ITEM.register(grassItemColorProvider, SpectrumBlocks.CLOVER);
			//ColorProviderRegistry.ITEM.register(grassItemColorProvider, SpectrumBlocks.FOUR_LEAF_CLOVER);
		}
	}
	


	// Vanilla models see: ModelPredicateProviderRegistry
	public static void registerBowPredicates(BowItem bowItem) {
		FabricModelPredicateProviderRegistry.register(bowItem, new Identifier("pull"), (itemStack, world, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return livingEntity.getActiveItem() != itemStack ? 0.0F : (float)(itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
			}
		});
		FabricModelPredicateProviderRegistry.register(bowItem, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
	}

	public static void registerCrossbowPredicates(CrossbowItem crossbowItem) {
		FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return CrossbowItem.isCharged(itemStack) ? 0.0F : (float)(itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float)CrossbowItem.getPullTime(itemStack);
			}
		});

		FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> {
			return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
		});

		FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("charged"), (itemStack, clientWorld, livingEntity, i) -> {
			return livingEntity != null && CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
		});

		FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("firework"), (itemStack, clientWorld, livingEntity, i) -> {
			return livingEntity != null && CrossbowItem.isCharged(itemStack) && CrossbowItem.hasProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
		});
	}

	public static void registerFishingRodPredicates(FishingRodItem fishingRodItem) {
		FabricModelPredicateProviderRegistry.register(fishingRodItem, new Identifier("cast"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				boolean bl = livingEntity.getMainHandStack() == itemStack;
				boolean bl2 = livingEntity.getOffHandStack() == itemStack;
				if (livingEntity.getMainHandStack().getItem() instanceof FishingRodItem) {
					bl2 = false;
				}

				return (bl || bl2) && livingEntity instanceof PlayerEntity && ((PlayerEntity)livingEntity).fishHook != null ? 1.0F : 0.0F;
			}
		});
	}

	public static void registerEnderSplicePredicates(EnderSpliceItem enderSpliceItem) {
		FabricModelPredicateProviderRegistry.register(enderSpliceItem, new Identifier("bound"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound compoundTag = itemStack.getNbt();
			if (compoundTag != null && compoundTag.contains("PosX")) {
				return 1.0F;
			} else {
				return 0.0F;
			}
		});
	}

	private void registerAnimatedWandPredicates(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("in_use"), (itemStack, clientWorld, livingEntity, i) -> {
			return (livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack) ? 1.0F : 0.0F;
		});
	}

	private void registerKnowledgeDropPredicates(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("stored_experience_10000"), (itemStack, clientWorld, livingEntity, i) -> {
			if(item instanceof ExperienceStorageItem) {
				return ExperienceStorageItem.getStoredExperience(itemStack) / 10000F;
			} else {
				return 0;
			}
		});
	}
	
}