package de.dafuqs.spectrum.registries.client;

import de.dafuqs.spectrum.api.energy.storage.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.bottomless_bundle.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.item.*;
import net.minecraft.core.component.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.neoforged.fml.event.lifecycle.*;

// Vanilla models see: ModelPredicateProviderRegistry
public class SpectrumModelPredicateProviders {
	
	public static void registerClient(FMLClientSetupEvent event) {
		registerBowPredicates(SpectrumItems.BEDROCK_BOW.get());
		registerCrossbowPredicates(SpectrumItems.BEDROCK_CROSSBOW.get());
		registerSpectrumFishingRodItemPredicates(SpectrumItems.LAGOON_ROD.get());
		registerSpectrumFishingRodItemPredicates(SpectrumItems.MOLTEN_ROD.get());
		registerSpectrumFishingRodItemPredicates(SpectrumItems.BEDROCK_FISHING_ROD.get());
		registerEnderSplicePredicates(SpectrumItems.ENDER_SPLICE.get());
		registerAnimatedWandPredicates(SpectrumItems.NATURES_STAFF.get());
		registerAnimatedWandPredicates(SpectrumItems.RADIANCE_STAFF.get());
		registerAnimatedWandPredicates(SpectrumItems.STAFF_OF_REMEMBRANCE.get());
		registerKnowledgeDropPredicates(SpectrumItems.KNOWLEDGE_GEM.get());
		registerAshenCircletPredicates(SpectrumItems.ASHEN_CIRCLET.get());
		registerNullableInkColorPredicate(SpectrumItems.INK_FLASK.get());
		registerInkFillStateItemPredicate(SpectrumItems.INK_FLASK.get());
		registerMoonPhasePredicates(SpectrumItems.CRESCENT_CLOCK.get());
		registerActivatableItemPredicate(SpectrumItems.DREAMFLAYER.get());
		registerOversizedItemPredicate(SpectrumItems.DREAMFLAYER.get());
		registerOversizedItemPredicate(SpectrumItems.KNOTTED_SWORD.get());
		registerOversizedItemPredicate(SpectrumItems.NECTAR_LANCE.get());
		registerOversizedItemPredicate(SpectrumItems.BEDROCK_SWORD.get());
		registerOversizedItemPredicate(SpectrumItems.BEDROCK_AXE.get());
		
		registerOversizedItemPredicate(SpectrumItems.PAINTBRUSH.get());
		
		registerOversizedItemPredicate(SpectrumItems.DRACONIC_TWINSWORD.get());
		registerOversizedItemPredicate(SpectrumItems.DRAGON_TALON.get());
		registerSlotReservingItem(SpectrumItems.DRAGON_TALON.get());
		registerSlotReservingItem(SpectrumItems.DRACONIC_TWINSWORD.get());
		
		registerOversizedItemPredicate(SpectrumItems.MALACHITE_WORKSTAFF.get());
		registerOversizedItemPredicate(SpectrumItems.MALACHITE_ULTRA_GREATSWORD.get());
		registerOversizedItemPredicate(SpectrumItems.MALACHITE_CROSSBOW.get());
		registerOversizedItemPredicate(SpectrumItems.MALACHITE_BIDENT.get());
		registerOversizedItemPredicate(SpectrumItems.GLASS_CREST_WORKSTAFF.get());
		registerOversizedItemPredicate(SpectrumItems.GLASS_CREST_ULTRA_GREATSWORD.get());
		registerOversizedItemPredicate(SpectrumItems.GLASS_CREST_CROSSBOW.get());
		registerOversizedItemPredicate(SpectrumItems.FEROCIOUS_GLASS_CREST_BIDENT.get());
		registerOversizedItemPredicate(SpectrumItems.FRACTAL_GLASS_CREST_BIDENT.get());
		registerOversizedItemPredicate(SpectrumItems.OMNI_ACCELERATOR.get());
		
		registerBidentThrowingItemPredicate(SpectrumItems.MALACHITE_BIDENT.get());
		registerBidentThrowingItemPredicate(SpectrumItems.FEROCIOUS_GLASS_CREST_BIDENT.get());
		registerBidentThrowingItemPredicate(SpectrumItems.FRACTAL_GLASS_CREST_BIDENT.get());
		
		registerMalachiteCrossbowPredicates(SpectrumItems.MALACHITE_CROSSBOW.get());
		registerMalachiteCrossbowPredicates(SpectrumItems.GLASS_CREST_CROSSBOW.get());
		
		registerBottomlessBundlePredicates(SpectrumBlocks.BOTTOMLESS_BUNDLE.get().asItem());
		registerEnchantmentCanvasPredicates(SpectrumItems.ENCHANTMENT_CANVAS.get());
		registerPresentPredicates(SpectrumBlocks.PRESENT.get().asItem());
		registerMysteriousLocketPredicates(SpectrumItems.MYSTERIOUS_LOCKET.get());
		registerStructureCompassPredicates(SpectrumItems.MYSTERIOUS_COMPASS.get());
		
		registerPipeBombPredicates(SpectrumItems.PIPE_BOMB.get());
	}
	
