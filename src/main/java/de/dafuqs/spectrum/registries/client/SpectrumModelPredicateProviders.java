package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.present.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.energy.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.item.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.*;

// Vanilla models see: ModelPredicateProviderRegistry
public class SpectrumModelPredicateProviders {
	
	public static ModelTransformationMode currentItemRenderMode;
	
	public static void registerClient() {
		registerBowPredicates(SpectrumItems.BEDROCK_BOW);
		registerCrossbowPredicates(SpectrumItems.BEDROCK_CROSSBOW);
		registerSpectrumFishingRodItemPredicates(SpectrumItems.LAGOON_ROD);
		registerSpectrumFishingRodItemPredicates(SpectrumItems.MOLTEN_ROD);
		registerSpectrumFishingRodItemPredicates(SpectrumItems.BEDROCK_FISHING_ROD);
		registerEnderSplicePredicates(SpectrumItems.ENDER_SPLICE);
		registerAnimatedWandPredicates(SpectrumItems.NATURES_STAFF);
		registerAnimatedWandPredicates(SpectrumItems.RADIANCE_STAFF);
		registerAnimatedWandPredicates(SpectrumItems.STAFF_OF_REMEMBRANCE);
		registerKnowledgeDropPredicates(SpectrumItems.KNOWLEDGE_GEM);
		registerAshenCircletPredicates(SpectrumItems.ASHEN_CIRCLET);
		registerStampingItemPredicate(SpectrumItems.TUNING_STAMP);
		registerInkColorPredicate(SpectrumItems.INK_FLASK);
		registerInkFillStateItemPredicate(SpectrumItems.INK_FLASK);
		registerMoonPhasePredicates(SpectrumItems.CRESCENT_CLOCK);
		registerActivatableItemPredicate(SpectrumItems.DREAMFLAYER);
		registerOversizedItemPredicate(SpectrumItems.DREAMFLAYER);
		registerOversizedItemPredicate(SpectrumItems.KNOTTED_SWORD);
		registerOversizedItemPredicate(SpectrumItems.NECTAR_LANCE);
		registerOversizedItemPredicate(SpectrumItems.BEDROCK_SWORD);
		registerOversizedItemPredicate(SpectrumItems.BEDROCK_AXE);

		registerOversizedItemPredicate(SpectrumItems.PAINTBRUSH);

		registerOversizedItemPredicate(SpectrumItems.DRACONIC_TWINSWORD);
		registerOversizedItemPredicate(SpectrumItems.DRAGON_TALON);
		registerSlotReservingItem(SpectrumItems.DRAGON_TALON);
		registerSlotReservingItem(SpectrumItems.DRACONIC_TWINSWORD);

		registerOversizedItemPredicate(SpectrumItems.MALACHITE_WORKSTAFF);
		registerOversizedItemPredicate(SpectrumItems.MALACHITE_ULTRA_GREATSWORD);
		registerOversizedItemPredicate(SpectrumItems.MALACHITE_CROSSBOW);
		registerOversizedItemPredicate(SpectrumItems.MALACHITE_BIDENT);
		registerOversizedItemPredicate(SpectrumItems.GLASS_CREST_WORKSTAFF);
		registerOversizedItemPredicate(SpectrumItems.GLASS_CREST_ULTRA_GREATSWORD);
		registerOversizedItemPredicate(SpectrumItems.GLASS_CREST_CROSSBOW);
		registerOversizedItemPredicate(SpectrumItems.FEROCIOUS_GLASS_CREST_BIDENT);
		registerOversizedItemPredicate(SpectrumItems.FRACTAL_GLASS_CREST_BIDENT);
		
		registerBidentThrowingItemPredicate(SpectrumItems.MALACHITE_BIDENT);
		registerBidentThrowingItemPredicate(SpectrumItems.FEROCIOUS_GLASS_CREST_BIDENT);
		registerBidentThrowingItemPredicate(SpectrumItems.FRACTAL_GLASS_CREST_BIDENT);
		
		registerMalachiteCrossbowPredicates(SpectrumItems.MALACHITE_CROSSBOW);
		registerMalachiteCrossbowPredicates(SpectrumItems.GLASS_CREST_CROSSBOW);
		
		registerBottomlessBundlePredicates(SpectrumItems.BOTTOMLESS_BUNDLE);
		registerEnchantmentCanvasPrediates(SpectrumItems.ENCHANTMENT_CANVAS);
		registerPresentPredicates(SpectrumBlocks.PRESENT.asItem());
		registerMysteriousLocketPredicates(SpectrumItems.MYSTERIOUS_LOCKET);
		registerStructureCompassPredicates(SpectrumItems.MYSTERIOUS_COMPASS);
		registerNullableDyeColorPredicate(SpectrumBlocks.CRYSTALLARIEUM.asItem());

		registerPipeBombPredicates(SpectrumItems.PIPE_BOMB);
	}
	
