package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.storage.SingleInkStorage;
import de.dafuqs.spectrum.interfaces.PlayerEntityAccessor;
import de.dafuqs.spectrum.items.ActivatableItem;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.items.energy.InkFlaskItem;
import de.dafuqs.spectrum.items.magic_items.EnderSpliceItem;
import de.dafuqs.spectrum.items.magic_items.PaintBrushItem;
import de.dafuqs.spectrum.items.tools.SpectrumFishingRodItem;
import de.dafuqs.spectrum.items.trinkets.AshenCircletItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.Optional;

// Vanilla models see: ModelPredicateProviderRegistry
public class SpectrumItemPredicates {
	
	public static ModelTransformation.Mode currentItemRenderMode;
	
	public static void registerClient() {
		registerBowPredicates(SpectrumItems.BEDROCK_BOW);
		registerCrossbowPredicates(SpectrumItems.BEDROCK_CROSSBOW);
		registerSpectrumFishingRodPredicates(SpectrumItems.LAGOON_ROD);
		registerSpectrumFishingRodPredicates(SpectrumItems.MOLTEN_ROD);
		registerSpectrumFishingRodPredicates(SpectrumItems.BEDROCK_FISHING_ROD);
		registerEnderSplicePredicates(SpectrumItems.ENDER_SPLICE);
		registerAnimatedWandPredicates(SpectrumItems.NATURES_STAFF);
		registerAnimatedWandPredicates(SpectrumItems.RADIANCE_STAFF);
		registerKnowledgeDropPredicates(SpectrumItems.KNOWLEDGE_GEM);
		registerAshenCircletPredicates(SpectrumItems.ASHEN_CIRCLET);
		registerColorPredicate(SpectrumItems.PAINTBRUSH);
		registerInkColorPredicate(SpectrumItems.INK_FLASK);
		registerInkFillStateItemPredicate(SpectrumItems.INK_FLASK);
		registerMoonPhasePredicates(SpectrumItems.CRESCENT_CLOCK);
		registerDreamFlayerPredicates(SpectrumItems.DREAMFLAYER);
		registerBottomlessBundlePredicates(SpectrumItems.BOTTOMLESS_BUNDLE);
	}
	
	private static void registerColorPredicate(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("color"), (itemStack, clientWorld, livingEntity, i) -> {
			Optional<InkColor> color = PaintBrushItem.getColor(itemStack);
			if(color.isEmpty()) {
				return 0.0F;
			}
			return (1F + color.get().getDyeColor().getId()) / 100F;
		});
	}
	
	private static void registerBottomlessBundlePredicates(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("locked"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound compound = itemStack.getNbt();
			if(compound == null)
				return 0.0F;
			return compound.contains("Locked") ? 1.0F : 0.0F;
		});
		FabricModelPredicateProviderRegistry.register(item, new Identifier("filled"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound compound = itemStack.getNbt();
			if(compound == null)
				return 0.0F;
			return compound.contains("StoredStack") ? 1.0F : 0.0F;
		});
		
	}
	
	private static void registerMoonPhasePredicates(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("phase"), (itemStack, clientWorld, livingEntity, i) -> {
			Entity entity = livingEntity != null ? livingEntity : itemStack.getHolder();
			if (entity == null) {
				return 0.0F;
			} else {
				if (clientWorld == null && entity.world instanceof ClientWorld clientWorld1) {
					clientWorld = clientWorld1;
				}
				
				if (clientWorld == null) {
					return 0.0F;
				} else if (!clientWorld.getDimension().isNatural()) {
					return 1.0F;
				} else {
					return clientWorld.getMoonPhase() / 8F;
				}
			}
		});
	}
	
	private static void registerDreamFlayerPredicates(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("in_inventory"), (itemStack, world, livingEntity, i) -> {
			return currentItemRenderMode == ModelTransformation.Mode.GUI ? 1.0F : 0.0F;
		});
		FabricModelPredicateProviderRegistry.register(item, new Identifier(ActivatableItem.NBT_STRING), (itemStack, clientWorld, livingEntity, i) -> {
			if(ActivatableItem.isActivated(itemStack)) {
				return 1.0F;
			} else {
				return 0.0F;
			}
		});
	}
	
	private static void registerBowPredicates(BowItem bowItem) {
		FabricModelPredicateProviderRegistry.register(bowItem, new Identifier("pull"), (itemStack, world, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return livingEntity.getActiveItem() != itemStack ? 0.0F : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
			}
		});
		FabricModelPredicateProviderRegistry.register(bowItem, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
	}
	
	private static void registerCrossbowPredicates(CrossbowItem crossbowItem) {
		FabricModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return CrossbowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float) CrossbowItem.getPullTime(itemStack);
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
	
	private static void registerSpectrumFishingRodPredicates(SpectrumFishingRodItem fishingRod) {
		FabricModelPredicateProviderRegistry.register(fishingRod, new Identifier("cast"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				boolean bl = livingEntity.getMainHandStack() == itemStack;
				boolean bl2 = livingEntity.getOffHandStack() == itemStack;
				if (livingEntity.getMainHandStack().getItem() instanceof SpectrumFishingRodItem) {
					bl2 = false;
				}
				return (bl || bl2) && livingEntity instanceof PlayerEntity && ((PlayerEntityAccessor) livingEntity).getSpectrumBobber() != null ? 1.0F : 0.0F;
			}
		});
	}
	
	private static void registerEnderSplicePredicates(EnderSpliceItem enderSpliceItem) {
		FabricModelPredicateProviderRegistry.register(enderSpliceItem, new Identifier("bound"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound compoundTag = itemStack.getNbt();
			if (compoundTag != null && (compoundTag.contains("PosX") || compoundTag.contains("TargetPlayerUUID"))) {
				return 1.0F;
			} else {
				return 0.0F;
			}
		});
	}
	
	private static void registerAshenCircletPredicates(Item ashenCircletItem) {
		FabricModelPredicateProviderRegistry.register(ashenCircletItem, new Identifier("cooldown"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity != null && AshenCircletItem.getCooldownTicks(itemStack, livingEntity.world) == 0) {
				return 0.0F;
			} else {
				return 1.0F;
			}
		});
	}
	
	private static void registerAnimatedWandPredicates(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("in_use"), (itemStack, clientWorld, livingEntity, i) -> {
			return (livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack) ? 1.0F : 0.0F;
		});
	}
	
	private static void registerKnowledgeDropPredicates(Item item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("stored_experience_10000"), (itemStack, clientWorld, livingEntity, i) -> {
			if (item instanceof ExperienceStorageItem) {
				return ExperienceStorageItem.getStoredExperience(itemStack) / 10000F;
			} else {
				return 0;
			}
		});
	}
	
	private static void registerInkColorPredicate(InkFlaskItem item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("color"), (itemStack, clientWorld, livingEntity, i) -> {
			SingleInkStorage storage = item.getEnergyStorage(itemStack);
			InkColor color = storage.getStoredColor();
			return (1F + color.getDyeColor().getId()) / 100F;
		});
	}
	
	private static void registerInkFillStateItemPredicate(InkFlaskItem item) {
		FabricModelPredicateProviderRegistry.register(item, new Identifier("fill_state"), (itemStack, world, livingEntity, i) -> {
			SingleInkStorage storage = item.getEnergyStorage(itemStack);
			long current = storage.getCurrentTotal();
			if (current == 0) {
				return 0.0F;
			} else {
				long max = storage.getMaxTotal();
				return (float) Math.max(0.01, (double) current / (double) max);
			}
		});
	}
	
}