	private static void registerNullableInkColorPredicate(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("color"), (stack, clientWorld, entity, i) -> {
			var color = stack.get(SpectrumDataComponentTypes.INK_COLOR);
			return color == null ? -1 : color.getColorInt();
		});
	}
	
	private static void registerMysteriousLocketPredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("socketed"), (stack, world, entity, i) ->
				stack.has(SpectrumDataComponentTypes.SOCKETED) ? 1.0F : 0.0F);
	}
	
	private static void registerStructureCompassPredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("angle"),
				new CompassItemPropertyFunction((world, stack, entity) -> StructureCompassItem.getStructurePos(stack)));
	}
	
	private static void registerMalachiteCrossbowPredicates(Item crossbowItem) {
		ItemProperties.register(crossbowItem, ResourceLocation.parse("pull"), (stack, world, user, i) ->
				user == null || CrossbowItem.isCharged(stack) ? 0.0F : (float) (stack.getUseDuration(user) - user.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, user));
		
		ItemProperties.register(crossbowItem, ResourceLocation.parse("pulling"), (stack, world, entity, i) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
		
		ItemProperties.register(crossbowItem, ResourceLocation.parse("charged"), (stack, world, entity, i) ->
				entity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
		
		ItemProperties.register(crossbowItem, ResourceLocation.parse("projectile"), (stack, world, entity, seed) -> {
			if (stack == null) {
				return 0F;
			}
			ItemStack projectile = MalachiteCrossbowItem.getFirstProjectile(stack);
			if (projectile.isEmpty()) {
				return 0F;
			}
			
			// Well, this is awkward
			if (projectile.is(Items.FIREWORK_ROCKET)) {
				return 0.1F;
			} else if (projectile.is(SpectrumItems.MALACHITE_GLASS_ARROW)) {
				return 0.2F;
			} else if (projectile.is(SpectrumItems.TOPAZ_GLASS_ARROW)) {
				return 0.3F;
			} else if (projectile.is(SpectrumItems.AMETHYST_GLASS_ARROW)) {
				return 0.4F;
			} else if (projectile.is(SpectrumItems.CITRINE_GLASS_ARROW)) {
				return 0.5F;
			} else if (projectile.is(SpectrumItems.ONYX_GLASS_ARROW)) {
				return 0.6F;
			} else if (projectile.is(SpectrumItems.MOONSTONE_GLASS_ARROW)) {
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
		ItemProperties.register(item, ResourceLocation.parse("bident_throwing"), (stack, world, entity, i) -> {
			return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 0.5F : 0.0F;
		});
	}
	
	private static void registerPresentPredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("variant"), (stack, world, entity, i) ->
				stack.getOrDefault(SpectrumDataComponentTypes.WRAPPED_PRESENT, WrappedPresentComponent.DEFAULT).variant().ordinal() / 10F);
	}
	
	private static void registerBottomlessBundlePredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("locked"), (stack, world, entity, i) ->
				BottomlessBundleItem.isLocked(stack) ? 1.0F : 0.0F);
		
		ItemProperties.register(item, ResourceLocation.parse("filled"), (stack, world, entity, i) ->
				BottomlessBundleItem.getStoredAmount(stack) > 0 ? 1.0F : 0.0F);
	}
	
	private static void registerMoonPhasePredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("phase"), (stack, world, entity, i) -> {
			Entity holder = entity != null ? entity : stack.getEntityRepresentation();
			if (entity == null) {
				return 0.0F;
			} else {
				if (world == null && holder.level() instanceof ClientLevel clientWorld) {
					world = clientWorld;
				}
				
				if (world == null) {
					return 0.0F;
				} else if (!world.dimensionType().natural()) {
					return 1.0F;
				} else {
					return world.getMoonPhase() / 8F;
				}
			}
		});
	}
	
	private static void registerActivatableItemPredicate(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("activated"), (stack, world, entity, i) ->
				ActivatableItem.isActivated(stack) ? 1.0F : 0.0F);
	}
	
	private static void registerSlotReservingItem(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("reserved"), (stack, world, entity, i) ->
				SlotReservingItem.isReservingSlot(stack) ? 1.0F : 0.0F);
	}
	
	private static void registerOversizedItemPredicate(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("oversized"), (stack, world, entity, seed) ->
				seed == 817210941 ? 1.0F : 0.0F);
	}
	
	private static void registerBowPredicates(Item bowItem) {
		ItemProperties.register(bowItem, ResourceLocation.parse("pull"), (stack, world, entity, i) ->
				entity == null || entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F);
		
		ItemProperties.register(bowItem, ResourceLocation.parse("pulling"), (stack, world, entity, i) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}
	
	private static void registerCrossbowPredicates(Item crossbowItem) {
		ItemProperties.register(crossbowItem, ResourceLocation.parse("pull"), (stack, world, entity, i) ->
				entity == null || CrossbowItem.isCharged(stack) ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(stack, entity));
		
		ItemProperties.register(crossbowItem, ResourceLocation.parse("pulling"), (stack, world, entity, i) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
		
		ItemProperties.register(crossbowItem, ResourceLocation.parse("charged"), (stack, world, entity, i) ->
				entity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
		
		ItemProperties.register(crossbowItem, ResourceLocation.parse("firework"), (stack, world, entity, seed) ->
				stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
	}
	
	private static void registerPipeBombPredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("armed"), (stack, world, entity, seed) ->
				PipeBombItem.isPrimed(stack) ? 1.0F : 0.0F);
	}
	
	private static void registerSpectrumFishingRodItemPredicates(Item fishingRodItem) {
		ItemProperties.register(fishingRodItem, ResourceLocation.parse("cast"), (stack, world, entity, i) -> {
			if (entity == null)
				return 0.0F;
			boolean isInMainHand = entity.getMainHandItem() == stack;
			boolean isInOffhand = entity.getOffhandItem() == stack && !(entity.getMainHandItem().getItem() instanceof SpectrumFishingRodItem);
			return (isInMainHand || isInOffhand) && entity instanceof Player && ((PlayerEntityAccessor) entity).getSpectrumBobber() != null ? 1.0F : 0.0F;
		});
	}
	
	private static void registerEnderSplicePredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("bound"), (stack, world, entity, i) ->
				EnderSpliceItem.hasTeleportTarget(stack) ? 1.0F : 0.0F);
	}
	
	private static void registerAshenCircletPredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("cooldown"), (stack, world, entity, i) ->
				world != null && AshenCircletItem.getCooldownTicks(stack, world) == 0 ? 0.0F : 1.0F);
	}
	
	private static void registerAnimatedWandPredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("in_use"), (stack, world, entity, i) ->
				entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
	}
	
	private static void registerKnowledgeDropPredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("stored_experience_10000"), (stack, world, entity, i) ->
				ExperienceStorageItem.getStoredExperience(stack) / 10000F);
	}
	
	private static void registerInkFillStateItemPredicate(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("fill_state"), (stack, world, entity, i) -> {
			SingleInkStorage storage = SpectrumItems.INK_FLASK.get().getEnergyStorage(stack);
			float current = (float) storage.getCurrentTotal();
			float maximum = (float) storage.getMaxTotal();
			return current == 0 || maximum == 0 ? 0.0F : Math.max(0.01F, current / maximum);
		});
	}
	
	private static void registerEnchantmentCanvasPredicates(Item item) {
		ItemProperties.register(item, ResourceLocation.parse("bound"), (stack, world, entity, i) ->
				stack.has(SpectrumDataComponentTypes.BOUND_ITEM) ? 1.0F : 0.0F);
	}
	
}
