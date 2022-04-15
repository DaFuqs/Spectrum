package de.dafuqs.spectrum;

import de.dafuqs.spectrum.blocks.memory.MemoryBlockEntity;
import de.dafuqs.spectrum.blocks.memory.MemoryItem;
import de.dafuqs.spectrum.compat.patchouli.PatchouliPages;
import de.dafuqs.spectrum.entity.SpectrumEntityRenderers;
import de.dafuqs.spectrum.inventories.SpectrumContainers;
import de.dafuqs.spectrum.inventories.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.items.magic_items.EnderSpliceItem;
import de.dafuqs.spectrum.items.trinkets.AshenCircletItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketReceiver;
import de.dafuqs.spectrum.particle.SpectrumParticleFactories;
import de.dafuqs.spectrum.progression.ToggleableBlockColorProvider;
import de.dafuqs.spectrum.progression.ToggleableItemColorProvider;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.render.GuiOverlay;
import de.dafuqs.spectrum.render.SkyLerper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;

import static de.dafuqs.spectrum.SpectrumCommon.logInfo;

public class SpectrumClient implements ClientModInitializer {
	
	public static ToggleableBlockColorProvider coloredLeavesBlockColorProvider;
	public static ToggleableItemColorProvider coloredLeavesItemColorProvider;

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
		registerBowPredicates(SpectrumItems.BEDROCK_BOW);
		registerCrossbowPredicates(SpectrumItems.BEDROCK_CROSSBOW);
		registerFishingRodPredicates(SpectrumItems.BEDROCK_FISHING_ROD);
		registerEnderSplicePredicates(SpectrumItems.ENDER_SPLICE);
		registerAnimatedWandPredicates(SpectrumItems.NATURES_STAFF);
		registerAnimatedWandPredicates(SpectrumItems.RADIANCE_STAFF);
		registerKnowledgeDropPredicates(SpectrumItems.KNOWLEDGE_GEM);
		registerAshenCircletPredicates(SpectrumItems.ASHEN_CIRCLET);

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

		ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
			SpectrumClient.minecraftClient = minecraftClient;
			registerColorProviders();
		});
		
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if(stack.isIn(SpectrumItemTags.COMING_SOON_TOOLTIP)) {
				lines.add(new TranslatableText("spectrum.tooltip.coming_soon"));
			}
		});

		logInfo("Registering custom Patchouli Pages...");
		PatchouliPages.register();

		logInfo("Client startup completed!");
	}

	private static void registerColorProviders() {
		logInfo("Registering Block and Item Color Providers...");
		
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
		}
		
		// MEMORIES
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			if(world == null) {
				return 0x0;
			}
			
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof MemoryBlockEntity memoryBlockEntity) {
				return memoryBlockEntity.getEggColor(tintIndex);
			}
			
			return 0x0;
		}, SpectrumBlocks.MEMORY);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> MemoryItem.getEggColor(stack.getNbt(), tintIndex), SpectrumBlocks.MEMORY.asItem());
		
		// Potion Pendant Potion Color Overlays
		ColorProviderRegistry.ITEM.register(SpectrumClient::potionColor, SpectrumItems.LESSER_POTION_PENDANT);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if(tintIndex != 0 && tintIndex < 4) {
				List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);
				if(tintIndex == 1) {
					if(effects.size() > 0) {
						return effects.get(0).getEffectType().getColor();
					}
				} else if(tintIndex == 2) {
					if(effects.size() > 1) {
						return effects.get(1).getEffectType().getColor();
					}
				} else {
					if(effects.size() > 2) {
						return effects.get(2).getEffectType().getColor();
					}
				}
			}
			return -1;
		}, SpectrumItems.GREATER_POTION_PENDANT);
	}
	
	private static int potionColor(ItemStack stack, int tintIndex) {
		if(tintIndex == 1) {
			List<StatusEffectInstance> effects = PotionUtil.getPotionEffects(stack);
			if(effects.size() > 0) {
				return PotionUtil.getColor(effects);
			}
		} else {
			return -1;
		}
		return -1;
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
			if (compoundTag != null && (compoundTag.contains("PosX") || compoundTag.contains("TargetPlayerUUID"))) {
				return 1.0F;
			} else {
				return 0.0F;
			}
		});
	}
	
	public static void registerAshenCircletPredicates(Item ashenCircletItem) {
		FabricModelPredicateProviderRegistry.register(ashenCircletItem, new Identifier("cooldown"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity != null && AshenCircletItem.getCooldownTicks(itemStack, livingEntity.world) == 0) {
				return 0.0F;
			} else {
				return 1.0F;
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