	private static void registerNullableDyeColorPredicate(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("color"), (itemStack, clientWorld, livingEntity, i) -> {
			NullableDyeColor color = NullableDyeColor.get(itemStack.getNbt());
			return color.getId() / 16F;
		});
	}
	
	private static void registerMysteriousLocketPredicates(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("socketed"), (itemStack, clientWorld, livingEntity, i) -> MysteriousLocketItem.isSocketed(itemStack) ? 1.0F : 0.0F);
	}
	
	private static void registerStructureCompassPredicates(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("angle"), new CompassAnglePredicateProvider((world, stack, entity) -> StructureCompassItem.getStructurePos(stack)));
	}
	
	private static void registerMalachiteCrossbowPredicates(Item crossbowItem) {
		ModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return CrossbowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float) CrossbowItem.getPullTime(itemStack);
			}
		});
		ModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) ->
				livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F
		);
		ModelPredicateProviderRegistry.register(crossbowItem, new Identifier("charged"), (itemStack, clientWorld, livingEntity, i) ->
				livingEntity != null && CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F
		);
		ModelPredicateProviderRegistry.register(crossbowItem, new Identifier("projectile"), (itemStack, world, entity, seed) -> {
			if (itemStack == null) {
				return 0F;
			}
			ItemStack projectile = MalachiteCrossbowItem.getFirstProjectile(itemStack);
			if(projectile.isEmpty()) {
				return 0F;
			}
			
			// Well, this is awkward
			if (projectile.isOf(Items.FIREWORK_ROCKET)) {
				return 0.1F;
			} else if (projectile.isOf(SpectrumItems.MALACHITE_GLASS_ARROW)) {
				return 0.2F;
			} else if (projectile.isOf(SpectrumItems.TOPAZ_GLASS_ARROW)) {
				return 0.3F;
			} else if (projectile.isOf(SpectrumItems.AMETHYST_GLASS_ARROW)) {
				return 0.4F;
			} else if (projectile.isOf(SpectrumItems.CITRINE_GLASS_ARROW)) {
				return 0.5F;
			} else if (projectile.isOf(SpectrumItems.ONYX_GLASS_ARROW)) {
				return 0.6F;
			} else if (projectile.isOf(SpectrumItems.MOONSTONE_GLASS_ARROW)) {
				return 0.7F;
			}
			return 0F;
		});
	}
	
	/**
	 * 0.0: not throwing
	 * 0.5: throwing in hand
	 * 1.0: as projectile
	 */
	private static void registerBidentThrowingItemPredicate(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("bident_throwing"), (itemStack, clientWorld, livingEntity, i) -> {
			if (currentItemRenderMode == ModelTransformationMode.NONE) {
				if (itemStack.getItem() instanceof FractalBidentItem fractal) {
					return fractal.isDisabled(itemStack) ? 0.5F : 1F;
				}
				return 1.0F;
			}
			return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 0.5F : 0.0F;
		});
	}
	
	private static void registerColorPredicate(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("color"), (itemStack, clientWorld, livingEntity, i) -> {
			Optional<InkColor> color = PaintbrushItem.getColor(itemStack);
			return color.map(inkColor -> (1F + inkColor.getDyeColor().getId()) / 100F).orElse(0.0F);
		});
	}
	
	private static void registerPresentPredicates(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("variant"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound compound = itemStack.getNbt();
			if (compound == null || !compound.contains("Variant", NbtElement.STRING_TYPE))
				return 0.0F;
			
			PresentBlock.WrappingPaper wrappingPaper = PresentBlock.WrappingPaper.valueOf(compound.getString("Variant").toUpperCase(Locale.ROOT));
			return wrappingPaper.ordinal() / 10F;
		});
	}
	
	private static void registerBottomlessBundlePredicates(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("locked"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound compound = itemStack.getNbt();
			if (compound == null)
				return 0.0F;
			return compound.contains("Locked") ? 1.0F : 0.0F;
		});
		ModelPredicateProviderRegistry.register(SpectrumItems.BOTTOMLESS_BUNDLE, new Identifier("filled"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound compound = itemStack.getNbt();
			if (compound == null)
				return 0.0F;
			return compound.contains("StoredStack") ? 1.0F : 0.0F;
		});
	}
	
	private static void registerMoonPhasePredicates(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("phase"), (itemStack, clientWorld, livingEntity, i) -> {
			Entity entity = livingEntity != null ? livingEntity : itemStack.getHolder();
			if (entity == null) {
				return 0.0F;
			} else {
				World world = entity.getWorld();
				if (clientWorld == null && world instanceof ClientWorld) {
					clientWorld = (ClientWorld) world;
				}
				
				if (clientWorld == null) {
					return 0.0F;
				} else if (!clientWorld.getDimension().natural()) {
					return 1.0F;
				} else {
					return clientWorld.getMoonPhase() / 8F;
				}
			}
		});
	}
	
	private static void registerActivatableItemPredicate(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier(ActivatableItem.NBT_STRING), (itemStack, clientWorld, livingEntity, i) -> {
			if (ActivatableItem.isActivated(itemStack)) {
				return 1.0F;
			} else {
				return 0.0F;
			}
		});
	}

	private static void registerStampingItemPredicate(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("stamped"), ((stack, world, entity, seed) -> {
			var nbt = stack.getOrCreateNbt();
			if (nbt.contains(Stampable.STAMPING_DATA_TAG))
				return 1F;

			return 0F;
		}));
	}
	
	private static void registerSlotReservingItem(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier(SlotReservingItem.NBT_STRING), (itemStack, clientWorld, livingEntity, i) -> {
			if (itemStack.getItem() instanceof SlotReservingItem reserver && reserver.isReservingSlot(itemStack)) {
				return 1.0F;
			} else {
				return 0.0F;
			}
		});
	}
	
	private static void registerOversizedItemPredicate(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("in_world"), (itemStack, world, livingEntity, i) -> {
			if (world == null && livingEntity == null && i == 0) { // REIs 'fast batch' render mode. Without mixin' into REI there is no better way to catch this, I am afraid
				return 0.0F;
			}
			return currentItemRenderMode == ModelTransformationMode.GUI || currentItemRenderMode == ModelTransformationMode.GROUND || currentItemRenderMode == ModelTransformationMode.FIXED ? 0.0F : 1.0F;
		});
	}
	
	private static void registerBowPredicates(Item bowItem) {
		ModelPredicateProviderRegistry.register(bowItem, new Identifier("pull"), (itemStack, world, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return livingEntity.getActiveItem() != itemStack ? 0.0F : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
			}
		});
		ModelPredicateProviderRegistry.register(bowItem, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F);
	}
	
	private static void registerCrossbowPredicates(Item crossbowItem) {
		ModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				return CrossbowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float) CrossbowItem.getPullTime(itemStack);
			}
		});
		
		ModelPredicateProviderRegistry.register(crossbowItem, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) ->
				livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F
		);
		
		ModelPredicateProviderRegistry.register(crossbowItem, new Identifier("charged"), (itemStack, clientWorld, livingEntity, i) ->
				livingEntity != null && CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F
		);
		
		ModelPredicateProviderRegistry.register(crossbowItem, new Identifier("firework"), (itemStack, clientWorld, livingEntity, i) ->
				livingEntity != null && CrossbowItem.isCharged(itemStack) && CrossbowItem.hasProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F
		);
	}

	private static void registerPipeBombPredicates(Item pipeBombItem) {
		ModelPredicateProviderRegistry.register(pipeBombItem, new Identifier("armed"), (stack, world, entity, seed) -> PipeBombItem.isArmed(stack) ? 1.0F : 0.0F);
	}
	
	private static void registerSpectrumFishingRodItemPredicates(Item fishingRodItem) {
		ModelPredicateProviderRegistry.register(fishingRodItem, new Identifier("cast"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			} else {
				boolean isInMainHand = livingEntity.getMainHandStack() == itemStack;
				boolean isInOffhand = livingEntity.getOffHandStack() == itemStack;
				if (livingEntity.getMainHandStack().getItem() instanceof SpectrumFishingRodItem) {
					isInOffhand = false;
				}
				return (isInMainHand || isInOffhand) && livingEntity instanceof PlayerEntity && ((PlayerEntityAccessor) livingEntity).getSpectrumBobber() != null ? 1.0F : 0.0F;
			}
		});
	}
	
	private static void registerEnderSplicePredicates(Item enderSpliceItem) {
		ModelPredicateProviderRegistry.register(enderSpliceItem, new Identifier("bound"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound compoundTag = itemStack.getNbt();
			if (compoundTag != null && (compoundTag.contains("PosX") || compoundTag.contains("TargetPlayerUUID"))) {
				return 1.0F;
			} else {
				return 0.0F;
			}
		});
	}
	
	private static void registerAshenCircletPredicates(Item ashenCircletItem) {
		ModelPredicateProviderRegistry.register(ashenCircletItem, new Identifier("cooldown"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity != null && AshenCircletItem.getCooldownTicks(itemStack, livingEntity.getWorld()) == 0) {
				return 0.0F;
			} else {
				return 1.0F;
			}
		});
	}
	
	private static void registerAnimatedWandPredicates(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("in_use"), (itemStack, clientWorld, livingEntity, i) ->
				(livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack) ? 1.0F : 0.0F
		);
	}
	
	private static void registerKnowledgeDropPredicates(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("stored_experience_10000"), (itemStack, clientWorld, livingEntity, i) -> {
			if (SpectrumItems.KNOWLEDGE_GEM instanceof ExperienceStorageItem) {
				return ExperienceStorageItem.getStoredExperience(itemStack) / 10000F;
			} else {
				return 0;
			}
		});
	}
	
	private static void registerInkColorPredicate(InkFlaskItem item) {
		ModelPredicateProviderRegistry.register(SpectrumItems.INK_FLASK, new Identifier("color"), (itemStack, clientWorld, livingEntity, i) -> {
			SingleInkStorage storage = SpectrumItems.INK_FLASK.getEnergyStorage(itemStack);
			InkColor color = storage.getStoredColor();
			
			if (color == null) {
				return 0F;
			}
			return (1F + color.getDyeColor().getId()) / 100F;
		});
	}
	
	private static void registerInkFillStateItemPredicate(InkFlaskItem item) {
		ModelPredicateProviderRegistry.register(SpectrumItems.INK_FLASK, new Identifier("fill_state"), (itemStack, world, livingEntity, i) -> {
			SingleInkStorage storage = SpectrumItems.INK_FLASK.getEnergyStorage(itemStack);
			long current = storage.getCurrentTotal();
			if (current == 0) {
				return 0.0F;
			} else {
				long max = storage.getMaxTotal();
				return (float) Math.max(0.01, (double) current / (double) max);
			}
		});
	}
	
	private static void registerEnchantmentCanvasPrediates(Item item) {
		ModelPredicateProviderRegistry.register(item, new Identifier("bound"), (itemStack, world, livingEntity, i) -> {
			NbtCompound nbt = itemStack.getNbt();
			if (nbt != null && nbt.contains("BoundItem", NbtElement.STRING_TYPE)) {
				return 1;
			}
			return 0;
		});
	}
	
}
